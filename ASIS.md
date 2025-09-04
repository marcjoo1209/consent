# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Technology Stack

This is a Spring MVC web application using:
- **Java 8** with Spring Framework 4.3.3
- **Spring Security 5.2.1** for authentication
- **MyBatis 3.4.6** for database access with MariaDB
- **Maven** for build management
- **JSP** for views
- **Apache POI** for Excel processing
- **iTextPDF** for PDF generation

## Build Commands

```bash
# Navigate to project directory
cd cfp

# Clean and compile
mvn clean compile

# Package the application
mvn clean package

# Run tests
mvn test

# Skip tests during build
mvn clean package -DskipTests
```

## Project Structure

```
cfp/
├── src/main/java/com/jyj/cfp/
│   ├── controller/     # Spring MVC Controllers
│   ├── service/        # Business logic layer
│   ├── dao/           # Data access objects
│   ├── util/          # Utility classes
│   └── common/        # Common components
├── src/main/resources/
│   ├── mappers/       # MyBatis mapper XML files
│   ├── properties/    # Configuration properties
│   └── mybatis-config.xml
└── src/main/webapp/
    ├── WEB-INF/views/ # JSP view templates
    ├── resources/     # Static resources (CSS, JS, images)
    └── WEB-INF/spring/ # Spring configuration
```

## Database Configuration

The application uses MariaDB with connection details in `src/main/resources/properties/mybatis.properties`. MyBatis is configured for SQL mapping with XML mappers in `src/main/resources/mappers/`.

## Key Architecture Patterns

- **MVC Pattern**: Controllers handle HTTP requests, Services contain business logic, DAOs handle data access
- **Spring Security**: Authentication and authorization configured in `spring-security.xml`
- **MyBatis Integration**: SQL queries defined in XML mapper files, linked to DAO interfaces
- **File Upload Support**: Configured for up to 100MB file uploads via CommonsMultipartResolver