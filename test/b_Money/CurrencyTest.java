package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CurrencyTest {
	Currency SEK, DKK, NOK, EUR;

	@Before
	public void setUp() throws Exception {
		/* Setup currencies with exchange rates */
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
	}

	@Test
	public void testGetName() {
		assertEquals("SEK", SEK.getName());
		assertEquals("DKK", DKK.getName());
		assertEquals("EUR", EUR.getName());
	}

	@Test
	public void testGetRate() {
		assertEquals(Double.valueOf(0.15), SEK.getRate());
		assertEquals(Double.valueOf(0.20), DKK.getRate());
		assertEquals(Double.valueOf(1.5), EUR.getRate());

	}

	@Test
	public void testSetRate() {
		SEK.setRate(0.30);
		assertEquals(Double.valueOf(0.30), SEK.getRate());

		DKK.setRate(0.30);
		assertEquals(Double.valueOf(0.30), DKK.getRate());

		EUR.setRate(0.30);
		assertEquals(Double.valueOf(0.30), EUR.getRate());
	}

	@Test
	public void testGlobalValue() {
		int amountSEK = 100;
		assertEquals(Integer.valueOf((int) (SEK.getRate() * amountSEK)), SEK.universalValue(amountSEK));

		int amountDKK = 100;
		assertEquals(Integer.valueOf((int)(DKK.getRate() * amountDKK)), DKK.universalValue(amountDKK));

		int amountEUR = 100;
		assertEquals(Integer.valueOf((int)(EUR.getRate() * amountEUR)), EUR.universalValue(amountEUR));
	}

	@Test
	public void testValueInThisCurrency() {
		assertEquals(Integer.valueOf(1000), SEK.valueInThisCurrency(100, EUR));
		assertEquals(Integer.valueOf(750), DKK.valueInThisCurrency(100, EUR));
		assertEquals(Integer.valueOf(13), EUR.valueInThisCurrency(100, DKK));
	}

}
