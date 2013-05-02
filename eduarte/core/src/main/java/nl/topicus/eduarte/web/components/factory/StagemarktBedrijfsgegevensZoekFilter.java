package nl.topicus.eduarte.web.components.factory;

import java.io.Serializable;

public class StagemarktBedrijfsgegevensZoekFilter implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String bedrijfsnaam;

	private String postcode;

	private Integer huisnummer;

	public String getBedrijfsnaam()
	{
		return bedrijfsnaam;
	}

	public void setBedrijfsnaam(String bedrijfsnaam)
	{
		this.bedrijfsnaam = bedrijfsnaam;
	}

	public String getPostcode()
	{
		return postcode;
	}

	public void setPostcode(String postcode)
	{
		this.postcode = postcode;
	}

	public Integer getHuisnummer()
	{
		return huisnummer;
	}

	public void setHuisnummer(Integer huisnummer)
	{
		this.huisnummer = huisnummer;
	}

	public boolean isEmpty()
	{
		return ((bedrijfsnaam == null || bedrijfsnaam.isEmpty())
			&& (postcode == null || postcode.isEmpty()) && huisnummer == null);
	}
}
