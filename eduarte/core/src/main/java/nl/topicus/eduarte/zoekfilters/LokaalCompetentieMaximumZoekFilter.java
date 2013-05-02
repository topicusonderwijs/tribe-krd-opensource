package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.LokaalCompetentieMaximum;

import org.apache.wicket.model.IModel;

/**
 * @author vanharen
 */
public class LokaalCompetentieMaximumZoekFilter extends
		NiveauVerzamelingZoekFilter<LokaalCompetentieMaximum>
{

	public LokaalCompetentieMaximumZoekFilter()
	{
	}

	private static final long serialVersionUID = 1L;

	private IModel<Opleiding> opleiding;

	public Opleiding getOpleiding()
	{
		return getModelObject(opleiding);
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = makeModelFor(opleiding);
	}

}
