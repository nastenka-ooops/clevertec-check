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

## Usage

The application is executed via a console command:

```sh
java -cp src ./src/main/java/ru/clevertec/check/CheckRunner.java id-quantity discountCard=xxxx balanceDebitCard=xxxx pathToFile=xxxx saveToFile=xxxx
```

### Example Command

```sh
java -cp src ./src/main/java/ru/clevertec/check/CheckRunner.java 3-1 2-5 5-1 discountCard=1111 balanceDebitCard=100 pathToFile=./src/main/resources/products.csv saveToFile=./result.csv
```

## Input Details

- `id-quantity`: Identifies the product and the quantity (e.g., `3-1` for product ID 3 with quantity 1).
- `discountCard=xxxx`: Specifies the discount card number (e.g., `discountCard=1111`).
- `balanceDebitCard=xxxx`: Specifies the debit card balance (e.g., `balanceDebitCard=100`).
- `pathToFile=xxxx`: Specifies the relative path to the input products file (e.g., `pathToFile=./src/main/resources/products.csv`).
- `saveToFile=xxxx`: Specifies the relative path to the output receipt file (e.g., `saveToFile=./result.csv`).

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
