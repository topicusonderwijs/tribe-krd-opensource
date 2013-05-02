package nl.topicus.eduarte.entities.hogeronderwijs;

public enum VooropleidingVerificatieStatus implements KeyValue
{
	Afmelden("A"),
	CentrGeverifrd("GD", "Centraal geverifieerd"),
	StudVolgtOpl("GV", "Student volgt vooropleiding"),
	NietGeverifrdIB("NG", "Niet geverifieerd o.b.v IB-groep"),
	GeenToest("NT", "Geen toestemming"),
	NietMogelijk("NV", "Niet mogelijk"),
	ToestOnbekend("NX", "Toestemming onbekend"),
	DecentrlGeverifrdInst("DD", "Decentraal geverifieerd bij instelling"),
	DecentrlGeverifrdIB("ID", "Decentraal geverifieerd bij IB-Groep"),
	NietGeverifrdIBInst("N", "Niet geverifieerd door IB en instelling");

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

	private VooropleidingVerificatieStatus(String key)
	{
		this.key = key;
		this.value = name();
	}

	private VooropleidingVerificatieStatus(String key, String value)
	{
		this.key = key;
		this.value = value;
	}

	public boolean isOK()
	{
		return this.equals(CentrGeverifrd) || this.equals(DecentrlGeverifrdIB)
			|| this.equals(DecentrlGeverifrdInst);
	}

	public boolean kanAfgemeldWorden()
	{
		return this.equals(DecentrlGeverifrdInst);
	}

	@Override
	public String toString()
	{
		return value;
	}
}
