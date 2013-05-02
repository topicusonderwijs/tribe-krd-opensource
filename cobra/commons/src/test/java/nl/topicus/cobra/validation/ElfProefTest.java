/*
 * Copyright (c) 2005-2007, Topicus b.v.
 * All rights reserved
 */
package nl.topicus.cobra.validation;

import junit.framework.TestCase;

/**
 * Test voor de elfproef.
 * 
 * @author Martijn Dashorst
 */
public class ElfProefTest extends TestCase
{
	/**
	 * Helper voor het controleren van arrays bij asserts.
	 * 
	 * @param array
	 *            het array dat naar een string omgezet moet worden.
	 * @return de array als een string
	 */
	private String asString(long[] array)
	{
		StringBuilder sb = new StringBuilder("[");
		String comma = "";
		for (long l : array)
		{
			sb.append(comma).append(l);
			comma = ",";
		}
		sb.append("]");
		return sb.toString();
	}

	public void testGetDigits0()
	{
		long[] digits = new long[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
		long[] result = new ElfProef().getDigits("0");
		assertEquals(9, result.length);
		assertEquals(asString(digits), asString(result));
	}

	public void testGetDigits1()
	{
		long[] digits = new long[] {0, 0, 0, 0, 0, 0, 0, 0, 1};
		long[] result = new ElfProef().getDigits("1");
		assertEquals(9, result.length);
		assertEquals(asString(digits), asString(result));
	}

	public void testGetDigits123456789()
	{
		long[] digits = new long[] {1, 2, 3, 4, 5, 6, 7, 8, 9};
		long[] result = new ElfProef().getDigits("123456789");
		assertEquals(9, result.length);
		assertEquals(asString(digits), asString(result));
	}

	public void testGetDigits987654321()
	{
		long[] digits = new long[] {9, 8, 7, 6, 5, 4, 3, 2, 1};
		long[] result = new ElfProef().getDigits("987654321");
		assertEquals(9, result.length);
		assertEquals(asString(digits), asString(result));
	}

	public void testGeldigSofiNummerString()
	{
		ElfProef proef = new ElfProef();
		assertTrue(proef.isGeldigSofiNummer("12"));
		assertTrue(proef.isGeldigSofiNummer("182071649"));
		assertFalse(proef.isGeldigSofiNummer("182071648"));
		assertFalse(proef.isGeldigSofiNummer("18207164A"));
	}

	public void testGeldigOnderwijsNummerString()
	{
		ElfProef proef = new ElfProef();
		assertTrue(proef.isGeldigOnderwijsNummer("100310303"));
		assertTrue(proef.isGeldigOnderwijsNummer("100729533"));
		assertFalse(proef.isGeldigOnderwijsNummer("182071649"));
		assertFalse(proef.isGeldigOnderwijsNummer("A82071649"));
	}

	public void testGeldigOnderwijsNummerLong()
	{
		ElfProef proef = new ElfProef();
		assertTrue(proef.isGeldigOnderwijsNummer(100310303L));
		assertTrue(proef.isGeldigOnderwijsNummer(100729533L));
		assertFalse(proef.isGeldigOnderwijsNummer(182071649L));
	}

	public void testGeldigSofiNummerLong()
	{
		ElfProef proef = new ElfProef();
		assertTrue(proef.isGeldigSofiNummer(12L));
		assertTrue(proef.isGeldigSofiNummer(182071649L));
		assertFalse(proef.isGeldigSofiNummer(182071648L));
	}

	public void testGeldigRekeningNummerString()
	{
		ElfProef proef = new ElfProef();
		assertTrue(proef.isGeldigRekeningNummer("948238593"));
		assertTrue(proef.isGeldigRekeningNummer("19"));
		assertFalse(proef.isGeldigRekeningNummer("948238592"));
	}
}
