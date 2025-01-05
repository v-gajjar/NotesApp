# A note-taking application based on John Crickett's 'Build Your Own Google Keep' Coding Challenge

This is a full-stack notes app based on John Crickett's 'Build Your Own Google Keep' challenge, from the Coding Challenges series.

See the challenge here: https://codingchallenges.fyi/challenges/challenge-keep/

I'm currently building the back-end which is a Spring Boot application, that uses Java version 21, and Maven to manage dependencies and project builds. Spring Data JPA is used to simplify interactions between the MySQL database and the RESTful API.

When I reach the relevant step in the challege, the client side will be set up as a React and TypeScript project which has been generated using Vite.js. The intention is to use Tailwind.css for styling. 

I am planning to push regular updates, so feel free to follow me on here, on alternatively on LinkedIn: https://www.linkedin.com/in/vinaygajjar/

# Installation and running the main application or tests

Cloning the repository: 
```
git clone https://github.com/v-gajjar/NotesApp.git
```

## Using an IDE
I would highly recommend using an IDE to run the project. 

I am using IntelliJ IDEA Community Edition.


## Environment Variables
Environment variables are currently used to specify database connection related information, and a filepath for where the logging file should be stored. 

### The following variables need to be defined to run the main application:

Variables used in the application.properties file

- db_url: used to set spring.datasource.url
- db_user: used to set spring.datasource.username
- db_password: used to set spring.datasource.password

Variables used in the logback-spring.xml file

- logging_file_path: used to set the value of the property with name "LOGS"

### The following variables need to be defined to run the NotesController integration test: 

Variables used in the integration-test.properties file

- test_db_url: used to set spring.datasource.url
- test_db_user: used to set spring.datasource.username
- test_db_password: used to set spring.datasource.password

## Spring Profiles
Spring profiles are used to define various logback logging configurations. There are currently two profiles:

- production: the logs are saved to a file stored at the filepath defined by the logging_file_path env variable
- development: logging is output to the console

When running the unit tests or the integration test, development is set as the default via the relevant application properties file

In order to specify the option for running the main application, a jvm paramter must be supplied:
```
-Dspring.profiles.active=production
```
or 
```
-Dspring.profiles.active=development
```







