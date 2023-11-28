package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Hashtable;

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
		assertEquals("SweBank", SweBank.getName());
		assertEquals("Nordea", Nordea.getName());
		assertEquals("DanskeBank", DanskeBank.getName());
	}

	@Test
	public void testGetCurrency() {
		assertEquals(SEK, SweBank.getCurrency());
		assertEquals(SEK, Nordea.getCurrency());
		assertEquals(DKK, DanskeBank.getCurrency());
	}

	@Test
	public void testOpenAccount() throws AccountExistsException, NoSuchFieldException, IllegalAccessException {
		String key = "Danya";

		Field field = Bank.class.getDeclaredField("accountlist");
		field.setAccessible(true);
		Hashtable<String, Account> hashtable = (Hashtable<String, Account>) field.get(DanskeBank);

		assertFalse(hashtable.containsKey(key));
		DanskeBank.openAccount("Danya");

		assertTrue(hashtable.containsKey(key));
	}

	@Test(expected = AccountExistsException.class)
	public void testOpenAccountWithException() throws AccountExistsException{
		SweBank.openAccount("Ulrika");
	}

	@Test
	public void testDeposit() throws AccountDoesNotExistException, AccountExistsException {
		String userId = "Danila";
		Money moneyDeposit = new Money(100000, SEK);
		SweBank.openAccount(userId);

		int account = SweBank.getBalance(userId);
		assertEquals(0, account);

		SweBank.deposit(userId, moneyDeposit);

		assertEquals(Integer.valueOf(1000), SweBank.getBalance(userId));
	}

	@Test
	public void testWithdraw() throws AccountDoesNotExistException, AccountExistsException {
		String userId = "Danila";
		Money moneyDeposit = new Money(100000, SEK);
		SweBank.openAccount(userId);
		SweBank.deposit(userId, moneyDeposit);

		Money moneyWithdraw = new Money(50000, SEK);
		SweBank.withdraw(userId, moneyWithdraw);

		assertEquals(Integer.valueOf(500), SweBank.getBalance(userId));
	}

	@Test
	public void testGetBalance() throws AccountDoesNotExistException, AccountExistsException {
		String firstUserId = "Danila";
		String secondUserId = "Olga";
		String thirdUserId = "Andrei";

		SweBank.openAccount(firstUserId);
		SweBank.openAccount(secondUserId);
		SweBank.openAccount(thirdUserId);

		Money depositMoney = new Money(10000, SEK);
		SweBank.deposit(firstUserId, depositMoney);
		SweBank.deposit(secondUserId, depositMoney);
		SweBank.deposit(thirdUserId, depositMoney);

		assertEquals(Integer.valueOf(100), SweBank.getBalance(firstUserId));
		assertEquals(Integer.valueOf(100), SweBank.getBalance(secondUserId));
		assertEquals(Integer.valueOf(100), SweBank.getBalance(thirdUserId));

	}

	@Test
	public void testTransferWithinOneBank() throws AccountDoesNotExistException, AccountExistsException {
		String firstUserId = "Danila";
		String secondUserId = "Olga";

		SweBank.openAccount(firstUserId);
		SweBank.openAccount(secondUserId);

		SweBank.deposit(firstUserId, new Money(1000, SEK));

		assertEquals(Integer.valueOf(10), SweBank.getBalance(firstUserId));
		assertEquals(Integer.valueOf(0), SweBank.getBalance(secondUserId));

		SweBank.transfer(firstUserId, secondUserId, new Money(1000, SEK));

		assertEquals(Integer.valueOf(0), SweBank.getBalance(firstUserId));
		assertEquals(Integer.valueOf(10), SweBank.getBalance(secondUserId));
	}

	@Test
	public void testTransferWithinDifferentBanks() throws AccountDoesNotExistException, AccountExistsException {
		String firstUserId = "Danila";
		String secondUserId = "Olga";

		SweBank.openAccount(firstUserId);
		DanskeBank.openAccount(secondUserId);

		SweBank.deposit(firstUserId, new Money(1000, SEK));

		assertEquals(Integer.valueOf(10), SweBank.getBalance(firstUserId));
		assertEquals(Integer.valueOf(0), DanskeBank.getBalance(secondUserId));

		SweBank.transfer(firstUserId, DanskeBank, secondUserId, new Money(1000, SEK));

		assertEquals(Integer.valueOf(0), SweBank.getBalance(firstUserId));
		assertEquals(Integer.valueOf(7), DanskeBank.getBalance(secondUserId));
	}

	@Test
	public void testTimedPayment() throws AccountDoesNotExistException, AccountExistsException {
		String firstUseId = "Danila";
		String secondUserId = "Olga";

		String id = "1";
		int interval = 2;
		int nextInterval = 3;

		SweBank.openAccount(firstUseId);
		DanskeBank.openAccount(secondUserId);

		SweBank.deposit(firstUseId, new Money(10000, SEK));

		assertEquals(Integer.valueOf(100), SweBank.getBalance(firstUseId));
		assertEquals(Integer.valueOf(0), DanskeBank.getBalance(secondUserId));

		SweBank.addTimedPayment(
				firstUseId, id, interval, nextInterval, new Money(1000, SEK), DanskeBank, secondUserId
		);

		for(int i = 0; i < nextInterval + 1; i++){
			SweBank.tick();
		}

		assertEquals(Integer.valueOf(90), SweBank.getBalance(firstUseId));
		assertEquals(Integer.valueOf(7), DanskeBank.getBalance(secondUserId));

		for (int i = 0; i < interval + 1; i++){
			SweBank.tick();
		}

		assertEquals(Integer.valueOf(80), SweBank.getBalance(firstUseId));
		assertEquals(Integer.valueOf(15), DanskeBank.getBalance(secondUserId));


	}
}
