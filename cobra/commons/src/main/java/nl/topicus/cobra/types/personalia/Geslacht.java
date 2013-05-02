/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.types.personalia;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * Geslacht van persoon.
 * 
 * @author marrink
 */
@XmlType
@XmlEnum
public enum Geslacht
{
	/**
	 * Iemand van het mannelijke geslacht.
	 */
	@XmlEnumValue("man")
	Man("heer", "De heer", 1),
	/**
	 * Iemand van het vrouwlijke geslacht.
	 */
	@XmlEnumValue("vrouw")
	Vrouw("mevrouw", "Mevrouw", 2),
	/**
	 * Iemand waarvan het geslacht niet bekend is.
	 */
	@XmlEnumValue("onbekend")
	Onbekend("heer of mevrouw", "De heer of mevrouw", 3);

	private String aanhef;

	private String adressering;

	private int nummer;

	/**
	 * De naam.
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString()
	{
		return name();
	}

	/**
	 * @return Eerste letter van de naam: M, V of O
	 */
	public String getEersteLetter()
	{
		return name().substring(0, 1);
	}

	private Geslacht(String aanhef, String adressering, int nummer)
	{
		this.aanhef = aanhef;
		this.adressering = adressering;
		this.nummer = nummer;
	}

	/**
	 * @return Tekst voor de aanhef van deze persoon: heer of mevrouw
	 */
	public String getAanhef()
	{
		return aanhef;
	}

	/**
	 * @return Tekst voor de afkorting van dit geslacht: De heer of Mevrouw
	 */
	public String getAdressering()
	{
		return adressering;
	}

	public int getNummer()
	{
		return nummer;
	}

	public static Geslacht parse(String geslacht)
	{
		if (geslacht == null)
			return null;
		for (Geslacht g : values())
		{
			if (geslacht.equalsIgnoreCase(g.toString())
				|| geslacht.equalsIgnoreCase(g.getEersteLetter()))
			{
				return g;
			}
		}
		return null;
	}
}
