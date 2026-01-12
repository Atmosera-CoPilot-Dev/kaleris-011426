import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

  @Test
  void transfer_ShouldTransferAmount_WhenSufficientBalance() {
    BankAccount account1 = new BankAccount("Account1", 1000.0);
    BankAccount account2 = new BankAccount("Account2", 500.0);
    
    account1.transfer(account2, 200.0);
    
    assertEquals(800.0, account1.getBalance());
    assertEquals(700.0, account2.getBalance());
  }

  @Test
  void transfer_ShouldNotTransferAmount_WhenInsufficientBalance() {
    BankAccount account1 = new BankAccount("Account1", 100.0);
    BankAccount account2 = new BankAccount("Account2", 500.0);
    
    account1.transfer(account2, 200.0);
    
    assertEquals(100.0, account1.getBalance());
    assertEquals(500.0, account2.getBalance());
  }

  @Test
  void printBalance_ShouldDisplayCorrectBalance() {
    BankAccount account = new BankAccount("Account1", 1000.0);
    
    assertEquals("Account1 balance: 1000.0", account.printBalance());
  }
}