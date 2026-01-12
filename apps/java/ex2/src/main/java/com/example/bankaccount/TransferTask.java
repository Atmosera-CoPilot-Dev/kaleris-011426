// This file defines the TransferTask class, which implements Runnable.
// It is responsible for executing the transfer operation between two BankAccount instances in a separate thread.

package com.example.bankaccount;

public class TransferTask implements Runnable {
  private final BankAccount fromAccount;
  private final BankAccount toAccount;
  private final double amount;

  public TransferTask(BankAccount fromAccount, BankAccount toAccount, double amount) {
    this.fromAccount = fromAccount;
    this.toAccount = toAccount;
    this.amount = amount;
  }

  @Override
  public void run() {
    fromAccount.transfer(toAccount, amount);
  }
}