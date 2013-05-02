package nl.topicus.eduarte.entities.adres;

import nl.topicus.cobra.util.StringUtil;

/**
 * Enum om aan te geven waar men het Contactgegeven standaard moet tonen op edit pages.
 * Voor deze contactgegevens wordt al direct een leeg veld aangemaakt, zodat je hem niet
 * met de hand hoeft toe te voegen.
 * 
 * @author hoeve
 */
public enum StandaardContactgegeven
{
	StandaardTonenBijPersoon("Standaard tonen bij personen"),
	StandaardTonenBijOrganisatie("Standaard tonen bij organisaties"),
	StandaardTonen("Overal standaard tonen");

	private String label = "";

	private StandaardContactgegeven(String label)
	{
		this.label = label;
	}

	@Override
	public String toString()
	{
		if (StringUtil.isEmpty(label))
			return super.toString();

		return label;
	}
}
