FROM maven:3.9.7-amazoncorretto-21 as build

WORKDIR /build

COPY . .

RUN mvn clean package -Dspring-boot.run.profiles=prod -DskipTests --batch-mode

FROM amazoncorretto:21-alpine

ENV JAVA_OPTS=""

COPY --from=build /build/target/demo-0.0.1-SNAPSHOT.jar /app.jar

ENTRYPOINT ["java", "-XX:+UseSerialGC", "-XX:+UseStringDeduplication", "-jar", "app.jar"]
