/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.participatie.enums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.TimeUtil;

/**
 * Geen echte entiteit, maar een handig object om standaard data mee op te halen.
 * 
 * @author loite
 */
public class Schooljaar implements Serializable
{
	private static final long serialVersionUID = 1L;

	private static final List<Schooljaar> INIT_SCHOOLJAREN = new ArrayList<Schooljaar>(30);
	static
	{
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(1980));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(1981));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(1982));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(1983));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(1984));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(1985));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(1986));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(1987));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(1988));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(1989));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(1990));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(1991));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(1992));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(1993));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(1994));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(1995));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(1996));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(1997));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(1998));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(1999));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(2000));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(2001));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(2002));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(2003));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(2004));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(2005));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(2006));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(2007));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(2008));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(2009));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(2010));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(2011));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(2012));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(2013));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(2014));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(2015));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(2016));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(2017));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(2018));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(2019));
		INIT_SCHOOLJAREN.add(Schooljaar.createSchooljaar(2020));
	}

	private static final List<Schooljaar> SCHOOLJAREN =
		Collections.unmodifiableList(INIT_SCHOOLJAREN);

	private Date vanafDatum;

	private Date totDatum;

	private String naam;

	// note these two will allways differ exactly 1 year minus 1 day.

	/**
	 * Begin maand van het schooljaar.
	 */
	public static final int BEGIN_MONTH = Calendar.AUGUST;

	/**
	 * dag van de maand waarop het schooljaar begint
	 */
	public static final int BEGIN_DAY = 1;

	/**
	 * Eind maand van het schholjaar.
	 */
	public static final int END_MONTH = Calendar.JULY;

	/**
	 * Dag van de maand waarop het schooljaar afloopt.
	 */
	public static final int END_DAY = 31;

	// 01-08-xxxx, 00:00:00:000 of this day is the first day of the new schooljaar,
	// 23:59:59:999 of the previous day is the last milisecond of the old schooljaar

	/**
	 * Constructor voor serialisatie.
	 */
	public Schooljaar()
	{
	}

	/**
	 * Maakt een schooljaar dat loopt van tot. Let op datums worden niet gecontroleerd.
	 * Gebruik liever de static methodes.
	 * 
	 * @param vanafDatum
	 * @param totDatum
	 * @param naam
	 */
	public Schooljaar(Date vanafDatum, Date totDatum, String naam)
	{
		setVanafDatum(vanafDatum);
		setTotDatum(totDatum);
		setNaam(naam);
	}

	/**
	 * Geeft true als de gegeven datum binnen het gegeven schooljaar valt. Een eventuele
	 * tijdgedeelte van de meegegeven datum wordt weggehaald. Als de datum op de begin- of
	 * einddatum van het schooljaar ligt, wordt dit als binnen het schooljaar beschouwd.
	 * 
	 * @param datum
	 *            De datum die getest moet worden.
	 * @return True als de datum binnen het schooljaar valt, anders false.
	 */
	public boolean bevatDatum(Date datum)
	{
		Date date = TimeUtil.getInstance().asDate(datum);
		if (date.before(getVanafDatum()))
			return false;
		if (date.after(getTotDatum()))
			return false;
		return true;
	}

	/**
	 * @return Returns the totDatum.
	 */
	public Date getTotDatum()
	{
		return totDatum;
	}

	/**
	 * @param totDatum
	 *            The totDatum to set.
	 */
	public void setTotDatum(Date totDatum)
	{
		this.totDatum = TimeUtil.getInstance().asDate(totDatum);
	}

	/**
	 * @return Returns the vanafDatum.
	 */
	public Date getVanafDatum()
	{
		return vanafDatum;
	}

	/**
	 * @param vanafDatum
	 *            The vanafDatum to set.
	 */
	public void setVanafDatum(Date vanafDatum)
	{
		this.vanafDatum = TimeUtil.getInstance().asDate(vanafDatum);
	}

	/**
	 * Kijkt of gegeven een bepaalde datum, deze datum binnen het huidige schooljaar valt.
	 * 
	 * @param referentieDatum
	 * @return true als de datum het huidige schooljaar is, anders false
	 */
	public boolean isHuidig(Date referentieDatum)
	{
		long vanaf = vanafDatum.getTime();
		long tot = totDatum.getTime();
		long referentie = TimeUtil.getInstance().asDate(referentieDatum).getTime();
		return (vanaf <= referentie && tot >= referentie);
	}

	/**
	 * @return true als dit het huidige schooljaar is volgende de systeemtijd.
	 */
	public boolean isHuidig()
	{
		return this.bevatDatum(TimeUtil.getInstance().currentDate());
	}

	/**
	 * @return true als dit een toekomstig schooljaar is volgens de systeemtijd.
	 */
	public boolean isToekomstig()
	{
		Date current = TimeUtil.getInstance().currentDate();
		return current.before(getVanafDatum());
	}

	/**
	 * @return true als dit een afgelopen schooljaar is volgens de systeemtijd.
	 */
	public boolean isAfgelopen()
	{
		Date current = TimeUtil.getInstance().currentDate();
		return current.after(getTotDatum());
	}

	/**
	 * Geeft het schooljaar direct opvolgend op dit schooljaar terug.
	 * 
	 * @return Het schooljaar direct opvolgend op dit.
	 */
	public Schooljaar getVolgendSchooljaar()
	{
		Calendar c = Calendar.getInstance();
		c.setTime(getVanafDatum());
		c.add(Calendar.YEAR, 1);
		return Schooljaar.getInstance(c.get(Calendar.YEAR));
	}

	/**
	 * Geeft het schooljaar direct voorafgaand van dit schooljaar terug.
	 * 
	 * @return Het schooljaar direct voorafgaand van dit.
	 */
	public Schooljaar getVorigSchooljaar()
	{
		Calendar c = Calendar.getInstance();
		c.setTime(getVanafDatum());
		c.add(Calendar.YEAR, -1);
		return Schooljaar.getInstance(c.get(Calendar.YEAR));
	}

	/**
	 * Geeft de bron peildatum van dit schooljaar. Op dit moment is dit altijd 1 oktober
	 * van het schooljaar.
	 * 
	 * @return bron peil datum
	 */
	public Date getBronPeildatum()
	{
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, Calendar.OCTOBER);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.YEAR, getStartJaar());

		return TimeUtil.getInstance().asDate(c.getTime());
	}

	/**
	 * @return unmodifiable list of available schooljaren
	 */
	public static List<Schooljaar> list()
	{
		return SCHOOLJAREN;
	}

	/**
	 * @param naam
	 * @return het schooljaar dat matched met de opgegeven naam
	 */
	public static Schooljaar getSchooljaarByName(String naam)
	{
		for (Schooljaar schooljaar : SCHOOLJAREN)
		{
			if (schooljaar.getNaam().equals(naam))
			{
				return schooljaar;
			}
		}
		return null;
	}

	/**
	 * Geeft het schooljaar dat dit jaar begint.
	 * 
	 * @return Het schooljaar dat dit jaar begint.
	 */
	public static Schooljaar getSchooljaarThisYear()
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(TimeUtil.getInstance().currentDate());
		return getInstance(cal.get(Calendar.YEAR));
	}

	/**
	 * @return het schooljaar dat dit kalenderjaar eindigt.
	 */
	public static Schooljaar getSchooljaarEindigendDitJaar()
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(TimeUtil.getInstance().currentDate());
		return getInstance(cal.get(Calendar.YEAR) - 1);
	}

	/**
	 * Returns a new Schooljaar. This object is not backed by a database entry and
	 * therefore should only be used as a temporary object, untill it is manually stored
	 * in a database. The id field of the object is not set, therefor this object can not
	 * be used for a comparison by the equals method.
	 * 
	 * @param beginJaar
	 *            e.g. 2005 when 2005/2006 is requested
	 * @return schooljaar
	 */
	private static Schooljaar createSchooljaar(int beginJaar)
	{
		Schooljaar jaar = new Schooljaar();
		Calendar temp = Calendar.getInstance();
		temp.set(Calendar.MILLISECOND, 0);
		temp.set(Calendar.SECOND, 0);
		temp.set(Calendar.MINUTE, 0);
		temp.set(Calendar.HOUR_OF_DAY, 0);
		temp.set(Calendar.DAY_OF_MONTH, BEGIN_DAY);
		temp.set(Calendar.MONTH, BEGIN_MONTH);
		temp.set(Calendar.YEAR, beginJaar);
		jaar.setVanafDatum(temp.getTime());
		temp.add(Calendar.YEAR, 1);
		temp.set(Calendar.MONTH, END_MONTH);
		temp.set(Calendar.DAY_OF_MONTH, END_DAY);
		jaar.setTotDatum(temp.getTime());
		jaar.setNaam(beginJaar + "/" + temp.get(Calendar.YEAR));

		// log.debug("start schooljaar = "
		// + jaar.getVanafDatum().toString() + " end schooljaar = "
		// + jaar.getTotDatum().toString());
		return jaar;
	}

	/**
	 * Geeft 1 oktober van het huidige, actieve schooljaar terug.
	 * 
	 * @return 1 oktober van het huidige actieve schooljaar.
	 */
	public static Date getPeilDatum()
	{
		TimeUtil util = TimeUtil.getInstance();
		Schooljaar schooljaar = Schooljaar.getInstance(util.currentDate());
		Date startVanSchoolJaar = schooljaar.getVanafDatum();
		Calendar cal = Calendar.getInstance();
		cal.setTime(startVanSchoolJaar);
		cal.set(Calendar.MONTH, Calendar.OCTOBER);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	/**
	 * Returns a new Schooljaar for the given Date. This object is not backed by a
	 * database entry and therefore should only be used as a temporary object, untill it
	 * is manually stored in a database. The id field of the object is not set, therefor
	 * this object can not be used for a comparison by the equals method.
	 * 
	 * @param datum
	 *            any Date inside the schooljaar
	 * @return schooljaar
	 */
	@SuppressWarnings("unused")
	private static Schooljaar createSchooljaar(Date datum)
	{
		Asserts.assertNotNull("datum", datum);
		Calendar myCalendar = Calendar.getInstance();
		myCalendar.setTime(datum);
		myCalendar.add(Calendar.MONTH, -BEGIN_MONTH);
		myCalendar.add(Calendar.DAY_OF_MONTH, -(BEGIN_DAY - 1));
		final int year = myCalendar.get(Calendar.YEAR);
		return createSchooljaar(year);
	}

	/**
	 * @param datum
	 *            any Date inside the Schooljaar
	 * @return the requested Schooljaar or null if it does not exist
	 */
	public static Schooljaar getInstance(Date datum)
	{
		Calendar myCalendar = Calendar.getInstance();
		myCalendar.setTime(datum);
		myCalendar.add(Calendar.MONTH, -BEGIN_MONTH);
		myCalendar.add(Calendar.DAY_OF_MONTH, -(BEGIN_DAY - 1));
		return getInstance(myCalendar.get(Calendar.YEAR));
	}

	/**
	 * @param startYear
	 *            the starting year for the Schooljaar e.g. 2005 when 2005/2006 is
	 *            requested
	 * @return the requested Schooljaar or null if it does not exist
	 */
	public static Schooljaar getInstance(int startYear)
	{
		for (Schooljaar schooljaar : SCHOOLJAREN)
		{
			if (schooljaar.getStartJaar() == startYear)
				return schooljaar;
		}
		return null;
	}

	/**
	 * @return het schooljaar dat op dit moment actief is.
	 */
	public static Schooljaar getHuidigSchooljaar()
	{
		Date peildatum = TimeUtil.getInstance().currentDate();
		return getInstance(peildatum);
	}

	/**
	 * @return Het jaartal van het begin van het schooljaar. Dus bij schooljaar 2004/2005
	 *         is dat 2004.
	 */
	public int getStartJaar()
	{
		Calendar myCalendar = Calendar.getInstance();
		myCalendar.setTime(getVanafDatum());
		return myCalendar.get(Calendar.YEAR);
	}

	/**
	 * @return Het jaartal van het eind van het schooljaar. Dus bij schooljaar 2005/2006
	 *         is dat 2006.
	 */
	public int getEindJaar()
	{
		Calendar myCalendar = Calendar.getInstance();
		myCalendar.setTime(getTotDatum());
		return myCalendar.get(Calendar.YEAR);
	}

	/**
	 * @return Returns the naam.
	 */
	public String getNaam()
	{
		return naam;
	}

	/**
	 * @param naam
	 *            The naam to set.
	 */
	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	@Override
	public String toString()
	{
		return naam;
	}

}
