# Bibliotopia Storage

## Running tests

Before running the tests, start the test PostgreSQL and MinIO containers:

```bash
docker compose -f docker-compose.yml -f docker-compose.test.yml up -d
```

Apply the database schema and test data from the repository root. The schema migrations are in `boot/src/main/resources/db/migration`; test data is in `boot/src/test/resources/db/testdata`.

```bash
./mvnw -N flyway:migrate \
  -Dflyway.url=jdbc:postgresql://localhost:5432/bibliotopia \
  -Dflyway.user=user \
  -Dflyway.password=user-pwd \
  -Dflyway.locations=filesystem:boot/src/main/resources/db/migration,filesystem:boot/src/test/resources/db/testdata
```

Finally, run the unit and integration tests:

```bash
./mvnw clean install
```

Run the migration command only against the test database. On its first run, the test database schema must be empty. If an existing Docker volume contains tables created by the former Docker init scripts, reset that volume before migrating.
