package nl.topicus.eduarte.zoekfilters.dbs;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.dbs.trajecten.TrajectSoort;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;
import nl.topicus.eduarte.zoekfilters.IActiefZoekFilter;

public class TrajectSoortZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<TrajectSoort> implements
		IActiefZoekFilter<TrajectSoort>
{
	private static final long serialVersionUID = 1L;

	@AutoForm(htmlClasses = "unit_160")
	private String naam;

	private Boolean actief;

	public TrajectSoortZoekFilter()
	{
		addOrderByProperty("naam");
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public String getNaam()
	{
		return naam;
	}

	@Override
	public Boolean getActief()
	{
		return actief;
	}

	@Override
	public void setActief(Boolean actief)
	{
		this.actief = actief;
	}
}
