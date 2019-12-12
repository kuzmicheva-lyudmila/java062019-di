package ru.otus.hw.webserver.models;

import javax.persistence.*;

@Entity
@Table(name = "phone")
public class PhoneDataSet {
    @Id
    @GeneratedValue
    private Long id;

    private String number;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public PhoneDataSet() {
    }

    public PhoneDataSet(String number, User user) {
        this.number = number;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Phone {" +
                "id = " + id +
                ", userId = " + user.getId() +
                ", number = '" + number + '\'' +
                '}';
    }
}
