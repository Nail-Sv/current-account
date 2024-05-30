# Current Account Project

## Overview

The assessment consists of an API to be used for opening a new “current account” of already
existing customers.

## Tech Stack

- Java 17
- Spring Boot
- OpenAPI Documentation
- Docker / Docker Compose
- Liquibase
- In-memory H2 database for testing.
- JUnit 5, Mockito

###   CI/CD Tools

- GitHub Action
- Docker

## Assignment Requirements

- The API will expose an endpoint which accepts the user information (customerID, initialCredit).
- Once the endpoint is called, a new account will be opened connected to the user whose ID is customerID.
- Also, if initialCredit is not 0, a transaction will be sent to the new account.
- Another Endpoint will output the user information showing Name, Surname, balance, and transactions of the accounts.

## Technical Requirements
- Docker
- Browser (For testing using SwaggerUI)
- Optional: Postman


## Setup Instructions

### Clone the Repository

```bash
git clone https://github.com/Nail-Sv/current-account.git
cd current-account
```

### Build and Run the Application

```bash
docker-compose build
docker-compose up -d
```

#### Stop the Application
```bash
docker-compose stop
```

# Usage
## API Endpoints
### The following are the main API endpoints available in the application:

### 1. Open customer account:

```json
POST /v1/account/ - creates a new account for existing customer
```
POST request body
```json
{
  "customerId": "550e8400-e29b-41d4-a716-446655440000",
  "initialCredit": "10"
}
```

### 2. Retrieve customer information

```json
GET /v1/customer/{customerId} - retrieves a customer
GET /v1/customer - retrieves all customers
```

## Swagger Docs
The application includes Swagger API documentation, which allows you to test the endpoints directly from the Swagger UI. 
You can access the Swagger UI by navigating to `http://localhost:8080/swagger-ui.html` after starting the application.

In the Swagger UI, you can see all available endpoints with detailed documentation. You can test the application by 
clicking the "Execute" button on each endpoint's documentation page. The documented examples in Swagger provide an easy 
way to interact with and test the application without writing any code.

```http request
http://localhost:8080/swagger-ui.html
```


# Acknowledgements

- Thanks to the Spring Boot team for their excellent framework.
- Special thanks to all contributors.



