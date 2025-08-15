package com.example.people.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.people.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByCpf(String cpf);
    Optional<Person> findByEmailIgnoreCase(String email);
    boolean existsByCpf(String cpf);
    boolean existsByCpfAndIdNot(String cpf, Long id);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
}
