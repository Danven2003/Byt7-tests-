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

		// all tests were failed with NullPointerException
		SweBank.deposit("Alice", new Money(1000000, SEK));

	}

	@Test
	public void testAddRemoveTimedPayment() {
		// Ensure no timed payment with ID "T0001" exists initially
		assertFalse(testAccount.timedPaymentExists("T0001"));

		// Add a timed payment with ID "T0001"
		testAccount.addTimedPayment("T0001", 1, 2, new Money(10000, SEK), SweBank, "Alice");

		// Verify the existence of the timed payment
		assertTrue(testAccount.timedPaymentExists("T0001"));

		// Remove the timed payment
		testAccount.removeTimedPayment("T0001");

		// Confirm the absence of the timed payment
		assertFalse(testAccount.timedPaymentExists("T0001"));
	}

	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		// Ensure no timed payment with ID "T0001" exists initially
		assertFalse(testAccount.timedPaymentExists("T0001"));

		// Add a timed payment with ID "T0001" to transfer money to SweBank's Alice account
		testAccount.addTimedPayment("T0001", 1, 2, new Money(10000, SEK), SweBank, "Alice");

		// Verify the initial balance
		assertEquals(Integer.valueOf(10000000), testAccount.getBalance().getAmount());

		// Advance time and verify the balance and SweBank's Alice account balance
		testAccount.tick();
		testAccount.tick();
		assertEquals(Integer.valueOf(9990000), testAccount.getBalance().getAmount());
		assertEquals(1010000, SweBank.getBalance("Alice"));

		// Advance time again and verify the balance and SweBank's Alice account balance
		testAccount.tick();
		assertEquals(Integer.valueOf(9980000), testAccount.getBalance().getAmount());
		assertEquals(1020000, SweBank.getBalance("Alice"));
	}

	@Test
	public void testAddWithdraw() {
		// Deposit money and verify the new balance
		testAccount.deposit(new Money(10000, SEK));
		assertEquals(Integer.valueOf(10010000), testAccount.getBalance().getAmount());

		// Withdraw money and verify the new balance
		testAccount.withdraw(new Money(5000, SEK));
		assertEquals(Integer.valueOf(10005000), testAccount.getBalance().getAmount());
	}

	@Test
	public void testGetBalance() {
		// Verify the initial balance
		assertEquals(Integer.valueOf(10000000), testAccount.getBalance().getAmount());
	}
}
