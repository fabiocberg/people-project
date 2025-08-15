package com.example.people.service;

import com.example.people.dto.PersonRequest;
import com.example.people.dto.PersonResponse;
import com.example.people.exception.ConflictException;
import com.example.people.exception.NotFoundException;
import com.example.people.model.Person;
import com.example.people.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {
    @Mock private PersonRepository repository;
    @InjectMocks private PersonService service;
    private PersonRequest req;

    @BeforeEach
    void setup() {
        req = new PersonRequest();
        req.setName("Maria");
        req.setBirthDate(LocalDate.of(1990, 5, 10));
        req.setCpf("390.533.447-05");
        req.setEmail("  MARIA@EXEMPLO.COM ");
    }

    @Test
    void create_success_normalizesAndPersists() {
        when(repository.existsByCpf("39053344705")).thenReturn(false);
        when(repository.existsByEmailIgnoreCase("maria@exemplo.com")).thenReturn(false);
        Person saved = new Person();
        saved.setId(1L); saved.setName("Maria"); saved.setCpf("39053344705"); saved.setEmail("maria@exemplo.com");
        when(repository.save(any(Person.class))).thenReturn(saved);
        PersonResponse resp = service.create(req);
        assertEquals(1L, resp.getId()); assertEquals("Maria", resp.getName());
        assertEquals("39053344705", resp.getCpf()); assertEquals("maria@exemplo.com", resp.getEmail());
        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        verify(repository).save(captor.capture());
        assertEquals("39053344705", captor.getValue().getCpf());
        assertEquals("maria@exemplo.com", captor.getValue().getEmail());
    }

    @Test void create_conflict_cpf() {
        when(repository.existsByCpf("39053344705")).thenReturn(true);
        ConflictException ex = assertThrows(ConflictException.class, () -> service.create(req));
        assertEquals("CPF já existente.", ex.getMessage());
    }

    @Test void create_conflict_email() {
        when(repository.existsByCpf("39053344705")).thenReturn(false);
        when(repository.existsByEmailIgnoreCase("maria@exemplo.com")).thenReturn(true);
        ConflictException ex = assertThrows(ConflictException.class, () -> service.create(req));
        assertEquals("Email já existente.", ex.getMessage());
    }

    @Test void getById_notFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getById(99L));
    }
}
