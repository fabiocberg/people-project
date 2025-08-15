package com.example.people.dto;

import com.example.people.model.Gender;
import com.example.people.validation.CPF;
import javax.validation.constraints.*;
import java.time.LocalDate;

public class PersonRequest {
    @NotBlank(message = "Nome é obrigatório")
    private String name;
    private Gender gender;
    @Email(message = "E-mail inválido")
    private String email;
    @NotNull @Past
    private LocalDate birthDate;
    private String naturalness;
    private String nationality;
    @NotBlank @CPF
    private String cpf;
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public Gender getGender() { return gender; } public void setGender(Gender gender) { this.gender = gender; }
    public String getEmail() { return email; } public void setEmail(String email) { this.email = email; }
    public LocalDate getBirthDate() { return birthDate; } public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public String getNaturalness() { return naturalness; } public void setNaturalness(String naturalness) { this.naturalness = naturalness; }
    public String getNationality() { return nationality; } public void setNationality(String nationality) { this.nationality = nationality; }
    public String getCpf() { return cpf; } public void setCpf(String cpf) { this.cpf = cpf; }
}
