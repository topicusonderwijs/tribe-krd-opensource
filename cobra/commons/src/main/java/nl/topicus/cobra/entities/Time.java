/*
 * Copyright (c) 2005, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.entities;

import java.util.Calendar;

/**
 * A class representing a time value excluding seconds.
 * 
 * @author loite
 */
public class Time extends java.sql.Time
{
	private static final long serialVersionUID = 1973159155192729297L;

	/**
	 * Maakt een nieuw timeobject aan die overeenkomt met ms milliseconden sinds de epoch.
	 */
	public Time(long ms)
	{
		super(ms);
		Calendar cal = Calendar.getInstance();
		cal.setTime(this);
		cal.set(Calendar.SECOND, 0);
		setTime(cal.getTimeInMillis());
	}

	@Override
	public String toString()
	{
		String hourString;
		String minuteString;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(getTime());
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);

		if (hour < 10)
		{
			hourString = "0" + hour;
		}
		else
		{
			hourString = Integer.toString(hour);
		}
		if (minute < 10)
		{
			minuteString = "0" + minute;
		}
		else
		{
			minuteString = Integer.toString(minute);
		}

		return (hourString + ":" + minuteString);
	}

	public static Time getHuidigeTijd()
	{
		Calendar cal = Calendar.getInstance();
		Calendar current = Calendar.getInstance();
		cal.setTimeInMillis(0);
		cal.set(Calendar.HOUR_OF_DAY, current.get(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, current.get(Calendar.MINUTE));

		return new Time(cal.getTimeInMillis());
	}

	/**
	 * Static factory method voor het maken van een time object van een string. De string
	 * moet in het formaat HH:mm zijn.
	 * 
	 * @see java.sql.Time#valueOf(java.lang.String)
	 */
	public static Time valueOf(String value)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0);

		int hour;
		int minute;

		if (value == null)
			throw new java.lang.IllegalArgumentException();

		int colon = value.indexOf(':');
		if ((colon > 0) && (colon < value.length() - 1))
		{
			hour = Math.abs(Integer.parseInt(value.substring(0, colon)));
			minute = Math.abs(Integer.parseInt(value.substring(colon + 1, value.length())));
		}
		else
		{
			throw new java.lang.IllegalArgumentException(
				"Tijd bevat geen juiste tekens, formaat moet bv zijn: HH:mm");
		}
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		Time time = new Time(cal.getTimeInMillis());
		return time;
	}
}
