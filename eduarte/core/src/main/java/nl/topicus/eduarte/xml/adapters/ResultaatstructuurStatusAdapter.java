package nl.topicus.eduarte.xml.adapters;

import nl.topicus.cobra.xml.adapters.AbstractEnumAdapter;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur.Status;

public class ResultaatstructuurStatusAdapter extends AbstractEnumAdapter<Status>
{
	public ResultaatstructuurStatusAdapter()
	{
		super(Status.class);
	}

	@Override
	public String marshal(Status v)
	{
		switch (v)
		{
			case FOUTIEF:
			case IN_HERBEREKENING:
				return super.marshal(Status.IN_ONDERHOUD);
			default:
				return super.marshal(v);
		}
	}
}
