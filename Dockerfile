FROM maven:3.8.2-openjdk-11-slim as build

WORKDIR /build

COPY . .

RUN mvn clean package --batch-mode

FROM openjdk:11-jre-slim

COPY --from=build /build/target/demo-0.0.1-SNAPSHOT.jar /app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
