package nl.topicus.eduarte.xml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.xml.resultaatstructuur.v10.XToetsRef;

public class ToetsAdapter extends XmlAdapter<XToetsRef, Toets>
{
	public ToetsAdapter()
	{
	}

	@Override
	public XToetsRef marshal(Toets v)
	{
		if (v == null)
			return null;

		Toets curToets = v.reget(Toets.class);
		XToetsRef ret = new XToetsRef();
		ret.setResultaatstructuur(curToets.getResultaatstructuur());
		while (curToets != null)
		{
			ret.getToetscodes().add(0, curToets.getCode());
			curToets = curToets.getParent();
		}
		return ret;
	}

	@Override
	public Toets unmarshal(XToetsRef v)
	{
		if (v == null)
			return null;

		Toets ret = v.getResultaatstructuur().getEindresultaat();
		for (String curCode : v.getToetscodes().subList(1, v.getToetscodes().size()))
		{
			ret = ret.getChild(curCode);
			if (ret == null)
				throw new IllegalArgumentException("De resultaatstructuur heeft geen toets voor '"
					+ v.getToetscodes() + "'");
		}
		return ret;
	}
}
