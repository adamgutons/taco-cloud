# taco-cloud

- This is a simple CRUD application built with Spring Boot.  App mocks a Taco restaurant and their online ordering webpage.  Users register, sign-in, and customize an order that is saved to an H2 database.

## Build and Deploy - (Maven and Docker)
### Run commands from root directory
- `mvn package`
- `docker build . -t tacocloud/tacocloud:0.0.1-SNAPSHOT`
- `docker run -p8080:8080 --name tacocloud tacocloud/tacocloud:0.0.1-SNAPSHOT`
- Verify application is running on localhost:8080