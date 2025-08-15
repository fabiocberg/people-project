package com.example.people.dto;

import com.example.people.model.Gender;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PersonResponse {
    private Long id;
    private String name;
    private Gender gender;
    private String email;
    private LocalDate birthDate;
    private String naturalness;
    private String nationality;
    private String cpf;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
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
