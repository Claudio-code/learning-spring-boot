FROM maven:3.8.2-adoptopenjdk-11 as build

WORKDIR /build

COPY . .

RUN mvn clean package -Dspring-boot.run.profiles=prod -DskipTests --batch-mode

FROM adoptopenjdk:11-jre-hotspot-focal

COPY --from=build /build/target/demo-0.0.1-SNAPSHOT.jar /app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
