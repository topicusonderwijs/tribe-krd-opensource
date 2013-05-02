package nl.topicus.eduarte.entities.hogeronderwijs;

import java.util.Arrays;

public enum GBAVerificatieStatus implements KeyValue
{
	Afmelden("A"),
	DecentrlGeverifrdInst("D", "Decentraal geverifieerd instelling"),
	NietGeverifrdInst("E", "Niet geverifieerd instelling"),
	DecentrlGeverifrdIB("F", "Decentraal geverifieerd IB-Groep"),
	CentrlGeverifrd("G", "Centraal geverifieerd"),
	InBewerking("I", "In bewerking"),
	GeenGBAAdres("K", "Geen GBA adres"),
	CentrlGeidentificrd("L", "Centraal geïdentificeerd"),
	NietGeverifieerd("O", "Niet geverifieerd");

	private String key;

	private String value;

	@Override
	public String getKey()
	{
		return key;
	}

	@Override
	public String getValue()
	{
		return value;
	}

	private GBAVerificatieStatus(String key)
	{
		this.key = key;
		this.value = name();
	}

	private GBAVerificatieStatus(String key, String value)
	{
		this.key = key;
		this.value = value;
	}

	public boolean isOK()
	{
		return Arrays.asList(GBAVerificatieStatus.CentrlGeidentificrd,
			GBAVerificatieStatus.CentrlGeverifrd, GBAVerificatieStatus.DecentrlGeverifrdIB,
			GBAVerificatieStatus.DecentrlGeverifrdInst).contains(this);
	}

	public boolean kanAfgemeldWorden()
	{
		return this == GBAVerificatieStatus.DecentrlGeverifrdInst;
	}

	@Override
	public String toString()
	{
		return value;
	}
}
