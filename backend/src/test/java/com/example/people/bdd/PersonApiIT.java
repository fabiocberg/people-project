package com.example.people.bdd;

import com.example.people.PeopleApiApplication;
import com.example.people.repository.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = PeopleApiApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PersonApiIT {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired PersonRepository personRepository;

    private String token;

    @BeforeEach
    void setup() throws Exception {
        personRepository.deleteAll();
        token = obtainToken("admin","admin");
    }

    String obtainToken(String u, String p) throws Exception {
        Map<String,String> login = new HashMap<>();
        login.put("username", u);
        login.put("password", p);

        MvcResult res = mvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", not(emptyOrNullString())))
                .andReturn();

        String body = res.getResponse().getContentAsString();
        return objectMapper.readTree(body).path("token").asText();
    }

    @Test
    @DisplayName("Deve criar e listar pessoa (fluxo feliz)")
    void shouldCreateAndListPerson() throws Exception {
        Map<String,Object> p = new HashMap<>();
        p.put("name","Maria");
        p.put("birthDate","1990-05-10");
        p.put("cpf","390.533.447-05");
        p.put("email","maria@exemplo.com");

        mvc.perform(post("/api/v1/people")
                        .header("Authorization","Bearer "+token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name").value("Maria"));

        mvc.perform(get("/api/v1/people")
                        .header("Authorization","Bearer "+token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].cpf").value("39053344705"));
    }

    @Test
    @DisplayName("Não deve permitir CPF duplicado")
    void shouldRejectDuplicateCpf() throws Exception {
        Map<String,Object> p1 = new HashMap<>();
        p1.put("name","Maria");
        p1.put("birthDate","1990-05-10");
        p1.put("cpf","390.533.447-05");
        p1.put("email","m1@exemplo.com");

        mvc.perform(post("/api/v1/people")
                        .header("Authorization","Bearer "+token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p1)))
                .andExpect(status().isCreated());

        Map<String,Object> p2 = new HashMap<>();
        p2.put("name","Outra");
        p2.put("birthDate","1992-01-01");
        p2.put("cpf","390.533.447-05");
        p2.put("email","m2@exemplo.com");

        mvc.perform(post("/api/v1/people")
                        .header("Authorization","Bearer "+token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p2)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("CPF já existente."));
    }

    @Test
    @DisplayName("Não deve permitir e-mail duplicado (case-insensitive)")
    void shouldRejectDuplicateEmail() throws Exception {
        Map<String,Object> p1 = new HashMap<>();
        p1.put("name","Maria");
        p1.put("birthDate","1990-05-10");
        p1.put("cpf","390.533.447-05");
        p1.put("email","maria@exemplo.com");

        mvc.perform(post("/api/v1/people")
                        .header("Authorization","Bearer "+token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p1)))
                .andExpect(status().isCreated());

        Map<String,Object> p2 = new HashMap<>();
        p2.put("name","Outra");
        p2.put("birthDate","1992-01-01");
        p2.put("cpf","123.456.789-09");
        p2.put("email","MARIA@EXEMPLO.COM");

        mvc.perform(post("/api/v1/people")
                        .header("Authorization","Bearer "+token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p2)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email já existente."));
    }

    @Test
    @DisplayName("Validações obrigatórias (nome e nascimento) → 400")
    void shouldValidateRequiredFields() throws Exception {
        Map<String,Object> body = new HashMap<>();
        body.put("cpf","390.533.447-05");

        mvc.perform(post("/api/v1/people")
                        .header("Authorization","Bearer "+token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name", not(emptyString())))
                .andExpect(jsonPath("$.birthDate", not(emptyString())));
    }
}
