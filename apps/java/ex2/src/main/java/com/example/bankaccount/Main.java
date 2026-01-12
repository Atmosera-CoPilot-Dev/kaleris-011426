public class Main {
  public static void main(String[] args) {
    BankAccount account1 = new BankAccount("Account1", 1000.0);
    BankAccount account2 = new BankAccount("Account2", 1000.0);

    Thread transfer1 = new Thread(new TransferTask(account1, account2, 100.0));
    Thread transfer2 = new Thread(new TransferTask(account2, account1, 200.0));

    transfer1.start();
    transfer2.start();

    try {
      transfer1.join();
      transfer2.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    account1.printBalance();
    account2.printBalance();
  }
}