# Guitar Management System

A Guitar Management System built with Java and Spring Framework that allows for the management of guitar data with CRUD operations. This application utilizes Spring Boot for easy setup and configuration, Spring Security for secure authentication, and Thymeleaf for rendering dynamic web pages.

## Render URL
- https://gms-q1b2.onrender.com/
- https://gms-q1b2.onrender.com/registration

## Features

- **Guitar Registration**: Add new guitars to the system.
- **User Authentication**: Securely log in with JWT-based authorization.
- **CRUD Operations**: Create, Read, Update, and Delete guitar data.
- **API Endpoints**: Expose RESTful APIs for guitar management.
- **Templating**: Render dynamic HTML pages using Thymeleaf.
- **Security**: Implement authentication and authorization with Spring Security.

## Technologies Used

- **Java**: Programming language used for building the application.
- **Spring Framework**: Comprehensive framework for enterprise Java development.
- **Spring Boot**: Framework for building standalone applications with Spring.
- **Spring Web**: Module for building web applications and RESTful APIs.
- **Spring Security**: Security framework for authentication and authorization.
- **Thymeleaf**: Templating engine for rendering HTML pages.
- **MySQL**: Relational database management system for storing guitar data.
- **Lombok**: Library to reduce boilerplate code in Java.
- **JWT**: JSON Web Tokens for secure user authentication.

## Getting Started

### Prerequisites

- Java 11 or higher
- MySQL Server
- Maven (or Gradle)

### Installation

1. **Clone the repository:**

   ```bash
   git clone https://github.com/emignardi/gms.git
   cd gms

2. **Create Database and Update application.properties Configuration:**

   ```bash
   CREATE DATABASE gms;

3. **Build the Application:**

   ```bash
   mvn clean install OR ./gradlew build

4. **Run the Application:**

   ```bash
   mvn spring-boot:run

![screenshot](/images/index.png)
![screenshot](/images/create.png)
![screenshot](/images/update.png)
