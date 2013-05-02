package nl.topicus.eduarte.entities.jobs;

public enum ScheduleType
{
	/**
	 * Geeft aan dat de job dagelijks getriggerd wordt. Op welk uur, en welke minuut van
	 * het uur moet bekend zijn.
	 */
	Dagelijks,
	/**
	 * Geeft aan dat de job om de x aantal minuten getriggerd wordt. De interval van het
	 * aantal minuten moet bekend zijn
	 */
	Interval;
}
