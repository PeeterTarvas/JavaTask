# Helmes Java task 

## This project has two subprojects inside of it:
    - helmes is the back-end made with Spring Boot that runs on port 8080
    - front is the front-end made with Angular that runs on port 4200
    - the helmes project folder also contains PostgreSQL files and compose.yaml
      with which you can run the database easily on Docker, database runs on port 5432. The database files are in:
      helmes/src/main/resources

## Running locally:
    - You need to have dependencies:
        - Java 17
        - Node 16.18.10
        - Docker 
    - First run compose.yaml, make sure that your Docker is running, commands are:
        - `cd helmes`
        - `docker compose up`
    - Then create jar file for back-end and run it:
        - `./gradlew build`
        - now you have a executable jar file inside the helmes/build/libs folder
        - execute the jar file with `java -jar build/libs/helmes-0.0.1-SNAPSHOT.jar`
    - After that running front end:
        - cd back to the first directory `cd ..`
        - then go to the front-end directory `cd ./front`
        - run `npm install -g @angular/cli`
        - run `ng serve`
    - now naviage to http://localhost:4200/ where you can see the login page for the application
    - if you don't have an account then register and then you can login
    - you can create a new company and update it's details afterward, enjoy!
        