package ru.otus.hw.webserver.database.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.webserver.database.models.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
