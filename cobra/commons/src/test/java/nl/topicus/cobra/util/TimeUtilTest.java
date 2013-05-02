/*
 * $Id: TimeUtilTest.java,v 1.1 2007-07-12 12:22:32 loite Exp $
 * $Revision: 1.1 $
 * $Date: 2007-07-12 12:22:32 $
 *
 * ====================================================================
 * Copyright (c) 2005, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.util;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import junit.framework.AssertionFailedError;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeUtilTest
{
	private static final Logger log = LoggerFactory.getLogger(TimeUtilTest.class);

	@Test
	public void testNull()
	{
		TimeUtil util = new TimeUtil();

		assertNull(util.asDate(null));
		assertNull(util.asTime(null));
		assertNull(util.asDateTime(null));
	}

	@Test
	public void testZero()
	{
		TimeUtil util = new TimeUtil();
		assertNotNull(util.asDate(0));
		assertNotNull(util.asTime(0));
		assertNotNull(util.asDateTime(0));
	}

	@Test
	public void testAsDate()
	{
		Calendar calendar = Calendar.getInstance();
		Date calibration = setAndTestDate(calendar, 4679, Calendar.JUNE, 4);
		TimeUtil util = new TimeUtil();
		Date result = util.asDate(calibration);
		assertNotNull(result);
		calendar.setTime(result);
		testDate(calendar, 4679, Calendar.JUNE, 4);
		// negative dates
		calibration = setAndTestDate(calendar, -4679, Calendar.JUNE, 4);
		result = util.asDate(calibration);
		assertNotNull(result);
		assertTrue(result.getTime() < 0);
		calendar.setTime(result);
		testDate(calendar, -4679, Calendar.JUNE, 4);
	}

	@Test
	public void testAsTime()
	{
		Calendar calendar = Calendar.getInstance();
		Date calibration = setAndTestTime(calendar, 16, 20, 55);
		TimeUtil util = new TimeUtil();
		Date result = util.asTime(calibration);
		assertNotNull(result);
		calendar.setTime(result);
		testTime(calendar, 16, 20, 55);
	}

	@Test
	public void testAsDateTime()
	{
		Calendar calendar = Calendar.getInstance();
		Date calibration = setAndTestDate(calendar, 4679, Calendar.JUNE, 4);
		calibration = setAndTestTime(calendar, 16, 20, 55);
		TimeUtil util = new TimeUtil();
		Date result = util.asDateTime(calibration);
		assertNotNull(result);
		calendar.setTime(result);
		testDate(calendar, 4679, Calendar.JUNE, 4);
		testTime(calendar, 16, 20, 55);
		// negative dates
		calibration = setAndTestDate(calendar, -4679, Calendar.JUNE, 4);
		calibration = setAndTestTime(calendar, 16, 20, 55);
		result = util.asDateTime(calibration);
		assertNotNull(result);
		assertTrue(result.getTime() < 0);
		calendar.setTime(result);
		testDate(calendar, -4679, Calendar.JUNE, 4);
		testTime(calendar, 16, 20, 55);

	}

	private Date setAndTestDate(Calendar calendar, int year, int month, int day)
	{
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		Date calibration = calendar.getTime();
		testDate(calendar, year, month, day);
		return calibration;
	}

	private Date setAndTestTime(Calendar calendar, int hour, int minute, int second)
	{
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		Date calibration = calendar.getTime();
		testTime(calendar, hour, minute, second);
		return calibration;
	}

	private void testTime(Calendar calendar, int hour, int minute, int second)
	{
		calendar.getTimeInMillis();
		assertEquals(hour, calendar.get(Calendar.HOUR_OF_DAY));
		assertEquals(minute, calendar.get(Calendar.MINUTE));
		assertEquals(second, calendar.get(Calendar.SECOND));
	}

	private void testDate(Calendar calendar, int year, int month, int day)
	{
		calendar.getTimeInMillis();
		if (year > 0)
		{
			assertEquals(GregorianCalendar.AD, calendar.get(Calendar.ERA));
			assertEquals(year, calendar.get(Calendar.YEAR));
		}
		else
		{
			assertEquals(GregorianCalendar.BC, calendar.get(Calendar.ERA));
			assertEquals(Math.abs(year) + 1, calendar.get(Calendar.YEAR));
		}
		assertEquals(month, calendar.get(Calendar.MONTH));
		assertEquals(day, calendar.get(Calendar.DAY_OF_MONTH));
	}

	@Test
	public void testDifferenceInYears()
	{
		TimeUtil util = new TimeUtil();
		Calendar calendar = Calendar.getInstance();
		assertEquals(
			util.getDifferenceInYears(setAndTestDate(calendar, 2005, 10, 1),
				setAndTestDate(calendar, 2005, 10, 1)), 0);
		assertEquals(
			util.getDifferenceInYears(setAndTestDate(calendar, 2005, 10, 1),
				setAndTestDate(calendar, 2004, 10, 10)), 0);
		assertEquals(
			util.getDifferenceInYears(setAndTestDate(calendar, 2005, 10, 1),
				setAndTestDate(calendar, 2004, 10, 1)), 1);
		assertEquals(
			util.getDifferenceInYears(setAndTestDate(calendar, 2005, 10, 1),
				setAndTestDate(calendar, 2003, 8, 1)), 2);
		assertEquals(
			util.getDifferenceInYears(setAndTestDate(calendar, 2005, 5, 10),
				setAndTestDate(calendar, 2001, 1, 1)), 4);
		assertEquals(
			util.getDifferenceInYears(setAndTestDate(calendar, 2005, 5, 10),
				setAndTestDate(calendar, 2001, 8, 1)), 3);
		assertEquals(
			util.getDifferenceInYears(setAndTestDate(calendar, 2001, 5, 10),
				setAndTestDate(calendar, 2001, 8, 1)), -1);
	}

	@Test
	public void testDifferenceInDays()
	{
		TimeUtil util = new TimeUtil();
		Calendar calendar = Calendar.getInstance();
		assertEquals(
			util.getDifferenceInDays(setAndTestDate(calendar, 2004, 2, 2),
				setAndTestDate(calendar, 2004, 3, 18)), 47);
		int diff =
			util.getDifferenceInDays(setAndTestDate(calendar, 2006, 7, 11),
				setAndTestDate(calendar, 2006, 7, 25));
		assertEquals(util.getWorkDays(setAndTestDate(calendar, 2006, 7, 11), diff), 10);

	}

	@Test
	public void testGetWorkDays()
	{
		TimeUtil util = new TimeUtil();
		Calendar calendar = Calendar.getInstance();
		assertEquals(util.getWorkDays(setAndTestDate(calendar, 2006, Calendar.JULY, 25), 3), 3);
		assertEquals(util.getWorkDays(setAndTestDate(calendar, 2006, Calendar.JULY, 25), 5), 3);
		assertEquals(util.getWorkDays(setAndTestDate(calendar, 2006, Calendar.JULY, 25), 6), 4);
		assertEquals(util.getWorkDays(setAndTestDate(calendar, 2006, Calendar.JULY, 25), 10), 8);
		assertEquals(util.getWorkDays(setAndTestDate(calendar, 2006, Calendar.JANUARY, 1), 5), 5);
		assertEquals(util.getWorkDays(setAndTestDate(calendar, 2006, Calendar.JANUARY, 1), 365),
			261);

	}

	@Test
	public void testGetWeekendDays()
	{
		TimeUtil util = new TimeUtil();
		Calendar calendar = Calendar.getInstance();
		assertEquals(util.getWeekendDays(setAndTestDate(calendar, 2006, Calendar.JULY, 25), 1), 0);
		assertEquals(util.getWeekendDays(setAndTestDate(calendar, 2006, Calendar.JANUARY, 1), 365),
			104);
		assertEquals(util.getWeekendDays(setAndTestDate(calendar, 2006, Calendar.JULY, 25), 4), 1);
		assertEquals(util.getWeekendDays(setAndTestDate(calendar, 2006, Calendar.JULY, 29), 31), 9);
		assertEquals(util.getWeekendDays(setAndTestDate(calendar, 2006, Calendar.AUGUST, 1), 5), 2);
		assertEquals(util.getWeekendDays(setAndTestDate(calendar, 2006, Calendar.AUGUST, 5), 15), 5);
		assertEquals(util.getWeekendDays(setAndTestDate(calendar, 2006, Calendar.JULY, 2), 14), 4);
		assertEquals(util.getWeekendDays(setAndTestDate(calendar, 2006, Calendar.JULY, 2), 0), 0);
		assertEquals(util.getWeekendDays(setAndTestDate(calendar, 2006, Calendar.JULY, 2), 1), 0);
		assertEquals(util.getWeekendDays(setAndTestDate(calendar, 2006, Calendar.JULY, 2), 6), 1);
		assertEquals(util.getWeekendDays(setAndTestDate(calendar, 2006, Calendar.JULY, 2), 7), 2);
		assertEquals(util.getWeekendDays(setAndTestDate(calendar, 2006, Calendar.JULY, 2), 8), 2);
		assertEquals(util.getWeekendDays(setAndTestDate(calendar, 2006, Calendar.JULY, 2), 12), 2);
		assertEquals(util.getWeekendDays(setAndTestDate(calendar, 2006, Calendar.JULY, 2), 13), 3);
		assertEquals(util.getWeekendDays(setAndTestDate(calendar, 2006, Calendar.JULY, 2), 14), 4);
		assertEquals(util.getWeekendDays(setAndTestDate(calendar, 2006, Calendar.JULY, 2), 15), 4);
		assertEquals(util.getWeekendDays(setAndTestDate(calendar, 2006, Calendar.JULY, 27), -1), 0);
		assertEquals(util.getWeekendDays(setAndTestDate(calendar, 2006, Calendar.JULY, 27), -4), 1);
		assertEquals(util.getWeekendDays(setAndTestDate(calendar, 2006, Calendar.JULY, 27), -13), 4);
		assertEquals(util.getWeekendDays(setAndTestDate(calendar, 2006, Calendar.JULY, 9), -1), 1);
		assertEquals(util.getWeekendDays(setAndTestDate(calendar, 2006, Calendar.JULY, 8), -1), 0);
		assertEquals(util.getWeekendDays(setAndTestDate(calendar, 2006, Calendar.JULY, 9), -7), 2);
		assertEquals(util.getWeekendDays(setAndTestDate(calendar, 2006, Calendar.JULY, 9), -6), 1);
		assertEquals(util.getWeekendDays(setAndTestDate(calendar, 2006, Calendar.JULY, 9), -8), 3);
		assertEquals(util.getWeekendDays(setAndTestDate(calendar, 2006, Calendar.JULY, 9), -13), 3);
		assertEquals(util.getWeekendDays(setAndTestDate(calendar, 2006, Calendar.JULY, 9), -14), 4);
	}

	@Test
	public void testDifferenceInMonths()
	{
		TimeUtil util = new TimeUtil();
		Calendar calendar = Calendar.getInstance();
		assertEquals(
			util.getDifferenceInMonths(setAndTestDate(calendar, 2005, 10, 1),
				setAndTestDate(calendar, 2005, 10, 1)), 0);
		assertEquals(
			util.getDifferenceInMonths(setAndTestDate(calendar, 2005, 10, 1),
				setAndTestDate(calendar, 2004, 10, 10)), 11);
		assertEquals(
			util.getDifferenceInMonths(setAndTestDate(calendar, 2005, 9, 20),
				setAndTestDate(calendar, 2005, 10, 10)), 0);
		assertEquals(
			util.getDifferenceInMonths(setAndTestDate(calendar, 2005, 10, 1),
				setAndTestDate(calendar, 2004, 10, 1)), 12);
		assertEquals(
			util.getDifferenceInMonths(setAndTestDate(calendar, 2005, 10, 1),
				setAndTestDate(calendar, 2003, 8, 1)), 26);
		assertEquals(
			util.getDifferenceInMonths(setAndTestDate(calendar, 2001, 5, 10),
				setAndTestDate(calendar, 2001, 1, 1)), 4);
		assertEquals(
			util.getDifferenceInMonths(setAndTestDate(calendar, 2005, 5, 10),
				setAndTestDate(calendar, 2005, 8, 1)), 2);
	}

	@Test
	public void testMultiThreadedGetInstance()
	{
		// if it works multithreaded it works singlethreaded
		int threadCount = 7;
		TestThread[] threads = new TestThread[threadCount];
		TimeUtil[] utils = new TimeUtil[threadCount];
		CyclicBarrier barrier = new CyclicBarrier(threadCount + 1);
		for (int i = 0; i < threadCount; i++)
		{
			Thread t = new Thread(threads[i] = new TestThread(barrier));
			t.start();
		}
		try
		{
			barrier.await();
		}
		catch (InterruptedException e)
		{
			log.error(e.getMessage(), e);
		}
		catch (BrokenBarrierException e)
		{
			log.error(e.getMessage(), e);
		}
		for (TestThread thread : threads)
		{
			AssertionFailedError error = thread.getError();
			if (error != null)
				throw error;
			// if 1 thread fails they will all fail, so might as well rethrow first
			// encounter
		}
		for (int i = 0; i < threadCount; i++)
		{
			utils[i] = threads[i].getUtil();
		}
		for (int i = 0; i < threadCount; i++)
		{
			TimeUtil util = utils[i];
			for (int j = i + 1; j < threadCount; j++)
				assertNotSame(util, utils[j]);
		}
	}

	private class TestThread implements Runnable
	{
		private AssertionFailedError error;

		private TimeUtil util;

		private CyclicBarrier barrier;

		public TestThread(CyclicBarrier barrier)
		{
			this.barrier = barrier;
		}

		@Override
		public void run()
		{
			log.debug("Thread starting");
			try
			{
				util = TimeUtil.getInstance();
				assertNotNull(util);
				Thread.yield();
				// Onderstaande klopt niet meer, TimeUtil maakt voor elke call naar
				// getInstance een nieuwe instance
				// assertEquals(util, TimeUtil.getInstance());
				// assertSame(util, TimeUtil.getInstance());
			}
			catch (AssertionFailedError e)
			{
				log.error(e.getMessage(), e);
				error = e;
			}
			finally
			{
				try
				{
					barrier.await();
				}
				catch (InterruptedException e)
				{
					log.error(e.getMessage(), e);
				}
				catch (BrokenBarrierException e)
				{
					log.error(e.getMessage(), e);
				}
				log.debug("Thread stopping");
			}
		}

		public final AssertionFailedError getError()
		{
			return error;
		}

		public final TimeUtil getUtil()
		{
			return util;
		}
	}
}
