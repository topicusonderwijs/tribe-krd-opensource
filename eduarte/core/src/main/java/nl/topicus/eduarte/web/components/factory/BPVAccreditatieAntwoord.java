package nl.topicus.eduarte.web.components.factory;

import java.util.Date;

public class BPVAccreditatieAntwoord
{
	private int signaalcode;

	private String melding;

	private Date startdatum;

	private Date einddatum;

	public BPVAccreditatieAntwoord()
	{

	}

	public int getSignaalcode()
	{
		return signaalcode;
	}

	public void setSignaalcode(int signaalcode)
	{
		this.signaalcode = signaalcode;
	}

	public String getMelding()
	{
		return melding;
	}

	public void setMelding(String melding)
	{
		this.melding = melding;
	}

	public Date getStartdatum()
	{
		return startdatum;
	}

	public void setStartdatum(Date startdatum)
	{
		this.startdatum = startdatum;
	}

	public Date getEinddatum()
	{
		return einddatum;
	}

	public void setEinddatum(Date einddatum)
	{
		this.einddatum = einddatum;
	}
}
