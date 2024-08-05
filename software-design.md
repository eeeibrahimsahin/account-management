# Software Design Java

## Table of Content

* [Software Design Identification](#abc-identification)
* [Artifact Details](#abc-artifact)
* [Context](#abc-context)
* [Domain Model](#abc-domain)
* [Software Structure](#abc-structure)
* [Software Behavior](#abc-behavior)
* [Data Model](#abc-data)
* [API Specification](#abc-api)
* [Java Specifications](#abc-java)
* [Frameworks, Internal Components, and Libraries](#abc-frameworks)
* [Cross Cutting Concerns](#abc-concerns)

# Software Design Identification <a id="abc-identification"></a>

| Name              | Value               |
|-------------------|---------------------|
| Unique identifier | account-management  |
| Version           | Check Git history   |

# Artifact Details <a id="abc-artifact"></a>

| Name           | Value                  |
|----------------|------------------------|
| Group id       | com.abcbank.onboarding |
| Artifact id    | account-management     |

# Context <a id="abc-context"></a>

The project is developed to manage ABC Bank's digital onboarding process. This API facilitates remote customer registration and login processes.

# Domain Model <a id="abc-domain"></a>

The domain model includes customer registration information and login processes. The main class is:
- **Customer**: Holds customer information (name, address, date of birth, ID number, username, password).

# Software Structure <a id="abc-structure"></a>

## Packages

The project is divided into the following main packages:

- **controller**: Classes that handle and process API requests.
- **service**: Service classes containing business logic.
- **repository**: Classes that perform database operations.
- **config**: Classes that contain configuration and security settings.
- **dto**: Classes containing Data Transfer Objects (DTO).


# Software Behavior <a id="abc-behavior"></a>

The project provides two main APIs for customer registration and login processes. Both processes are supported by necessary validation and security controls.

# Data Model <a id="abc-data"></a>

The data model includes tables that hold customer information. The main table is:
- **customers**: Holds customer information.

<!--
![Data Model](images/data-model.png)
-->

# API Specification <a id="abc-api"></a>

### Customer Registration API

* Used for customer registration.

#### Request Body
The `CustomerRegistrationRequestDTO` class is used to capture the details of a new customer registration. This class includes fields such as `name`, `address`, `dateOfBirth`, `idDocumentNumber`, and `username`. Each field is validated using annotations.

#### Response Body
The `CustomerRegistrationResponseDTO` class represents the response body returned upon successful customer registration. This class includes fields such as `username`, `password`, and `iban`.

### Customer Login API

* Allows customers to log in using their username and password.

#### Request Body
The `LoginRequestDTO` class represents the request body containing the information required for user login. This class includes fields such as `username` and `password`.

#### Security Configuration

Spring Security is used to ensure the security of the application. The security configuration includes the following features:
- **Whitelisted Endpoints**: Certain endpoints do not require authentication. This is useful for public endpoints such as registration and login.
- **HTTP Basic Authentication**: Used for simplicity. Can be replaced with more robust methods like JWT for better security.
- **CSRF Protection**: CSRF protection is disabled for simplicity. It should be enabled in a production environment.
- **Password Encoding**: Passwords are securely stored using `BCryptPasswordEncoder`.

### UserDetailsService Implementation

A custom `UserDetailsService` implementation is used to load user-specific data during authentication. This service interacts with the database to retrieve and verify user information.

### Authentication Endpoint

The login endpoint handles authentication requests. It uses the `AuthenticationManager` to authenticate the provided username and password. If authentication is successful, a success message is returned. If authentication fails, an `AuthenticationException` is thrown.

# Service Classes

### DatabaseService

The `DatabaseService` class is responsible for interacting with the database to manage customer data. It includes methods for saving a customer, finding a customer by username, and checking the availability of a username. It also integrates rate limiting to control the frequency of database access.

Key Methods:
- `saveCustomer(Customer customer)`: Saves a customer to the database, checking the rate limit before proceeding.
- `findCustomerByUsername(String username)`: Finds a customer by their username, checking the rate limit before proceeding. Throws `UsernameNotFoundException` if the user is not found.
- `checkUsernameAvailability(String username)`: Checks if a username is available, checking the rate limit before proceeding.
- `rateLimitCheck()`: Ensures that the rate limit is not exceeded by throwing a `TooManyRequestsException` if the limit is surpassed.

### CustomerService

The `CustomerService` class handles the core business logic for customer registration and login processes. It interacts with the `DatabaseService` and other utility classes to manage these processes.

Key Methods:
- `registerCustomer(CustomerRegistrationRequestDTO requestDTO)`: Handles the registration process by checking if the username is available, generating an IBAN, and saving the customer data. Throws `UsernameAlreadyExistsException` if the username is already taken.
- `authenticateCustomer(String username, String password)`: Authenticates a customer by verifying the provided username and password. Throws `AuthenticationException` if the credentials are invalid.

### RateLimiterService

The `RateLimiterService` class uses the Bucket4j library to implement rate limiting. This ensures that the number of requests to the database is controlled to prevent overload.

Key Methods:
- `RateLimiterService()`: Initializes the rate limiter with a limit of 2 requests per second.
- `tryConsume()`: Attempts to consume a token from the rate limiter bucket. Returns `true` if successful, `false` otherwise.

### UserDetailsServiceImpl

The `UserDetailsServiceImpl` class is a custom implementation of Spring Security's `UserDetailsService`. It loads user-specific data during the authentication process.

Key Methods:
- `loadUserByUsername(String username)`: Loads user details by username using the `DatabaseService`. Returns a `User` object containing the user's username and password.

# Java Specifications <a id="abc-java"></a>

## Bean Validation (JSR 380)

* Implemented by Hibernate Validator (used via Spring Boot Starter Validation)
* Used for validating request payloads to ensure data integrity and consistency.

## Contexts and Dependency Injection (CDI)

* Implemented by Spring Framework
* Used to manage the lifecycle of beans and their dependencies.

## Java API for RESTful Web Services 

* Implemented by Spring Boot
* Used to create RESTful web services.

# Frameworks, Internal Components, and Libraries  <a id="abc-frameworks"></a>

## Spring Boot

* **Why Used**: Provides a robust and easy-to-configure framework for building Java applications. It includes embedded servers to run the application standalone.

## Spring Security

* **Why Used**: Ensures secure authentication and authorization mechanisms. Basic HTTP authentication is implemented for simplicity, but can be replaced with JWT for better security.

## Spring Data JPA

* **Why Used**: Simplifies database interactions with JPA (Java Persistence API). Used for ORM (Object Relational Mapping) to manage database operations.

## MySQL Connector/J

* **Why Used**: Provides connectivity with MySQL database from Java applications.

## Lombok

* **Why Used**: Reduces boilerplate code by providing annotations to automatically generate getters, setters, and other common methods.

## Spring Boot Starter Web

* **Why Used**: Facilitates building web applications and RESTful services with Spring MVC.

## Spring Boot Starter Validation

* **Why Used**: Provides validation functionality to ensure data integrity and consistency in request payloads.

## Bucket4j

* **Why Used**: Implements rate limiting to control the number of requests sent to the database, preventing overload.

## Springdoc OpenAPI

* **Why Used**: Generates interactive API documentation for Spring Boot applications using OpenAPI.

## SLF4J with Logback

* **Why Used**: Used for logging application events, aiding in tracking the application's behavior and diagnosing issues.

## AspectJ

* **Why Used**: Used to implement cross-cutting concerns like logging using AOP (Aspect-Oriented Programming).

# Cross Cutting Concerns <a id="abc-concerns"></a>

## Security, Authentication, Authorization

* **Current Solution**: Basic HTTP authentication is implemented for simplicity.
* **Alternative Solutions**: Implementing JWT (JSON Web Token) for stateless and more secure authentication would be a better solution.

## Error and Exception Handling

* Centralized error handling is implemented to manage application errors and provide meaningful responses. This ensures that all exceptions are properly caught and handled, returning user-friendly error messages.

## Logging, Monitoring, Observability

* **Logging**: SLF4J with Logback is used for logging application events. An `Aspect` is used to log method calls for controllers, services, and repositories. This helps in tracking the application's behavior and diagnosing issues.
* **Monitoring**: Micrometer is used for collecting metrics and monitoring the application's performance. Future implementations may include integrating monitoring solutions like Prometheus and Grafana for better observability.

## Configuration Management

* Configuration is managed via `application.properties` file, ensuring that all configurable properties are centralized and easily manageable. Sensitive data is managed using environment variables to enhance security.

## Communication, Data Transfer

* RESTful APIs are used for communication and data transfer between client and server. The APIs are designed to be stateless, ensuring scalability and simplicity in interactions.