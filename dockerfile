FROM openjdk:17-jdk-alpine
COPY target/lciii-scaffolding-0.0.1-SNAPSHOT.jar countries-api
ENTRYPOINT ["java", "-jar", "countries-api"]


