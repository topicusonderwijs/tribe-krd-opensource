/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.participatie.zoekfilters;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import nl.topicus.cobra.util.DecimalUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.zoekfilters.DetachableZoekFilter;
import nl.topicus.eduarte.entities.participatie.Waarneming;
import nl.topicus.eduarte.entities.personen.Deelnemer;

/**
 * @author loite
 */
public interface AanwezigheidMaandFilter extends DetachableZoekFilter<Waarneming>
{
	/**
	 * Class voor het bijhouden van een maandoverzicht van aanwezigheidsgegevens.
	 * 
	 * @author loite
	 */
	public static final class AanwezigheidMaandOverzicht implements Serializable
	{
		private static final long serialVersionUID = 1L;

		private static final NumberFormat NUMBER_FORMAT =
			NumberFormat.getInstance(new Locale("nl", "NL"));

		private Maand maand;

		/**
		 * Het aantal lessen uit het absolute rooster.
		 */
		private int aantalLessen;

		/**
		 * Het aantal presentiewaarnemingen gekoppeld aan een lesuur.
		 */
		private int aantalPresentieWaarnemingen;

		/**
		 * Het aantal absentiewaarnemingen.
		 */
		private int aantalAbsentieWaarnemingen;

		/**
		 * Het aantal vrije waarnemingen buiten lesuren om.
		 */
		private int aantalVrijeWaarnemingen;

		/**
		 * Het aantal uren les volgens het absolute rooster.
		 */
		private BigDecimal urenLes;

		/**
		 * Het aantal uren presentiewaarnemingen gekoppeld aan lesuur.
		 */
		private BigDecimal urenPresentieWaarnemingen;

		/**
		 * Het aantal uren absentiewaarnemingen.
		 */
		private BigDecimal urenAbsentieWaarnemingen;

		/**
		 * Het aantal uren present volgens vrije waarnemingen.
		 */
		private BigDecimal urenVrijeWaarnemingen;

		/**
		 * Het aantal absentiemeldingen in de maand.
		 */
		private int aantalAbsentiemeldingen;

		/**
		 * Het aantal hele dagen dat de deelnemer absent is gemeld.
		 */
		private int dagenAbsentiemeldingen;

		/**
		 * Het aantal lesuren dat de deelnemer absent is gemeld.
		 */
		private int lesurenAbsentiemeldingen;

		/**
		 * Constructor
		 */
		public AanwezigheidMaandOverzicht()
		{
		}

		/**
		 * @return Returns the aantalAbsentiemeldingen.
		 */
		public int getAantalAbsentiemeldingen()
		{
			return aantalAbsentiemeldingen;
		}

		/**
		 * @param aantalAbsentiemeldingen
		 *            The aantalAbsentiemeldingen to set.
		 */
		public void setAantalAbsentiemeldingen(int aantalAbsentiemeldingen)
		{
			this.aantalAbsentiemeldingen = aantalAbsentiemeldingen;
		}

		/**
		 * @return Returns the dagenAbsentiemeldingen.
		 */
		public int getDagenAbsentiemeldingen()
		{
			return dagenAbsentiemeldingen;
		}

		/**
		 * @param dagenAbsentiemeldingen
		 *            The dagenAbsentiemeldingen to set.
		 */
		public void setDagenAbsentiemeldingen(int dagenAbsentiemeldingen)
		{
			this.dagenAbsentiemeldingen = dagenAbsentiemeldingen;
		}

		/**
		 * @return Returns the lesurenAbsentiemeldingen.
		 */
		public int getLesurenAbsentiemeldingen()
		{
			return lesurenAbsentiemeldingen;
		}

		/**
		 * @param lesurenAbsentiemeldingen
		 *            The lesurenAbsentiemeldingen to set.
		 */
		public void setLesurenAbsentiemeldingen(int lesurenAbsentiemeldingen)
		{
			this.lesurenAbsentiemeldingen = lesurenAbsentiemeldingen;
		}

		/**
		 * @return Returns the maand.
		 */
		public Maand getMaand()
		{
			return maand;
		}

		/**
		 * @param maand
		 *            The maand to set.
		 */
		public void setMaand(Maand maand)
		{
			this.maand = maand;
		}

		/**
		 * @return Returns the aantalLessen.
		 */
		public int getAantalLessen()
		{
			return aantalLessen;
		}

		/**
		 * @param aantalLessen
		 *            The aantalLessen to set.
		 */
		public void setAantalLessen(int aantalLessen)
		{
			this.aantalLessen = aantalLessen;
		}

		/**
		 * @return Returns the aantalPresentieWaarnemingen.
		 */
		public int getAantalPresentieWaarnemingen()
		{
			return aantalPresentieWaarnemingen;
		}

		/**
		 * @param aantalPresentieWaarnemingen
		 *            The aantalPresentieWaarnemingen to set.
		 */
		public void setAantalPresentieWaarnemingen(int aantalPresentieWaarnemingen)
		{
			this.aantalPresentieWaarnemingen = aantalPresentieWaarnemingen;
		}

		/**
		 * @return Returns the aantalAbsentieWaarnemingen.
		 */
		public int getAantalAbsentieWaarnemingen()
		{
			return aantalAbsentieWaarnemingen;
		}

		/**
		 * @param aantalAbsentieWaarnemingen
		 *            The aantalAbsentieWaarnemingen to set.
		 */
		public void setAantalAbsentieWaarnemingen(int aantalAbsentieWaarnemingen)
		{
			this.aantalAbsentieWaarnemingen = aantalAbsentieWaarnemingen;
		}

		/**
		 * @return Returns the aantalVrijeWaarnemingen.
		 */
		public int getAantalVrijeWaarnemingen()
		{
			return aantalVrijeWaarnemingen;
		}

		/**
		 * @param aantalVrijeWaarnemingen
		 *            The aantalVrijeWaarnemingen to set.
		 */
		public void setAantalVrijeWaarnemingen(int aantalVrijeWaarnemingen)
		{
			this.aantalVrijeWaarnemingen = aantalVrijeWaarnemingen;
		}

		/**
		 * @return Returns the urenLes.
		 */
		public BigDecimal getUrenLes()
		{
			return urenLes;
		}

		/**
		 * @param urenLes
		 *            The urenLes to set.
		 */
		public void setUrenLes(BigDecimal urenLes)
		{
			this.urenLes = urenLes.setScale(1, RoundingMode.HALF_UP);
		}

		/**
		 * @return Returns the urenPresentieWaarnemingen.
		 */
		public BigDecimal getUrenPresentieWaarnemingen()
		{
			return urenPresentieWaarnemingen;
		}

		/**
		 * @param urenPresentieWaarnemingen
		 *            The urenPresentieWaarnemingen to set.
		 */
		public void setUrenPresentieWaarnemingen(BigDecimal urenPresentieWaarnemingen)
		{
			this.urenPresentieWaarnemingen =
				urenPresentieWaarnemingen.setScale(1, RoundingMode.HALF_UP);
		}

		/**
		 * @return Returns the urenAbsentieWaarnemingen.
		 */
		public BigDecimal getUrenAbsentieWaarnemingen()
		{
			return urenAbsentieWaarnemingen;
		}

		/**
		 * @param urenAbsentieWaarnemingen
		 *            The urenAbsentieWaarnemingen to set.
		 */
		public void setUrenAbsentieWaarnemingen(BigDecimal urenAbsentieWaarnemingen)
		{
			this.urenAbsentieWaarnemingen =
				urenAbsentieWaarnemingen.setScale(1, RoundingMode.HALF_UP);
		}

		/**
		 * @return Returns the urenVrijeWaarnemingen.
		 */
		public BigDecimal getUrenVrijeWaarnemingen()
		{
			return urenVrijeWaarnemingen;
		}

		/**
		 * @param urenVrijeWaarnemingen
		 *            The urenVrijeWaarnemingen to set.
		 */
		public void setUrenVrijeWaarnemingen(BigDecimal urenVrijeWaarnemingen)
		{
			this.urenVrijeWaarnemingen = urenVrijeWaarnemingen.setScale(1, RoundingMode.HALF_UP);
		}

		/**
		 * @return dagen absent gemeld
		 */
		public String getDagenAbsentGemeld()
		{
			if (dagenAbsentiemeldingen == 1)
				return dagenAbsentiemeldingen + " dag";
			return dagenAbsentiemeldingen + " dagen";
		}

		/**
		 * @return lesuren absent gemeld
		 */
		public String getLesurenAbsentGemeld()
		{
			if (lesurenAbsentiemeldingen == 1)
				return lesurenAbsentiemeldingen + " lesuur";
			return lesurenAbsentiemeldingen + " lesuren";
		}

		/**
		 * @return Percentage present bij absolute lessen
		 */
		public String getPercentagePresent()
		{
			BigDecimal res = getPercentage();
			if (res == null)
			{
				return null;
			}
			return NUMBER_FORMAT.format(res) + "%";
		}

		/**
		 * Het percentage dat de deelnemer aanwezig was bij absolute lessen.
		 * 
		 * @return percentage aanwezigheid
		 */
		public BigDecimal getPercentage()
		{
			if (DecimalUtil.isZero(urenLes))
			{
				return null;
			}
			if (DecimalUtil.isZero(urenPresentieWaarnemingen))
			{
				return BigDecimal.ZERO;
			}
			BigDecimal res =
				urenPresentieWaarnemingen.multiply(DecimalUtil.HUNDRED).divide(urenLes,
					RoundingMode.HALF_UP);
			res = res.setScale(0, RoundingMode.HALF_UP);
			return res;
		}

		/**
		 * @return Aantal uren absent gemeld of aantal uren met absentiewaarnemingen
		 */
		public int getUrenAbsentGemeldOfWaargenomen()
		{
			if (getLesurenAbsentiemeldingen() > 0)
			{
				return getLesurenAbsentiemeldingen();
			}
			if (getAantalAbsentieWaarnemingen() > 0)
			{
				return getAantalAbsentieWaarnemingen();
			}
			return 0;
		}

		/**
		 * @return Aantal uren absent gemeld of aantal uren met absentiewaarnemingen
		 */
		public String getUrenAbsentGemeldOfWaargenomenAsString()
		{
			if (getUrenAbsentGemeldOfWaargenomen() == 0)
			{
				return null;
			}
			if (getLesurenAbsentiemeldingen() > 0)
			{
				return getLesurenAbsentGemeld();
			}
			if (getAantalAbsentieWaarnemingen() == 1)
				return "1 lesuur";
			return getAantalAbsentieWaarnemingen() + " lesuren";
		}

		/**
		 * @return Aantal uren absent waargenomen
		 */
		public String getUrenAbsentWaargenomen()
		{
			if (getAantalAbsentieWaarnemingen() == 1)
				return "1 lesuur";
			return getAantalAbsentieWaarnemingen() + " lesuren";
		}

		/**
		 * @return Het aantal uren dat de deelnemer present is waargenomen via 'normale'
		 *         waarnemingen en vrije waarnemingen.
		 */
		public int getUrenPresentWaargenomen()
		{
			int uren = 0;
			if (getUrenPresentieWaarnemingen() != null)
			{
				uren = getUrenPresentieWaarnemingen().intValue();
			}
			if (getUrenVrijeWaarnemingen() != null)
			{
				uren = uren + getUrenVrijeWaarnemingen().intValue();
			}
			return uren;
		}
	}

	/**
	 * Kalendermaand
	 * 
	 * @author loite
	 */
	public static final class Maand implements Serializable
	{
		private static final long serialVersionUID = 1L;

		/**
		 * Lijst met beschikbare maanden.
		 */
		public static final List<Maand> MAANDEN = new ArrayList<Maand>();
		static
		{
			for (int jaar = 1990; jaar <= 2020; jaar++)
			{
				for (int maand = 1; maand <= 12; maand++)
				{
					MAANDEN.add(new Maand(jaar, maand));
				}
			}
		}

		private final int maand;

		private final int jaar;

		private final String naam;

		private final String langeNaam;

		private final Date begindatum;

		private final Date einddatum;

		/**
		 * Constructor
		 * 
		 * @param jaar
		 * @param maand
		 */
		private Maand(int jaar, int maand)
		{
			this.jaar = jaar;
			this.maand = maand;
			if (maand < 10)
			{
				naam = "0" + maand + "/" + jaar;
			}
			else
			{
				naam = maand + "/" + jaar;
			}
			langeNaam = TimeUtil.getInstance().getMaandNaam(maand) + " " + jaar;
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(0);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.MONTH, maand - 1);
			cal.set(Calendar.YEAR, jaar);
			begindatum = cal.getTime();
			cal.add(Calendar.MONTH, 1);
			cal.add(Calendar.DAY_OF_MONTH, -1);
			einddatum = cal.getTime();
		}

		/**
		 * Geeft de gegeven maand
		 * 
		 * @param jaar
		 * @param maand
		 * @return de maand
		 */
		public static Maand get(int jaar, int maand)
		{
			for (Maand mnd : MAANDEN)
			{
				if (mnd.jaar == jaar && mnd.maand == maand)
				{
					return mnd;
				}
			}
			return null;
		}

		/**
		 * @return De huidige maand
		 */
		public static Maand getHuidigeMaand()
		{
			Date datum = TimeUtil.getInstance().currentDate();
			int jaar = TimeUtil.getInstance().getYear(datum);
			int maand = TimeUtil.getInstance().getMonth(datum) + 1;
			return get(jaar, maand);
		}

		/**
		 * @return De volgende maand
		 */
		public Maand volgendeMaand()
		{
			int volgendJaar = jaar;
			int volgendeMaand = maand + 1;
			if (volgendeMaand > 12)
			{
				volgendeMaand = 1;
				volgendJaar = volgendJaar + 1;
			}
			return Maand.get(volgendJaar, volgendeMaand);
		}

		/**
		 * @return De vorige maand
		 */
		public Maand vorigeMaand()
		{
			int vorigJaar = jaar;
			int vorigeMaand = maand - 1;
			if (vorigeMaand == 0)
			{
				vorigeMaand = 12;
				vorigJaar = vorigJaar - 1;
			}
			return Maand.get(vorigJaar, vorigeMaand);
		}

		/**
		 * @return Returns the begindatum.
		 */
		public Date getBegindatum()
		{
			return begindatum;
		}

		/**
		 * @return Returns the einddatum.
		 */
		public Date getEinddatum()
		{
			return einddatum;
		}

		/**
		 * @return Returns the maand.
		 */
		public int getMaand()
		{
			return maand;
		}

		/**
		 * @return Returns the jaar.
		 */
		public int getJaar()
		{
			return jaar;
		}

		/**
		 * @return Returns the naam.
		 */
		public String getNaam()
		{
			return naam;
		}

		/**
		 * @return Returns the langeNaam.
		 */
		public String getLangeNaam()
		{
			return langeNaam;
		}

		/**
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + jaar;
			result = prime * result + maand;
			return result;
		}

		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final Maand other = (Maand) obj;
			if (jaar != other.jaar)
				return false;
			if (maand != other.maand)
				return false;
			return true;
		}

	}

	/**
	 * @return de deelnemer
	 */
	public Deelnemer getDeelnemer();

	/**
	 * @param deelnemer
	 */
	public void setDeelnemer(Deelnemer deelnemer);

	/**
	 * @return Returns the vanafMaand.
	 */
	public Maand getVanafMaand();

	/**
	 * @param vanafMaand
	 *            The vanafMaand to set.
	 */
	public void setVanafMaand(Maand vanafMaand);

	/**
	 * @return Returns the totMaand.
	 */
	public Maand getTotMaand();

	/**
	 * @param totMaand
	 *            The totMaand to set.
	 */
	public void setTotMaand(Maand totMaand);

}
