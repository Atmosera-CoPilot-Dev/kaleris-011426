// filepath: /BankAccountApp/BankAccountApp/src/main/java/com/example/bankaccount/BankAccount.java
package com.example.bankaccount;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
  private final String _name;
  private double _balance;
  private final Lock _lock = new ReentrantLock();

  public BankAccount(String name, double balance) {
    this._name = name;
    this._balance = balance;
  }

  /**
   * Transfers money from this account to another account.
   * @param toAccount The account to transfer money to.
   * @param amount The amount to transfer.
   */
  public void transfer(BankAccount toAccount, double amount) {
    if (_lock.tryLock()) {
      try {
        if (toAccount._lock.tryLock()) {
          try {
            if (_balance >= amount) {
              _balance -= amount;
              toAccount._balance += amount;
              System.out.println("Transferred " + amount + " from " + _name + " to " + toAccount._name + ".");
            } else {
              System.out.println("Insufficient funds in " + _name + " to transfer " + amount + ".");
            }
          } finally {
            toAccount._lock.unlock();
          }
        }
      } finally {
        _lock.unlock();
      }
    }
  }

  /**
   * Prints the current balance of the account.
   */
  public void printBalance() {
    System.out.println(_name + " balance: " + _balance);
  }
}