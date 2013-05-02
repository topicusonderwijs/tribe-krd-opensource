package nl.topicus.eduarte.xml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.TaxonomieElementDataAccessHelper;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;

public class TaxonomieAdapter extends XmlAdapter<String, Taxonomie>
{

	@Override
	public Taxonomie unmarshal(String taxonomieCode)
	{
		if (taxonomieCode == null)
			return null;

		TaxonomieElementDataAccessHelper helper =
			DataAccessRegistry.getHelper(TaxonomieElementDataAccessHelper.class);
		Taxonomie ret = (Taxonomie) helper.get(taxonomieCode);
		if (ret == null)
			throw new IllegalArgumentException("Onbekende taxonomie '" + taxonomieCode + "'");
		return ret;
	}

	@Override
	public String marshal(Taxonomie value)
	{
		if (value == null)
			return null;

		return value.reget(Taxonomie.class).getTaxonomiecode();
	}
}
