package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.resultaatstructuur.StandaardToetsCodeFilter;

import org.apache.wicket.model.IModel;

public class StandaardToetsCodeFilterZoekFilter extends
		AbstractZoekFilter<StandaardToetsCodeFilter>
{
	private static final long serialVersionUID = 1L;

	private IModel<Opleiding> opleiding;

	public StandaardToetsCodeFilterZoekFilter()
	{
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = makeModelFor(opleiding);
	}

	public Opleiding getOpleiding()
	{
		return getModelObject(opleiding);
	}
}
