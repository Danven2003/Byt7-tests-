package b_Money;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountTest {
    Currency SEK, DKK;
    Bank Nordea;
    Bank DanskeBank;
    Bank SweBank;
    Account testAccount;

    @Before
    public void setUp() throws Exception {
        SEK = new Currency("SEK", 0.15);
        SweBank = new Bank("SweBank", SEK);
        SweBank.openAccount("Alice");
        testAccount = new Account("Hans", SEK);
        testAccount.deposit(new Money(10000000, SEK));

        SweBank.deposit("Alice", new Money(1000000, SEK));
    }

    @Test
    public void testGetBalance() {
        Money money = new Money(10000000, SEK);
        assertEquals(money.getAmount(), testAccount.getBalance().getAmount());
        assertEquals(money.getCurrency(), testAccount.getBalance().getCurrency());
    }

//	/**
//	 * Add a timed payment
//	 * @param id Id of timed payment
//	 * @param interval Number of ticks between payments
//	 * @param next Number of ticks till first payment
//	 * @param amount Amount of Money to transfer each payment
//	 * @param tobank Bank where receiving account resides
//	 * @param toaccount Id of receiving account
//	 */

    @Test
    public void testAddRemoveTimedPayment() {
        testAccount.addTimedPayment("1", 2, 1, new Money(1000, SEK), SweBank, "Alice");
        assertTrue(testAccount.timedPaymentExists("1"));

        testAccount.removeTimedPayment("1");
        assertFalse(testAccount.timedPaymentExists("1"));
    }

    @Test
    public void testAddWithdraw() {
        Money money = new Money(10000, SEK);
        //balance for testing
        Double balance = testAccount.getBalance().getAmount();

        //withdraw
        testAccount.withdraw(money);

        //expected result
        Double expected = balance - money.getAmount();

        assertEquals(expected, testAccount.getBalance().getAmount());
    }

    @Test
    public void testTimedPayment() throws AccountDoesNotExistException {
        String id = "1";
        int interval = 2;
        int nextInterval = 3;
        int moneyAmount = 100000;
        Money money = new Money(moneyAmount, SEK);
        String toAccount = "Alice";
        //for the testing result
        Double balance = testAccount.getBalance().getAmount();
        //imitate tick
        testAccount.addTimedPayment(id, interval, nextInterval, money, SweBank, toAccount);
        //count next interval ticks until the first transfer will come
        for (int count = 0; count < nextInterval + 1; count++) {
            testAccount.tick();
        }
        Double expectAfterFirstTick = balance - moneyAmount / 100;
        //check after the first tick
        assertEquals(expectAfterFirstTick, testAccount.getBalance().getAmount());

        Double expectAfterTick = expectAfterFirstTick;
        //loop for checking ticks and values
        while (testAccount.getBalance().getAmount() > 0) {
            expectAfterTick = expectAfterTick - moneyAmount / 100;
            for (int count = 0; count < interval + 1; count++) {
                testAccount.tick();
            }
            assertEquals(expectAfterTick, testAccount.getBalance().getAmount());
        }
    }
}
