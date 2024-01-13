# Yoga App !

Yoga is a booking platform for Yoga sessions.

This is the Yoga app back-end.

## Prerequisites

Java 11

Maven 3.8.6

MySQL 8.0.35

## Installation

First install [Java](https://www.oracle.com/fr/java/technologies/downloads/#java17) according your operating system.

Then install [Maven](https://maven.apache.org/install.html).

And finally install [MySQL](https://dev.mysql.com/doc/mysql-installation-excerpt/8.0/en/).

Go to `resources/sql` in the root directory and :
* open a terminal,
* connect to mysql with your credentials,
* create a database `CREATE DATABASE yoga;`,
* import the SQL script `SOURCE script.sql;`.

Open `src/main/resources/application.properties` and update `spring.datasource.username` and `spring.datasource.password` with your MySQL credentials.

## Tests

In the back-end root directory, run `mvn clean verify` to run the tests and package the app.

Tests report with Jacoco code coverage is available at `target/site/jacoco/index.html`.

## Run

Go to `target` directory and execute `java -jar yoga-app-0.0.1-SNAPSHOT.jar`.

By default the admin account is:
- login: yoga@studio.com
- password: test!1234

You can test the back-end with a tool like `Postman`. A collection is available at `resources/postman`.

## Folder structure

```bash
.
├── src
   ├── main
   │   ├── java
   │   │   └── com
   │   │       └── openclassrooms
   │   │           └── starterjwt
   │   │               ├── controllers
   │   │               ├── dto
   │   │               ├── exception
   │   │               ├── mapper
   │   │               ├── models
   │   │               ├── payload
   │   │               │   ├── request
   │   │               │   └── response
   │   │               ├── repository
   │   │               ├── security
   │   │               │   ├── jwt
   │   │               │   └── services
   │   │               └── services
   │   └── resources
   └── test
       └── java
           └── com
               └── openclassrooms
                   └── starterjwt
                       ├── integration
                       │   ├── controller
                       │   ├── mapper
                       │   ├── repository
                       │   ├── security
                       │   └── service
                       └── unit
                           ├── controller
                           ├── security
                           └── service
```

## Author

Gilles BERNARD (@tipikae)
