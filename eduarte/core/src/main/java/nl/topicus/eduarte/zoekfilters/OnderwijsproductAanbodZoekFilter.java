package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAanbod;

import org.apache.wicket.model.IModel;

/**
 * Zoekfilter voor onderwijsproductaanbod.
 * 
 * @author vandekamp
 */
public class OnderwijsproductAanbodZoekFilter extends AbstractZoekFilter<OnderwijsproductAanbod>
{
	private static final long serialVersionUID = 1L;

	private IModel<Onderwijsproduct> onderwijsproduct;

	public OnderwijsproductAanbodZoekFilter()
	{
	}

	public Onderwijsproduct getOnderwijsproduct()
	{
		return getModelObject(onderwijsproduct);
	}

	public void setOnderwijsproduct(Onderwijsproduct onderwijsproduct)
	{
		this.onderwijsproduct = makeModelFor(onderwijsproduct);
	}
}
