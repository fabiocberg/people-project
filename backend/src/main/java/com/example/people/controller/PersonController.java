package com.example.people.controller;

import com.example.people.dto.PersonRequest;
import com.example.people.dto.PersonResponse;
import com.example.people.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/people")
@Validated
public class PersonController {
    private final PersonService service;
    public PersonController(PersonService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<PersonResponse> create(@Valid @RequestBody PersonRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
    }

    @GetMapping("/{id}")
    public PersonResponse getById(@PathVariable Long id) { return service.getById(id); }

    @GetMapping
    public ResponseEntity<?> listOrFindByCpf(@RequestParam(value = "cpf", required = false) String cpf) {
        if (cpf != null && !cpf.trim().isEmpty()) return ResponseEntity.ok(service.getByCpf(cpf));
        List<PersonResponse> list = service.listAll();
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    public PersonResponse update(@PathVariable Long id, @Valid @RequestBody PersonRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
