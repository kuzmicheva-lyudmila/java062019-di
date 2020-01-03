       var stompClient = null;

       function setConnected(connected) {
           $("#connect").prop("disabled", connected);
           $("#disconnect").prop("disabled", !connected);
           if (connected) {
               $("#conversation").show();
           }
           else {
               $("#conversation").hide();
           }
           $("#userList").html("");
       }

       function connect() {
           var socket = new SockJS('/gs-guide-websocket');
           stompClient = Stomp.over(socket);
           stompClient.connect({}, function (frame) {
               setConnected(true);
               console.log('Connected: ' + frame);
               stompClient.subscribe('/topic/users', function (msg) {
                    showUserList(msg.body);
               });
           });
       }

       function disconnect() {
           if (stompClient !== null) {
               stompClient.disconnect();
           }
           setConnected(false);
           console.log("Disconnected");
       }

       function saveUser() {
           stompClient.send("/app/create-user", {}, JSON.stringify({'name': $("#name").val(), 'age': $("#age").val()}));
       }

       function getUsers() {
           stompClient.send("/app/list-users", {}, '');
       }

       function showUserList(message) {
           $("#userList").append("<tr><td>" + message + "</td></tr>");
       }

       $(function () {
           $("form").on('submit', function (e) {
               e.preventDefault();
           });
           $( "#connect" ).click(function() { connect(); });
           $( "#disconnect" ).click(function() { disconnect(); });
           $( "#save" ).click(function() { saveUser(); });
           $( "#list" ).click(function() { getUsers(); });
       });