package nl.topicus.eduarte.xml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurDataAccessHelper;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.xml.resultaatstructuur.v10.XResultaatstructuurRef;

public class ResultaatstructuurAdapter extends
		XmlAdapter<XResultaatstructuurRef, Resultaatstructuur>
{
	public ResultaatstructuurAdapter()
	{
	}

	@Override
	public XResultaatstructuurRef marshal(Resultaatstructuur v)
	{
		if (v == null)
			return null;

		Resultaatstructuur value = v.reget(Resultaatstructuur.class);
		XResultaatstructuurRef ret = new XResultaatstructuurRef();
		ret.setCode(value.getCode());
		ret.setCohort(value.getCohort());
		ret.setOnderwijsproduct(value.getOnderwijsproduct());
		return ret;
	}

	@Override
	public Resultaatstructuur unmarshal(XResultaatstructuurRef v)
	{
		if (v == null)
			return null;

		Resultaatstructuur ret =
			DataAccessRegistry.getHelper(ResultaatstructuurDataAccessHelper.class)
				.getResultaatstructuur(v.getOnderwijsproduct(), v.getCohort(), null, v.getCode());
		if (ret == null)
			throw new IllegalArgumentException("Onbekende resultaatstructuur '" + v.getCode() + "'");
		return ret;
	}
}
