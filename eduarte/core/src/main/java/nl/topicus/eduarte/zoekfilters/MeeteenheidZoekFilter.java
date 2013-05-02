package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Meeteenheid;

import org.apache.wicket.model.IModel;

/**
 * @author vandenbrink
 */
public class MeeteenheidZoekFilter extends AbstractLandelijkOfInstellingZoekFilter<Meeteenheid>
{
	private static final long serialVersionUID = 1L;

	private String naam;

	private String omschrijving;

	private IModel<Cohort> cohort;

	private IModel<Opleiding> opleiding;

	public MeeteenheidZoekFilter()
	{
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public Cohort getCohort()
	{
		return cohort.getObject();
	}

	public void setCohort(Cohort cohort)
	{
		this.cohort = makeModelFor(cohort);
	}

	public Opleiding getOpleiding()
	{
		return opleiding.getObject();
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = makeModelFor(opleiding);
	}

}
