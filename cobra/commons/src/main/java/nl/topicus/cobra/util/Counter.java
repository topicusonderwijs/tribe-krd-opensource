/*
 * $Id: Counter.java,v 1.2 2007-07-23 13:40:42 loite Exp $
 * $Revision: 1.2 $
 * $Date: 2007-07-23 13:40:42 $
 *
 * ====================================================================
 * Copyright (c) 2005, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.util;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple tellertje om omhoog of naar beneden te tellen in stapjes van 1. Counter is
 * threadsafe en overflow safe. Bij overflow wordt gereset naar 0 waarna de juiste
 * operatie (optellen of aftrekken) uitgevoerd wordt
 * 
 * @author marrink
 */
public final class Counter implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Globale instantie om naarbeneden te tellen.
	 */
	private static final Counter instanceDown = new Counter();

	/**
	 * Globale instantie om omhoog te tellen
	 */
	private static final Counter instanceUp = new Counter(CountMode.Up);

	/**
	 * log
	 */
	private static final Logger log = LoggerFactory.getLogger(Counter.class);

	/**
	 * Tel omhoog of omlaag
	 */
	public enum CountMode
	{
		/**
		 * Voor het optellen
		 */
		Up,
		/**
		 * Voor het aftellen
		 */
		Down
	}

	/**
	 * huidige waarde teller.
	 */
	private int current;

	/**
	 * Is er een overflow geweest.
	 */
	private boolean overflow;

	/**
	 * Tellen we omhoog of omlaag.
	 */
	private CountMode mode;

	/**
	 * Een nieuwe Counter geinitialiseerd op 0 die naar beneden telt.
	 */
	public Counter()
	{
		this(0, CountMode.Down);
	}

	/**
	 * Een nieuwe teller in de gewenste mode, geinitialiseerd op 0.
	 * 
	 * @param mode
	 */
	public Counter(CountMode mode)
	{
		this(0, mode);
	}

	/**
	 * Een nieuwe teller in de gewenste mode en geinitialiseerd op het gewenste getal.
	 * 
	 * @param init
	 * @param mode
	 */
	public Counter(int init, CountMode mode)
	{
		current = init;
		this.mode = mode;
	}

	/**
	 * Een Globaal unieke teller in de gewenste mode.
	 * 
	 * @param mode
	 * @return Een globale teller in de gewenste mode.
	 */
	public static Counter getInstance(CountMode mode)
	{
		if (mode == CountMode.Down)
			return instanceDown;
		return instanceUp;
	}

	/**
	 * Tellen we omhoog of omlaag.
	 * 
	 * @return De mode van deze counter
	 */
	public CountMode mode()
	{
		return mode;
	}

	/**
	 * De volgende waarde van de teller of dat nu omhoog of omlaag is. ThreadSafe.
	 * 
	 * @return Het volgende nummer van de teller
	 */
	public int next()
	{
		if (mode == CountMode.Down)
			return nextLower();
		return nextHigher();
	}

	/**
	 * implementatie voor omlaag tellen.
	 */
	private synchronized int nextLower()
	{
		boolean belowZero = current < 0;
		current--;
		if (belowZero)
		{
			if (current >= 0)
			{
				log.debug("overflow detected, current value after overflow: " + current);
				overflow = true;
				current = -1;
			}
		}
		return current;
	}

	/**
	 * implementatie voor optellen.
	 */
	private synchronized int nextHigher()
	{
		boolean aboveZero = current > 0;
		current++;
		if (aboveZero && current <= 0)
		{
			log.debug("overflow detected, current value after overflow: " + current);
			overflow = true;
			current = 1;
		}
		return current;
	}

	/**
	 * Huidige waarde van de teller. ThreadSafe.
	 * 
	 * @return De huidige waarde van de teller
	 */
	public synchronized int currentValue()
	{
		return current;
	}

	/**
	 * Is er een Overflow geweest. ThreadSafe.
	 * 
	 * @return true als de Counter een overflow meegemaakt heeft
	 */
	public synchronized boolean hasOverFlown()
	{
		return overflow;
	}

	/**
	 * Resets overflow flag. ThreadSafe.
	 */
	public synchronized void resetOverFlow()
	{
		overflow = false;
	}

	/**
	 * Reset counter op 0. Pas op dit kan onverwachte resultaten hebben in de rest van de
	 * applicatie indien uitgevoerd op 1 van de telers verkregen via Counter.Instance().
	 * ThreadSafe.
	 */
	public void Reset()
	{
		Reset(0);
	}

	/**
	 * Reset counter naar gewenste waarde. Pas op dit kan onverwachte resultaten hebben in
	 * de rest van de applicatie indien uitgevoerd op 1 van de telers verkregen via
	 * Counter.Instance(). ThreadSafe.
	 * 
	 * @param reset
	 *            De waarde van de counter na de reset
	 */
	public synchronized void Reset(int reset)
	{
		current = reset;
	}
}
