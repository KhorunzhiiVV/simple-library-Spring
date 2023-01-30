package ru.khorunzhii.simplelibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khorunzhii.simplelibrary.models.Person;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    public Person findByName(String name);
}
