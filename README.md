# People Project

Aplicação **fullstack** para cadastro e gerenciamento de pessoas.  
Inclui **autenticação de usuários, CRUD de pessoas** e interface web integrada, com suporte a execução via **Docker Compose** ou diretamente pelo código fonte.

---

## 🚀 Funcionalidades

- Login de usuário com autenticação JWT.  
- Cadastro, listagem, edição e remoção de pessoas.  
- Validação de CPF no frontend.  
- Integração entre frontend (React) e backend (Spring Boot).  
- Deploy simplificado com Docker Compose.  

---

## 🛠️ Arquitetura

```
people-project/
│── backend/       # API REST em Spring Boot
│   ├── src/main/java/com/example/people/
│   │   ├── auth/         # Autenticação e usuários
│   │   ├── config/       # Configurações de segurança e CORS
│   │   ├── controller/   # Controllers (Auth, Person)
│   │   ├── dto/          # DTOs de entrada/saída
│   │   ├── exception/    # Tratamento de erros
│   │   └── ...           # Classe principal (PeopleApiApplication.java)
│
│── frontend/      # Interface web em React + Vite + TS
│   ├── src/
│   │   ├── pages/        # Páginas (Login, People)
│   │   ├── auth/         # Contexto de autenticação
│   │   ├── api/          # Cliente HTTP
│   │   ├── utils/        # Utilitários (ex: validação de CPF)
│   │   └── App.tsx       # App principal
│
│── docker-compose.yml        # Orquestração padrão
│── docker-compose.yml.local  # Versão para rodar localmente
│── docker-compose.yml.prod   # Versão para produção
└── README.md
```

---

## 📦 Tecnologias Utilizadas

### Backend
- Java 17+  
- Spring Boot  
- Spring Security + JWT  
- Spring Data JPA  
- Banco de dados (configurável via Docker Compose)  
- Maven  

### Frontend
- React  
- TypeScript  
- Vite  
- Context API para autenticação  
- Axios para chamadas à API  
- Nginx (em modo produção com Docker)  

### Infra
- Docker & Docker Compose  

---

## ▶️ Como Rodar o Projeto

### 1. Usando Docker (recomendado)
Na raiz do projeto:
```bash
docker compose up -d --build
```

- **Frontend:** [http://localhost:5173](http://localhost:5173)  
- **Backend API:** [http://localhost:8080](http://localhost:8080)  

**Login padrão:**  
```
Usuário: admin  
Senha: admin
```

---

### 2. Rodar localmente (sem Docker)

#### Backend
```bash
cd backend
mvn spring-boot:run
```
API disponível em: [http://localhost:8080](http://localhost:8080)

#### Frontend
```bash
cd frontend
echo 'VITE_API_BASE_URL=http://localhost:8080' > .env
npm install
npm run dev
```
Acesse: [http://localhost:5173](http://localhost:5173)

---

## 📌 Próximos Passos

- Adicionar testes unitários (JUnit no backend, React Testing Library no frontend).  
- Adicionar CI/CD com GitHub Actions.  
- Melhorar UI/UX do frontend.  
- Suporte a outros bancos de dados no backend.  
