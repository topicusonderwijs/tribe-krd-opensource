package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.hogeronderwijs.Hoofdfase;
import nl.topicus.eduarte.entities.hogeronderwijs.OpleidingFase;
import nl.topicus.eduarte.entities.hogeronderwijs.OpleidingsVorm;
import nl.topicus.eduarte.entities.opleiding.Opleiding;

import org.apache.wicket.model.IModel;

public class OpleidingFaseZoekFilter extends AbstractZoekFilter<OpleidingFase>
{
	private static final long serialVersionUID = 1L;

	private IModel<Opleiding> opleiding;

	private Hoofdfase hoofdfase;

	private OpleidingsVorm opleidingsvorm;

	public void setHoofdfase(Hoofdfase hoofdfase)
	{
		this.hoofdfase = hoofdfase;
	}

	public Hoofdfase getHoofdfase()
	{
		return hoofdfase;
	}

	public void setOpleidingsvorm(OpleidingsVorm opleidingsvorm)
	{
		this.opleidingsvorm = opleidingsvorm;
	}

	public OpleidingsVorm getOpleidingsvorm()
	{
		return opleidingsvorm;
	}

	public Opleiding getOpleiding()
	{
		return getModelObject(opleiding);
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = makeModelFor(opleiding);
	}
}
