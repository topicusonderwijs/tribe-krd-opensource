package nl.topicus.eduarte.xml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import nl.topicus.eduarte.entities.resultaatstructuur.Schaalwaarde;
import nl.topicus.eduarte.xml.resultaatstructuur.v10.XSchaalwaardeRef;

public class SchaalwaardeAdapter extends XmlAdapter<XSchaalwaardeRef, Schaalwaarde>
{
	public SchaalwaardeAdapter()
	{
	}

	@Override
	public XSchaalwaardeRef marshal(Schaalwaarde v)
	{
		if (v == null)
			return null;

		Schaalwaarde value = v.reget(Schaalwaarde.class);
		XSchaalwaardeRef ret = new XSchaalwaardeRef();
		ret.setSchaal(value.getSchaal());
		ret.setWaarde(value.getInterneWaarde());
		return ret;
	}

	@Override
	public Schaalwaarde unmarshal(XSchaalwaardeRef v)
	{
		if (v == null)
			return null;

		for (Schaalwaarde curWaarde : v.getSchaal().getSchaalwaarden())
			if (curWaarde.getInterneWaarde().equals(v.getWaarde()))
				return curWaarde;
		throw new IllegalArgumentException("Schaal '" + v.getSchaal().getNaam()
			+ "' heeft geen waarde '" + v.getWaarde() + "'");
	}
}
