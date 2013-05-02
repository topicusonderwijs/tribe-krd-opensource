package nl.topicus.eduarte.participatie.zoekfilters;

import nl.topicus.eduarte.entities.participatie.DeelnemerPersoonlijkeGroep;
import nl.topicus.eduarte.entities.participatie.PersoonlijkeGroep;
import nl.topicus.eduarte.zoekfilters.AbstractZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * @author loite
 */
public class DeelnemerPersoonlijkeGroepZoekFilter extends
		AbstractZoekFilter<DeelnemerPersoonlijkeGroep>
{
	private static final long serialVersionUID = 1L;

	private IModel<PersoonlijkeGroep> persoonlijkeGroep;

	public DeelnemerPersoonlijkeGroepZoekFilter(PersoonlijkeGroep persoonlijkeGroep)
	{
		setPersoonlijkeGroep(persoonlijkeGroep);
	}

	public PersoonlijkeGroep getPersoonlijkeGroep()
	{
		return getModelObject(persoonlijkeGroep);
	}

	public void setPersoonlijkeGroep(PersoonlijkeGroep persoonlijkeGroep)
	{
		this.persoonlijkeGroep = makeModelFor(persoonlijkeGroep);
	}
}
