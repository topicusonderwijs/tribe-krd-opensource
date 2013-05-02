package nl.topicus.eduarte.xml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.SchaalDataAccessHelper;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaal;

public class SchaalAdapter extends XmlAdapter<String, Schaal>
{

	@Override
	public Schaal unmarshal(String schaalNaam)
	{
		if (schaalNaam == null)
			return null;

		SchaalDataAccessHelper helper = DataAccessRegistry.getHelper(SchaalDataAccessHelper.class);
		Schaal ret = helper.get(schaalNaam);
		if (ret == null)
			throw new IllegalArgumentException("Onbekende schaal '" + schaalNaam + "'");
		return ret;
	}

	@Override
	public String marshal(Schaal value)
	{
		if (value == null)
			return null;

		return value.reget(Schaal.class).getNaam();
	}
}
