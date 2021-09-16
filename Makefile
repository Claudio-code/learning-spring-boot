build:
	./mvnw spring-boot:build-image
	docker-compose up --build -d

stop:
	docker-compose stop

start:
	docker-compose up -d