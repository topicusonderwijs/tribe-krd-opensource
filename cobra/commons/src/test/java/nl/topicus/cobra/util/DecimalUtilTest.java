package nl.topicus.cobra.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import junit.framework.Assert;

import org.junit.Test;

public class DecimalUtilTest
{
	@Test
	public void testNonZero()
	{
		Assert.assertFalse(DecimalUtil.nonZero(null));
		Assert.assertFalse(DecimalUtil.nonZero(BigDecimal.ZERO));
		Assert.assertTrue(DecimalUtil.nonZero(DecimalUtil.THREE));
	}

	@Test
	public void testIsZero()
	{
		Assert.assertTrue(DecimalUtil.isZero(BigDecimal.ZERO));
		Assert.assertFalse(DecimalUtil.isZero(DecimalUtil.THREE));
	}

	@Test
	public void testGreaterThanBigDecimalBigDecimal()
	{
		Assert.assertTrue(DecimalUtil.greaterThan(DecimalUtil.SEVEN, DecimalUtil.THREE));
		Assert.assertFalse(DecimalUtil.greaterThan(DecimalUtil.TWO, DecimalUtil.EIGHT));
		Assert.assertFalse(DecimalUtil.greaterThan(DecimalUtil.SEVEN, DecimalUtil.SEVEN));
	}

	@Test
	public void testGreaterThanBigDecimalInt()
	{
		Assert.assertTrue(DecimalUtil.greaterThan(DecimalUtil.SEVEN, 3));
		Assert.assertFalse(DecimalUtil.greaterThan(DecimalUtil.TWO, 8));
		Assert.assertFalse(DecimalUtil.greaterThan(DecimalUtil.SEVEN, 7));
	}

	@Test
	public void testGreaterThanOrEqual()
	{
		Assert.assertTrue(DecimalUtil.greaterThanOrEqual(DecimalUtil.SEVEN, 3));
		Assert.assertFalse(DecimalUtil.greaterThanOrEqual(DecimalUtil.TWO, 8));
		Assert.assertTrue(DecimalUtil.greaterThanOrEqual(DecimalUtil.SEVEN, 7));
	}

	@Test
	public void testCompare()
	{
		Assert.assertEquals(0, DecimalUtil.compare(null, null));
		Assert.assertEquals(-1, DecimalUtil.compare(null, DecimalUtil.SEVEN));
		Assert.assertEquals(1, DecimalUtil.compare(DecimalUtil.SEVEN, null));
		Assert.assertEquals(0, DecimalUtil.compare(DecimalUtil.SEVEN, DecimalUtil.SEVEN));
		Assert.assertEquals(-1, DecimalUtil.compare(DecimalUtil.THREE, DecimalUtil.SEVEN));
		Assert.assertEquals(1, DecimalUtil.compare(DecimalUtil.FIVE, DecimalUtil.FOUR));
	}

	@Test
	public void testMax()
	{
		Assert.assertEquals(DecimalUtil.FIVE, DecimalUtil.max(DecimalUtil.FIVE, DecimalUtil.FOUR));
		Assert.assertEquals(DecimalUtil.FIVE, DecimalUtil.max(DecimalUtil.ONE, DecimalUtil.FIVE));
	}

	@Test
	public void testMin()
	{
		Assert.assertEquals(DecimalUtil.FOUR, DecimalUtil.min(DecimalUtil.FIVE, DecimalUtil.FOUR));
		Assert.assertEquals(DecimalUtil.FOUR, DecimalUtil.min(DecimalUtil.FOUR, DecimalUtil.NINE));
	}

	@Test
	public void testGreaterThanZero()
	{
		Assert.assertFalse(DecimalUtil.greaterThanZero(null));
		Assert.assertFalse(DecimalUtil.greaterThanZero(BigDecimal.ZERO));
		Assert.assertTrue(DecimalUtil.greaterThanZero(DecimalUtil.SEVEN));
	}

	@Test
	public void testZeroOrMore()
	{
		Assert.assertFalse(DecimalUtil.zeroOrMore(null));
		Assert.assertFalse(DecimalUtil.zeroOrMore(BigDecimal.valueOf(-0.01)));
		Assert.assertTrue(DecimalUtil.zeroOrMore(BigDecimal.ZERO));
		Assert.assertTrue(DecimalUtil.zeroOrMore(BigDecimal.valueOf(0.01)));
		Assert.assertTrue(DecimalUtil.zeroOrMore(DecimalUtil.SEVEN));
	}

	@Test
	public void testLessThanZero()
	{
		Assert.assertFalse(DecimalUtil.lessThanZero(null));
		Assert.assertTrue(DecimalUtil.lessThanZero(BigDecimal.valueOf(-0.01)));
		Assert.assertFalse(DecimalUtil.lessThanZero(BigDecimal.ZERO));
		Assert.assertFalse(DecimalUtil.lessThanZero(BigDecimal.valueOf(0.01)));
		Assert.assertTrue(DecimalUtil.lessThanZero(BigDecimal.valueOf(-7)));
	}

	@Test
	public void testLessOrEqualtoZero()
	{
		Assert.assertFalse(DecimalUtil.lessOrEqualtoZero(null));
		Assert.assertTrue(DecimalUtil.lessOrEqualtoZero(BigDecimal.valueOf(-0.01)));
		Assert.assertTrue(DecimalUtil.lessOrEqualtoZero(BigDecimal.ZERO));
		Assert.assertFalse(DecimalUtil.lessOrEqualtoZero(BigDecimal.valueOf(0.01)));
		Assert.assertTrue(DecimalUtil.lessOrEqualtoZero(BigDecimal.valueOf(-7)));
	}

	@Test
	public void testLessOrEqual()
	{
		Assert.assertTrue(DecimalUtil.lessOrEqual(DecimalUtil.FIVE, 11));
		Assert.assertFalse(DecimalUtil.lessOrEqual(DecimalUtil.FIVE, 3));
	}

	@Test
	public void testDivideBigDecimalInt()
	{
		Assert.assertEquals(BigDecimal.ZERO, DecimalUtil.divide(BigDecimal.ZERO, 3));
		Assert.assertEquals(DecimalUtil.TWO, DecimalUtil.divide(DecimalUtil.EIGHT, 4));
	}

	@Test
	public void testDivideBigDecimalMathContextInt()
	{
		MathContext mc = new MathContext(1, RoundingMode.HALF_UP);

		Assert.assertEquals(DecimalUtil.THREE, DecimalUtil.divide(DecimalUtil.FIVE, mc, 2));
	}

	@Test
	public void testDivideBigDecimalMathContextBigDecimal()
	{
		MathContext mc = new MathContext(1, RoundingMode.HALF_UP);

		Assert.assertEquals(DecimalUtil.THREE, DecimalUtil.divide(DecimalUtil.FIVE, mc,
			DecimalUtil.TWO));
	}

	@Test
	public void testDivideIntInt()
	{
		Assert.assertEquals(BigDecimal.valueOf(2.5), DecimalUtil.divide(5, 2));
	}

	@Test
	public void testDivideIntMathContextInt()
	{
		MathContext mc = new MathContext(1, RoundingMode.HALF_UP);

		Assert.assertEquals(DecimalUtil.THREE, DecimalUtil.divide(5, mc, 2));
	}

	@Test
	public void testMultiply()
	{
		Assert.assertEquals(DecimalUtil.SIX, DecimalUtil.multiply(DecimalUtil.THREE, 2));
	}

	@Test
	public void testValueOf()
	{
		Assert.assertEquals(BigDecimal.ZERO, DecimalUtil.valueOf("0"));
		Assert.assertEquals(BigDecimal.valueOf(0.1), DecimalUtil.valueOf("0.1"));
		Assert.assertEquals(BigDecimal.valueOf(0.1), DecimalUtil.valueOf("0,1"));
		Assert.assertEquals(BigDecimal.valueOf(1000.1), DecimalUtil.valueOf("1.000,1"));
		Assert.assertEquals(BigDecimal.valueOf(1000.1), DecimalUtil.valueOf("1,000.1"));
		Assert.assertEquals(DecimalUtil.THREE, DecimalUtil.valueOf("3,"));
		Assert.assertEquals(BigDecimal.valueOf(0.2), DecimalUtil.valueOf(",2"));
		Assert.assertEquals(BigDecimal.valueOf(1000000000), DecimalUtil.valueOf("1.000.000.000"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testValueOfMeerderePuntenEnKommas()
	{
		Assert.assertEquals(BigDecimal.ZERO, DecimalUtil.valueOf("0,1,2.3.4"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testValueOfMeerdereKommas()
	{
		Assert.assertEquals(BigDecimal.ZERO, DecimalUtil.valueOf("0.3,4,5"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testValueOfMeerderePunten()
	{
		Assert.assertEquals(BigDecimal.ZERO, DecimalUtil.valueOf("0,3.4.5"));
	}
}
