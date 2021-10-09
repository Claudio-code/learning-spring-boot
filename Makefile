build:
	docker-compose up --build -d

stop:
	docker-compose stop

start:
	docker-compose up -d

run-tests:
	mvn clean test

run-api-local:
	mvn spring-boot:run -Dspring-boot.run.profiles=dev
