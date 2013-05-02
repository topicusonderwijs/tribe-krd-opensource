package nl.topicus.eduarte.entities.hogeronderwijs;

import nl.topicus.cobra.util.StringUtil;

//TODO Paul - Statussen moeten verschillend zijn voor instelling en IB-Groep
public enum DeficientieStatus
{
	DefInst("D", "Deficiënt volgens instelling"),
	DefIB("J", "Deficiënt volgens IB-Groep"),
	NogNietBep("N", "Nog niet bepaald"),
	Sufficiënt("S", "Sufficiënt"),
	NietDefIB("T", "Niet Deficiënt volgens IB-Groep"),
	VoorlNietDefIB("V", "Voorlopig niet deficiënt volgens IB-Groep"),
	VoorlNietDefInst("W", "Voorlopig niet deficiënt volgens Instelling");

	private String veldKey;

	private String veldValue;

	private String omschrijving;

	private DeficientieStatus(String veldValue)
	{
		this.veldKey = StringUtil.convertCamelCase(name());
		this.veldValue = veldValue;
		this.omschrijving = veldValue;
	}

	private DeficientieStatus(String veldValue, String omschrijving)
	{
		this.veldKey = StringUtil.convertCamelCase(name());
		this.veldValue = veldValue;
		this.omschrijving = omschrijving;
	}

	public String getVeldKey()
	{
		return veldKey;
	}

	public String getVeldValue()
	{
		return veldValue;
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	@Override
	public String toString()
	{
		return omschrijving;
	}

	public Boolean isOK()
	{
		return this.equals(Sufficiënt);
	}
}
