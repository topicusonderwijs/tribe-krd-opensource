package nl.topicus.eduarte.krd.web.pages.deelnemer.examens.procesverbaal;

import java.io.Serializable;

public class Surveillant implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String achternaam;

	private String voorletters;

	public Surveillant()
	{

	}

	public Surveillant(String achternaam, String voorletters)
	{
		this.achternaam = achternaam;
		this.voorletters = voorletters;
	}

	public void setAchternaam(String achternaam)
	{
		this.achternaam = achternaam;
	}

	public String getAchternaam()
	{
		return achternaam;
	}

	public void setVoorletters(String voorletters)
	{
		this.voorletters = voorletters;
	}

	public String getVoorletters()
	{
		return voorletters;
	}
}