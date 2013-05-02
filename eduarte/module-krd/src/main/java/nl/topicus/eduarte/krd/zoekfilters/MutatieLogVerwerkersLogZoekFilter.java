package nl.topicus.eduarte.krd.zoekfilters;

import nl.topicus.eduarte.entities.organisatie.Organisatie;
import nl.topicus.eduarte.krd.entities.mutatielog.MutatieLogVerwerker;
import nl.topicus.eduarte.krd.entities.mutatielog.MutatieLogVerwerkersLog;
import nl.topicus.eduarte.zoekfilters.AbstractZoekFilter;

import org.apache.wicket.model.IModel;

public class MutatieLogVerwerkersLogZoekFilter extends AbstractZoekFilter<MutatieLogVerwerkersLog>
{
	private static final long serialVersionUID = 1L;

	private Boolean actief;

	private IModel<MutatieLogVerwerker> verwerker;

	private IModel<Organisatie> organisatie;

	public MutatieLogVerwerkersLogZoekFilter()
	{
	}

	public Boolean isActief()
	{
		return actief;
	}

	public void setActief(Boolean actief)
	{
		this.actief = actief;
	}

	public MutatieLogVerwerker getVerwerker()
	{
		return getModelObject(verwerker);
	}

	public void setVerwerker(MutatieLogVerwerker verwerker)
	{
		this.verwerker = makeModelFor(verwerker);
	}

	public void setOrganisatie(Organisatie organisatie)
	{
		this.organisatie = makeModelFor(organisatie);
	}

	public Organisatie getOrganisatie()
	{
		return getModelObject(organisatie);
	}
}
