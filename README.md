# People Monorepo (Backend + Frontend)

## Rodar a partir do código fonte local

Assim que o código fonte é baixado, o docker-compose está buscando as imagens no docker-hub. Para utilizar a partir do código local, faça o seguinte:

Atualize o conteúdo do arquivo **docker-compose.yml** com o conteúdo do arquivo **docker-compose.yml.local**.

## Rodar com Docker

```bash
docker compose up -d --build
# Frontend: http://localhost:5173
# Backend:  http://localhost:8080
# Login: admin / admin
```

## Rodar localmente (sem Docker)

Backend:

```bash
cd backend
mvn spring-boot:run
```

Frontend:

```bash
cd frontend
echo 'VITE_API_BASE_URL=http://localhost:8080' > .env
npm install
npm run dev
# http://localhost:5173
```
