/*
 * Copyright (c) 2005-2009, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import nl.topicus.cobra.entities.Dag;
import nl.topicus.cobra.entities.Time;

/**
 * Simpel utillity class for working with dates. This class is not thread safe.
 * 
 * @author marrink
 */
public final class TimeUtil
{
	private Calendar calendar;

	/**
	 * Constructs a new TimeUtil using the default Calendar.
	 * 
	 * @see Calendar#getInstance()
	 */
	public TimeUtil()
	{
		super();
		calendar = Calendar.getInstance();

		// Set enkele properties die ervoor moeten zorgen dat ook weekafhandeling
		// volgens Europese standaarden gebeurt.
		calendar.setMinimalDaysInFirstWeek(4);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
	}

	/**
	 * Constructs a new TimeUtil with the given Calendar.
	 * 
	 * @param calendar
	 */
	public TimeUtil(Calendar calendar)
	{
		super();
		this.calendar = calendar;
	}

	/**
	 * Returns a new Date containing only the date information from the specified Date,
	 * all time information is set to 0.
	 * 
	 * @param date
	 * @return new date containing no time information.
	 */
	public Date asDate(Date date)
	{
		if (date == null)
			return null;
		return asDate(date.getTime());
	}

	/**
	 * Returns a new Date containing only the date information from the specified Date,
	 * all time information is set to 0.
	 * 
	 * @param date
	 *            # ms since epoch
	 * @return new date containing no time information.
	 */
	public Date asDate(long date)
	{
		calendar.setTimeInMillis(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * Returns a date which corresponds to the parametervalues <br/>
	 * Month value is 0-based. e.g., 0 for January.
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return The date
	 */
	public Date asDate(int year, int month, int day)
	{
		calendar.set(year, month, day);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * Returns a new date that is parsed from the given date string in iso format
	 * (yyyymmdd).
	 * 
	 * @param isoDateString
	 *            The date string to parse in yyyymmdd format.
	 * @return A new date.
	 */
	public Date isoStringAsDate(String isoDateString)
	{
		if (isoDateString == null || isoDateString.equals("        "))
			return null;
		if (isoDateString.length() != 8)
			return null;
		int year = Integer.valueOf(isoDateString.substring(0, 4));
		int month = Integer.valueOf(isoDateString.substring(4, 6)) - 1; // -1 omdat in
		// Java Januari=0,
		// en in het
		// bestand
		// Januari=1
		int day = Integer.valueOf(isoDateString.substring(6, 8));
		calendar.set(year, month, day);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * Returns a new date/time that is parsed from the given date string in iso format
	 * (yyyymmddHHmmss).
	 * 
	 * @param isoDateTimeString
	 *            The date string to parse in yyyymmddHHmmss format.
	 * @return A new date.
	 */
	public Date isoStringAsDateTime(String isoDateTimeString)
	{
		if (isoDateTimeString == null || isoDateTimeString.equals("")
			|| isoDateTimeString.equals("              "))
			return null;
		if (isoDateTimeString.length() != 14)
			return null;
		int year = Integer.valueOf(isoDateTimeString.substring(0, 4));
		int month = Integer.valueOf(isoDateTimeString.substring(4, 6)) - 1; // -1 omdat in
		// Java Januari=0,
		// en in het
		// bestand
		// Januari=1
		int day = Integer.valueOf(isoDateTimeString.substring(6, 8));
		int hour = Integer.valueOf(isoDateTimeString.substring(8, 10));
		int minute = Integer.valueOf(isoDateTimeString.substring(10, 12));
		int second = Integer.valueOf(isoDateTimeString.substring(12, 14));
		calendar.set(year, month, day);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * Parses the given date string into a date using the given date format.
	 * 
	 * @param dateString
	 *            The date string to be parsed.
	 * @param dateFormat
	 *            A string containing the format of the date string. MUST AT LEAST contain
	 *            the following: yyyy (the position of a 4-digit year) or yy (the position
	 *            of a 1-digit year) mm (the position of a 2-digit month) or m (the
	 *            position of a 1-digit month) dd (the position of a 2-digit day of month)
	 *            or d (the position of a 1-digit day)
	 * @return The parsed date, or null if the string is not a valid date.
	 */
	public Date parseDateString(String dateString, String dateFormat)
	{
		Date date = null;

		if (dateString.length() != dateFormat.length())
			return null;

		int index = -1;
		while ((index = dateFormat.indexOf("-", index + 1)) > -1)
		{
			char checkChar = dateString.charAt(index);
			if (checkChar != '-' && checkChar != '.' && checkChar != ',' && checkChar != '/'
				&& checkChar != '\\' && checkChar != ' ' && checkChar != ':' && checkChar != ';')
				return null;
		}

		int year = 0;
		int month = 0;
		int day = 0;
		try
		{
			index = dateFormat.indexOf("yyyy");
			if (index > -1)
				year = Integer.valueOf(dateString.substring(index, index + 4));
			else if (dateFormat.indexOf("yy") > -1)
			{
				index = dateFormat.indexOf("yy");
				year = Integer.valueOf(dateString.substring(index, index + 2));

				if (year > -1 && index == dateString.length() - 2)
				{
					if (year > 30)
						year = 1900 + year;
					else
						year = 2000 + year;
				}
				// situatie voor bijvoorbeeld: 11-05-985
				// else if (year > -1 && index == dateString.length()-3)
				// }
				else
					return null;
			}
			else
			{
				index = dateFormat.indexOf("y");
				year = Integer.valueOf(dateString.substring(index, index + 1));
				if (year > -1 && index == dateString.length() - 1)
				{
					year = 2000 + year;
				}
				else
					return null;

			}
			index = dateFormat.indexOf("mm");
			if (index > -1)
			{
				month = Integer.valueOf(dateString.substring(index, index + 2)) - 1;
				// situatie van de GBA-datum
				if (month == -1)
					month = 0;
				else if (month < 0 || month > 11)
					return null;
			}
			else
			{
				index = dateFormat.indexOf("m");
				month = Integer.valueOf(dateString.substring(index, index + 1)) - 1;
				if (month < 0 || month > 11)
					return null;
			}
			index = dateFormat.indexOf("dd");
			if (index > -1)
			{
				day = Integer.valueOf(dateString.substring(index, index + 2));
				if (day < 0 || day > 31)
					return null;
			}
			else
			{
				index = dateFormat.indexOf("d");
				day = Integer.valueOf(dateString.substring(index, index + 1));
				if (day < 0 || day > 31)
					return null;
			}

			// wanneer de dag waarde voor die maand fout is dan mag de calendar de maand
			// niet
			// wijzigen (bv 30-02-2009). We stellen dit tijdelijk in om niet alles in deze
			// Util te laten crashen.
			calendar.setLenient(false);

			// if(month == 0) month = 1; //nee dus, Calendar month begint bij 0 en is dus
			// correct
			if (day == 0)
				day = 1;
			calendar.set(year, month, day);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);

			date = calendar.getTime();
			Date currentDate = currentDate();
			if (getDifferenceInYears(date, currentDate) > 20 || year < 1900)
			{
				// A date that is before 1900 or more than 20 years from now is probably
				// not a valid date.
				return null;
			}

			// back to default.
			calendar.setLenient(true);

			return date;
		}
		catch (NumberFormatException e)
		{
			return null;
		}
		catch (IndexOutOfBoundsException e)
		{
			return null;
		}
		catch (IllegalArgumentException e)
		{
			return null;
		}
	}

	/**
	 * Tries to parse the given string as a date using the most common Dutch date formats.
	 * 
	 * @param value
	 *            The string to be parsed.
	 * @return The parsed date, or null if the string cannot be parsed as a date.
	 */
	public Date parseDateString(String value)
	{
		Date date = null;
		date = parseDateString(value, "dd-mm-yyyy");
		if (date != null)
			return date;
		date = parseDateString(value, "ddmmyyyy");
		if (date != null)
			return date;
		date = parseDateString(value, "dd-mmyyyy");
		if (date != null)
			return date;
		date = parseDateString(value, "ddmm-yyyy");
		if (date != null)
			return date;
		date = parseDateString(value, "d-mm-yyyy");
		if (date != null)
			return date;
		date = parseDateString(value, "dd-m-yyyy");
		if (date != null)
			return date;
		date = parseDateString(value, "dmyyyy");
		if (date != null)
			return date;
		date = parseDateString(value, "d-m-yyyy");
		if (date != null)
			return date;
		date = parseDateString(value, "dd-mm-yy");
		if (date != null)
			return date;
		date = parseDateString(value, "ddmmyy");
		if (date != null)
			return date;
		date = parseDateString(value, "d-mm-yy");
		if (date != null)
			return date;
		date = parseDateString(value, "dd-m-yy");
		if (date != null)
			return date;
		date = parseDateString(value, "d-m-yy");
		if (date != null)
			return date;
		date = parseDateString(value, "ddmyy");
		if (date != null)
			return date;
		date = parseDateString(value, "dmmyy");
		if (date != null)
			return date;
		date = parseDateString(value, "dm-yy");
		if (date != null)
			return date;
		date = parseDateString(value, "d-myy");
		if (date != null)
			return date;
		date = parseDateString(value, "yyyymmdd");
		if (date != null)
			return date;
		date = parseDateString(value, "yyyy-mm-dd");
		if (date != null)
			return date;
		date = parseDateString(value, "dmyy");
		if (date != null)
			return date;
		date = parseDateString(value, "d-m-y");
		if (date != null)
			return date;
		date = parseDateString(value, "dd-m-y");
		if (date != null)
			return date;
		date = parseDateString(value, "dd-mm-y");
		if (date != null)
			return date;
		date = parseDateString(value, "d-mm-y");
		if (date != null)
			return date;
		return null;
	}

	/**
	 * Tries to parse the given string as a string using the most common Dutch date
	 * formats.
	 * 
	 * @param value
	 *            The string to be parsed.
	 * @return The parsed date as a string, or the null if no parse.
	 */
	public String parseDateStringToString(String value)
	{
		Date date = null;
		date = parseDateString(value, "dd-mm-yyyy");
		if (date != null)
		{
			String[] values = value.split("-");

			return values[0] + "-" + values[1] + "-" + values[2];
		}
		date = parseDateString(value, "ddmmyyyy");
		if (date != null)
		{
			String datum = formatDate(date);
			String[] datumValues = datum.split("-");
			datumValues[0] = value.substring(0, 2);
			datumValues[1] = value.substring(2, 4);
			datumValues[2] = value.substring(4);

			return datumValues[0] + "-" + datumValues[1] + "-" + datumValues[2];
		}
		date = parseDateString(value, "dd-mmyyyy");
		if (date != null)
		{
			String datum = formatDate(date);
			String[] datumValues = datum.split("-");
			datumValues[0] = value.substring(0, 2);
			datumValues[1] = value.substring(3, 5);
			datumValues[2] = value.substring(5);

			return datumValues[0] + "-" + datumValues[1] + "-" + datumValues[2];
		}
		date = parseDateString(value, "ddmm-yyyy");
		if (date != null)
		{
			String datum = formatDate(date);
			String[] datumValues = datum.split("-");
			datumValues[0] = value.substring(0, 2);
			datumValues[1] = value.substring(2, 4);
			datumValues[2] = value.substring(5);

			return datumValues[0] + "-" + datumValues[1] + "-" + datumValues[2];
		}

		// Deze datum notatie wordt gebruikt in retourberichten van het RVC
		date = parseDateString(value, "yyyy-mm-dd");
		if (date != null)
		{
			return formatDate(date);
		}

		/*
		 * onderstaande mogelijkheden nog niet date = parseDateString(value, "dd-mm-yy");
		 * if(date != null) return formatDate(date); date = parseDateString(value,
		 * "ddmmyy"); if(date != null) return formatDate(date); date =
		 * parseDateString(value, "d-mm-yyyy"); if(date != null) return date; date =
		 * parseDateString(value, "dd-m-yyyy"); if(date != null) return date; date =
		 * parseDateString(value, "d-m-yyyy"); if(date != null) return date; date =
		 * parseDateString(value, "d-mm-yy"); if(date != null) return date; date =
		 * parseDateString(value, "dd-m-yy"); if(date != null) return date; date =
		 * parseDateString(value, "d-m-yy"); if(date != null) return date;
		 */
		return null;
	}

	/**
	 * @param date
	 * @return The date in format d MMMMM yyyy. Example: 3 maart 2007
	 */
	public String formatDateOfficial(Date date)
	{
		if (date == null)
			return null;
		Locale loc = new Locale("nl", "NL");
		DateFormat format = new SimpleDateFormat("d MMMM yyyy", loc);

		return format.format(date);
	}

	/**
	 * Formats the given date to a String according to the normal Dutch date notation.
	 * 
	 * @param date
	 *            The date to format.
	 * @return A String containing the date
	 */
	public String formatDate(Date date)
	{
		return formatDate(date, "-");
	}

	/**
	 * Formats the given date to a String according to the normal Dutch date notation.
	 * 
	 * @param date
	 *            The date to format.
	 * @param separator
	 * @return A String containing the date
	 */
	public String formatDate(Date date, String separator)
	{
		if (date == null)
			return null;
		calendar.setTime(date);

		String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		if (day.length() == 1)
			day = "0" + day;
		String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
		if (month.length() == 1)
			month = "0" + month;
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		return day + separator + month + separator + year;
	}

	/**
	 * @param date
	 * @param separator
	 * @return A String containing the date
	 */
	public String formatDateZonderJaar(Date date, String separator)
	{
		calendar.setTime(date);

		String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		if (day.length() == 1)
			day = "0" + day;
		String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
		if (month.length() == 1)
			month = "0" + month;
		return day + separator + month;

	}

	/**
	 * Formateert de gegeven datum in de vorm 'Vrijdag 4 april 2008 11:00'
	 * 
	 * @param date
	 * @return de datumstring
	 */
	public String formatDateTimeLong(Date date)
	{
		if (date == null)
			return null;
		Locale loc = new Locale("nl", "NL");
		DateFormat format = new SimpleDateFormat("EEEE d MMMM yyyy HH:mm", loc);

		return format.format(date);
	}

	/**
	 * Formateert de gegeven datum in het meegegeven format volgens SimpleDateFormat'
	 * 
	 * @param date
	 * @return de datumstring
	 */
	public String formatDateTime(Date date, String stringFormat)
	{
		if (date == null || stringFormat == null || stringFormat.isEmpty())
			return null;
		Locale loc = new Locale("nl", "NL");
		DateFormat format = new SimpleDateFormat(stringFormat, loc);
		return format.format(date);
	}

	/**
	 * Formats the given date/time to a String according to the normal Dutch date/time
	 * notation.
	 * 
	 * @param date
	 * @return A String containing the date
	 */
	public String formatDateTime(Date date)
	{
		String dateString = formatDate(date);
		String separator = ":";

		String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
		if (hour.length() == 1)
			hour = "0" + hour;
		String minute = String.valueOf(calendar.get(Calendar.MINUTE));
		if (minute.length() == 1)
			minute = "0" + minute;
		String second = String.valueOf(calendar.get(Calendar.SECOND));
		if (second.length() == 1)
			second = "0" + second;

		return dateString + " " + hour + separator + minute + separator + second;
	}

	/**
	 * Formats the given date/time to a String according to the normal Dutch date/time
	 * notation with the exception of showing the seconds.
	 * 
	 * @param date
	 * @return A String with the corresponding notation
	 */
	public String formatDateTimeNoSeconds(Date date)
	{
		if (date == null)
			return "";

		String dateString = formatDate(date);
		String separator = ":";

		String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
		if (hour.length() == 1)
			hour = "0" + hour;
		String minute = String.valueOf(calendar.get(Calendar.MINUTE));
		if (minute.length() == 1)
			minute = "0" + minute;

		return dateString + " " + hour + separator + minute;
	}

	/**
	 * Formats the given date/time to a String according to the normal Dutch time
	 * notation.
	 * 
	 * @param date
	 * @param separator
	 * @return A String containing the hour and minute of the date
	 */
	public String formatHourMinute(Date date, String separator)
	{
		calendar.setTime(date);
		String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
		if (hour.length() == 1)
			hour = "0" + hour;
		String minute = String.valueOf(calendar.get(Calendar.MINUTE));
		if (minute.length() == 1)
			minute = "0" + minute;

		return hour + separator + minute;
	}

	/**
	 * Formats the given date to a String according to the ISO standard, i.e. yyyy-mm-dd.
	 * 
	 * @param date
	 * @return A String containing the date
	 */
	public String formatDateAsIsoString(Date date)
	{
		calendar.setTime(date);

		String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		if (day.length() == 1)
			day = "0" + day;
		String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
		if (month.length() == 1)
			month = "0" + month;
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		return year + "-" + month + "-" + day;
	}

	/**
	 * Formats the given date to a compact String for use in for example id's or exports.
	 * The format is 'yyyymmddHHmiss'
	 * 
	 * @param date
	 *            The date to format
	 * @return A string containing the date/time.
	 */
	public String formatDateTimeSystem(Date date)
	{
		calendar.setTime(date);

		String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		if (day.length() == 1)
			day = "0" + day;
		String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
		if (month.length() == 1)
			month = "0" + month;
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
		if (hour.length() == 1)
			hour = "0" + hour;
		String minute = String.valueOf(calendar.get(Calendar.MINUTE));
		if (minute.length() == 1)
			minute = "0" + minute;
		String second = String.valueOf(calendar.get(Calendar.SECOND));
		if (second.length() == 1)
			second = "0" + second;

		return year + month + day + hour + minute + second;
	}

	/**
	 * Formateert de gegeven datum naar het formaat dat gebruikt wordt in SchoolPlus
	 * databases. Dit komt overeen met yyyy-m-d, waarbij de maanden van 0 t/m 11 lopen.
	 * 
	 * @param date
	 * @return A String containing the date
	 */
	public String formatDateAsSPlusDateString(Date date)
	{
		calendar.setTime(date);

		String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		String month = String.valueOf(calendar.get(Calendar.MONTH));
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		return year + "-" + month + "-" + day;
	}

	/**
	 * Formats the given date to a String according to the format 'ddd dd-mm', for
	 * example: Ma 03-04
	 * 
	 * @param date
	 * @return A String containing the date
	 */
	public String formatDate_ddd_dd_mm(Date date)
	{
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY + 1;
		String dayName = Dag.getDag(dayOfWeek).getAfkorting();
		String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		if (day.length() == 1)
			day = "0" + day;
		String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
		if (month.length() == 1)
			month = "0" + month;
		return dayName + " " + day + "-" + month;
	}

	/**
	 * Formateert de gegeven datum naar het formaat 'mm-yyyy', en geeft het als string
	 * terug. Bijvoorbeeld: 02-2010.
	 * 
	 * @param date
	 * @return Een string met de maand en het jaar.
	 */
	public String formatDateAs_mm_yyyy(Date date)
	{
		calendar.setTime(date);
		String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
		if (month.length() == 1)
			month = "0" + month;
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		return month + "-" + year;
	}

	/**
	 * Formateert de gegeven datum naar het formaat 'yyyy', en geeft het als string terug.
	 * Bijvoorbeeld: 2010.
	 * 
	 * @param date
	 * @return Een string met het jaar.
	 */
	public String formatDateAs_yyyy(Date date)
	{
		calendar.setTime(date);
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		return year;
	}

	/**
	 * Returns a new Date containing only time information, all date information is set to
	 * the epoch.
	 * 
	 * @param date
	 * @return new Date containing only time information
	 */
	public Date asTime(Date date)
	{
		if (date == null)
			return null;
		return asTime(date.getTime());
	}

	/**
	 * Returns a new Date containing only time information, all date information is set to
	 * the epoch.
	 * 
	 * @param time
	 *            # ms since epoch
	 * @return new Date containing only time information
	 */
	public Date asTime(long time)
	{
		calendar.setTimeInMillis(time);
		if (calendar instanceof GregorianCalendar)
			calendar.set(Calendar.ERA, GregorianCalendar.AD);
		// else we skip the era since it is subclass depended
		calendar.set(Calendar.YEAR, 1970);
		calendar.set(Calendar.DAY_OF_YEAR, 1);
		return calendar.getTime();
	}

	/**
	 * Returns a new Date containing date and time information. Milliseconds are set to 0
	 * since not all databases support that field in a datetime field.
	 * 
	 * @param date
	 * @return new Date containing date and time informtaion
	 */
	public Date asDateTime(Date date)
	{
		if (date == null)
			return null;
		return asDateTime(date.getTime());
	}

	/**
	 * Returns a new Date containing date and time information. Milliseconds are set to 0
	 * since not all databases support that field in a datetime field.
	 * 
	 * @param datetime
	 *            # ms since epoch
	 * @return new Date containing date and time informtaion
	 */
	public Date asDateTime(long datetime)
	{
		calendar.setTimeInMillis(datetime);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * @param date1
	 * @param date2
	 * @return true als de datums van date1 en date2 gelijk zijn. Time gedeelte van de
	 *         dates wordt genegeerd. Als beide dates null zijn, wordt true teruggegeven.
	 *         Als een van de twee dates null is, en de andere niet, wordt false
	 *         teruggegeven.
	 */
	public boolean datesEqual(Date date1, Date date2)
	{
		if (date1 == null && date2 == null)
			return true;
		if (date1 == null || date2 == null)
			return false;
		return asDate(date1).equals(asDate(date2));
	}

	/**
	 * Returns true if the given check date is between the begin- and end date. The end
	 * date may be null.
	 * 
	 * @param beginDate
	 *            The begin of the interval (inclusive). May not be null.
	 * @param endDate
	 *            The end of the interval (inclusive). May be null.
	 * @param checkDate
	 *            The date to check.
	 * @return true if checkDate is between beginDate and endDate
	 */
	public boolean dateBetween(Date beginDate, Date endDate, Date checkDate)
	{
		Asserts.assertNotNull("beginDate", beginDate);
		Asserts.assertNotNull("checkDate", checkDate);
		if (checkDate.before(beginDate))
			return false;
		if (endDate != null && checkDate.after(endDate))
			return false;

		return true;
	}

	/**
	 * Returns true if the given check date is between the begin and end date. The begin
	 * and end date may be null.
	 * 
	 * @param beginDate
	 *            The begin of the interval (inclusive). May be null.
	 * @param endDate
	 *            The end of the interval (inclusive). May be null.
	 * @param checkDate
	 *            The date to check
	 * @return true if checkDate is between beginDate and endDate. Begin date is
	 *         disregarded if null. End date is disregarded if null.
	 */
	public boolean dateBetweenOrBeginEndIsNull(Date beginDate, Date endDate, Date checkDate)
	{
		Asserts.assertNotNull("checkDate", checkDate);
		if (beginDate != null && checkDate.before(beginDate))
			return false;
		if (endDate != null && checkDate.after(endDate))
			return false;

		return true;
	}

	/**
	 * Returns true if two intervals are overlapping.
	 * 
	 * @param beginDate1
	 *            Begin date of first interval.
	 * @param endDate1
	 *            End date of first interval.
	 * @param beginDate2
	 *            Begin date of second interval.
	 * @param endDate2
	 *            End date of sdecond interval.
	 */
	public boolean isOverlapping(Date beginDate1, Date endDate1, Date beginDate2, Date endDate2)
	{
		Asserts.assertNotNull("beginDate1", beginDate1);
		Asserts.assertNotNull("beginDate2", beginDate2);

		Date endDate1b = (endDate1 != null ? endDate1 : new Date(Long.MAX_VALUE));
		Date endDate2b = (endDate2 != null ? endDate2 : new Date(Long.MAX_VALUE));

		return (beginDate1.before(endDate2b) && beginDate2.before(endDate1b));
	}

	/**
	 * Returns a new instance of a TimeUtil. Previously, this method returned a thread
	 * local instance, but this could lead to corruption of the calendar instance between
	 * request, due to subtile changes to the fields of the calendar object, such as the
	 * timezone.
	 * 
	 * @return a TimeUtil.
	 */
	public static TimeUtil getInstance()
	{
		return new TimeUtil();
		// return localTimeUtil.get();
	}

	/**
	 * Returns a new Date representing the current date. time information is reset to 0.
	 * 
	 * @return new Date.
	 */
	public Date currentDate()
	{
		return asDate(System.currentTimeMillis());
	}

	/**
	 * Returns a new Date representing the current time. date information is reset to the
	 * epoch.
	 * 
	 * @return new Date.
	 */
	public Date currentTime()
	{
		return asTime(System.currentTimeMillis());
	}

	/**
	 * Returns a new Date representing the current date and time. milliseconds are set to
	 * 0.
	 * 
	 * @return new Date.
	 */
	public Date currentDateTime()
	{
		return asDateTime(System.currentTimeMillis());
	}

	/**
	 * Returns the years as an int
	 * 
	 * @param date
	 * @return the year
	 */
	public int getYear(Date date)
	{
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * Maak een nieuw Date object met dezelfde datum/tijd als het gegeven Date en met het
	 * gegeven jaartal.
	 * 
	 * @param date
	 * @param year
	 *            het jaartal
	 * @return gegeven datum in het gegeven jaar
	 */
	public Date setYear(Date date, int year)
	{
		calendar.setTime(date);
		calendar.set(Calendar.YEAR, year);
		return asDate(calendar.getTime());
	}

	/**
	 * Returns the current year
	 * 
	 * @return current year
	 */
	public int getCurrentYear()
	{
		return getYear(currentDate());
	}

	/**
	 * Returns the month
	 * 
	 * @param date
	 * @return the month
	 */
	public int getMonth(Date date)
	{
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH);
	}

	/**
	 * Returns the day of the month
	 * 
	 * @param date
	 * @return the day of the month
	 */
	public int getDayOfMonth(Date date)
	{
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Returns the day of the year
	 * 
	 * @param date
	 * @return the day of the year
	 */
	public int getDayOfYear(Date date)
	{
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_YEAR);
	}

	public int getHour(Date date)
	{
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	public int getMinutes(Date date)
	{
		calendar.setTime(date);
		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * Returns the time between date1 and date2.
	 * 
	 * @param date1
	 * @param date2
	 * @return Difference in time. The result is positive if date1 is after date2.
	 */
	public long getDifferenceInMillis(Date date1, Date date2)
	{
		if (date1 == null || date2 == null)
			return 0;
		return date1.getTime() - date2.getTime();
	}

	/**
	 * Returns the number of minutes between date1 and date2.
	 * 
	 * @param date1
	 * @param date2
	 * @return Difference in minutes. The result is positive if date1 is after date2.
	 */
	public int getDifferenceInMinutes(Date date1, Date date2)
	{
		if (date1 == null || date2 == null)
			return 0;
		long millis = date1.getTime() - date2.getTime();
		long seconds = millis / 1000;
		long minutes = seconds / 60;
		return (int) minutes;
	}

	/**
	 * Returns the number of full years between two dates.
	 * 
	 * @param date1
	 * @param date2
	 * @return Number of years. The result is positive if date1 is after date2.
	 */
	public int getDifferenceInYears(Date date1, Date date2)
	{
		if (date1 == null || date2 == null)
			return 0;
		int years = getYear(date1) - getYear(date2);
		int months = getMonth(date1) - getMonth(date2);
		int days = getDayOfMonth(date1) - getDayOfMonth(date2);
		if (months > 0 || (months == 0 && days >= 0))
			return years;
		return years - 1;
	}

	/**
	 * geeft het verschil in dagen terug
	 * 
	 * @param a
	 * @param b
	 * @return aantal dagen
	 */
	public int getDifferenceInDays(Date a, Date b)
	{
		int tempDifference = 0;
		int difference = 0;
		Calendar earlier = Calendar.getInstance();
		Calendar later = Calendar.getInstance();

		if (a.compareTo(b) < 0)
		{
			earlier.setTime(a);
			later.setTime(b);
		}
		else
		{
			earlier.setTime(b);
			later.setTime(a);
		}

		while (earlier.get(Calendar.YEAR) != later.get(Calendar.YEAR))
		{
			tempDifference = 365 * (later.get(Calendar.YEAR) - earlier.get(Calendar.YEAR));
			difference += tempDifference;

			earlier.add(Calendar.DAY_OF_YEAR, tempDifference);
		}

		if (earlier.get(Calendar.DAY_OF_YEAR) != later.get(Calendar.DAY_OF_YEAR))
		{
			tempDifference = later.get(Calendar.DAY_OF_YEAR) - earlier.get(Calendar.DAY_OF_YEAR);
			difference += tempDifference;
		}
		return difference;
	}

	/**
	 * geeft het verschil in hele maanden terug
	 * 
	 * @param a
	 * @param b
	 * @return aantal maanden
	 */
	public int getDifferenceInMonths(Date a, Date b)
	{
		int difference = 0;
		Calendar earlier = Calendar.getInstance();
		Calendar later = Calendar.getInstance();

		if (a.compareTo(b) < 0)
		{
			earlier.setTime(a);
			later.setTime(b);
		}
		else
		{
			earlier.setTime(b);
			later.setTime(a);
		}

		difference += 12 * (later.get(Calendar.YEAR) - earlier.get(Calendar.YEAR));
		earlier.set(Calendar.YEAR, later.get(Calendar.YEAR));
		difference += later.get(Calendar.MONTH) - earlier.get(Calendar.MONTH);
		earlier.set(Calendar.MONTH, later.get(Calendar.MONTH));
		if (earlier.after(later))
			difference--;
		return difference;
	}

	/**
	 * Returns the number of workdays between two dates
	 * 
	 * @param a
	 * @param b
	 * @return the number of workdays
	 */
	public int getDifferenceInWorkDays(Date a, Date b)
	{
		Date earlier = a;
		Date later = b;
		if (later.before(earlier))
		{
			earlier = b;
			later = a;
		}
		int days = getDifferenceInDays(earlier, later);
		return getWorkDays(earlier, days);
	}

	/**
	 * @return Een datum in de verre toekomst die gebruikt kan worden voor queries die
	 *         geacht worden om alle records ongeacht de tijd te returneren.
	 */
	public Date getMaxDate()
	{
		// Datum in de verre toekomst.
		calendar.set(2999, 11, 31);
		return asDate(calendar.getTime());
	}

	/**
	 * @return Een datum in het verre verleden die gebruikt kan worden voor queries die
	 *         geacht worden om alle records ongeacht de tijd te returneren.
	 */
	public Date getMinDate()
	{
		// Datum in het verre verleden.
		calendar.set(1900, 0, 1);
		return asDate(calendar.getTime());
	}

	/**
	 * Voegt days dagen toe aan de gegeven datum. days mag negatief zijn.
	 * 
	 * @param date
	 * @param days
	 * @return nieuwe date
	 */
	public Date addDays(Date date, int days)
	{
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, days);
		return calendar.getTime();
	}

	/**
	 * Voegt weeks weken toe aan de gegeven datum. Weeks mag negatief zijn.
	 * 
	 * @param date
	 * @param weeks
	 * @return nieuwe date
	 */
	public Date addWeeks(Date date, int weeks)
	{
		calendar.setTime(date);
		calendar.add(Calendar.WEEK_OF_YEAR, weeks);
		return calendar.getTime();
	}

	/**
	 * Voegt months maanden toe aan de gegeven datum. Months mag negatief zijn.
	 * 
	 * @param date
	 * @param months
	 * @return nieuwe month
	 */
	public Date addMonths(Date date, int months)
	{
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, months);
		return calendar.getTime();
	}

	/**
	 * Voegt years jaren toe aan de gegeven datum. years mag negatief zijn
	 * 
	 * @param date
	 * @param years
	 * @return De gegeven datum + years jaren
	 */
	public Date addYears(Date date, int years)
	{
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, years);
		return calendar.getTime();
	}

	/**
	 * Voegt hours toe aan de gegeven datum.
	 * 
	 * @param date
	 * @param hours
	 * @return De gegeven datum + hours uren
	 */
	public Date addHours(Date date, int hours)
	{
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, hours);
		return calendar.getTime();
	}

	/**
	 * Voegt minutes minuten toe aan de gegeven datum.
	 * 
	 * @param date
	 * @param minutes
	 * @return De gegeven datum + minutes minuten
	 */
	public Date addMinutes(Date date, int minutes)
	{
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minutes);
		return calendar.getTime();
	}

	/**
	 * Voegt seconds seconden toe aan de gegeven datum.
	 * 
	 * @param date
	 * @param seconds
	 * @return De gegeven datum + seconds seconden
	 */
	public Date addSeconds(Date date, int seconds)
	{
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, seconds);
		return calendar.getTime();
	}

	/**
	 * Voegt milliseconds seconden toe aan de gegeven datum.
	 * 
	 * @param date
	 * @param milliseconds
	 * @return De gegeven datum + milliseconds milliseconden
	 */
	public Date addMiliseconds(Date date, int milliseconds)
	{
		calendar.setTime(date);
		calendar.add(Calendar.MILLISECOND, milliseconds);
		return calendar.getTime();
	}

	/**
	 * geeft de datum van volgende werkdag op basis van de opgegeven datum
	 * 
	 * @param date
	 * @return De volgende werkdag.
	 */
	public Date nextWorkDay(Date date)
	{
		int day = getDayOfWeek(date);
		if (day == Calendar.FRIDAY)
			return addDays(date, 3);
		return addDays(date, 1);
	}

	/**
	 * vergelijkt de twee dates op dagen, maanden en jaren
	 * 
	 * @param date1
	 * @param date2
	 * @return <code>true</code> als de twee dezelfde dag zijn
	 */
	public boolean isZelfdeDatum(Date date1, Date date2)
	{
		return getDayOfMonth(date1) == getDayOfMonth(date2) && getMonth(date1) == getMonth(date2)
			&& getYear(date1) == getYear(date2);
	}

	/**
	 * vergelijkt de twee dates op dag in de week (ma, di, etc), maanden en jaren
	 */
	public boolean isZelfdeDagInWeek(Date date1, Date date2)
	{
		return (getDayOfWeek(date1) == getDayOfWeek(date2) && getMonth(date1) == getMonth(date2) && getYear(date1) == getYear(date2));
	}

	/**
	 * geeft de datum van de werkdag die volgt na de gegeven datum
	 * 
	 * @param date
	 * @return de vorige werkdag (previousWorkDay(maandag)==vrijdag)
	 */
	public Date previousWorkDay(Date date)
	{
		int day = getDayOfWeek(date);
		if (day == Calendar.MONDAY)
			return addDays(date, -3);
		return addDays(date, -1);
	}

	/**
	 * Geeft de datum terug na het aantal
	 * 
	 * @param date
	 *            huidige datum
	 * @param workdays
	 *            het aantal werkdagen
	 * @return De date
	 */
	public Date addWorkDays(Date date, int workdays)
	{
		calendar.setTime(date);
		Date returnDate = calendar.getTime();

		if (workdays < 0)
		{
			for (int i = 0; i > workdays; i--)
			{
				returnDate = previousWorkDay(returnDate);
			}
		}
		else
		{
			for (int i = 0; i < workdays; i++)
			{
				returnDate = nextWorkDay(returnDate);
			}
		}

		return returnDate;
	}

	/**
	 * geeft het aantal werkdagen, telt vanaf date+1 t/m date+days
	 * 
	 * @param date
	 * @param days
	 * @return het aantal werkdagen vanaf date tot het aantal dagen daarna
	 */
	public int getWorkDays(Date date, int days)
	{
		int workDays = days;
		if (workDays < 0)
			Math.abs(workDays);
		workDays = workDays - getWeekendDays(date, days);
		return workDays;
	}

	/**
	 * geeft het aantal weekenddagen, telt vanaf date+1 t/m date+days
	 * 
	 * @param date
	 * @param days
	 * @return het aantal weekenddagen
	 */
	public int getWeekendDays(Date date, int days)
	{
		Date tempDate = date;
		int tempDays = days;

		boolean future = days >= 0;
		if (!future)
		{
			tempDate = addDays(tempDate, tempDays - 1);
			tempDays = Math.abs(tempDays);
		}
		int fixedSunday = Calendar.SUNDAY;
		int dateDay = getDayOfWeek(tempDate);
		if (Calendar.SUNDAY < Calendar.SATURDAY)
		{
			fixedSunday = Calendar.SATURDAY + 1;
			if (dateDay == Calendar.SUNDAY)
				dateDay = fixedSunday;
		}
		int weekendDays = (tempDays / 7) * 2;
		int dayRest = tempDays % 7;
		if (dayRest > 0)
		{
			if (dateDay < Calendar.SATURDAY)
			{
				weekendDays += (dateDay + dayRest) / Calendar.SATURDAY;
				weekendDays += (dateDay + dayRest) / fixedSunday;
			}
			else
			{
				if (dateDay == Calendar.SATURDAY)
					weekendDays += 1;
				else if (dayRest == 6) // sunday
					weekendDays += 1;
			}
		}
		return weekendDays;
	}

	/**
	 * Geeft het ISO weeknummer van de gegeven datum.
	 * 
	 * @param date
	 * @return het weeknummer
	 */
	public int getWeekOfYear(Date date)
	{
		calendar.setTime(date);
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * Geeft het ISO weeknummer van de huidige systeemdatum.
	 * 
	 * @return het weeknummer
	 */
	public int getCurrentWeekOfYear()
	{
		return getWeekOfYear(currentDate());
	}

	/**
	 * Geeft de dag van de week van de gegeven datum als getal.
	 * 
	 * @param date
	 * @return De dag van de week. Zondag=1, Maandag=2 etc.
	 */
	public int getDayOfWeek(Date date)
	{
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * Geeft een list met de data van de werkdagen van de gegeven week.
	 * 
	 * @param year
	 * @param week
	 * @return lijst van data
	 */
	public List<Date> getWeekDates(int year, int week)
	{
		List<Date> weekDates = new ArrayList<Date>(5);
		for (int i = 1; i <= 5; i++)
		{
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.WEEK_OF_YEAR, week);
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY + i - 1);
			weekDates.add(asDate(calendar.getTime()));
		}
		return weekDates;
	}

	/**
	 * Geeft de datum van de gegeven combinatie van jaar, week en weekdagnummer.
	 * 
	 * @param year
	 * @param week
	 * @param weekday
	 * @return de datum
	 */
	public Date getDate(int year, int week, int weekday)
	{
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.WEEK_OF_YEAR, week);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY + weekday - 1);

		return asDate(calendar.getTime());
	}

	/**
	 * Geeft de eerste dag van de gegeven maand.
	 * 
	 * @param year
	 * @param month
	 *            De maand (0-11)
	 * @return De eerste dag van de gegeven maand
	 */
	public Date getFirstDayOfMonth(int year, int month)
	{
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, 1);

		return asDate(calendar.getTime());
	}

	public Date getFirstDayOfMonth(Date date)
	{
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);

		return asDate(calendar.getTime());
	}

	public Date getFirstDayOfWeek(Date date)
	{
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

		return asDate(calendar.getTime());
	}

	public Date getFirstDayOfYear(Date date)
	{
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_YEAR, 1);

		return asDate(calendar.getTime());
	}

	/**
	 * Geeft een array met de begin- en einddatum van de gegeven week. De begindatum van
	 * de week is altijd een maandag, en de einddatum is altijd een zondag.
	 * 
	 * @param jaar
	 * @param week
	 * @return een array met twee data
	 */
	public Date[] getWeekBeginEnEindDatum(int jaar, int week)
	{
		Date[] dates = new Date[2];
		calendar.set(Calendar.YEAR, jaar);
		calendar.set(Calendar.WEEK_OF_YEAR, week);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		dates[0] = asDate(calendar.getTime());
		calendar.set(Calendar.YEAR, jaar);
		calendar.set(Calendar.WEEK_OF_YEAR, week);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		dates[1] = asDate(calendar.getTime());

		return dates;
	}

	/**
	 * Geeft een array met de begin- en einddatum van de werk week. De begindatum van de
	 * week is altijd een maandag (00:00), en de einddatum is altijd een zaterdag (00:00).
	 * 
	 * @param jaar
	 * @param week
	 * @return een array met twee data
	 */
	public Date[] getWerkWeekBeginEnEindDatum(int jaar, int week)
	{
		Date[] dates = new Date[2];
		calendar.set(Calendar.YEAR, jaar);
		calendar.set(Calendar.WEEK_OF_YEAR, week);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		dates[0] = asDate(calendar.getTime());
		calendar.set(Calendar.YEAR, jaar);
		calendar.set(Calendar.WEEK_OF_YEAR, week);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		dates[1] = asDate(calendar.getTime());

		return dates;
	}

	/**
	 * Geeft een tijd terug die overeenkomt met de string.
	 * 
	 * @param value
	 * @return De tijd die correspondeert met de string, <code>null</code> als niet
	 *         succesvol
	 */
	public Time parseTimeString(String value)
	{
		if (StringUtil.isEmpty(value))
			return null;
		Time tijd = null;

		if (value.contains(":"))
		{
			tijd = parseTimeString(value, "HH:mm");
			if (tijd != null)
				return tijd;
			tijd = parseTimeString(value, "H:mm");
			if (tijd != null)
				return tijd;
		}
		if (value.contains("."))
		{
			tijd = parseTimeString(value, "HH.mm");
			if (tijd != null)
				return tijd;
			tijd = parseTimeString(value, "H.mm");
			if (tijd != null)
				return tijd;
		}

		tijd = parseTimeString(value, "HHmm");
		if (tijd != null)
			return tijd;
		tijd = parseTimeString(value, "Hmm");
		if (tijd != null)
			return tijd;
		tijd = parseTimeString(value, "HH");
		if (tijd != null)
			return tijd;

		return null;
	}

	/**
	 * Parst een string naar een datum adhv een formaat
	 * 
	 * @param dateString
	 * @param dateFormat
	 * @return De datum en <code>null</code> bij fout
	 */
	public Time parseTimeString(String dateString, String dateFormat)
	{
		int index = 0;

		int hour = 0;
		int minute = 0;
		int second = 0;
		try
		{
			index = dateFormat.indexOf("HH");
			if (index > -1)
			{
				hour = Integer.valueOf(dateString.substring(index, index + 2));
			}
			else
			{
				index = dateFormat.indexOf("H");
				if (index > -1)
				{
					hour = Integer.valueOf(dateString.substring(index, index + 1));
					if (hour < 0 || hour > 23)
						hour = 0;
				}
			}

			index = dateFormat.indexOf("mm");
			if (index > -1)
			{
				minute = Integer.valueOf(dateString.substring(index, index + 2));
			}
			else
			{
				index = dateFormat.indexOf("m");
				if (index > -1)
				{
					minute = Integer.valueOf(dateString.substring(index, index + 1));
					if (minute < 0 || minute > 60)
						minute = 0;
				}
			}

			index = dateFormat.indexOf("ss");
			if (index > -1)
			{
				second = Integer.valueOf(dateString.substring(index, index + 2));
			}
			else
			{
				index = dateFormat.indexOf("s");
				if (index > -1)
				{
					second = Integer.valueOf(dateString.substring(index, index + 1));
					if (second < 0 || second > 60)
						second = 0;
				}
			}
		}
		catch (NumberFormatException e)
		{
			return null;
		}
		catch (IndexOutOfBoundsException e)
		{
			return null;
		}

		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);

		return new Time(asTime(cal.getTime()).getTime());
	}

	/**
	 * @param nummer
	 * @return De naam van de gegeven weekdag. 1=Zondag etc.
	 */
	public String getWeekdagNaamMetZondagIs_1(int nummer)
	{
		switch (nummer)
		{
			case 1:
				return "Zondag";
			case 2:
				return "Maandag";
			case 3:
				return "Dinsdag";
			case 4:
				return "Woensdag";
			case 5:
				return "Donderdag";
			case 6:
				return "Vrijdag";
			case 7:
				return "Zaterdag";

			default:
				return "Onbekende dag";
		}

	}

	/**
	 * @param nummer
	 * @return De naam van de gegeven weekdag. 1=Maandag etc.
	 */
	public String getWeekdagNaam(int nummer)
	{
		switch (nummer)
		{
			case 1:
				return "Maandag";
			case 2:
				return "Dinsdag";
			case 3:
				return "Woensdag";
			case 4:
				return "Donderdag";
			case 5:
				return "Vrijdag";
			case 6:
				return "Zaterdag";
			case 7:
				return "Zondag";

			default:
				return "Onbekende dag";
		}

	}

	/**
	 * @param nummer
	 *            Het nummer van de maand, 1=Jan, 2=Feb etc.
	 * @return De naam van de gegeven maand. 1=Jan, 2=Feb etc.
	 */
	public String getMaandNaam(int nummer)
	{
		switch (nummer)
		{
			case 1:
				return "Jan";
			case 2:
				return "Feb";
			case 3:
				return "Mar";
			case 4:
				return "Apr";
			case 5:
				return "Mei";
			case 6:
				return "Jun";
			case 7:
				return "Jul";
			case 8:
				return "Aug";
			case 9:
				return "Sep";
			case 10:
				return "Okt";
			case 11:
				return "Nov";
			case 12:
				return "Dec";

			default:
				return "Onbekende maand";
		}
	}

	/**
	 * @param nummer
	 *            Het nummer van de maand, 1=Januari, 2=Februari etc.
	 * @return De naam van de gegeven maand. 1=Januari, 2=Februari etc.
	 */
	public String getMaandNaamVolledig(int nummer)
	{
		switch (nummer)
		{
			case 1:
				return "Januari";
			case 2:
				return "Februari";
			case 3:
				return "Maart";
			case 4:
				return "April";
			case 5:
				return "Mei";
			case 6:
				return "Juni";
			case 7:
				return "Juli";
			case 8:
				return "Augustus";
			case 9:
				return "September";
			case 10:
				return "Oktober";
			case 11:
				return "November";
			case 12:
				return "December";

			default:
				return "Onbekende maand";
		}
	}

	/**
	 * @param naam
	 *            Tekstuele representatie van een maand, bijvoorbeeld "April".
	 * @return Het maandnummer, bijvoorbeeld 4.
	 */
	public Integer getMaandNummer(String naam)
	{
		if (StringUtil.isEmpty(naam))
			return null;

		for (int i = 1; i <= 12; i++)
		{
			if (naam.toLowerCase().trim().equals(getMaandNaamVolledig(i).toLowerCase()))
				return i;
		}

		return null;
	}

	/**
	 * Voegt de tijd toe aan de date
	 * 
	 * @param date
	 * @param tijd
	 * @return De nieuwe date met de tijd informatie
	 */
	public Date setTimeOnDate(Date date, Time tijd)
	{
		calendar.clear();
		calendar.setTime(tijd);
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		int minutes = calendar.get(Calendar.MINUTE);
		int seconds = calendar.get(Calendar.SECOND);

		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, hours);
		calendar.set(Calendar.MINUTE, minutes);
		calendar.set(Calendar.SECOND, seconds);

		Date datum = calendar.getTime();
		return datum;
	}

	/**
	 * Zet de tijd van de gegeven datum op het einde van de dag.
	 * 
	 * @param datum
	 *            De datum waarvan de tijd gezet moet worden
	 * @return een datum waarvan de tijd gelijk is gezet aan 23:59:59
	 */
	public Date maakEindeVanDagVanDatum(Date datum)
	{
		if (datum == null)
			return null;
		calendar.clear();
		calendar.setTime(datum);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);

		return calendar.getTime();
	}

	/**
	 * Zet de tijd van de gegeven datum op het einde van de dag.
	 * 
	 * @param datum
	 *            De datum waarvan de tijd gezet moet worden
	 * @return een datum waarvan de tijd gelijk is gezet aan 00:00:00
	 */
	public Date maakBeginVanDagVanDatum(Date datum)
	{
		if (datum == null)
			return null;
		calendar.clear();
		calendar.setTime(datum);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		return calendar.getTime();
	}

	/**
	 * Geeft een string terug met daarin de tijd-informatie
	 * 
	 * @param date
	 * @return Een string van de tijd
	 */
	public String getTimeString(Date date)
	{
		calendar.clear();
		calendar.setTime(date);
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		int minutes = calendar.get(Calendar.MINUTE);
		String min;
		if (minutes < 10)
		{
			min = "0" + String.valueOf(minutes);
		}
		else
		{
			min = String.valueOf(minutes);
		}
		return hours + ":" + min;
	}

	/**
	 * Geeft een string terug met daarin een omschrijving van de datum in de vorm:
	 * toekomstig, vandaag, gisteren, deze maand, dit schooljaar, schooljaar XXXX
	 * 
	 * @param date
	 * @return omschrijving van de datum
	 */
	public String getDateGroup(Date date)
	{
		// morgen of later
		calendar.setTime(currentDate());
		calendar.add(Calendar.DATE, 1);

		if (!calendar.getTime().after(date))
			return "Toekomstig";

		// vandaag
		calendar.add(Calendar.DATE, -1);
		if (!calendar.getTime().after(date))
			return "Vandaag";

		// gisteren
		calendar.add(Calendar.DATE, -1);
		if (!calendar.getTime().after(date))
			return "Gisteren";

		// deze maand
		calendar.add(Calendar.DATE, 1); // naar vandaag
		calendar.set(Calendar.DAY_OF_MONTH, 1); // naar begin van de maand
		if (!calendar.getTime().after(date))
			return "Deze maand";

		// dit schooljaar
		calendar.add(Calendar.MONTH, -Calendar.AUGUST); // naar kalenderjaar van begin
		// schooljaar
		calendar.set(Calendar.MONTH, Calendar.JULY); // naar 1 juli
		if (!calendar.getTime().after(date))
			return "Dit schooljaar";

		// bereken schooljaar van de datum
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -Calendar.AUGUST); // naar kalenderjaar van begin
		// schooljaar
		int kalenderjaar = calendar.get(Calendar.YEAR);

		return "Schooljaar " + Integer.toString(kalenderjaar) + " / "
			+ Integer.toString(kalenderjaar + 1);
	}

	public Date mergeDateAndTime(Date datum, Date time)
	{

		Calendar cal = new GregorianCalendar();
		cal.setTime(time);
		calendar.clear();
		calendar.setTime(datum);
		calendar.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
		return calendar.getTime();
	}

	public Time getDateAsTime(Date datum)
	{
		Calendar cal = new GregorianCalendar();
		cal.setTime(datum);
		return new Time(asTime(cal.getTime()).getTime());
	}

	/**
	 * @return yesterday
	 */
	public Date yesterday()
	{
		return addDays(currentDate(), -1);
	}

	/**
	 * @return De eerstvolgende datum na de gegeven datum die op de gegeven maand en dag
	 *         valt. Als date bijvoorbeeld 01-10-2009 is, en month=Februari en day=1,
	 *         geeft de methode 01-02-2010. De maand is 0-based, oftewel Jan=0, Feb=1,
	 *         ..., Dec=11
	 */
	public Date getNextOccurrenceOfDate(Date date, int month, int day)
	{
		Date res = asDate(getYear(date), month, day);
		if (!res.after(date))
		{
			res = addYears(res, 1);
		}
		return res;
	}

	public Date getTimeWithoutSeconds(Date time)
	{
		calendar.setTime(time);
		calendar.set(Calendar.SECOND, 0);
		return new Time(asTime(time.getTime()).getTime());
	}

	/**
	 * @param jaar
	 * @return Het aantal weken in het gegeven jaar volgens ISO 8601. De laatste week van
	 *         het jaar is de week die de datum 28 december bevat. Dit houdt in dat week 1
	 *         van het volgende jaar in potentie de datum 29 december van het jaar
	 *         daarvoor kan bevatten.
	 */
	public int getAantalWekenInJaar(int jaar)
	{
		Date datum = isoStringAsDate(jaar + "1228");
		return getWeekOfYear(datum);
	}

	public static XMLGregorianCalendar toXmlDate(Date date)
	{
		if (date == null)
			return null;
		XMLGregorianCalendar result = toXmlDateTime(date);
		result.setHour(DatatypeConstants.FIELD_UNDEFINED);
		result.setMinute(DatatypeConstants.FIELD_UNDEFINED);
		result.setSecond(DatatypeConstants.FIELD_UNDEFINED);
		result.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);
		result.toXMLFormat();
		return result;
	}

	public static XMLGregorianCalendar toXmlDateTime(Date date)
	{
		if (date == null)
			return null;
		GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
		cal.setTime(date);
		try
		{
			XMLGregorianCalendar result =
				DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
			return result;
		}
		catch (DatatypeConfigurationException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * Bepaalt of datum1 op of na datum2 ligt.
	 */
	public static boolean opOfNa(Date datum1, Date datum2)
	{
		if (datum1 != null && datum2 != null)
		{
			TimeUtil util = getInstance();
			datum1 = util.asDate(datum1);
			datum2 = util.asDate(datum2);

			return datum1.equals(datum2) || datum1.after(datum2);
		}
		return false;
	}

	/**
	 * Bepaalt of datum1 na datum2 ligt.
	 */
	public static boolean na(Date datum1, Date datum2)
	{
		if (datum1 != null && datum2 != null)
		{
			TimeUtil util = getInstance();
			datum1 = util.asDate(datum1);
			datum2 = util.asDate(datum2);

			return datum1.after(datum2);
		}
		return false;
	}

	/**
	 * Bepaalt of datum1 op of voor datum2 ligt.
	 */
	public static boolean opOfVoor(Date datum1, Date datum2)
	{
		if (datum1 != null && datum2 != null)
		{
			TimeUtil util = getInstance();
			datum1 = util.asDate(datum1);
			datum2 = util.asDate(datum2);

			return datum1.equals(datum2) || datum1.before(datum2);
		}
		return false;
	}

	public static boolean voor(Date datum1, Date datum2)
	{
		if (datum1 != null && datum2 != null)
		{
			TimeUtil util = getInstance();
			datum1 = util.asDate(datum1);
			datum2 = util.asDate(datum2);

			return datum1.before(datum2);
		}
		return false;
	}

	public static boolean allenNa(Date peildatum, Date... datums)
	{
		if (peildatum != null && datums != null && datums.length != 0)
		{
			for (Date datum : datums)
			{
				if (!na(datum, peildatum))
					return false;
			}
			return true;
		}
		return false;
	}

	public static boolean allenOpOfNa(Date peildatum, Date... datums)
	{
		if (peildatum != null && datums != null && datums.length != 0)
		{
			for (Date datum : datums)
			{
				if (!opOfNa(datum, peildatum))
					return false;
			}
			return true;
		}
		return false;
	}

	public static boolean allenVoor(Date peildatum, Date... datums)
	{
		if (peildatum != null && datums != null && datums.length != 0)
		{
			for (Date datum : datums)
			{
				if (!voor(datum, peildatum))
					return false;
			}
			return true;
		}
		return false;
	}

	public static boolean allenOpOfVoor(Date peildatum, Date... datums)
	{
		if (peildatum != null && datums != null && datums.length != 0)
		{
			for (Date datum : datums)
			{
				if (!opOfVoor(datum, peildatum))
					return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * Bepaalt of de peildatum tussen min en max ligt (inclusief). 1 oktober 2010 ligt dus
	 * tussen 1 oktober 2010 en 1 oktober 2010.
	 */
	public static boolean tussen(Date peildatum, Date min, Date max)
	{
		return opOfNa(peildatum, min) && opOfVoor(peildatum, max);
	}

	/**
	 * De datum van vandaag (zonder tijd).
	 */
	public static Date vandaag()
	{
		return TimeUtil.getInstance().currentDate();
	}

	/**
	 * De datum van gisteren (zonder tijd).
	 */
	public static Date gisteren()
	{
		return TimeUtil.getInstance().yesterday();
	}

	/**
	 * De datum van morgen (zonder tijd).
	 */
	public static Date morgen()
	{
		return TimeUtil.getInstance().addDays(vandaag(), 1);
	}

	/**
	 * De datum en tijd van <em>nu</em>.
	 */
	public static Date nu()
	{
		return new Date();
	}

	/**
	 * Bepaalt het maximum van de opgegeven datums.
	 */
	public static Date max(Date... dates)
	{
		if (dates == null || dates.length == 0)
			return null;

		Date max = dates[0];
		for (int i = 1; i < dates.length; i++)
		{
			Date current = dates[i];
			if (max == null)
				max = current;
			else if (current != null && current.after(max))
				max = current;
		}
		return max;
	}

	/**
	 * Bepaalt het minimum van de opgegeven datums.
	 */
	public static Date min(Date... dates)
	{
		if (dates == null || dates.length == 0)
			return null;

		Date max = dates[0];
		for (int i = 1; i < dates.length; i++)
		{
			Date current = dates[i];
			if (max == null)
				max = current;
			else if (current != null && current.before(max))
				max = current;
		}
		return max;
	}

	/**
	 * Formatteert de datum als een string volgens de standaard notatie.
	 */
	public static String asString(Date date)
	{
		return getInstance().formatDate(date);
	}

	/**
	 * Formatteert de datum als een string volgens het gegeven formaat.
	 */
	public static String asString(Date date, String format)
	{
		return getInstance().formatDate(date, format);
	}
}
