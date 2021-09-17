build:
	docker-compose up --build -d

stop:
	docker-compose stop

start:
	docker-compose up -d

run-tests:
	mvn clean test
