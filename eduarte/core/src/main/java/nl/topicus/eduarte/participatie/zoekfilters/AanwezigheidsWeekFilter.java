/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.participatie.zoekfilters;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.zoekfilters.DetachableZoekFilter;
import nl.topicus.eduarte.entities.participatie.AbsentieMelding;
import nl.topicus.eduarte.entities.personen.Deelnemer;

/**
 * Filter om aanwezigheid van deelnemers te tonen in bepaalde weken. De weken zijn
 * aaneensluiten maar mogen niet meer dan ?? uit elkaar liggen. Weken zijn altijd van het
 * opgegeven jaar tenzei beginweek voor eindweek ligt dan is beginweek van het daar
 * opvolgende jaar. invoer wordt automatisch gecorrigeerd.
 * 
 * @author marrink
 */
public interface AanwezigheidsWeekFilter extends DetachableZoekFilter<AbsentieMelding>
{

	/**
	 * Het max aantal weken dat getoond mag worden. Standaard 10.
	 * 
	 * @return max aantal weken.
	 */
	public int getMaxAantalweken();

	public Deelnemer getDeelnemer();

	public void setDeelnemer(Deelnemer deelnemer);

	public Integer getBeginWeek();

	public void setBeginWeek(Integer beginWeek);

	public Integer getEindWeek();

	public void setEindWeek(Integer eindWeek);

	public int getJaar();

	public void setJaar(int jaar);

	/**
	 * De eerste maandag van de begin week.
	 * 
	 * @return datum
	 */
	public Date getBeginDatum();

	/**
	 * De laatste zondag van de eind week.
	 * 
	 * @return datum
	 */
	public Date getEindDatum();

	/**
	 * De lijst met weken die we willen tonen.
	 * 
	 * @return weken
	 */
	public List<Week> getWeken();

	/**
	 * Abstracte notie van een week met een week nr, begin en eind datum
	 * 
	 * @author marrink
	 */
	public static final class Week implements Serializable
	{
		private static final long serialVersionUID = 1L;

		private final int weekNr;

		private final Date start;

		private final Date end;

		protected Week(Date start, Date end)
		{
			this.start = start;
			this.end = end;
			Calendar cal = Calendar.getInstance();
			cal.setTime(start);
			weekNr = cal.get(Calendar.WEEK_OF_YEAR);

		}

		public int getWeekNr()
		{
			return weekNr;
		}

		public Date getStart()
		{
			return start;
		}

		public Date getEnd()
		{
			return end;
		}

		@Override
		public String toString()
		{
			return "Week " + getWeekNr();
		}

		/**
		 * Lijst met alle dagen uit deze week.
		 * 
		 * @return dagen
		 */
		public List<Dag> getDagen()
		{
			return getDagen(7);
		}

		/**
		 * Lijst met dagen maandag tm zaterdag.
		 * 
		 * @return dagen
		 */
		public List<Dag> getWinkelDagen()
		{
			return getDagen(6);
		}

		/**
		 * interne methode om de dagen op te hoesten. Gebruik 7 voor alle dagen, 6 voor
		 * alle dagen behalve zondag, 5 voor alle werkdagen, etc.
		 * 
		 * @param aantal
		 *            aantal dagen
		 * @return dagen
		 */
		private List<Dag> getDagen(int aantal)
		{
			Calendar cal = Calendar.getInstance();
			cal.setTime(getStart());
			List<Dag> temp = new ArrayList<Dag>(aantal);
			for (int i = 0; i < aantal; i++)
			{
				temp.add(new Dag(cal.getTime()));
				cal.add(Calendar.DATE, 1);
			}
			return temp;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((end == null) ? 0 : end.hashCode());
			result = prime * result + ((start == null) ? 0 : start.hashCode());
			result = prime * result + weekNr;
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final Week other = (Week) obj;
			if (weekNr != other.weekNr)
				return false;
			if (end == null)
			{
				if (other.end != null)
					return false;
			}
			else if (!end.equals(other.end))
				return false;
			if (start == null)
			{
				if (other.start != null)
					return false;
			}
			else if (!start.equals(other.start))
				return false;
			return true;
		}

	}

	/**
	 * Notie van een dag.
	 * 
	 * @author marrink
	 */
	public static final class Dag implements Serializable
	{
		private static final long serialVersionUID = 1L;

		private final Date datum;

		public Dag(Date datum)
		{
			super();
			this.datum = datum;
		}

		@Override
		public String toString()
		{
			return new SimpleDateFormat("EEEE dd MMMM").format(datum);
		}

		public Date getDatum()
		{
			return datum;
		}

		/**
		 * @return De weekdag van deze datum, 1=Ma, 2=Wo etc.
		 */
		public int getWeekdag()
		{
			int weekdag = TimeUtil.getInstance().getDayOfWeek(datum) - 1;
			if (weekdag == 0)
				weekdag = 7;
			return weekdag;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((datum == null) ? 0 : datum.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final Dag other = (Dag) obj;
			if (datum == null)
			{
				if (other.datum != null)
					return false;
			}
			else if (!datum.equals(other.datum))
				return false;
			return true;
		}
	}
}
