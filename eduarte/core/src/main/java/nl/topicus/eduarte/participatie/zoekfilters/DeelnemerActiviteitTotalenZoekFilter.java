package nl.topicus.eduarte.participatie.zoekfilters;

import java.util.Date;
import java.util.List;

import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.participatie.Afspraak;
import nl.topicus.eduarte.entities.participatie.LesweekIndeling;
import nl.topicus.eduarte.entities.participatie.enums.AfspraakTypeCategory;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.web.components.panels.templates.RapportageConfiguratieFactory;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * @author vandekamp
 */
public class DeelnemerActiviteitTotalenZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<Afspraak> implements
		RapportageConfiguratieFactory<Verbintenis>
{
	private static final long serialVersionUID = 1L;

	private IModel<Deelnemer> deelnemer;

	private IModel<LesweekIndeling> lesweekIndeling;

	private Date beginDatum;

	private Date eindDatum;

	private List<AfspraakTypeCategory> afspraakTypeCategories;

	private Boolean activiteitenTonen;

	private IModel<Contract> contract;

	public DeelnemerActiviteitTotalenZoekFilter()
	{

	}

	public Date getBeginDatum()
	{
		return beginDatum;
	}

	public void setBeginDatum(Date beginDatum)
	{
		this.beginDatum = beginDatum;
	}

	public Date getEindDatum()
	{
		return eindDatum;
	}

	public void setEindDatum(Date eindDatum)
	{
		this.eindDatum = eindDatum;
	}

	public Boolean isActiviteitenTonen()
	{
		return activiteitenTonen;
	}

	public void setActiviteitenTonen(Boolean activiteitenTonen)
	{
		this.activiteitenTonen = activiteitenTonen;
	}

	public List<AfspraakTypeCategory> getAfspraakTypeCategories()
	{
		return afspraakTypeCategories;
	}

	public void setAfspraakTypeCategories(List<AfspraakTypeCategory> afspraakTypeCategories)
	{
		this.afspraakTypeCategories = afspraakTypeCategories;
	}

	public Contract getContract()
	{
		return getModelObject(contract);
	}

	public void setContract(Contract contract)
	{
		this.contract = makeModelFor(contract);
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = makeModelFor(deelnemer);
	}

	public Deelnemer getDeelnemer()
	{
		return getModelObject(deelnemer);
	}

	public void setLesweekIndeling(LesweekIndeling lesweekIndeling)
	{
		this.lesweekIndeling = makeModelFor(lesweekIndeling);
	}

	public LesweekIndeling getLesweekIndeling()
	{
		return getModelObject(lesweekIndeling);
	}

	@Override
	public Object createConfiguratie(Verbintenis contextObject)
	{
		return this;
	}
}
