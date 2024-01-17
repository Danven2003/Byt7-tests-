package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MoneyTest {
	Currency SEK, DKK, NOK, EUR;
	Money SEK100, EUR10, SEK200, EUR20, SEK0, EUR0, SEKn100, DKK100;

	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
		SEK100 = new Money(10000, SEK);
		EUR10 = new Money(1000, EUR);
		SEK200 = new Money(20000, SEK);
		EUR20 = new Money(2000, EUR);
		SEK0 = new Money(0, SEK);
		EUR0 = new Money(0, EUR);
		SEKn100 = new Money(-10000, SEK);
		DKK100 = new Money(10000, DKK);
	}

	@Test
	public void testGetAmount() {
		// Verify the amounts of different money instances
		assertEquals(Integer.valueOf(10000), SEK100.getAmount());
		assertEquals(Integer.valueOf(1000), EUR10.getAmount());
		assertEquals(Integer.valueOf(20000), SEK200.getAmount());
		assertEquals(Integer.valueOf(2000), EUR20.getAmount());
		assertEquals(Integer.valueOf(0), SEK0.getAmount());
		assertEquals(Integer.valueOf(0), EUR0.getAmount());
		assertEquals(Integer.valueOf(-10000), SEKn100.getAmount());
		assertEquals(Integer.valueOf(10000), DKK100.getAmount());
	}

	@Test
	public void testGetCurrency() {
		// Verify the currencies of different money instances
		assertEquals(SEK, SEK100.getCurrency());
		assertEquals(EUR, EUR10.getCurrency());
		assertEquals(DKK, DKK100.getCurrency());
	}

	@Test
	public void testToString() {
		// Verify the string representations of different money instances
		assertEquals("100 SEK", SEK100.toString());
		assertEquals("10 EUR", EUR10.toString());
		assertEquals("100 DKK", DKK100.toString());
	}

	@Test
	public void testGlobalValue() {
		// Verify the global values of different money instances
		assertEquals(Integer.valueOf(1500), SEK100.universalValue());
		assertEquals(Integer.valueOf(3000), SEK200.universalValue());
		assertEquals(Integer.valueOf(-1500), SEKn100.universalValue());
		assertEquals(Integer.valueOf(1500), EUR10.universalValue());
		assertEquals(Integer.valueOf(3000), EUR20.universalValue());
		assertEquals(Integer.valueOf(0), EUR0.universalValue());
		assertEquals(Integer.valueOf(2000), DKK100.universalValue());
	}

	@Test
	public void testEqualsMoney() {
		// Verify the equality of different money instances
		assertTrue(EUR0.equals(SEK0));
		assertTrue(SEK100.equals(EUR10));
		assertTrue(SEK200.equals(EUR20));
		assertFalse(SEK100.equals(DKK100));
	}

	@Test
	public void testAdd() {
		// Verify the result of adding different money instances
		assertEquals(new Money(30000, SEK).getAmount(), SEK100.add(SEK200).getAmount());
		assertEquals(new Money(20000, SEK).getAmount(), SEK100.add(EUR10).getAmount());
		assertEquals(new Money(30000, SEK).getAmount(), SEK200.add(EUR10).getAmount());
		assertEquals(new Money(17500, DKK).getAmount(), DKK100.add(EUR10).getAmount());
	}

	@Test
	public void testSub() {
		// Verify the result of subtracting different money instances
		assertEquals(new Money(-10000, SEK).getAmount(), SEK100.sub(SEK200).getAmount());
		assertEquals(new Money(0, SEK).getAmount(), SEK100.sub(EUR10).getAmount());
		assertEquals(new Money(10000, SEK).getAmount(), SEK200.sub(EUR10).getAmount());
		assertEquals(new Money(2500, DKK).getAmount(), DKK100.sub(EUR10).getAmount());
	}

	@Test
	public void testIsZero() {
		// Verify if different money instances are zero or not
		assertTrue(SEK0.isZero());
		assertTrue(EUR0.isZero());
		assertFalse(EUR10.isZero());
	}

	@Test
	public void testNegate() {
		// Verify the negation of different money instances
		assertEquals(new Money(10000, SEK).getAmount(), SEKn100.negate().getAmount());
		assertEquals(new Money(-1000, EUR).getAmount(), EUR10.negate().getAmount());
	}

	@Test
	public void testCompareTo() {
		// Verify the comparison of different money instances
		assertEquals(0, EUR10.compareTo(SEK100));
		assertEquals(0, EUR20.compareTo(SEK200));
		assertTrue(EUR20.compareTo(SEK100) > 0);
		assertTrue(EUR10.compareTo(SEK200) < 0);
	}
}
