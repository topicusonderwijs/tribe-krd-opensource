package nl.topicus.eduarte.participatie.zoekfilters;

import java.util.Date;
import java.util.List;

import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.participatie.Waarneming;
import nl.topicus.eduarte.entities.participatie.enums.AfspraakTypeCategory;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * @author vandekamp
 */
public class TotalenPerGroepZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<Waarneming>
{
	private static final long serialVersionUID = 1L;

	private Date beginDatum;

	private Date eindDatum;

	private List<AfspraakTypeCategory> afspraakTypeCategories;

	private IModel<Contract> contract;

	public TotalenPerGroepZoekFilter()
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

	public void setFinancieringsLabel(Contract contract)
	{
		this.contract = makeModelFor(contract);
	}
}
