# Web Banking API

## Requirements

- JDK 17
- Maven
- Docker

## Setup and Run

### Step 1: Build the Project

First, ensure you have Maven and JDK 17 installed. Navigate to the project directory and execute the following command to build the jar artifact:

```sh
mvn clean install
```

### Step 2: Run Docker Compose

Ensure Docker is installed and running on your machine. Then, run the following command to start the services defined in the docker-compose.yml file:

```sh
docker-compose up --build
```

### Step 3: Access the Application


Once the Docker containers are up and running, you can access the application via the following URL:

http://localhost:8080/swagger-ui/index.html#/Authentication/authenticate


### Database Users

The following users are pre-populated in the database:

| Name | Username | Password |
|------|----------|----------|
| Joe  | joe      | 12345    |
| Jack | jack     | 123456   |
| Paul | paul     | 12345   |

![img.png](img.png)

login, get your token and authorize it, and go !

Additional Information
The application uses a PostgreSQL database initialized with a script located in the sql directory.
Ensure all environment variables are correctly set in the docker-compose.yml file.

## Remarks

In this code, I tried to show some of the knowledge in Spring and Java language that I acquired over the last few years. For the tests, I have written some examples for unit testing and integration testing.
