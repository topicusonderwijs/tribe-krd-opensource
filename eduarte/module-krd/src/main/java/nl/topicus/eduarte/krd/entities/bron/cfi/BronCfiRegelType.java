package nl.topicus.eduarte.krd.entities.bron.cfi;

public enum BronCfiRegelType
{
	BRI(""),
	BEK("Bekostiging"),
	EXP("deelnemers experiment vmbo-mbo2"),
	SAG("Signalen op geaggregeerd niveau"),
	SIN("Signalen op individueel niveau"),
	SBH("Bekostiging signalen hoog tarief (volledige bekostiging)"),
	SBL("Bekostiging signalen laag tarief (volledige bekostiging)"),
	SLR("");

	private String omschrijving;

	private BronCfiRegelType(String omschrijving)
	{
		this.setOmschrijving(omschrijving);
	}

	public static BronCfiRegelType parse(String value)
	{
		for (BronCfiRegelType regelType : values())
		{
			if (regelType.name().equals(value))
				return regelType;
		}
		return null;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}
}