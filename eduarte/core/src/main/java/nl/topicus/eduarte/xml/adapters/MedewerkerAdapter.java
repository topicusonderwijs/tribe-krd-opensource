package nl.topicus.eduarte.xml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.MedewerkerDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Medewerker;

public class MedewerkerAdapter extends XmlAdapter<String, Medewerker>
{
	@Override
	public Medewerker unmarshal(String medewerkerCode)
	{
		if (medewerkerCode == null)
			return null;

		MedewerkerDataAccessHelper helper =
			DataAccessRegistry.getHelper(MedewerkerDataAccessHelper.class);
		Medewerker ret = helper.batchGetByAfkorting(medewerkerCode);
		if (ret == null)
			throw new IllegalArgumentException("Onbekende medewerker '" + medewerkerCode + "'");
		return ret;
	}

	@Override
	public String marshal(Medewerker value)
	{
		if (value == null)
			return null;

		return value.reget(Medewerker.class).getAfkorting();
	}
}
