FROM openjdk:17-oracle
LABEL authors="peetertaravas"
ENV APP_HOME=/usr/app/
COPY helmes/build/libs/helmes-0.0.1-SNAPSHOT.jar helmes-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "helmes-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080