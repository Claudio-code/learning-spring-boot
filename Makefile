build:
	./mvnw spring-boot:build-image
	docker build -t claudio2424/spring-boot-docker-app .
	docker-compose up --build -d

stop:
	docker-compose stop

start:
	docker-compose up -d