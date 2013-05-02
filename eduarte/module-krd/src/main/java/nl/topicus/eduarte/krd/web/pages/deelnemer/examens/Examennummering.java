package nl.topicus.eduarte.krd.web.pages.deelnemer.examens;

import java.io.Serializable;

import nl.topicus.cobra.web.components.form.AutoForm;

public class Examennummering implements Serializable
{
	@AutoForm(required = false)
	private String prefix;

	@AutoForm(required = true)
	private int beginnummer = 1;

	@AutoForm(required = true)
	private BijBestaandeNummers bijBestaandeNummers;

	private static final long serialVersionUID = 1L;

	public Examennummering()
	{
	}

	public enum BijBestaandeNummers
	{

		NietWijzigen
		{
			@Override
			public String toString()
			{
				return "Niet wijzigen";
			}
		},
		Overschrijven;
	}

	public String getPrefix()
	{
		return prefix;
	}

	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}

	public int getBeginnummer()
	{
		return beginnummer;
	}

	public void setBeginnummer(int beginnummer)
	{
		this.beginnummer = beginnummer;
	}

	public BijBestaandeNummers getBijBestaandeNummers()
	{
		return bijBestaandeNummers;
	}

	public void setBijBestaandeNummers(BijBestaandeNummers bijBestaandeNummers)
	{
		this.bijBestaandeNummers = bijBestaandeNummers;
	}
}