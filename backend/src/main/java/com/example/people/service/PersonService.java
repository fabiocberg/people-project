package com.example.people.service;

import com.example.people.dto.PersonRequest;
import com.example.people.dto.PersonResponse;
import com.example.people.exception.ConflictException;
import com.example.people.exception.NotFoundException;
import com.example.people.model.Person;
import com.example.people.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {
    private final PersonRepository repository;
    public PersonService(PersonRepository repository) { this.repository = repository; }

    @Transactional
    public PersonResponse create(PersonRequest req) {
        String normalizedCpf = req.getCpf().replaceAll("[^0-9]", "");
        String email = req.getEmail() != null ? req.getEmail().trim().toLowerCase() : null;
        if (repository.existsByCpf(normalizedCpf)) throw new ConflictException("CPF já existente.");
        if (email != null && repository.existsByEmailIgnoreCase(email)) throw new ConflictException("Email já existente.");
        Person p = toEntity(new Person(), req);
        p.setCpf(normalizedCpf);
        p.setEmail(email);
        return toResponse(repository.save(p));
    }

    @Transactional(readOnly = true)
    public PersonResponse getById(Long id) {
        Person p = repository.findById(id).orElseThrow(() -> new NotFoundException("Pessoa não encontrada."));
        return toResponse(p);
    }

    @Transactional(readOnly = true)
    public PersonResponse getByCpf(String cpf) {
        String normalizedCpf = cpf.replaceAll("[^0-9]", "");
        Person p = repository.findByCpf(normalizedCpf).orElseThrow(() -> new NotFoundException("Pessoa não encontrada."));
        return toResponse(p);
    }

    @Transactional(readOnly = true)
    public List<PersonResponse> listAll() {
        return repository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public PersonResponse update(Long id, PersonRequest req) {
        Person p = repository.findById(id).orElseThrow(() -> new NotFoundException("Pessoa não encontrada."));
        String newCpf = req.getCpf() != null ? req.getCpf().replaceAll("[^0-9]", "") : p.getCpf();
        String newEmail = req.getEmail() != null ? req.getEmail().trim().toLowerCase() : p.getEmail();
        if (!p.getCpf().equals(newCpf) && repository.existsByCpfAndIdNot(newCpf, p.getId())) throw new ConflictException("CPF já existente.");
        if (newEmail != null) {
            String currentEmail = p.getEmail();
            boolean emailChanged = currentEmail == null || !currentEmail.equalsIgnoreCase(newEmail);
            if (emailChanged && repository.existsByEmailIgnoreCaseAndIdNot(newEmail, p.getId())) throw new ConflictException("Email já existente.");
        }
        toEntity(p, req);
        p.setCpf(newCpf);
        p.setEmail(newEmail);
        return toResponse(repository.save(p));
    }

    @Transactional
    public void delete(Long id) {
        Person p = repository.findById(id).orElseThrow(() -> new NotFoundException("Pessoa não encontrada."));
        repository.delete(p);
    }

    private Person toEntity(Person p, PersonRequest req) {
        p.setName(req.getName());
        p.setGender(req.getGender());
        p.setEmail(req.getEmail());
        p.setBirthDate(req.getBirthDate());
        p.setNaturalness(req.getNaturalness());
        p.setNationality(req.getNationality());
        return p;
    }

    private PersonResponse toResponse(Person p) {
        PersonResponse r = new PersonResponse();
        r.setId(p.getId()); r.setName(p.getName()); r.setGender(p.getGender()); r.setEmail(p.getEmail());
        r.setBirthDate(p.getBirthDate()); r.setNaturalness(p.getNaturalness()); r.setNationality(p.getNationality());
        r.setCpf(p.getCpf()); r.setCreatedAt(p.getCreatedAt()); r.setUpdatedAt(p.getUpdatedAt());
        return r;
    }
}
