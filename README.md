# TaskFlow Pro

![Badge: Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen)
![Badge: Java](https://img.shields.io/badge/Java-17-orange)
![Badge: MySQL](https://img.shields.io/badge/MySQL-8.0-blue)

## Introduction

ProjectTaskHub is a comprehensive task and project management application developed to help teams track and manage project progress effectively. Built with Spring Boot, the application provides a user-friendly interface and powerful features to support team workflows.

## Key Features

- **Project Management**: Create, update, and track multiple projects simultaneously
- **Task Management**: Break down projects into smaller, manageable tasks
- **Work Assignment**: Assign tasks to team members
- **Progress Tracking**: View progress reports and detailed project statistics
- **Project Announcements**: Send important notifications to project members
- **Document Management**: Attach files and documents to projects and tasks
- **Comments and Interaction**: Comment on tasks for discussion and updates
- **User Role Management**: Role-based access control system

## Technologies Used

- **Backend**: Spring Boot, Spring Security, Spring Data JPA
- **Frontend**: Thymeleaf, HTML, CSS, JavaScript
- **Database**: MySQL
- **Build Tool**: Maven
- **Additional Libraries**: Lombok, MapStruct, Cloudinary

## Installation and Running the Application

### Prerequisites

- JDK 17 or higher
- Maven 3.6+
- MySQL 8.0+

### Step 1: Clone the project

```bash
git clone https://github.com/your-username/taskflow-pro.git
cd taskflow-pro
```

### Step 2: Configure the database

Create an `application.properties` file in the `src/main/resources` directory with the following content:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/taskflow_pro?createDatabaseIfNotExist=true
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

### Step 3: Build and run the application

```bash
mvn clean install
mvn spring-boot:run
```

The application will be available at [http://localhost:8080](http://localhost:8080)

## Project Structure

```
taskmanagement/
├── src/
│   ├── main/
│   │   ├── java/com/team11/taskmanagement/
│   │   │   ├── config/           # Application configuration
│   │   │   ├── controller/       # REST and MVC controllers
│   │   │   ├── dto/              # Data transfer objects
│   │   │   ├── exception/        # Exception handling
│   │   │   ├── mapper/           # MapStruct for Entity-DTO conversion
│   │   │   ├── model/            # Data models and entities
│   │   │   ├── repository/       # JPA repository interfaces
│   │   │   └── service/          # Business logic
│   │   └── resources/            # Application resources
│   └── test/                     # Unit tests
└── pom.xml                       # Maven configuration
```
