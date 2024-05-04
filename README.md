# Java task 

[Link to task description](https://www.helmes.com/java-task/)

## This project has two subprojects inside of it:
    - helmes is the back-end made with Spring Boot that runs on port 8080
    - front is the front-end made with Angular that runs on port 4200
    - the helmes project folder also contains PostgreSQL files and compose.yaml
      with which you can run the database easily on Docker, the database runs on port 5432. The database files are in:
      helmes/src/main/resources

## Dependencies links:
- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)
- [Java 17](https://www.azul.com/downloads/?version=java-17-lts&package=jdk#zulu)
- Node
  - [16.18.10](https://nodejs.org/dist/v16.18.1/)
  - [Current - 18.18.0](https://nodejs.org/en/download)

## Running tests
  - cd into helmes
  - run 'docker compose up' - creates database instance that app uses and is needed for tests
  - ./gradlew test
  - I tried to make tests run on H2 database, but something in my setup failed and I could not get it to work 

## 2 ways of running the application

## Running manually:
    - You need to have dependencies:
        - Java 17
        - Node 16.18.10
        - Docker(with Compose)
    - First run compose.yaml inside the back-end 'helmes' project, this is needed because tests used the database with docker-compose.
        Make sure that your Docker is running, commands are:
        - clean up any other containers made by this project before
        - `cd helmes`
        - `docker compose up`
    - Then create jar file for the back-end and run it:
        - `./gradlew build` -> be sure that docker compose database is up because we need
        - now you have an executable jar file inside the helmes/build/libs folder
        - execute the jar file with `java -jar build/libs/helmes-0.0.1-SNAPSHOT.jar`
    - After that running front end:
        - cd back to the first directory `cd ..`
        - then go to the front-end directory `cd ./front`
        - run `npm install -g @angular/cli`
        - run `ng serve`
    - now navigate to http://localhost:4200/ where you can see the login page for the application
    - if you don't have an account then register and then you can log in
    - you can create a new company and update its details afterward, enjoy!

## Running only compose:
    - NB!! This has 1 flaw in that while building the back-end jar the tests cannot be run because they require the
    database to be operational
    - You need to have Docker with Compose in your computer up and running
    - Clean up any other containers made by this project before
    - be in the root folder 
    - run docker compose up
    - navigate to 'http://localhost/' on your browser



TODO:
Front-end validations
Front-end authentication
Sector sql injection is possible
Implement a solution that one user can have multiple companies
Database container setup

        
