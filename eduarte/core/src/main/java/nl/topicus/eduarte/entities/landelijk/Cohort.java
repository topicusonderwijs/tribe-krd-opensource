package nl.topicus.eduarte.entities.landelijk;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.CohortDataAccessHelper;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumLandelijkEntiteit;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Cohort wordt gebruikt voor versionering van elementen zoals resultaatstructuren,
 * productregels en formules. Een verbintenis is altijd gekoppeld aan een cohort zodat
 * bepaald kan worden welke regels voor de deelnemer gelden. De begin- en einddatum van
 * een cohort komen overeen met de begin- en einddatum van het (begin)schooljaar van het
 * cohort, dus bijvoorbeeld 01-08-2008 t/m 31-07-2009.
 * 
 * @author loite
 */
@Exportable
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
@BatchSize(size = 100)
public class Cohort extends BeginEinddatumLandelijkEntiteit implements Comparable<Cohort>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Begin maand van het cohort.
	 */
	public static final int BEGIN_MONTH = Calendar.AUGUST;

	/**
	 * dag van de maand waarop het cohort begint
	 */
	public static final int BEGIN_DAY = 1;

	/**
	 * Eind maand van het cohort.
	 */
	public static final int END_MONTH = Calendar.JULY;

	/**
	 * Dag van de maand waarop het cohort afloopt.
	 */
	public static final int END_DAY = 31;

	/**
	 * De naam van het cohort in het formaat '2008/2009'.
	 */
	@Column(nullable = false, length = 20)
	private String naam;

	/**
	 * Creeert een cohort op basis van de cohort string. Verwacht wordt 2008-2009 voor de
	 * cohort waarde.
	 * 
	 * @param cohort
	 *            de jaartallen waarvoor een cohort gemaakt moet worden (bijv. 2008-2009).
	 * @return het Cohort
	 */
	public static Cohort asCohort(String cohort)
	{
		cohort = cohort.replace("Cohort", "");
		String[] jaren = cohort.split("-");
		jaren[0] = jaren[0].trim();
		jaren[1] = jaren[1].trim();
		if (!StringUtil.isNumeric(jaren[0]))
			return null;
		int jaar = Integer.valueOf(jaren[0]);
		Date datum = TimeUtil.getInstance().asDate(jaar, 7, 1);
		return getHelper().getCohortOpDatum(datum);
	}

	public Cohort()
	{
	}

	@Exportable
	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	/**
	 * @param beginjaar
	 *            bijv 2005 wanneer 2005/2006 aangemaakt moet worden
	 * @return schooljaar
	 */
	public static Cohort createCohort(int beginjaar)
	{
		Cohort cohort = new Cohort();
		Calendar temp = Calendar.getInstance();
		temp.set(Calendar.MILLISECOND, 0);
		temp.set(Calendar.SECOND, 0);
		temp.set(Calendar.MINUTE, 0);
		temp.set(Calendar.HOUR_OF_DAY, 0);
		temp.set(Calendar.DAY_OF_MONTH, BEGIN_DAY);
		temp.set(Calendar.MONTH, BEGIN_MONTH);
		temp.set(Calendar.YEAR, beginjaar);
		cohort.setBegindatum(temp.getTime());
		temp.add(Calendar.YEAR, 1);
		temp.set(Calendar.MONTH, END_MONTH);
		temp.set(Calendar.DAY_OF_MONTH, END_DAY);
		cohort.setEinddatum(temp.getTime());
		cohort.setNaam(beginjaar + "/" + temp.get(Calendar.YEAR));

		return cohort;
	}

	public static Cohort getCohort(int beginjaar)
	{
		Calendar temp = Calendar.getInstance();
		temp.set(Calendar.MILLISECOND, 0);
		temp.set(Calendar.SECOND, 0);
		temp.set(Calendar.MINUTE, 0);
		temp.set(Calendar.HOUR_OF_DAY, 0);
		temp.set(Calendar.DAY_OF_MONTH, BEGIN_DAY);
		temp.set(Calendar.MONTH, BEGIN_MONTH);
		temp.set(Calendar.YEAR, beginjaar);
		return getCohort(temp.getTime());
	}

	public static Cohort getCohort(Date datum)
	{
		return getHelper().getCohortOpDatum(datum);
	}

	public static List<Cohort> getCohorten(int beginjaar, int eindjaar)
	{
		List<Cohort> ret = new ArrayList<Cohort>();
		for (int curJaar = beginjaar; curJaar <= eindjaar; curJaar++)
			ret.add(getCohort(curJaar));
		return ret;
	}

	private static final CohortDataAccessHelper getHelper()
	{
		return DataAccessRegistry.getHelper(CohortDataAccessHelper.class);
	}

	/**
	 * @return Het cohort dat actief is op de huidige systeemdatum.
	 */
	public static final Cohort getHuidigCohort()
	{
		return getCohort(TimeUtil.getInstance().currentDate());
	}

	public Cohort getVolgende()
	{
		return getHelper().getVolgendeCohort(this);
	}

	@Override
	public String toString()
	{
		return getNaam();
	}

	@Exportable
	public String getBeginjaar()
	{
		return String.valueOf(TimeUtil.getInstance().getYear(getBegindatum()));
	}

	@Override
	public int compareTo(Cohort o)
	{
		return getBegindatum().compareTo(o.getBegindatum());
	}
}
