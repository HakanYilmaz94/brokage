# Brokerage Service

This project is a Spring Boot based backend service for brokerage operations.  
It provides authentication, order management, and role-based access control using JWT and Spring Security.

## 📦 Build

Make sure you have **Java 21** and **Maven 3.9+** installed.

bash
mvn clean package -DskipTests


This will create an executable JAR under:

ing\target\brokage-0.0.1-SNAPSHOT.jar

## ▶️ Run
Run with default profile
java -jar ing\target\brokage-0.0.1-SNAPSHOT.jar

Run with a specific profile
java -jar -Dspring.profiles.active=test ing\target\brokage-0.0.1-SNAPSHOT.jar


Available profiles:

test → test environment

prod → production

## ⚙️ Configuration

The application uses profile-specific configuration files:

application-test.properties → test environment settings

application-prod.properties → production settings

Set the active profile via spring.profiles.active system property or environment variable.

## 🐳 Run with Docker

Build docker image:

docker build -t brokerage-service .


Run container:

docker run -p 8080:8080 brokerage-service

## 🔑 Authentication

The service uses JWT tokens for authentication.

Obtain a token by calling the /brokage/getToken endpoint with valid credentials.

Include the token in request headers:

Authorization: Bearer <token>

## 📖 API Documentation

After starting the application, Swagger UI is available at:

http://localhost:5050/brokage/swagger-ui/index.html

## Initial Data Setup (`data.sql`)

When you run the project, some initial data will be inserted into the H2 database. These entries are provided for testing purposes and include customers and assets.

You can obtain a token using the following credentials:

- **Customer 1**
    - Username: `HY`
    - Password: `password`

- **Customer 2**
    - Username: `HY2`
    - Password: `password2`

- **Admin**
    - Username: `admin`
    - Password: `admin`