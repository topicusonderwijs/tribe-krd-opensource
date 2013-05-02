package nl.topicus.eduarte.participatie.zoekfilters;

import java.util.Date;

import nl.topicus.eduarte.dao.participatie.hibernate.AfspraakHibernateDataAccessHelper.IIVOTijd;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * @author loite
 */
public class IIVOTijdZoekFilter extends AbstractOrganisatieEenheidLocatieZoekFilter<IIVOTijd>
{
	private static final long serialVersionUID = 1L;

	private Date begindatum;

	private Date einddatum;

	private IModel<Opleiding> opleiding;

	private IModel<Deelnemer> deelnemer;

	public IIVOTijdZoekFilter()
	{
	}

	public Date getBegindatum()
	{
		return begindatum;
	}

	public void setBegindatum(Date begindatum)
	{
		this.begindatum = begindatum;
	}

	public Date getEinddatum()
	{
		return einddatum;
	}

	public void setEinddatum(Date einddatum)
	{
		this.einddatum = einddatum;
	}

	public Opleiding getOpleiding()
	{
		return getModelObject(opleiding);
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = makeModelFor(opleiding);
	}

	public Deelnemer getDeelnemer()
	{
		return getModelObject(deelnemer);
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = makeModelFor(deelnemer);
	}
}
