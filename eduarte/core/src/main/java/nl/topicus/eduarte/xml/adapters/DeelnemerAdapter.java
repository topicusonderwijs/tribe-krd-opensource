package nl.topicus.eduarte.xml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.DeelnemerDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Deelnemer;

public class DeelnemerAdapter extends XmlAdapter<Integer, Deelnemer>
{
	@Override
	public Deelnemer unmarshal(Integer deelnemerNummer)
	{
		if (deelnemerNummer == null)
			return null;

		DeelnemerDataAccessHelper helper =
			DataAccessRegistry.getHelper(DeelnemerDataAccessHelper.class);
		Deelnemer ret = helper.getByDeelnemernummer(deelnemerNummer);
		if (ret == null)
			throw new IllegalArgumentException("Onbekende deelnemer '" + deelnemerNummer + "'");
		return ret;
	}

	@Override
	public Integer marshal(Deelnemer value)
	{
		if (value == null)
			return null;

		return value.reget(Deelnemer.class).getDeelnemernummer();
	}
}
