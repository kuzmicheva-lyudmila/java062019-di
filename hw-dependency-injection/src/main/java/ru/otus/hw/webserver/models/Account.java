package ru.otus.hw.webserver.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Entity
public class Account {
    @Id
    private String login;

    private String password;

    public Account() {
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Account(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Account {" +
                "login = " + login +
                ", password = " + password +
                '}';
    }

    public static String passwordHash(String password) {
        String hash = null;
        try {
            MessageDigest digester = MessageDigest.getInstance("SHA-512");
            byte[] digest =  digester.digest(password.getBytes());
            hash = DatatypeConverter.printHexBinary(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
    }
}
