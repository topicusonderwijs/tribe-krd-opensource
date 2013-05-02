package nl.topicus.eduarte.entities.inschrijving;

public enum VrijstellingType
{
	/**
	 * Het onderwijsproduct is gewoon afgenomen en is dus geen vrijstelling.
	 */
	Geen("", ""),
	/**
	 * Voor het onderwijsproduct is een vrijstelling afgegeven wegens een Eerder/Elders
	 * Behaalde Competentie/Kwalificatie. Het onderwijsproduct is per definitie behaald en
	 * de deelnemer/student krijgt eventuele bijbehorende credits.
	 */
	EVC("vr", "vrijstelling"),
	/**
	 * Alleen voor het HO. Het onderwijsproduct hoeft niet te worden gedaan, maar wordt
	 * vervangen door een ander onderwijsproduct. Het onderwijsproduct is weliswaar
	 * behaald (de student kan er niet op zakken qua criteria), maar de student krijgt er
	 * geen credits voor.
	 */
	Vervallen("XX", "vervallen");

	private String afkorting;

	private String omschrijving;

	public boolean isVrijstelling()
	{
		return this != Geen;
	}

	public boolean tellenCreditsMee()
	{
		return this != Vervallen;
	}

	public static VrijstellingType[] valuesBVE()
	{
		return new VrijstellingType[] {Geen, EVC};
	}

	public static VrijstellingType[] valuesHO()
	{
		return values();
	}

	public String getAfkorting()
	{
		return afkorting;
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	private VrijstellingType(String afkorting, String omschrijving)
	{
		this.afkorting = afkorting;
		this.omschrijving = omschrijving;
	}

}
