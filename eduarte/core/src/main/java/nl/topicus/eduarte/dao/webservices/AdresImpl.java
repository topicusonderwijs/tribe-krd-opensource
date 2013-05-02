package nl.topicus.eduarte.dao.webservices;

import nl.topicus.eduarte.dao.webservices.PostcodeDataAccessHelper.Adres;
import nl.topicus.eduarte.entities.landelijk.Gemeente;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.landelijk.Provincie;

/**
 *
 */
public class AdresImpl implements Adres
{
	private String straatnaam;

	private String huisnummer;

	private String postcode;

	private String plaatsnaam;

	private Gemeente gemeente;

	private String huisnummerToevoeging;

	private Land land;

	private Provincie provincie;

	public AdresImpl()
	{
	}

	public AdresImpl(AdresImpl other)
	{
		straatnaam = other.straatnaam;
		huisnummer = other.huisnummer;
		postcode = other.postcode;
		plaatsnaam = other.plaatsnaam;
		gemeente = other.gemeente;
		huisnummerToevoeging = other.huisnummerToevoeging;
		land = other.land;
		provincie = other.provincie;
	}

	@Override
	public String getStraatnaam()
	{
		return straatnaam;
	}

	@Override
	public String getHuisnummer()
	{
		return huisnummer;
	}

	@Override
	public String getPostcode()
	{
		return postcode;
	}

	@Override
	public String getPlaatsnaam()
	{
		return plaatsnaam;
	}

	@Override
	public Gemeente getGemeente()
	{
		return gemeente;
	}

	@Override
	public Land getLand()
	{
		return land;
	}

	@Override
	public Provincie getProvincie()
	{
		return provincie;
	}

	void setStraatnaam(String straatnaam)
	{
		this.straatnaam = straatnaam;
	}

	void setHuisnummer(String huisnummer)
	{
		this.huisnummer = huisnummer;
	}

	void setPostcode(String postcode)
	{
		this.postcode = postcode;
	}

	void setPlaatsnaam(String woonplaats)
	{
		this.plaatsnaam = woonplaats;
	}

	void setGemeente(Gemeente gemeente)
	{
		this.gemeente = gemeente;
	}

	void setLand(Land land)
	{
		this.land = land;
	}

	void setProvincie(Provincie provincie)
	{
		this.provincie = provincie;
	}

	public String getHuisnummerToevoeging()
	{
		return huisnummerToevoeging;
	}

	public void setHuisnummerToevoeging(String huisnummerToevoeging)
	{
		this.huisnummerToevoeging = huisnummerToevoeging;
	}
}
