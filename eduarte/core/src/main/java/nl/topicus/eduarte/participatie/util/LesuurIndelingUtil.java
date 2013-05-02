package nl.topicus.eduarte.participatie.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.Time;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.participatie.helpers.LesweekIndelingDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.AbsentieMelding;
import nl.topicus.eduarte.entities.participatie.LesdagIndeling;
import nl.topicus.eduarte.entities.participatie.LesuurIndeling;
import nl.topicus.eduarte.entities.participatie.LesweekIndeling;
import nl.topicus.eduarte.entities.participatie.Waarneming;
import nl.topicus.eduarte.participatie.zoekfilters.LesweekindelingZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.security.checks.AlwaysGrantedSecurityCheck;

/**
 * @author vanderkamp, henzen
 */
public final class LesuurIndelingUtil
{

	private static LesweekIndeling getGoedeLesweekIndeling(Locatie locatie,
			OrganisatieEenheid organisatieEenheid)
	{
		LesweekIndeling lesweekIndeling = null;

		// if (locatie != null && organisatieEenheid != null)
		// {
		LesweekindelingZoekFilter abstractFilter = new LesweekindelingZoekFilter();
		abstractFilter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
			new AlwaysGrantedSecurityCheck()));
		abstractFilter.setLocatie(locatie);
		abstractFilter.setOrganisatieEenheid(organisatieEenheid);
		lesweekIndeling =
			DataAccessRegistry.getHelper(LesweekIndelingDataAccessHelper.class).getlesweekIndeling(
				abstractFilter);
		// }
		// else if (organisatieEenheid != null)
		// organisatieEenheid.getLesweekindeling();
		// else
		// EduArteRequestCycle.get().getDefaultOrganisatieEenheid().getLesweekindeling();

		return lesweekIndeling;
	}

	private static LesuurIndeling getLestijdVanDatum(Locatie locatie,
			OrganisatieEenheid organisatieEenheid, Date datumTijd)
	{
		return getLesuurIndelingVanDatumTijd(datumTijd, locatie, organisatieEenheid);
	}

	private static LesuurIndeling getLestijdVanLesuur(int lesuur, Date datum,
			OrganisatieEenheid organisatieEenheid, Locatie locatie)
	{
		return getLesuurIndelingVanLesuurDatum(lesuur, datum, locatie, organisatieEenheid);
	}

	private static int getEindlesuur(Date eindDatumTijd, OrganisatieEenheid organisatieEenheid,
			Locatie locatie)
	{
		return getLesuurAlsExactBeginOfEind(eindDatumTijd, false, locatie, organisatieEenheid);
	}

	private static int getBeginlesuur(Date beginDatumTijd, OrganisatieEenheid organisatieEenheid,
			Locatie locatie)
	{
		return getLesuurAlsExactBeginOfEind(beginDatumTijd, true, locatie, organisatieEenheid);
	}

	/**
	 * Geeft de lestijd terug als de meegegeven tijd gelijk is of groter dan de begintijd,
	 * en kleiner dan de eindtijd
	 */

	private static LesuurIndeling getLesuurIndelingVanDatumTijd(Date datum, Locatie locatie,
			OrganisatieEenheid orgEenheid)
	{
		LesweekIndeling lesweekIndeling = getGoedeLesweekIndeling(locatie, orgEenheid);

		Date currentDate = TimeUtil.getInstance().asDate(datum);
		Date currentTime = TimeUtil.getInstance().asTime(datum);
		int currentDayOfWeek = TimeUtil.getInstance().getDayOfWeek(currentDate) - 1;
		if (currentDayOfWeek == 0)
			currentDayOfWeek = 7;
		String weekDagString = TimeUtil.getInstance().getWeekdagNaam(currentDayOfWeek);

		if (lesweekIndeling != null)
		{
			List<LesdagIndeling> lesdagen =
				new ArrayList<LesdagIndeling>(lesweekIndeling.getLesdagIndelingen());
			LesdagIndeling vandaag = null;
			for (LesdagIndeling lesdag : lesdagen)
			{
				if (lesdag.getDag().trim().equalsIgnoreCase(weekDagString.substring(0, 2)))
					vandaag = lesdag;
			}
			if (vandaag != null)
			{
				for (LesuurIndeling lestijd : vandaag.getLesuurIndeling())
				{
					Date lesBeginTijd = TimeUtil.getInstance().asTime(lestijd.getBeginTijd());
					Date lesEindTijd = TimeUtil.getInstance().asTime(lestijd.getEindTijd());
					if ((currentTime.after(lesBeginTijd) || currentTime.equals(lesBeginTijd))
						&& currentTime.before(lesEindTijd))
						return lestijd;
				}
			}

		}
		return null;
	}

	/**
	 * Als begin is true geeft het lesuur terug als de begintijd precies overeenkomt met
	 * de meegegeven datumTijd. Als begin is false geeft het lesuur terug als de eindtijd
	 * precies overeenkomt met de meegegeven datumTijd
	 */

	private static int getLesuurAlsExactBeginOfEind(Date datum, boolean begin, Locatie locatie,
			OrganisatieEenheid orgEenheid)
	{
		LesweekIndeling lesweekIndeling = getGoedeLesweekIndeling(locatie, orgEenheid);
		Date currentDate = TimeUtil.getInstance().asDate(datum);
		Date currentTime = TimeUtil.getInstance().asTime(datum);
		int currentDayOfWeek = TimeUtil.getInstance().getDayOfWeek(currentDate);
		String weekDagString = TimeUtil.getInstance().getWeekdagNaamMetZondagIs_1(currentDayOfWeek);

		if (lesweekIndeling != null)
		{
			List<LesdagIndeling> lesdagen =
				new ArrayList<LesdagIndeling>(lesweekIndeling.getLesdagIndelingen());
			LesdagIndeling vandaag = null;
			for (LesdagIndeling lesdag : lesdagen)
			{
				if (lesdag.getDag().trim().equalsIgnoreCase(weekDagString.substring(0, 2)))
					vandaag = lesdag;
			}
			if (vandaag != null)
			{
				for (LesuurIndeling lestijd : vandaag.getLesuurIndeling())
				{
					Date lesBeginTijd = TimeUtil.getInstance().asTime(lestijd.getBeginTijd());
					Date lesEindTijd = TimeUtil.getInstance().asTime(lestijd.getEindTijd());
					if (begin && currentTime.equals(lesBeginTijd))
						return lestijd.getLesuur();
					if (!begin && currentTime.equals(lesEindTijd))
						return lestijd.getLesuur();
				}
			}

		}
		return 0;
	}

	/**
	 * Geeft de lestijd terug van het het meegegeven lesuur op de meegegeven datum
	 */

	private static LesuurIndeling getLesuurIndelingVanLesuurDatum(int lesuur, Date datum,
			Locatie locatie, OrganisatieEenheid orgEenheid)
	{
		LesweekIndeling lesweekIndeling = getGoedeLesweekIndeling(locatie, orgEenheid);
		Date currentDate = TimeUtil.getInstance().asDate(datum);
		int currentDayOfWeek = TimeUtil.getInstance().getDayOfWeek(currentDate);
		String weekDagString = TimeUtil.getInstance().getWeekdagNaam(currentDayOfWeek);

		if (lesweekIndeling != null)
		{
			List<LesdagIndeling> lesdagen =
				new ArrayList<LesdagIndeling>(lesweekIndeling.getLesdagIndelingen());
			LesdagIndeling vandaag = null;
			for (LesdagIndeling lesdag : lesdagen)
			{
				if (lesdag.getDag().trim().equalsIgnoreCase(weekDagString.substring(0, 2)))
					vandaag = lesdag;
			}
			if (vandaag != null)
			{
				for (LesuurIndeling lestijd : vandaag.getLesuurIndeling())
				{
					if (lestijd.getLesuur() == lesuur)
						return lestijd;
				}
			}

		}
		return null;
	}

	public Time getHuidigeEindTijd(Locatie locatie, OrganisatieEenheid organisatieEenheid)
	{
		LesuurIndeling lestijd =
			getLesuurIndelingVanDatumTijd(TimeUtil.getInstance().currentDateTime(), locatie,
				organisatieEenheid);

		if (lestijd != null)
		{
			return new Time(lestijd.getEindTijd().getTime());
		}
		return null;
	}

	/**
	 * Geeft de begintijd van de lestijd op het huidige tijdstip
	 */

	public static Time getHuidigeBeginTijd(OrganisatieEenheid organisatieEenheid, Locatie locatie)
	{
		LesuurIndeling lestijd =
			getLestijdVanDatum(locatie, organisatieEenheid, TimeUtil.getInstance()
				.currentDateTime());

		if (lestijd != null)
		{
			return new Time(lestijd.getBeginTijd().getTime());
		}
		return null;
	}

	/**
	 * Geeft de begintijd van de lestijd van de meegegeven datum tijd.
	 */

	public static Time getBeginTijdVanDatumTijd(Date datum, OrganisatieEenheid organisatieEenheid,
			Locatie locatie)
	{
		LesuurIndeling lestijd = getLestijdVanDatum(locatie, organisatieEenheid, datum);
		if (lestijd != null)
		{
			return new Time(lestijd.getBeginTijd().getTime());
		}
		return null;
	}

	/**
	 * Geeft de begintijd van het meegegeven lesuur op de meegegeven datum
	 */

	public static Time getBeginTijdVanLesuurDatum(int lesuur, Date datum,
			OrganisatieEenheid organisatieEenheid, Locatie locatie)
	{
		LesuurIndeling lestijd = getLestijdVanLesuur(lesuur, datum, organisatieEenheid, locatie);

		if (lestijd != null)
		{
			return new Time(lestijd.getBeginTijd().getTime());
		}
		return null;
	}

	/**
	 * Geeft de eindtijd van het meegegeven lesuur op de meegegeven datum
	 */

	public static Time getEindTijdVanLesuurDatum(int lesuur, Date datum,
			OrganisatieEenheid organisatieEenheid, Locatie locatie)
	{
		LesuurIndeling lestijd = getLestijdVanLesuur(lesuur, datum, organisatieEenheid, locatie);

		if (lestijd != null)
		{
			return new Time(lestijd.getEindTijd().getTime());
		}
		return null;
	}

	/**
	 * Zet de lesuren als de begindatumtijd en einddatumtijd ingevuld zijn, en als die
	 * overeenkomen met een begintijd van een lesuur of een eindtijd van een lesuur,
	 * alleen als ze beide gezet kunnen worden ,anders beide op null
	 */

	public static AbsentieMelding setLesuren(AbsentieMelding melding,
			OrganisatieEenheid organisatieEenheid, Locatie locatie)
	{
		if (melding.getBeginDatumTijd() != null && melding.getEindDatumTijd() != null)
		{
			int beginLesuur =
				getBeginlesuur(melding.getBeginDatumTijd(), organisatieEenheid, locatie);
			int eindLesuur = getEindlesuur(melding.getEindDatumTijd(), organisatieEenheid, locatie);

			if (beginLesuur != 0 && eindLesuur != 0)
			{
				melding.setBeginLesuur(beginLesuur);
				melding.setEindLesuur(eindLesuur);
				return melding;
			}
		}
		melding.setBeginLesuur(null);
		melding.setEindLesuur(null);
		return melding;
	}

	/**
	 * Zet de lesuren als de begindatumtijd en einddatumtijd ingevuld zijn, en als die
	 * overeenkomen met een begintijd van een lesuur of een eindtijd van een lesuur,
	 * alleen als ze beide gezet kunnen worden ,anders beide op null
	 */

	public static Waarneming setLesuren(Waarneming waarneming,
			OrganisatieEenheid organisatieEenheid, Locatie locatie)
	{
		if (waarneming.getBeginDatumTijd() != null && waarneming.getEindDatumTijd() != null)
		{

			int beginLesuur =
				getBeginlesuur(waarneming.getBeginDatumTijd(), organisatieEenheid, locatie);
			int eindLesuur =
				getEindlesuur(waarneming.getEindDatumTijd(), organisatieEenheid, locatie);
			if (beginLesuur != 0 && eindLesuur != 0)
			{
				waarneming.setBeginLesuur(beginLesuur);
				waarneming.setEindLesuur(eindLesuur);
				return waarneming;
			}
		}
		waarneming.setBeginLesuur(null);
		waarneming.setEindLesuur(null);
		return waarneming;
	}

	/**
	 * Geeft de lestijde die bij deze Locatie of organisatieeenheid horen
	 */
	public static List<LesuurIndeling> getLesuurIndelingen(Locatie locatie,
			OrganisatieEenheid orgEenheid)
	{
		LesweekIndeling lesweekIndeling = getGoedeLesweekIndeling(locatie, orgEenheid);
		if (lesweekIndeling != null)
		{
			return lesweekIndeling.getIndelingMetMeesteLestijden().getLesuurIndeling();

		}
		return new ArrayList<LesuurIndeling>();

	}

}
