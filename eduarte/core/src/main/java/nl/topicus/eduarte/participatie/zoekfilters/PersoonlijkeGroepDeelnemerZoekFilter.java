package nl.topicus.eduarte.participatie.zoekfilters;

import java.util.Date;

import nl.topicus.eduarte.entities.participatie.PersoonlijkeGroep;
import nl.topicus.eduarte.entities.participatie.PersoonlijkeGroepDeelnemer;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;

import org.apache.wicket.model.IModel;

public class PersoonlijkeGroepDeelnemerZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<PersoonlijkeGroepDeelnemer>
{
	private static final long serialVersionUID = 1L;

	private IModel<PersoonlijkeGroep> persoonlijkeGroep;

	private boolean alleenDeelnemers;

	public PersoonlijkeGroepDeelnemerZoekFilter()
	{
	}

	public PersoonlijkeGroepDeelnemerZoekFilter(PersoonlijkeGroep persoonlijkeGroep, Date peildatum)
	{
		super();
		setPersoonlijkeGroep(persoonlijkeGroep);
		setPeildatum(peildatum);
	}

	public PersoonlijkeGroep getPersoonlijkeGroep()
	{
		return getModelObject(persoonlijkeGroep);
	}

	public void setPersoonlijkeGroep(PersoonlijkeGroep persoonlijkeGroep)
	{
		this.persoonlijkeGroep = makeModelFor(persoonlijkeGroep);
	}

	public boolean isAlleenDeelnemers()
	{
		return alleenDeelnemers;
	}

	public void setAlleenDeelnemers(boolean alleenDeelnemers)
	{
		this.alleenDeelnemers = alleenDeelnemers;
	}
}
