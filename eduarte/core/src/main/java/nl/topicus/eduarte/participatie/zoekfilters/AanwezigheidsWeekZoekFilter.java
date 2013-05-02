/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.participatie.zoekfilters;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.participatie.AbsentieMelding;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.web.components.panels.templates.RapportageConfiguratieFactory;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * Filter om aanwezigheid van deelnemers te tonen in bepaalde weken. De weken zijn
 * aaneensluiten maar mogen niet meer dan {@link #maxDiff} uit elkaar liggen. Weken zijn
 * altijd van het opgegeven jaar tenzei beginweek voor eindweek ligt dan is beginweek van
 * het daar opvolgende jaar. invoer wordt automatisch gecorrigeerd.
 * 
 * @author marrink
 */
public class AanwezigheidsWeekZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<AbsentieMelding> implements
		AanwezigheidsWeekFilter, RapportageConfiguratieFactory<Verbintenis>
{
	private static final long serialVersionUID = 1L;

	private IModel<Deelnemer> deelnemer;

	private int maxDiff = 10;

	private int jaar;

	private Integer beginWeek;

	private Integer eindWeek;

	public AanwezigheidsWeekZoekFilter()
	{
		Calendar cal = Calendar.getInstance();
		setJaar(cal.get(Calendar.YEAR));
		setBeginWeek(cal.get(Calendar.WEEK_OF_YEAR));
		setEindWeek(cal.get(Calendar.WEEK_OF_YEAR));
	}

	/**
	 * Het max aantal weken dat getoond mag worden. Standaard 10.
	 * 
	 * @return max aantal weken.
	 */
	public int getMaxAantalweken()
	{
		return maxDiff;
	}

	public final IModel<Deelnemer> getDeelnemerModel()
	{
		return deelnemer;
	}

	public Deelnemer getDeelnemer()
	{
		return getModelObject(deelnemer);
	}

	public final void setDeelnemerModel(IModel<Deelnemer> deelnemer)
	{
		this.deelnemer = deelnemer;
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = makeModelFor(deelnemer);
	}

	public Integer getBeginWeek()
	{
		return beginWeek;
	}

	public void setBeginWeek(Integer beginWeek)
	{
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, getJaar());
		cal.getTimeInMillis();
		this.beginWeek = checkCalendarRange(cal, beginWeek);
		// int diff = this.eindWeek - this.beginWeek;
		// if (diff > maxDiff)
		// this.beginWeek = this.eindWeek - maxDiff;
		// // eind voor begin maar in volgend jaar
		// else if (diff < -maxDiff)
		// this.beginWeek =
		// cal.getActualMaximum(Calendar.WEEK_OF_YEAR) + (this.eindWeek - maxDiff);
	}

	/**
	 * Als een weeknr buiten de range van weeknrs voor het gekozen jaar ligt wordt de
	 * huidige week gereturned (of de laatste week in alle jaren behalve het huidige
	 * jaar). anders het gekozen nr.
	 * 
	 * @param weekNr
	 * @return geldig week nr
	 */
	private int checkCalendarRange(Calendar cal, Integer weekNr)
	{
		int maxWeek = cal.getActualMaximum(Calendar.WEEK_OF_YEAR);
		if (weekNr == null || weekNr <= 0)
			return cal.get(Calendar.WEEK_OF_YEAR);
		if (weekNr > maxWeek)
		{
			if (Calendar.getInstance().get(Calendar.YEAR) == getJaar())
				return cal.get(Calendar.WEEK_OF_YEAR);
			return maxWeek;
		}
		return weekNr;
	}

	public Integer getEindWeek()
	{
		return eindWeek;
	}

	public void setEindWeek(Integer eindWeek)
	{
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, getJaar());
		cal.getTimeInMillis();
		// int maxWeek = cal.getActualMaximum(Calendar.WEEK_OF_YEAR);
		this.eindWeek = checkCalendarRange(cal, eindWeek);
		// int diff = this.eindWeek - this.beginWeek;
		// if (diff > maxDiff)
		// this.eindWeek = this.beginWeek + maxDiff;
		// // eind voor begin maar in volgend jaar
		// else if (diff < -maxDiff)
		// this.eindWeek = (this.beginWeek + maxDiff) % maxWeek;
	}

	public int getJaar()
	{
		return jaar;
	}

	public void setJaar(int jaar)
	{
		this.jaar = jaar;
	}

	/**
	 * De eerste maandag van de begin week.
	 * 
	 * @return datum
	 */
	public Date getBeginDatum()
	{
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.set(Calendar.YEAR, getJaar());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.WEEK_OF_YEAR, getBeginWeek());
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return cal.getTime();
	}

	/**
	 * De laatste zondag van de eind week.
	 * 
	 * @return datum
	 */
	public Date getEindDatum()
	{
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.set(Calendar.YEAR, getJaar());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.WEEK_OF_YEAR, getEindWeek());
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return cal.getTime();
	}

	/**
	 * De lijst met weken die we willen tonen.
	 * 
	 * @return weken
	 */
	public List<Week> getWeken()
	{
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, getJaar());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.getTimeInMillis();
		List<Week> temp = new ArrayList<Week>(maxDiff);
		int start = getBeginWeek();
		int end = getEindWeek();
		if (start <= end)
		{
			for (int i = start; i <= end; i++)
			{
				insertNextdate(cal, temp, i);
			}
		}
		else
		{
			int maxWeek = cal.getActualMaximum(Calendar.WEEK_OF_YEAR);
			for (int i = start; i <= maxWeek; i++)
			{
				insertNextdate(cal, temp, i);
			}
			cal.set(Calendar.YEAR, getJaar() + 1);
			cal.getTimeInMillis();
			for (int i = 1; i <= end; i++)
			{
				insertNextdate(cal, temp, i);
			}

		}
		return temp;
	}

	/**
	 * Interne methode voor getWeken. Voegt nieuwe week toe en zet de calendar op de
	 * eerste dag van de volgende week.
	 * 
	 * @param cal
	 * @param temp
	 * @param i
	 */
	private void insertNextdate(Calendar cal, List<Week> temp, int i)
	{
		cal.set(Calendar.WEEK_OF_YEAR, i);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		Date startDate = cal.getTime();
		cal.add(Calendar.DAY_OF_YEAR, 6);
		Date endDate = cal.getTime();
		temp.add(new Week(startDate, endDate));
		cal.add(Calendar.DAY_OF_YEAR, 1);
	}

	@Override
	public Object createConfiguratie(Verbintenis contextObject)
	{
		return this;
	}
}
