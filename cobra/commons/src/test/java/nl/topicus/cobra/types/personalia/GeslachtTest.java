/*
 * Copyright (c) 2009, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.types.personalia;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author steenbeeke
 */
public class GeslachtTest
{
	@Test
	public void man()
	{
		assertEquals("Man", Geslacht.Man.name());
		assertEquals("Man", Geslacht.Man.toString());
		assertEquals(Geslacht.valueOf("Man"), Geslacht.Man);
	}

	@Test
	public void vrouw()
	{
		assertEquals("Vrouw", Geslacht.Vrouw.name());
		assertEquals("Vrouw", Geslacht.Vrouw.toString());
		assertEquals(Geslacht.valueOf("Vrouw"), Geslacht.Vrouw);
	}

	@Test
	public void onbekend()
	{
		assertEquals("Onbekend", Geslacht.Onbekend.name());
		assertEquals("Onbekend", Geslacht.Onbekend.toString());
		assertEquals(Geslacht.valueOf("Onbekend"), Geslacht.Onbekend);
	}
}
