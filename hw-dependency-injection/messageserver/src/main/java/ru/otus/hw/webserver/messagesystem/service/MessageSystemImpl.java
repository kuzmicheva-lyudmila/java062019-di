package ru.otus.hw.webserver.messagesystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.hw.messageserver.Message;
import ru.otus.hw.messageserver.SocketClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MessageSystemImpl implements MessageSystem {
    private static final Logger logger = LoggerFactory.getLogger(MessageSystemImpl.class);
    private static final int MESSAGE_QUEUE_SIZE = 1_000;
    private static final int MSG_HANDLER_THREAD_LIMIT = 2;
    private static String MESSAGE_TYPE_REGISTER_CLIENT = "RegisterClient";
    private static String MESSAGE_TYPE_STOPPED_SERVER = "StoppedServer";
    private static final Message VOID_MESSAGE = new Message(
            "",
            "",
            0,
            "",
            "",
            0,
            MESSAGE_TYPE_STOPPED_SERVER,
            ""
        );

    private final AtomicBoolean runFlag = new AtomicBoolean(true);

    private final Map<String, List<MessageClient>> clientMap = new ConcurrentHashMap<>();
    private final BlockingQueue<Message> messageQueue = new ArrayBlockingQueue<>(MESSAGE_QUEUE_SIZE);

    private final SocketClient messageSystemClient;

    private final ExecutorService msgProcessor = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable);
        thread.setName("msg-processor-thread");
        return thread;
    });

    private final ExecutorService msgHandler = Executors.newFixedThreadPool(MSG_HANDLER_THREAD_LIMIT, new ThreadFactory() {
        private final AtomicInteger threadNameSeq = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName("msg-handler-thread-" + threadNameSeq.incrementAndGet());
            return thread;
        }
    });

    public MessageSystemImpl(SocketClient messageSystemClient) {
        this.messageSystemClient = messageSystemClient;
        msgProcessor.submit(this::msgProcessor);
    }

    private void msgProcessor() {
        logger.info("msgProcessor started");
        while (runFlag.get()) {
            try {
                Message msg = messageQueue.take();
                if (msg == VOID_MESSAGE) {
                    logger.info("received the stop message");
                } else {
                    MessageClient clientTo = getClient(msg);
                    if (clientTo == null) {
                        logger.warn("client not found");
                    } else {
                        msgHandler.submit(() -> handleMessage(clientTo, msg));
                    }
                }
            } catch (InterruptedException ex) {
                logger.error(ex.getMessage(), ex);
                Thread.currentThread().interrupt();
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
        msgHandler.submit(this::messageHandlerShutdown);
        logger.info("msgProcessor finished");
    }

    private MessageClient getClient(Message msg) {
        if (clientMap.containsKey(msg.getToClientName())) {
            if (msg.getToHost().isEmpty()) {
                List<MessageClient> clients = clientMap.get(msg.getToClientName());
                return clients.get((int) (Math.random() * clients.size()));
            } else {
                return clientMap.get(msg.getToClientName()).stream()
                        .filter(value ->
                                (msg.getToHost().equals(value.getSocketURL().getHost())
                                        && msg.getToPort() == value.getSocketURL().getPort()))
                        .findFirst()
                        .orElse(null);
            }
        } else {
            return null;
        }
    }

    private void messageHandlerShutdown() {
        msgHandler.shutdown();
        logger.info("msgHandler has been shut down");
    }


    private void handleMessage(MessageClient msClient, Message msg) {
        try {
            msg.setToHost(msClient.getSocketURL().getHost());
            msg.setToPort(msClient.getSocketURL().getPort());
            messageSystemClient.sendMessage(msg);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            logger.error("message:{}", msg);
        }
    }

    private void insertStopMessage() throws InterruptedException {
        boolean result = messageQueue.offer(VOID_MESSAGE);
        while (!result) {
            Thread.sleep(100);
            result = messageQueue.offer(VOID_MESSAGE);
        }
    }

    @Override
    public void addClient(MessageClient msClient) {
        logger.info("new client: {}", msClient.getSocketURL().toString());
        if (clientMap.containsKey(msClient.getName())) {
            clientMap.get(msClient.getName()).add(msClient);
        } else {
            List<MessageClient> messageClients = new ArrayList<>();
            messageClients.add(msClient);
            clientMap.put(msClient.getName(), messageClients);
        }
    }

    @Override
    public void removeClient(String clientId, String host, int port) {
        List<MessageClient> clients = clientMap.get(clientId);
        if (clients == null) {
            logger.warn("client not found: {} {} {}", clientId, host, port);
        } else {
            clients.stream()
                    .filter(value ->
                            value.getSocketURL().getHost().equals(host) && value.getSocketURL().getPort() == port)
                    .findFirst()
                    .ifPresentOrElse(msClient ->
                            {
                                clients.remove(msClient);
                                if (clients.size() == 0) {
                                    clientMap.remove(clientId);
                                }
                                logger.info("removed client:{} {} {}", clientId, host, port);
                            },
                            () -> logger.warn("client not found: {}", clientId, host, port)
                    );
        }
    }

    @Override
    public boolean getMessage(Message msg) {
        if (runFlag.get()) {
            if (msg.getType().equals(MESSAGE_TYPE_REGISTER_CLIENT)) {
                addClient(
                        new MessageClient(
                            msg.getClientName(),
                            msg.getHost(),
                            msg.getPort()
                        )
                );
                return true;
            } else {
                return messageQueue.offer(msg);
            }
        } else {
            logger.warn("MS is being shutting down... rejected:{}", msg);
            return false;
        }
    }

    @Override
    public void dispose() throws InterruptedException {
        runFlag.set(false);
        insertStopMessage();
        msgProcessor.shutdown();
        msgHandler.awaitTermination(60, TimeUnit.SECONDS);
    }
}
