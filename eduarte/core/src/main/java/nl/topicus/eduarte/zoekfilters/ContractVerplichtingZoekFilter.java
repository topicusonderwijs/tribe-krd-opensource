package nl.topicus.eduarte.zoekfilters;

import java.util.Date;

import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.contract.ContractVerplichting;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.personen.Medewerker;

import org.apache.wicket.model.IModel;

/**
 * 
 * 
 * @author vanharen
 */
public class ContractVerplichtingZoekFilter extends AbstractZoekFilter<ContractVerplichting>
		implements ICodeNaamZoekFilter<ContractVerplichting>
{

	private static final long serialVersionUID = 1L;

	private ContractZoekFilter contractFilter;

	private String naam;

	private String code;

	private String omschrijving;

	@AutoForm(editorClass = JaNeeCombobox.class)
	private Boolean uitgevoerd;

	private Date uitgevoerdOp;

	private Date beginDatum;

	private Date deadline;

	private IModel<Medewerker> medewerker;

	public ContractVerplichtingZoekFilter()
	{
		setContractZoekFilter(new ContractZoekFilter());
	}

	public ContractZoekFilter getContractZoekFilter()
	{
		return contractFilter;
	}

	public void setContractZoekFilter(ContractZoekFilter contractFilter)
	{
		this.contractFilter = contractFilter;
	}

	public Medewerker getBeheerder()
	{
		return contractFilter.getBeheerder();
	}

	public void setBeheerder(Medewerker beheerder)
	{
		contractFilter.setBeheerder(beheerder);
	}

	public ContractZoekFilter getContractFilter()
	{
		return contractFilter;
	}

	public void setContractFilter(ContractZoekFilter contractFilter)
	{
		this.contractFilter = contractFilter;
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public Boolean getUitgevoerd()
	{
		return uitgevoerd;
	}

	public void setUitgevoerd(Boolean uitgevoerd)
	{
		this.uitgevoerd = uitgevoerd;
	}

	public Date getUitgevoerdOp()
	{
		return uitgevoerdOp;
	}

	public void setUitgevoerdOp(Date uitgevoerdOp)
	{
		this.uitgevoerdOp = uitgevoerdOp;
	}

	public Date getBeginDatum()
	{
		return beginDatum;
	}

	public void setBeginDatum(Date beginDatum)
	{
		this.beginDatum = beginDatum;
	}

	public Date getDeadline()
	{
		return deadline;
	}

	public void setDeadline(Date deadline)
	{
		this.deadline = deadline;
	}

	@Override
	public String getNaam()
	{
		return naam;
	}

	@Override
	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	@Override
	public String getCode()
	{
		return code;
	}

	@Override
	public void setCode(String code)
	{
		this.code = code;
	}

	public Medewerker getMedewerker()
	{
		return getModelObject(medewerker);
	}

	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerker = makeModelFor(medewerker);
	}

	public ExterneOrganisatie getExterneOrganisatie()
	{
		return contractFilter.getExterneOrganisatie();
	}

	public void setExterneOrganisatie(ExterneOrganisatie externeOrganisatie)
	{
		this.contractFilter.setExterneOrganisatie(externeOrganisatie);
	}

	public boolean hasSearchCredentials()
	{
		return getExterneOrganisatie() != null || getBeginDatum() != null;
	}

	@Override
	public Class<ContractVerplichting> getEntityClass()
	{
		return ContractVerplichting.class;
	}
}
