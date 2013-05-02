package nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author vanderkamp
 */
public class WaarnemingenTotaalTelling implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String waarnemingSoort;

	private String absentieRedenOmschrijving;

	private BigDecimal aantalKlokuren;

	private int aantalLesuren;

	private String percentageAanwezig;

	public WaarnemingenTotaalTelling()
	{
	}

	public String getAbsentieRedenOmschrijving()
	{
		return absentieRedenOmschrijving;
	}

	public void setAbsentieRedenOmschrijving(String absentieRedenOmschrijving)
	{
		this.absentieRedenOmschrijving = absentieRedenOmschrijving;
	}

	public BigDecimal getAantalKlokuren()
	{
		return aantalKlokuren;
	}

	public void setAantalKlokuren(BigDecimal aantalKlokuren)
	{
		this.aantalKlokuren = aantalKlokuren;
	}

	public int getAantalLesuren()
	{
		return aantalLesuren;
	}

	public void setAantalLesuren(int aantalLesuren)
	{
		this.aantalLesuren = aantalLesuren;
	}

	public String getPercentageAanwezig()
	{
		return percentageAanwezig;
	}

	public void setPercentageAanwezig(String percentageAanwezig)
	{
		this.percentageAanwezig = percentageAanwezig;
	}

	public String getWaarnemingSoort()
	{
		return waarnemingSoort;
	}

	public void setWaarnemingSoort(String waarnemingSoort)
	{
		this.waarnemingSoort = waarnemingSoort;
	}
}
