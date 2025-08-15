package com.example.people.model;

import com.example.people.validation.CPF;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "people", indexes = {
        @Index(name = "idx_people_cpf", columnList = "cpf", unique = true),
        @Index(name = "idx_people_email", columnList = "email", unique = true)
})
public class Person {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Email(message = "E-mail inválido")
    @Column(unique = true)
    private String email;

    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "Data de nascimento deve estar no passado")
    @Column(nullable = false)
    private LocalDate birthDate;

    private String naturalness;
    private String nationality;

    @NotBlank(message = "CPF é obrigatório")
    @CPF
    @Column(nullable = false, unique = true, length = 11)
    private String cpf; // apenas dígitos

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        normalizeCpf();
        normalizeEmail();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        normalizeCpf();
        normalizeEmail();
    }

    private void normalizeCpf() { if (this.cpf != null) this.cpf = this.cpf.replaceAll("[^0-9]", ""); }
    private void normalizeEmail() { if (this.email != null) this.email = this.email.trim().toLowerCase(); }

    // Getters/setters
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public Gender getGender() { return gender; } public void setGender(Gender gender) { this.gender = gender; }
    public String getEmail() { return email; } public void setEmail(String email) { this.email = email; }
    public LocalDate getBirthDate() { return birthDate; } public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public String getNaturalness() { return naturalness; } public void setNaturalness(String naturalness) { this.naturalness = naturalness; }
    public String getNationality() { return nationality; } public void setNationality(String nationality) { this.nationality = nationality; }
    public String getCpf() { return cpf; } public void setCpf(String cpf) { this.cpf = cpf; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; } public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
