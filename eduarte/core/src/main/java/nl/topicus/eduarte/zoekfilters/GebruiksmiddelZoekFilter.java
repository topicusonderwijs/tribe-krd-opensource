package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.onderwijsproduct.Gebruiksmiddel;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;

import org.apache.wicket.model.IModel;

/**
 * Zoekfilter voor gebruiksmiddelen.
 * 
 * @author loite
 */
public class GebruiksmiddelZoekFilter extends AbstractCodeNaamActiefZoekFilter<Gebruiksmiddel>
{
	private static final long serialVersionUID = 1L;

	private IModel<Onderwijsproduct> onderwijsproduct;

	private boolean alleenOngekoppeld;

	public GebruiksmiddelZoekFilter()
	{
		super(Gebruiksmiddel.class);
	}

	public Onderwijsproduct getOnderwijsproduct()
	{
		return getModelObject(onderwijsproduct);
	}

	public void setOnderwijsproduct(Onderwijsproduct onderwijsproduct)
	{
		this.onderwijsproduct = makeModelFor(onderwijsproduct);
	}

	public void setAlleenOngekoppeld(boolean alleenOngekoppeld)
	{
		this.alleenOngekoppeld = alleenOngekoppeld;
	}

	public boolean isAlleenOngekoppeld()
	{
		return alleenOngekoppeld;
	}
}
