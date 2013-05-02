package nl.topicus.eduarte.zoekfilters.dbs;

import nl.topicus.eduarte.entities.dbs.trajecten.TrajectSoort;
import nl.topicus.eduarte.entities.dbs.trajecten.TrajectStatusSoort;
import nl.topicus.eduarte.zoekfilters.AbstractZoekFilter;

import org.apache.wicket.model.IModel;

public class TrajectStatusSoortZoekFilter extends AbstractZoekFilter<TrajectStatusSoort>
{
	private static final long serialVersionUID = 1L;

	private IModel<TrajectSoort> trajectSoort;

	private Boolean actief;

	public TrajectStatusSoortZoekFilter()
	{
		addOrderByProperty("omschrijving");
	}

	public TrajectSoort getTrajectSoort()
	{
		return getModelObject(trajectSoort);
	}

	public void setTrajectSoort(TrajectSoort trajectSoort)
	{
		this.trajectSoort = makeModelFor(trajectSoort);
	}

	public void setTrajectSoortModel(IModel<TrajectSoort> trajectSoortModel)
	{
		this.trajectSoort = trajectSoortModel;
	}

	public Boolean getActief()
	{
		return actief;
	}

	public void setActief(Boolean actief)
	{
		this.actief = actief;
	}
}
