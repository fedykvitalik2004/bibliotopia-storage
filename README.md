# Bibliotopia BFF

## Database Migrations

To ensure your local database schema is synchronized with your current code, you must execute the Flyway migration scripts.

Before running this command, ensure your Docker container is up and running (e.g., docker compose up -d). From the project root, run:

```bash
mvn flyway:migrate -Dflyway.url=jdbc:postgresql://localhost:5432/bibliotopia -Dflyway.user=user -Dflyway.password=user-pwd
```

## Testing Setup

To start the containers specifically for the test environment, run the following command in your terminal:

```bash
docker compose -f docker-compose.yml -f docker-compose.test.yml up
```