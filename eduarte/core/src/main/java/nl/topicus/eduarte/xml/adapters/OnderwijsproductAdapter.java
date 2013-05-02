package nl.topicus.eduarte.xml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;

public class OnderwijsproductAdapter extends XmlAdapter<String, Onderwijsproduct>
{

	@Override
	public Onderwijsproduct unmarshal(String onderwijsproductCode)
	{
		if (onderwijsproductCode == null)
			return null;

		OnderwijsproductDataAccessHelper helper =
			DataAccessRegistry.getHelper(OnderwijsproductDataAccessHelper.class);
		Onderwijsproduct ret = helper.get(onderwijsproductCode);
		if (ret == null)
			throw new IllegalArgumentException("Onbekende onderwijsproductcode '"
				+ onderwijsproductCode + "'");
		return ret;
	}

	@Override
	public String marshal(Onderwijsproduct value)
	{
		if (value == null)
			return null;

		return value.reget(Onderwijsproduct.class).getCode();
	}
}
