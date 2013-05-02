package nl.topicus.eduarte.krd.zoekfilters;

import nl.topicus.eduarte.entities.organisatie.Organisatie;
import nl.topicus.eduarte.krd.entities.mutatielog.MutatieLogVerwerker;
import nl.topicus.eduarte.zoekfilters.AbstractZoekFilter;

import org.apache.wicket.model.IModel;

public class MutatieLogVerwerkersZoekFilter extends AbstractZoekFilter<MutatieLogVerwerker>
{
	private static final long serialVersionUID = 1L;

	private boolean nogNietUitgegeven = true;

	private IModel<Organisatie> organisatie;

	public void setNogNietUitgegeven(boolean nogNietUitgegeven)
	{
		this.nogNietUitgegeven = nogNietUitgegeven;
	}

	public boolean isNogNietUitgegeven()
	{
		return nogNietUitgegeven;
	}

	public Organisatie getOrganisatie()
	{
		return getModelObject(organisatie);
	}

	public void setOrganisatie(Organisatie organisatie)
	{
		this.organisatie = makeModelFor(organisatie);
	}
}
