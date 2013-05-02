package nl.topicus.eduarte.dao.webservices;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.GemeenteDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ProvincieDataAccessHelper;

/**
 * Een dummy implementatie om te voorkomen dat men de webservice gebruikt wanneer dit niet
 * nodig is.
 * 
 * @author hoeve
 */
public class MockPostcodeDataAccessHelperImpl extends AbstractPostcodeDataAccessHelper
{

	@Override
	public Adres fillAdresByPostcodeHuisnummer(String postcode, String huisnummer,
			String huisnummerToevoeging)
	{
		AdresImpl result = newAdres(postcode, huisnummer, huisnummerToevoeging);

		if (postcode.contains(" "))
			return result;
		if ("13".equals(huisnummer))
			return result; // for testing ;)

		GemeenteDataAccessHelper gemeentes =
			DataAccessRegistry.getHelper(GemeenteDataAccessHelper.class);
		ProvincieDataAccessHelper provincies =
			DataAccessRegistry.getHelper(ProvincieDataAccessHelper.class);

		result.setStraatnaam("Teststraat");
		result.setGemeente(gemeentes.get("0150"));
		result.setPlaatsnaam(result.getGemeente().getNaam());
		result.setProvincie(provincies.get("23"));

		return result;
	}
}
