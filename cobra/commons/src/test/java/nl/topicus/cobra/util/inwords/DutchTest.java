package nl.topicus.cobra.util.inwords;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DutchTest
{
	private Dutch dutch = null;

	@Before
	public void setUp()
	{
		dutch = new Dutch();
	}

	@After
	public void tearDown()
	{
		dutch = null;
	}

	@Test
	public void test0()
	{
		Assert.assertEquals("nul", dutch.toWords(0));
	}

	@Test
	public void testMin1()
	{
		Assert.assertEquals("min een", dutch.toWords(-1));
	}

	@Test
	public void test1()
	{
		Assert.assertEquals("een", dutch.toWords(1));
	}

	@Test
	public void testMin10()
	{
		Assert.assertEquals("min tien", dutch.toWords(-10));
	}

	@Test
	public void test10()
	{
		Assert.assertEquals("tien", dutch.toWords(10));
	}

	@Test
	public void test21()
	{
		Assert.assertEquals("eenentwintig", dutch.toWords(21));
	}

	@Test
	public void testMin100()
	{
		Assert.assertEquals("min honderd", dutch.toWords(-100));
	}

	@Test
	public void test100()
	{
		Assert.assertEquals("honderd", dutch.toWords(100));
	}

	@Test
	public void test111()
	{
		Assert.assertEquals("honderdelf", dutch.toWords(111));
	}

	@Test
	public void testPositive1113()
	{
		Assert.assertEquals("elfhonderddertien", dutch.toWords(1113));
	}

	@Test
	public void testPositive1999()
	{
		Assert.assertEquals("negentienhonderdnegenennegentig", dutch.toWords(1999));
	}

	@Test
	public void testPositive1miljoen()
	{
		Assert.assertEquals("een miljoen", dutch.toWords(1000000));
	}
}
