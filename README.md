# transactions

The codebase was build with:
- Java (jdk 17);
- Gradle (7.4.1);
- Mapstruct (1.4.2);
- Spring Boot (2.6.7);
- Spring Sleuth - distributed tracing;
- Flyway - database versioning;
- h2database - dev, postgresql - prod;
- Springdoc - swagger-ui - openapi v3.

If you want to run it locally, you must have jdk17 as your JAVA_HOME, just run the following command:

```
./gradlew bootRun --args='--spring.profiles.active=dev'
```

Otherwise, you can build up a container:

```
 docker compose up --build
```

Once you have the application running, you can check the documentation on this link: http://localhost:8080/swagger-ui/index.html (suppose you are running on the 8080 port).

### GET /api/status

```
curl -X 'GET' \
  'http://localhost:8080/api/status' \
  -H 'accept: */*'
```

### POST /accounts

```
curl -X 'POST' \
  'http://localhost:8080/accounts' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "document_number": 12345678900
}'
```

### GET /accounts/{id}

```
curl -X 'GET' \
  'http://localhost:8080/accounts/1' \
  -H 'accept: application/json'
```

### POST /transactions

```
curl -X 'POST' \
  'http://localhost:8080/transactions' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "account_id": 1,
  "operation_type_id": 4,
  "amount": 123.45
}'
```
