# Store Receipt Console Application

This Java-based console application generates a receipt for a store, based on provided product IDs, quantities, discount cards, and debit card balances. The application demonstrates the use of OOP principles, design patterns, and robust exception handling.

## Table of Contents

- [Requirements](#requirements)
- [Usage](#usage)
- [Input Details](#input-details)
- [Output](#output)
- [Exception Handling](#exception-handling)
- [Building and Running](#building-and-running)

## Requirements

- Java 22
- Understanding of OOP principles
- Design patterns (e.g., Factory, Builder, FactoryMethod)
- Gradle 8.5
- PostgreSQL for database storage
- JDBC (org.postgresql.Driver) for database connectivity
- Minimum 70% unit test coverage

## Usage

The application is executed via a console command:

```sh
java -jar clevertec-check.jar id-quantity discountCard=xxxx balanceDebitCard=xxxx saveToFile=xxxx datasource.url=xxxx datasource.username=xxxx datasource.password=xxxx
```

### Example Command

```sh
java -jar clevertec-check.jar 3-1 2-5 5-1 discountCard=1111 balanceDebitCard=100 saveToFile=./result.csv datasource.url=jdbc:postgresql://localhost:5432/check datasource.username=postgres datasource.password=postgres
```

## Input Details

- `id-quantity`: Identifies the product and the quantity (e.g., `3-1` for product ID 3 with quantity 1).
- `discountCard=xxxx`: Specifies the discount card number (e.g., `discountCard=1111`).
- `balanceDebitCard=xxxx`: Specifies the debit card balance (e.g., `balanceDebitCard=100`).
- `saveToFile=xxxx`: Specifies the relative path to the output receipt file (e.g., `saveToFile=./result.csv`).
- `datasource.url`: Specifies the JDBC URL for connecting to PostgreSQL.
- `datasource.username`: Specifies the username for PostgreSQL authentication.
- `datasource.password`: Specifies the password for PostgreSQL authentication.

## Output

- The generated receipt will be saved to the specified file.
- The receipt will also be printed to the console.

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
