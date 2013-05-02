package nl.topicus.eduarte.xml.adapters;

import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.OpleidingDataAccessHelper;
import nl.topicus.eduarte.entities.opleiding.Opleiding;

public class OpleidingAdapter extends XmlAdapter<String, Opleiding>
{

	@Override
	public Opleiding unmarshal(String opleidingCode)
	{
		if (opleidingCode == null)
			return null;

		OpleidingDataAccessHelper helper =
			DataAccessRegistry.getHelper(OpleidingDataAccessHelper.class);
		List<Opleiding> ret = helper.getOpleidingen(opleidingCode);
		if (ret.isEmpty())
			throw new IllegalArgumentException("Onbekende opleiding '" + opleidingCode + "'");
		if (ret.size() > 1)
			throw new IllegalArgumentException("Meerdere opleidingen met code '" + opleidingCode
				+ "'");
		return ret.get(0);
	}

	@Override
	public String marshal(Opleiding value)
	{
		if (value == null)
			return null;

		return value.reget(Opleiding.class).getCode();
	}
}
