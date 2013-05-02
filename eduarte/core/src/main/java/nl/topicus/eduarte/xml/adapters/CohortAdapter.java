package nl.topicus.eduarte.xml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import nl.topicus.eduarte.entities.landelijk.Cohort;

public class CohortAdapter extends XmlAdapter<String, Cohort>
{

	@Override
	public Cohort unmarshal(String value)
	{
		if (value == null)
			return null;

		return Cohort.asCohort(value);
	}

	@Override
	public String marshal(Cohort value)
	{
		if (value == null)
			return null;

		return value.reget(Cohort.class).toString().replace('/', '-');
	}
}
