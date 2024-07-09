# Store Receipt Console Application

This Java-based console application generates a receipt for a store, based on provided product IDs, quantities, discount cards, and debit card balances. The application demonstrates the use of OOP principles, design patterns, and robust exception handling and includes a RESTful API for CRUD operations.

## Table of Contents

- [Requirements](#requirements)
- [RESTful API](#restful-api)
- [Configuration](#configuration)
- [Exception Handling](#exception-handling)
- [Building and Running](#building-and-running)
- [New Features](#new-features)

## Requirements

- Java 22
- Understanding of OOP principles
- Design patterns (e.g., Factory, Builder, FactoryMethod)
- Gradle 8.5
- PostgreSQL for database storage
- JDBC (org.postgresql.Driver) for database connectivity
- Minimum 70% unit test coverage


## RESTful API

The application includes a RESTful API for managing products and discount cards. This API supports CRUD operations and integrates with the console application to ensure data consistency.

### Endpoints

#### Generate Receipt

**POST** `/check`

Request Body:
```json
{
  "products": [
    {
      "id": 1,
      "quantity": 5
    },
    {
      "id": 2,
      "quantity": 3
    }
  ],
  "discountCard": 1234,
  "balanceDebitCard": 100
}
```

Response:
- Returns a CSV file with the generated receipt.
- Save check to database

#### Products

- **GET** `/products?id=1`  
  Retrieves product details by ID.

- **POST** `/products`  
  Adds a new product to the database.  
  Request Body:
  ```json
  {
    "description": "Eat 100g.",
    "price": 3.25,
    "quantity": 5,
    "isWholesale": true
  }
  ```

- **PUT** `/products?id=1`  
  Updates an existing product by ID.  
  Request Body:
  ```json
  {
    "description": "Chocolate Ritter sport 100g.",
    "price": 3.25,
    "quantity": 5,
    "isWholesale": true
  }
  ```

- **DELETE** `/products?id=1`  
  Deletes a product by ID.

#### Discount Cards

- **GET** `/discountcards?id=1`  
  Retrieves discount card details by ID.

- **POST** `/discountcards`  
  Adds a new discount card to the database.  
  Request Body:
  ```json
  {
    "discountCard": 5265,
    "discountAmount": 2
  }
  ```

- **PUT** `/discountcards?id=1`  
  Updates an existing discount card by ID.  
  Request Body:
  ```json
  {
    "discountCard": 6786,
    "discountAmount": 3
  }
  ```

- **DELETE** `/discountcards?id=1`  
  Deletes a discount card by ID.

## Configuration

### Environment Variables

- `datasource.url`: Set in the system environment.
- `datasource.username`: Set in the system environment.
- `datasource.password`: Set in the system environment.

Alternatively, configure Tomcat by adding the following variables to `catalina.properties`:

```
datasource.url=jdbc:postgresql://localhost:5432/check
datasource.username=postgres
datasource.password=postgres
```

### Building the WAR

Add the following to your `build.gradle` to produce a WAR file:

```groovy
war {
    archiveFileName = 'clevertec-check.war'
}
```

Run `gradle build` to generate the WAR file.

## Exception Handling

The application includes comprehensive exception handling to manage various potential issues, such as:

- If the input data is incorrect (not arguments filled in correctly, errors quantity, lack of products)
- If there are insufficient funds (balance less than the amount on the check)
- In any other situation

## Building and Running

To build and run the application, follow these steps:

1. Ensure Java 22 is installed.
2. Navigate to the project root directory.
3. Execute the example command provided above or construct your own based on the usage section.

## New Features

### Manage Checks

The application now includes functionality to manage checks. This includes creating checks and retrieving all checks.

#### Create Check

**POST** `/checks`  
Adds a new check to the database.  
Request Body:
```json
{
  "products": [
    {
      "id": 1,
      "quantity": 5
    },
    {
      "id": 2,
      "quantity": 3
    }
  ],
  "discountCard": 1234,
  "balanceDebitCard": 100
}
```

#### Retrieve All Checks

**GET** `/checks`  
Retrieves all checks from the database. Response contains a list of checks with details about each check.

### Product sorting

**GET** `/products?sortBy=<field>`  
  Retrieves products sorting by field.
