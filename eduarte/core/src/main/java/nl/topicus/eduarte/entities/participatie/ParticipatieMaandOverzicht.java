package nl.topicus.eduarte.entities.participatie;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

import nl.topicus.cobra.util.DecimalUtil;
import nl.topicus.eduarte.participatie.zoekfilters.AanwezigheidMaandFilter.Maand;

/**
 * Class voor het bijhouden van een maandoverzicht van aanwezigheidsgegevens.
 * 
 * @author vandekamp
 */
public class ParticipatieMaandOverzicht implements Serializable
{
	private static final long serialVersionUID = 1L;

	private static final NumberFormat NUMBER_FORMAT =
		NumberFormat.getInstance(new Locale("nl", "NL"));

	private Maand maand;

	/**
	 * Het aantal lessen uit het absolute rooster.
	 */
	private int aantalAfspraken;

	/**
	 * Het aantal uren les volgens het absolute rooster.
	 */
	private BigDecimal totaalUrenAfspraken;

	/**
	 * Het aantal uren van alle afspraken waarvan de aanwezigheids registratie verplicht
	 * is
	 */
	private BigDecimal aanwezigheidVereistAfspraken;

	/**
	 * Het aantal uren van alle openstaande afspraken
	 */
	private BigDecimal openAfspraken;

	/**
	 * Het aantal uren presentiewaarnemingen gekoppeld aan lesuur.
	 */
	private BigDecimal urenPresentieWaarnemingen;

	/**
	 * Het aantal uren absentiewaarnemingen.
	 */
	private BigDecimal urenAbsentieWaarnemingen;

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
	private int urenAbsentiemeldingen;

	/**
	 * Constructor
	 */
	public ParticipatieMaandOverzicht()
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
	public int getUrenAbsentiemeldingen()
	{
		return urenAbsentiemeldingen;
	}

	/**
	 * @param urenAbsentiemeldingen
	 */
	public void setUrenAbsentiemeldingen(int urenAbsentiemeldingen)
	{
		this.urenAbsentiemeldingen = urenAbsentiemeldingen;
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
	 * @return totaalUrenAfspraken
	 */
	public BigDecimal getTotaalUrenAfspraken()
	{
		return totaalUrenAfspraken;
	}

	/**
	 * @param totaalUrenAfspraken
	 */
	public void setTotaalUrenAfspraken(BigDecimal totaalUrenAfspraken)
	{
		this.totaalUrenAfspraken = totaalUrenAfspraken.setScale(1, RoundingMode.HALF_UP);
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
		this.urenAbsentieWaarnemingen = urenAbsentieWaarnemingen.setScale(1, RoundingMode.HALF_UP);
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
	public String getUrenAbsentGemeld()
	{
		if (urenAbsentiemeldingen == 1)
			return urenAbsentiemeldingen + " uur";
		return urenAbsentiemeldingen + " uren";
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
		BigDecimal total = urenAbsentieWaarnemingen.add(urenPresentieWaarnemingen);
		if (DecimalUtil.isZero(total))
		{
			return null;
		}
		if (DecimalUtil.isZero(urenPresentieWaarnemingen))
		{
			return BigDecimal.ZERO;
		}
		BigDecimal res =
			urenPresentieWaarnemingen.multiply(DecimalUtil.HUNDRED).divide(total,
				RoundingMode.HALF_UP);
		res = res.setScale(0, RoundingMode.HALF_UP);
		return res;
	}

	/**
	 * @return Returns the aantalAfspraken.
	 */
	public int getAantalAfspraken()
	{
		return aantalAfspraken;
	}

	/**
	 * @param aantalAfspraken
	 *            The aantalAfspraken to set.
	 */
	public void setAantalAfspraken(int aantalAfspraken)
	{
		this.aantalAfspraken = aantalAfspraken;
	}

	/**
	 * @return Returns the aanwezigheidVereistAfspraken.
	 */
	public BigDecimal getAanwezigheidVereistAfspraken()
	{
		return aanwezigheidVereistAfspraken;
	}

	/**
	 * @param aanwezigheidVereistAfspraken
	 *            The aanwezigheidVereistAfspraken to set.
	 */
	public void setAanwezigheidVereistAfspraken(BigDecimal aanwezigheidVereistAfspraken)
	{
		this.aanwezigheidVereistAfspraken =
			aanwezigheidVereistAfspraken.setScale(1, RoundingMode.HALF_UP);
	}

	/**
	 * @return Returns the openAfspraken.
	 */
	public BigDecimal getOpenAfspraken()
	{
		return openAfspraken;
	}

	/**
	 * @param openAfspraken
	 *            The openAfspraken to set.
	 */
	public void setOpenAfspraken(BigDecimal openAfspraken)
	{
		this.openAfspraken = openAfspraken.setScale(1, RoundingMode.HALF_UP);
	}
}
