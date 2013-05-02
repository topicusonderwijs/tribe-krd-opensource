package nl.topicus.eduarte.krdparticipatie.web.components.panels.waarneming;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.Time;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.participatie.helpers.WaarnemingDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.LesuurIndeling;
import nl.topicus.eduarte.entities.participatie.Waarneming;
import nl.topicus.eduarte.entities.participatie.enums.AbsentiePresentieEnum;
import nl.topicus.eduarte.entities.participatie.enums.AfspraakTypeCategory;
import nl.topicus.eduarte.entities.participatie.enums.WaarnemingSoort;
import nl.topicus.eduarte.entities.participatie.enums.WaarnemingWeergaveEnum;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.participatie.zoekfilters.WaarnemingOverzichtZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.WaarnemingZoekFilter;

/**
 * @author vanderkamp
 */
public class WeekWaarnemingen implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Date datum;

	private Date startDatum;

	private Date eindDatum;

	private int week;

	private int jaar;

	private Deelnemer deelnemer;

	private List<Waarneming> waarnemingenList;

	private int totaalAanwezig = 0;

	private int totaalOngeoorloofdAbsent = 0;

	private int totaalGeoorloofdAbsent = 0;

	private String lastLabelColor = null;

	private Waarneming lastWaarneming = null;

	private WaarnemingOverzichtZoekFilter filter;

	public WeekWaarnemingen(Date datum, Deelnemer deelnemer, WaarnemingOverzichtZoekFilter filter)
	{
		this.datum = datum;
		this.deelnemer = deelnemer;
		this.filter = filter;
		setWeekData();
		laadWaarnemingen();
	}

	/**
	 * @return het totaal keer dat de leerling een aanwezig waarneming heeft in deze week
	 */
	public int getTotaalAanwezig()
	{
		return totaalAanwezig;
	}

	public String getLabel(int dagVanWeek, LesuurIndeling lestijd)
	{
		Date dag = TimeUtil.getInstance().getDate(jaar, week, dagVanWeek);
		Date beginDatumTijd =
			TimeUtil.getInstance().setTimeOnDate(dag, new Time(lestijd.getBeginTijd().getTime()));
		Date eindDatumTijd =
			TimeUtil.getInstance().setTimeOnDate(dag, new Time(lestijd.getEindTijd().getTime()));
		List<Waarneming> waarnemingVoorUuur = new ArrayList<Waarneming>();
		for (Waarneming waarneming : waarnemingenList)
		{
			if (waarneming.getBeginDatumTijd().before(eindDatumTijd)
				&& waarneming.getEindDatumTijd().after(beginDatumTijd))
			{
				waarnemingVoorUuur.add(waarneming);
			}
		}
		String label = " ";
		if (!waarnemingVoorUuur.isEmpty())
		{
			label = " ";
			Waarneming absentWaarneming = null;
			for (Waarneming waarneming : waarnemingVoorUuur)
			{
				if (waarneming.getWaarnemingSoort().equals(WaarnemingSoort.Afwezig))
				{
					absentWaarneming = waarneming;
					break;
				}
			}
			// Dit is een stukje om de totalen goed te zetten
			if (absentWaarneming != null)
			{
				if (absentWaarneming.getAbsentieMelding() != null
					&& absentWaarneming.getAbsentieMelding().getAbsentieReden().isGeoorloofd())
				{
					totaalGeoorloofdAbsent++;
					lastLabelColor = "Orange";
				}
				else
				{
					totaalOngeoorloofdAbsent++;
					lastLabelColor = "Red";
				}
				lastWaarneming = absentWaarneming;
			}
			else
			{

				totaalAanwezig++;
				lastLabelColor = "Green";
				lastWaarneming = waarnemingVoorUuur.get(0);
			}

			// Hier proberen we weer het juiste label terug te geven
			if (absentWaarneming != null)
			{
				label = " ";
				if (filter.getWaarnemingWeergave().equals(
					WaarnemingWeergaveEnum.AbsentieMeldingOfWaarneming))
				{
					if (absentWaarneming.getAbsentieMelding() != null)
						label =
							absentWaarneming.getAbsentieMelding().getAbsentieReden().getAfkorting();
				}
				else if (filter.getWaarnemingWeergave().equals(
					WaarnemingWeergaveEnum.Geoorloofd_Ongeoorloofd))
				{
					label = "O";
					if (absentWaarneming.getAbsentieMelding() != null
						&& absentWaarneming.getAbsentieMelding().getAbsentieReden().isGeoorloofd())
						label = "G";
				}
			}
		}
		else
		{
			lastLabelColor = null;
		}
		return label;
	}

	private void laadWaarnemingen()
	{
		WaarnemingZoekFilter zoekFilter = new WaarnemingZoekFilter();
		zoekFilter.setDeelnemer(deelnemer);
		zoekFilter.setBeginDatumTijd(startDatum);
		zoekFilter.setEindDatumTijd(eindDatum);
		zoekFilter.setZonderNvtWaarnemingen(Boolean.TRUE);
		if (filter.getAbsentieOfPresentie().equals(AbsentiePresentieEnum.Absentie))
			zoekFilter.setWaarnemingSoort(WaarnemingSoort.Afwezig);
		if (filter.getAbsentieOfPresentie().equals(AbsentiePresentieEnum.Presentie))
			zoekFilter.setWaarnemingSoort(WaarnemingSoort.Aanwezig);
		zoekFilter.setContract(filter.getContract());
		WaarnemingDataAccessHelper helper =
			DataAccessRegistry.getHelper(WaarnemingDataAccessHelper.class);
		waarnemingenList = helper.getOverlappendeWaarnemingen(zoekFilter);
	}

	private void setWeekData()
	{
		week = TimeUtil.getInstance().getWeekOfYear(datum);
		jaar = TimeUtil.getInstance().getYear(datum);
		Date[] beginEindWeek = TimeUtil.getInstance().getWeekBeginEnEindDatum(jaar, week);
		startDatum = beginEindWeek[0];
		eindDatum = beginEindWeek[1];
	}

	public int getTotaalOngeoorloofdAbsent()
	{
		return totaalOngeoorloofdAbsent;
	}

	public int getTotaalGeoorloofdAbsent()
	{
		return totaalGeoorloofdAbsent;
	}

	/**
	 * @return Returns the lastLabelColor.
	 */
	public String getLastLabelColor()
	{
		if (lastLabelColor != null)
		{
			String color = new String(lastLabelColor);
			lastLabelColor = null;
			return color;
		}
		return null;
	}

	/**
	 * @return de title van de laatste waarneming
	 */
	public String getLastWaarnemingTitle()
	{
		if (lastWaarneming != null)
		{
			String title = "";
			title += lastWaarneming.getBeginEnEindTijd();
			if (lastWaarneming.getLastModifiedBy() != null
				&& lastWaarneming.getLastModifiedBy().getEigenaar() != null)
				title += ", " + lastWaarneming.getLastModifiedBy().getEigenaar().getVolledigeNaam();
			EnumSet<AfspraakTypeCategory> zichtbareAfspraakTypes =
				EnumSet.of(AfspraakTypeCategory.INDIVIDUEEL, AfspraakTypeCategory.ROOSTER);
			if (lastWaarneming.getAfspraak() != null
				&& lastWaarneming.getAfspraak().getOnderwijsproduct() != null
				&& zichtbareAfspraakTypes.contains(lastWaarneming.getAfspraak().getAfspraakType()
					.getCategory()))
				title +=
					", " + lastWaarneming.getAfspraak().getOnderwijsproduct().getOmschrijving();
			if (lastWaarneming.getAbsentieMelding() != null)
				title +=
					", " + lastWaarneming.getAbsentieMelding().getAbsentieReden().getOmschrijving();
			lastWaarneming = null;
			return title;
		}
		return "";
	}

}
