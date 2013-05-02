/*
 * $Id: JavaUtilTest.java,v 1.1 2007-07-12 12:22:32 loite Exp $
 * $Revision: 1.1 $
 * $Date: 2007-07-12 12:22:32 $
 *
 * ====================================================================
 * Copyright (c) 2005, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.util;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author marrink
 * 
 */
public class JavaUtilTest extends TestCase
{

	/**
	 * Test method for 'nl.topicus.iridium.util.JavaUtil.asList'
	 */
	@SuppressWarnings("unchecked")
	public void testAsList()
	{
		List<Double> temp = JavaUtil.asList();
		assertNotNull(temp);
		assertTrue(temp.isEmpty());
		List<Integer> temp2 = JavaUtil.asList((Integer) null);
		assertNotNull(temp2);
		assertFalse(temp2.isEmpty());
		assertEquals(1, temp2.size());
		temp2 = JavaUtil.asList((Integer[]) null);
		assertNotNull(temp2);
		assertTrue(temp2.isEmpty());
		List< ? extends Number> temp3 = Arrays.asList(1, 2, 3.14, 4, null, 6.0f, 7L);
		assertFalse(temp3.isEmpty());
		assertEquals(7, temp3.size());
		assertEquals(1, temp3.get(0));
		assertEquals(2, temp3.get(1));
		assertEquals(3.14, temp3.get(2));
		assertEquals(4, temp3.get(3));
		assertNull(temp3.get(4));
		assertEquals(6.0f, temp3.get(5));
		assertEquals(7L, temp3.get(6));
		assertTrue(temp3.get(0) instanceof Integer);
		assertTrue(temp3.get(2) instanceof Double);
		assertTrue(temp3.get(5) instanceof Float);
		assertTrue(temp3.get(6) instanceof Long);
	}

	/**
	 * Test method for 'nl.topicus.iridium.util.JavaUtil.removeEqualItems(List<T>) <T>'
	 */
	public void testRemoveEqualItems()
	{
		assertNull(JavaUtil.removeEqualItems(null));
		List<Integer> empty = JavaUtil.asList();
		assertEquals(empty, JavaUtil.removeEqualItems(empty));
		List<Integer> temp = JavaUtil.asList(1, 2, 3);
		assertEquals(temp, JavaUtil.removeEqualItems(JavaUtil.asList(1, 2, 3)));
		assertEquals(temp, JavaUtil.removeEqualItems(JavaUtil.asList(1, 2, 3, 3)));
		assertEquals(temp, JavaUtil.removeEqualItems(JavaUtil.asList(1, 2, 3, 2)));
		assertEquals(temp, JavaUtil.removeEqualItems(JavaUtil.asList(1, 2, 3, 2, 3, 1, 2, 3)));
		assertEquals(temp, JavaUtil.removeEqualItems(JavaUtil.asList(1, 2, 3, 3, 3, 3, 1, 3, 3, 3)));
	}

}
