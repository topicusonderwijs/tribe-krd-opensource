package nl.topicus.eduarte.xml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.GroepDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;

public class GroepAdapter extends XmlAdapter<String, Groep>
{
	@Override
	public Groep unmarshal(String groepCode)
	{
		if (groepCode == null)
			return null;

		GroepDataAccessHelper helper = DataAccessRegistry.getHelper(GroepDataAccessHelper.class);
		Groep ret = helper.getByGroepcode(groepCode);
		if (ret == null)
			throw new IllegalArgumentException("Onbekende groep '" + groepCode + "'");
		return ret;
	}

	@Override
	public String marshal(Groep value)
	{
		if (value == null)
			return null;

		return value.reget(Groep.class).getCode();
	}
}
