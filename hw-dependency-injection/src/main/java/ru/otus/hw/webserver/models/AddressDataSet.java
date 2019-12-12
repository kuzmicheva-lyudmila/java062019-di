package ru.otus.hw.webserver.models;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class AddressDataSet {
    @Id
    @GeneratedValue
    private Long id;

    private String street;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public AddressDataSet() {
    }

    public AddressDataSet(String street, User user) {
        this.street = street;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Address {" +
                "id = " + id +
                ", userId = " + user.getId() +
                ", street = '" + street + '\'' +
                '}';
    }
}

