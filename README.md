# A note-taking application based on John Crickett's 'Build Your Own Google Keep' Coding Challenge

This is a full-stack notes app based on John Crickett's 'Build Your Own Google Keep' challenge, from the Coding Challenges series.

See the challenge here: https://codingchallenges.fyi/challenges/challenge-keep/

I'm currently building the back-end which is a Spring Boot application, that uses Java version 21, and Maven to manage dependencies and project builds. Spring Data JPA is used to simplify interactions between the MySQL database and the RESTful API.

When I reach the relevant step in the challege, the client side will be set up as a React and TypeScript project which has been generated using Vite.js. The intention is to use Tailwind.css for styling. 

I am planning to push regular updates, so feel free to follow me on here, on alternatively on LinkedIn: https://www.linkedin.com/in/vinaygajjar/.

# MySQL Tables
There is currently just one MySQL table, for persisting a Note entity:

```
+---------+-------------+------+-----+---------+----------------+
| Field   | Type        | Null | Key | Default | Extra          |
+---------+-------------+------+-----+---------+----------------+
| id      | int         | NO   | PRI | NULL    | auto_increment |
| title   | varchar(45) | YES  |     | NULL    |                |
| content | text        | NO   |     | NULL    |                |
+---------+-------------+------+-----+---------+----------------+
```

```
CREATE TABLE `note` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(45) DEFAULT NULL,
  `content` text NOT NULL,
  PRIMARY KEY (`id`)
)
```

# Installation

Cloning the repository: 
```
git clone https://github.com/v-gajjar/NotesApp.git
```

# Using an IDE
I would highly recommend using an IDE to run the project. 

I am using IntelliJ IDEA Community Edition.

# Running the main application
Before the main application can be run, there are a few environment variables that must be set up. 
A JVM parameter must also be specified to select the logging behaviour.

## Environment Variables
Environment variables are currently used to specify database connection related information, and a filepath for where the logging file should be stored. 


| File Name | Location |
| :--- | :--- |
| application.properties | NotesService/src/main/resources |
| logback-spring.xml | NotesService/src/main/resources |


### Variables used in the application.properties file 

| Name | Used to Set |
| :--- | :--- |
| db_url | spring.datasource.url |
| db_user | spring.datasource.username|
| db_password | spring.datasource.password |

### Variables used in the logback-spring.xml file 

| Name | Used to Set |
| :--- | :--- |
| logging_file_path | value of the property with name "LOGS" |

```
<property name="LOGS" value="${logging_file_path}" />
```

## Spring Profiles
Spring profiles are used to define various logback logging configurations. The logback-spring.xml is used to define the configuration. 

There are currently two profiles:

| name | decription |
| :--- | :--- |
| production | the logs are saved to a file stored at the filepath defined by the logging_file_path env variable |
| development | logging is output to the console |

When running the unit tests or the integration test, development is set as the default via the relevant application properties file.

In order to specify the option for running the main application, a jvm paramter must be supplied:
```
-Dspring.profiles.active=production
```
or 
```
-Dspring.profiles.active=development
```

# Running Integration Tests
There is currently one integration test.

| Test Name | Location |
| :--- | :--- |
| NotesControllerIntegrationTest.java | NotesService/src/test/java/com/app/NotesService/controller/ |

## Environment variables
The following variables need to be defined to run the NotesControllerIntegrationTest.java

| File Name | Location |
| :--- | :--- |
| application-integration-test.properties | NotesService/src/main/resources |


### Variable used in application-integration-test.properties file

| Name | Used to Set |
| :--- | :--- |
| test_db_url | spring.datasource.url |
| test_db_user | spring.datasource.username |
| test_db_password | spring.datasource.password |

## Spring Profiles
The spring profiles for integration tests has been assigned as "development". This is set in the application-integration-test.properties file.

| File Name | Location |
| :--- | :--- |
| application-integration-test.properties | NotesService/src/main/resources |

## MySQL Table Setup
Before the NotesController integration tests are run, a script is run in order to delete and re-create the tables. This ensures the database is in a known state prior to testing.

| File Name | Location |
| :--- | :--- |
| pre-test-setup.sql | NotesService/src/main/resources/scripts/pre-test-setup.sql |

# Running Unit Tests
There are currently three unit tests. No additional setup is required in order to run the unit tests. 

| Test Name | Location |
| :--- | :--- |
| NotesControllerTest.java | NotesService/src/test/java/com/app/NotesService/controller/ |
| NotesServiceTest.java | NotesService/src/test/java/com/app/NotesService/service/ |
| NotesRepositoryTest.java | NotesService/src/test/java/com/app/NotesService/repository/ |

## Enivronment Variables
There are currently no environment variables used within any unit tests.

## Spring profiles
The spring profiles for unit tests has been assigned as "development". This is set in the application-unit-test.properties file.

| File Name | Location |
| :--- | :--- |
| application-unit-test.properties | NotesService/src/main/resources |













