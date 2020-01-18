package ru.otus.hw.webserver.backend.models;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table(name= "person", schema = "persons")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fullname;
    private Integer age;

    public Person() {}

    public Person(String fullname, Integer age) {
        this.fullname = fullname;
        this.age = age;
    }

    @Override
    public String toString() {
        return String.format(
                "Person {id=%d, fullname='%s', age='%s'}",
                id, fullname, age);
    }
}
