package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BankTest {
    Currency SEK, DKK;
    Bank SweBank, Nordea, DanskeBank;

    @Before
    public void setUp() throws Exception {

        DKK = new Currency("DKK", 0.20);
        SEK = new Currency("SEK", 0.15);
        SweBank = new Bank("SweBank", SEK);
        Nordea = new Bank("Nordea", SEK);
        DanskeBank = new Bank("DanskeBank", DKK);
        SweBank.openAccount("Ulrika");
        SweBank.openAccount("Bob");
        Nordea.openAccount("Bob");
        DanskeBank.openAccount("Gertrud");

    }

    @Test
    public void testGetName() {
        // Verify the names of different banks
        assertEquals("SweBank", SweBank.getName());
        assertEquals("Nordea", Nordea.getName());
        assertEquals("DanskeBank", DanskeBank.getName());
    }

    @Test
    public void testGetCurrency() {
        // Verify the currencies of different banks
        assertEquals(SEK, SweBank.getCurrency());
        assertEquals(SEK, Nordea.getCurrency());
        assertEquals(DKK, DanskeBank.getCurrency());
    }

    @Test
    public void testOpenAccount() throws AccountExistsException {
        // Open accounts in different banks
        SweBank.openAccount("Gavin");
        Nordea.openAccount("Alice");
    }

    @Test(expected = AccountExistsException.class)
    public void testOpenAccountFailure() throws AccountExistsException {
        // Attempt to open an account with an existing name, expecting an exception
        SweBank.openAccount("Ulrika");
    }

    @Test
    public void testDeposit() throws AccountDoesNotExistException {
        // Deposit money into accounts in different banks
        SweBank.deposit("Ulrika", new Money(10000, SEK));
        assertEquals(10000, SweBank.getBalance("Ulrika"));

        SweBank.deposit("Bob", new Money(10000, DKK));
        assertEquals(13333, SweBank.getBalance("Bob"));
    }

    @Test(expected = AccountDoesNotExistException.class)
    public void testDepositFailure() throws AccountDoesNotExistException {
        // Attempt to deposit into a non-existing account, expecting an exception
        Nordea.deposit("Gavin", new Money(10000, SEK));
    }

    @Test
    public void testWithdraw() throws AccountDoesNotExistException {
        // Withdraw money from accounts in different banks
        SweBank.withdraw("Ulrika", new Money(10000, SEK));
        assertEquals(-10000, SweBank.getBalance("Ulrika"));

        SweBank.withdraw("Bob", new Money(10000, DKK));
        assertEquals(-13333, SweBank.getBalance("Bob"));
    }

    @Test(expected = AccountDoesNotExistException.class)
    public void testWithdrawFailure() throws AccountDoesNotExistException {
        // Attempt to withdraw from a non-existing account, expecting an exception
        Nordea.withdraw("Gavin", new Money(10000, SEK));
    }

    @Test
    public void testGetBalance() throws AccountDoesNotExistException {
        // Deposit money and verify the balance
        SweBank.deposit("Ulrika", new Money(10000, SEK));
        assertEquals(10000, SweBank.getBalance("Ulrika"));
    }

    @Test(expected = AccountDoesNotExistException.class)
    public void testGetBalanceFailure() throws AccountDoesNotExistException {
        // Attempt to get the balance of a non-existing account, expecting an exception
        assertEquals(10000, Nordea.getBalance("Gavin"));
    }

    @Test
    public void testTransferSameBank() throws AccountDoesNotExistException {
        // Deposit money and transfer within the same bank, verify balances
        SweBank.deposit("Bob", new Money(10000, SEK));
        SweBank.transfer("Bob", "Ulrika", new Money(4000, SEK));
        assertEquals(4000, SweBank.getBalance("Ulrika"));
        assertEquals(6000, SweBank.getBalance("Bob"));
    }

    @Test
    public void testTransferDifferentBanks() throws AccountDoesNotExistException {
        // Deposit money and transfer between different banks, verify balances
        DanskeBank.deposit("Gertrud", new Money(10000, DKK));
        DanskeBank.transfer("Gertrud", SweBank, "Ulrika", new Money(4000, DKK));
        assertEquals(5333, SweBank.getBalance("Ulrika"));
        assertEquals(6000, DanskeBank.getBalance("Gertrud"));
    }

    @Test
    public void testTimedPayment() throws AccountDoesNotExistException {
        // Deposit money, set up a timed payment, advance time, and verify balances
        SweBank.deposit("Ulrika", new Money(100000, SEK));
        SweBank.addTimedPayment("Ulrika", "T0002", 1, 2, new Money(10000, SEK), DanskeBank, "Gertrud");
        assertEquals(100000, SweBank.getBalance("Ulrika"));
        SweBank.tick();
        SweBank.tick();
        assertEquals(90000, SweBank.getBalance("Ulrika"));
        assertEquals(7500, DanskeBank.getBalance("Gertrud"));
        SweBank.tick();
        assertEquals(80000, SweBank.getBalance("Ulrika"));
        assertEquals(15000, DanskeBank.getBalance("Gertrud"));
    }
}
