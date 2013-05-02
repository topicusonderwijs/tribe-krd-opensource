package nl.topicus.eduarte.participatie.zoekfilters;

import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.participatie.Afspraak;
import nl.topicus.eduarte.entities.participatie.AfspraakParticipant;
import nl.topicus.eduarte.entities.participatie.enums.AfspraakRol;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * @author vandekamp
 */
public class AfspraakParticipantZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<AfspraakParticipant>
{
	private static final long serialVersionUID = 1L;

	private AfspraakZoekFilter afspraakZoekFilter;

	private IModel<Afspraak> afspraak;

	private IModel<Groep> groep;

	private IModel<Persoon> persoon;

	private AfspraakRol afspraakRol;

	private Boolean auteur = null;

	public AfspraakParticipantZoekFilter()
	{
	}

	public AfspraakZoekFilter getAfspraakZoekFilter()
	{
		return afspraakZoekFilter;
	}

	public void setAfspraakZoekFilter(AfspraakZoekFilter afspraakZoekFilter)
	{
		this.afspraakZoekFilter = afspraakZoekFilter;
	}

	public Afspraak getAfspraak()
	{
		return getModelObject(afspraak);
	}

	public void setAfspraak(Afspraak afspraak)
	{
		this.afspraak = makeModelFor(afspraak);
	}

	public Groep getGroep()
	{
		return getModelObject(groep);
	}

	public void setGroep(Groep groep)
	{
		this.groep = makeModelFor(groep);
	}

	public Persoon getPersoon()
	{
		return getModelObject(persoon);
	}

	public void setPersoon(Persoon persoon)
	{
		this.persoon = makeModelFor(persoon);
	}

	public AfspraakRol getAfspraakRol()
	{
		return afspraakRol;
	}

	public void setAfspraakRol(AfspraakRol afspraakRol)
	{
		this.afspraakRol = afspraakRol;
	}

	public Boolean getAuteur()
	{
		return auteur;
	}

	public void setAuteur(Boolean auteur)
	{
		this.auteur = auteur;
	}
}
