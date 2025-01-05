# A note-taking application based on John Crickett's 'Build Your Own Google Keep' Coding Challenge

This is a full-stack notes app based on John Crickett's 'Build Your Own Google Keep' challenge, from the Coding Challenges series.

See the challenge here: https://codingchallenges.fyi/challenges/challenge-keep/

I'm currently building the back-end which is a Spring Boot application, that uses Java version 21, and Maven to manage dependencies and project builds. Spring Data JPA is used to simplify interactions between the MySQL database and the RESTful API.

When I reach the relevant step in the challege, the client side will be set up as a React and TypeScript project which has been generated using Vite.js. The intention is to use Tailwind.css for styling. 

I am planning to push regular updates, so feel free to follow me on here, on alternatively on LinkedIn: https://www.linkedin.com/in/vinaygajjar/

# MySQL Tables
There is currently just one MySQL table, for persisting a Note entity:

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
A JVM parameter must also be specified to select the logging behaviour

## Environment Variables
Environment variables are currently used to specify database connection related information, and a filepath for where the logging file should be stored. 

The relevant files are located at the NotesService/src/main/resources folder

### Variables used in the application.properties file 

| name | used to set |
| :--- | :--- |
| db_url | spring.datasource.url |
| db_user | set spring.datasource.username|
| db_password | spring.datasource.password |

### Variables used in the logback-spring.xml file 

| name | used to set |
| :--- | :--- |
| logging_file_path | value of the property with name "LOGS" |

```
<property name="LOGS" value="${logging_file_path}" />
```

## Spring Profiles
Spring profiles are used to define various logback logging configurations. There are currently two profiles:

| name | decription |
| :--- | :--- |
| production | the logs are saved to a file stored at the filepath defined by the logging_file_path env variable |
| development | logging is output to the console |

When running the unit tests or the integration test, development is set as the default via the relevant application properties file

In order to specify the option for running the main application, a jvm paramter must be supplied:
```
-Dspring.profiles.active=production
```
or 
```
-Dspring.profiles.active=development
```

# Running Integration Tests
There is currently one Integration test, NotesControllerIntegrationTest.

This test is located in the NotesService/src/test/java/com/app/NotesService/controller folder. 

## Environment variables
The following variables need to be defined to run the NotesControllerIntegrationTest 

The relevant files are located at the NotesService/src/main/resources folder

### Variable used in application-integration-test.properties file

| name | used to set |
| :--- | :--- |
| test_db_url | spring.datasource.url |
| test_db_user | spring.datasource.username |
| test_db_password | used to set spring.datasource.password |







