package nl.topicus.eduarte.dao.webservices;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.LandDataAccessHelper;
import nl.topicus.eduarte.entities.Entiteit;

public abstract class AbstractPostcodeDataAccessHelper implements PostcodeDataAccessHelper
{
	protected static final Map<String, String> provincieCodes;

	static
	{
		// 3A WS NAAM
		// 20 A Groningen
		// 21 B Friesland
		// 22 D Drenthe
		// 23 E Overijssel
		// 24 X Flevoland
		// 25 G Gelderland
		// 26 M Utrecht
		// 27 L Noord-Holland
		// 28 H Zuid-Holland
		// 29 S Zeeland
		// 30 P Noord-Brabant
		// 31 K Limburg

		provincieCodes = new HashMap<String, String>();
		provincieCodes.put("A", "20");
		provincieCodes.put("B", "21");
		provincieCodes.put("D", "22");
		provincieCodes.put("E", "23");
		provincieCodes.put("X", "24");
		provincieCodes.put("G", "25");
		provincieCodes.put("M", "26");
		provincieCodes.put("L", "27");
		provincieCodes.put("H", "28");
		provincieCodes.put("S", "29");
		provincieCodes.put("P", "30");
		provincieCodes.put("K", "31");
	}

	protected static final String CODE_NEDERLAND = "6030";

	protected AdresImpl newAdres(String postcode, String huisnummer, String huisnummerToevoeging)
	{
		LandDataAccessHelper landen = DataAccessRegistry.getHelper(LandDataAccessHelper.class);

		AdresImpl newAdres = new AdresImpl();
		newAdres.setHuisnummer(huisnummer);
		newAdres.setPostcode(postcode);
		newAdres.setLand(landen.get(CODE_NEDERLAND));
		newAdres.setHuisnummerToevoeging(huisnummerToevoeging);
		return newAdres;
	}

	@Override
	public void delete(Entiteit dataObject)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void evict(Entiteit dataObject)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <R extends Entiteit> R get(Class<R> class1, Serializable id)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <R extends Entiteit> List<R> list(Class<R> class1, String... orderBy)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <R extends Entiteit> List<R> list(Class<R> class1,
			Collection< ? extends Serializable> ids)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <R extends Entiteit> R load(Class<R> class1, Serializable id)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Serializable save(Entiteit dataObject)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void saveOrUpdate(Entiteit dataObject)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void update(Entiteit dataObject)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <Y> int touch(Class< ? > clz, String property, Y van, Y totEnMet)
	{
		throw new UnsupportedOperationException("update not supported");
	}
}
