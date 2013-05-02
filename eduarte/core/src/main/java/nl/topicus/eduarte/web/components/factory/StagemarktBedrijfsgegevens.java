package nl.topicus.eduarte.web.components.factory;

import java.io.Serializable;

import nl.topicus.cobra.entities.IdObject;

public class StagemarktBedrijfsgegevens implements IdObject
{
	private static final long serialVersionUID = 1L;

	private String bedrijfsnaam;

	private String postcode;

	private int huisnummer;

	private String codeleerbedrijf;

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

	public int getHuisnummer()
	{
		return huisnummer;
	}

	public void setHuisnummer(int huisnummer)
	{
		this.huisnummer = huisnummer;
	}

	@Override
	public Serializable getIdAsSerializable()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getVersion()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSaved()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setVersion(Long version)
	{
		// TODO Auto-generated method stub

	}

	public String getCodeleerbedrijf()
	{
		return codeleerbedrijf;
	}

	public void setCodeleerbedrijf(String codeleerbedrijf)
	{
		this.codeleerbedrijf = codeleerbedrijf;
	}
}
