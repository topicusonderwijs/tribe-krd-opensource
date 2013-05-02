package nl.topicus.eduarte.entities.participatie.enums;

import java.util.Calendar;

public enum AfspraakHerhalingDag
{
	MAANDAG("maandag", "ma.", Calendar.MONDAY),
	DINSDAG("dinsdag", "di.", Calendar.TUESDAY),
	WOENSDAG("woensdag", "wo.", Calendar.WEDNESDAY),
	DONDERDAG("donderdag", "do.", Calendar.THURSDAY),
	VRIJDAG("vrijdag", "vr.", Calendar.FRIDAY),
	ZATERDAG("zaterdag", "za.", Calendar.SATURDAY),
	ZONDAG("zondag", "zo.", Calendar.SUNDAY),
	DAG("dag", "dag", 0),
	WERKDAG("werkdag", "werkdag", 0);

	private String naam;

	private String afkorting;

	private int calendarDay;

	public static AfspraakHerhalingDag getHerhalingDag(int calendarDag)
	{
		switch (calendarDag)
		{
			case Calendar.MONDAY:
				return MAANDAG;
			case Calendar.TUESDAY:
				return DINSDAG;
			case Calendar.WEDNESDAY:
				return WOENSDAG;
			case Calendar.THURSDAY:
				return DONDERDAG;
			case Calendar.FRIDAY:
				return VRIJDAG;
			case Calendar.SATURDAY:
				return ZATERDAG;
			case Calendar.SUNDAY:
				return ZONDAG;
		}
		throw new IllegalArgumentException("calendarDag is geen geldige dag: " + calendarDag);
	}

	AfspraakHerhalingDag(String naam, String afkorting, int calendarDay)
	{
		this.naam = naam;
		this.afkorting = afkorting;
		this.calendarDay = calendarDay;
	}

	public int getCalendarDay()
	{
		return calendarDay;
	}

	public String getAfkorting()
	{
		return afkorting;
	}

	@Override
	public String toString()
	{
		return naam;
	}
}
