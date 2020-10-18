package ru.otus.hw.webserver.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.webserver.backend.models.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
