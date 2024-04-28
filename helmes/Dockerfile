# Stage 1: Build gradle
FROM gradle:8.3.0-jdk17 AS build
COPY --chown=gradle:gradle helmes /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build -x test --no-daemon

# Stage 2: Build JAR file
FROM openjdk:17-oracle
LABEL authors="peetertaravas"
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/helmes-0.0.1-SNAPSHOT.jar /app/spring-boot-application.jar

#Stage 3: Run applicaiton file
ENTRYPOINT ["java", "-jar", "/app/spring-boot-application.jar"]
EXPOSE 8080