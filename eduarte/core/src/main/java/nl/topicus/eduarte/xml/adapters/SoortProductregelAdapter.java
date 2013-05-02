package nl.topicus.eduarte.xml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.SoortProductregelDataAccessHelper;
import nl.topicus.eduarte.entities.productregel.SoortProductregel;
import nl.topicus.eduarte.xml.onderwijscatalogus.v10.XSoortProductregelRef;

public class SoortProductregelAdapter extends XmlAdapter<XSoortProductregelRef, SoortProductregel>
{
	public SoortProductregelAdapter()
	{
	}

	@Override
	public XSoortProductregelRef marshal(SoortProductregel v)
	{
		if (v == null)
			return null;

		SoortProductregel value = v.reget(SoortProductregel.class);
		XSoortProductregelRef ret = new XSoortProductregelRef();
		ret.setTaxonomie(value.getTaxonomie());
		ret.setNaam(value.getNaam());
		return ret;
	}

	@Override
	public SoortProductregel unmarshal(XSoortProductregelRef v)
	{
		if (v == null)
			return null;

		SoortProductregelDataAccessHelper helper =
			DataAccessRegistry.getHelper(SoortProductregelDataAccessHelper.class);
		SoortProductregel ret = helper.get(v.getTaxonomie(), v.getNaam());
		if (ret == null)
			throw new IllegalArgumentException("Onbekende soort productregel '"
				+ v.getTaxonomie().getTaxonomiecode() + " - " + v.getNaam() + "'");
		return ret;
	}
}
