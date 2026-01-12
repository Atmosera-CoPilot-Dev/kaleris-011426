# Bank Account Application

This project is a simple Java application that simulates a bank account system. It allows for the transfer of money between two bank accounts and displays the account balances.

## Project Structure

```
BankAccountApp
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── bankaccount
│   │   │               ├── BankAccount.java
│   │   │               ├── TransferTask.java
│   │   │               └── Main.java
│   │   └── resources
│   └── test
│       ├── java
│       │   └── com
│       │       └── example
│       │           └── bankaccount
│       │               └── BankAccountTest.java
│       └── resources
├── pom.xml
└── README.md
```


## Features

- Create bank accounts with a specified name and balance.
- Transfer money between accounts.
- Display the current balance of each account.

## Testing

Unit tests are provided for the `BankAccount` class. To run the tests, use the following command:
```
mvn test
```

## License

This project is licensed under the MIT License.