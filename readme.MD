# boardcamp-api

This project is a Java Spring Boot API for a board game rental store. It provides all core functionalities required to manage customers, games, and rentals, including validations, pricing, and return logic.

## Features

- **Games**: create and list games with validations for name and stock
- **Customers**: register, search, and list customers with CPF and phone validations
- **Rentals**: create, finish, and delete rentals with late fee calculation
- **Business rules and validations** applied to all operations
- **Unit and integration tests** with minimum required coverage for all entities

## Technologies

The following tools and frameworks were used in the development of this project:

<p>
  <img style='margin: 5px;' src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white" height="40" alt="Java logo" />
  <img style='margin: 5px;' src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" height="40" alt="Spring Boot logo" />
  <img style='margin: 5px;' src="https://img.shields.io/badge/PostgreSQL-336791?style=for-the-badge&logo=postgresql&logoColor=white" height="40" alt="PostgreSQL logo" />
  <img style='margin: 5px;' src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white" height="40" alt="Maven logo" />
  <img style='margin: 5px;' src="https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white" height="40" alt="JUnit5 logo" />
  <img style='margin: 5px;' src="https://img.shields.io/badge/Mockito-FFCD00?style=for-the-badge&logo=mockito&logoColor=black" height="40" alt="Mockito logo" />
  <img style='margin: 5px;' src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white" height="40" alt="Hibernate logo" />
  <img style='margin: 5px;' src="https://img.shields.io/badge/JPA-00599C?style=for-the-badge&logo=java&logoColor=white" height="40" alt="JPA logo" />
</p>


## Getting Started

Prerequisites
- Java 17 or newer
- PostgreSQL database
- Maven

## Installation

### 1. Clone the Repository

```bash
git clone https://github.com/truds99/boardcamp-api.git
```

### 2. Configure the Environment

Create a .env file or configure your application.properties (Spring Boot default):

```bash
spring.datasource.url=jdbc:postgresql://localhost:5432/boardcamp
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password
spring.jpa.hibernate.ddl-auto=update
```

### 3. Run the Project

```bash
./mvnw spring-boot:run
```

## Contributions
Contributions are welcome! Fork the repository and submit a pull request to improve features, fix bugs, or write more tests.

