package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.Verbruiksmiddel;

import org.apache.wicket.model.IModel;

/**
 * Zoekfilter voor verbruiksmiddelen.
 * 
 * @author loite
 */
public class VerbruiksmiddelZoekFilter extends AbstractCodeNaamActiefZoekFilter<Verbruiksmiddel>
{
	private static final long serialVersionUID = 1L;

	private IModel<Onderwijsproduct> onderwijsproduct;

	private boolean alleenOngekoppeld;

	public VerbruiksmiddelZoekFilter()
	{
		super(Verbruiksmiddel.class);
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
