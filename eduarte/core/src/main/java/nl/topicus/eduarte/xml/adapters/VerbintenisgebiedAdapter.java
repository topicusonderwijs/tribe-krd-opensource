package nl.topicus.eduarte.xml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.TaxonomieElementDataAccessHelper;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;

public class VerbintenisgebiedAdapter extends XmlAdapter<String, Verbintenisgebied>
{

	@Override
	public Verbintenisgebied unmarshal(String taxonomieCode)
	{
		if (taxonomieCode == null)
			return null;

		TaxonomieElementDataAccessHelper helper =
			DataAccessRegistry.getHelper(TaxonomieElementDataAccessHelper.class);
		Verbintenisgebied ret = (Verbintenisgebied) helper.get(taxonomieCode);
		if (ret == null)
			throw new IllegalArgumentException("Onbekend verbintenisgebied '" + taxonomieCode + "'");
		return ret;
	}

	@Override
	public String marshal(Verbintenisgebied value)
	{
		if (value == null)
			return null;

		return value.reget(Verbintenisgebied.class).getTaxonomiecode();
	}
}
