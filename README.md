# ABC Bank Digital Onboarding API

## Project Overview

ABC Bank has launched a new digital transformation project to enable customers to register and open new accounts
remotely using any electronic device. This project involves developing backend REST APIs to facilitate this
functionality.

## Requirements

- Java 17+
- Maven
- MySQL 
- Docker

## Setup and Running

### 1. Clone the Repository

First, clone the project to your local machine:

```bash
git clone https://github.com/eeeibrahimsahin/account-management.git
cd account-management
```
### 2. Configure the Application Properties
Open the `src/main/resources/application.properties` file and configure the following properties according to your setup:

```properties
spring.application.name=Account Management
spring.datasource.url=jdbc:mysql://localhost:3307/abc_bank
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
security.whitelist=/actuator/**,/swagger-ui/**,/swagger-ui.html,/v3/api-docs/**,/api/register,/api/logon
debug.mode=true
```
- **Whitelisted Endpoints**: The endpoints listed in the `security.whitelist` property can be accessed without logging in.
- **Debug Mode**: When `debug.mode` is set to true, log information for all methods invoked by a request will be logged both before and after execution.

Ensure that the MySQL database is running on the specified host and port, and update the username and password accordingly.


### 3. Package the Application with Maven
   To avoid issues related to the database not being up during the Maven build, you have two options:

#### Option 1: Skip Tests During Build
Skip the tests during the build process (the tests require the database to be up), use the following command:

```bash
mvn clean package -DskipTests
```
### 4. Running with Docker and Docker Compose

#### Running with Docker

To run the application as a Docker container, first build the Docker image:

```bash
docker build -t abc-bank-api .
````

#### Running with Docker Compose - DB and APP together
Run the application and the database together using Docker Compose:

```bash
docker-compose up --build
```
## API Usage
### 1. Customer Registration
   To register a new customer, use the following endpoint:

```bash
POST http://localhost:8080/register
````
#### Request:
```json
{
"name": "John Doe",
"address": "123 Main St",
"dob": "1990-01-01",
"idDocument": "123456789",
"username": "johndoe"
}
```
### Response:
#### Success:

```json
{
"username": "johndoe",
"password": "generatedPassword",
"iban": ""
}
```
#### Failure:

```json
{
  "traceId": "123e4567-e89b-12d3-a456-426614174000",
  "status": 409,
  "message": "Username already exists"
}
```

### 2. Customer Login
   To log in an existing customer, use the following endpoint:

```bash
POST http://localhost:8080/logon
```
#### Request:
```json
{
"username": "johndoe",
"password": "generatedPassword"
}
```
### Response:
#### Success:

```json
{
"message": "Login successful"
}
```
#### Failure:

```json
{
  "traceId": "123e4567-e89b-12d3-a456-426614174000",
  "status": 401,
  "message": "Invalid username or password"
}
```
### Running Tests
#### To run the JUnit tests:

```bash
mvn test
```
### API Documentation
Access the API documentation and test it via Swagger UI:

```bash
http://localhost:8080/swagger-ui.html
```
### Postman Collection
Import the provided Postman collection (`postman_collection.json`) to test various API scenarios.

### Error Handling
The application distinguishes between technical (HTTP 500) and functional (HTTP 400) errors. Errors are returned with appropriate messages.

## Design Decisions and Alternatives

### Rate Limiting for DB

- **Current Solution**: The `bucket4j` library was used for rate limiting as a simple solution to control the number of requests sent to the database.
- **Alternative Solutions**: '
  - More robust queuing solutions like RabbitMQ or Kafka could be implemented for better scalability and fault tolerance.
  -  Additionally, limiting the number of database connections can also serve as an effective way to control the load on the database, ensuring stability and consistent performance under heavy request volumes.

### Authentication Mechanism

- **Current Solution**: Basic HTTP authentication is used for simplicity.
- **Alternative Solutions**: 
  - Implementing JWT (JSON Web Token) for stateless and more secure authentication would be a better solution.
  - Additionally, considering OAuth2 could provide a more secure and flexible framework for access delegation, which is particularly useful in applications that require integration with other services.


### Contact
For any questions or issues, please contact [eeeibrahimsahin@gmail.com].



