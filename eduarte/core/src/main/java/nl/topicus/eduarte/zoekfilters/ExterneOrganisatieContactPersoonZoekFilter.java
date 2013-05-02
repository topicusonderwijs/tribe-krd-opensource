package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoon;

import org.apache.wicket.model.IModel;

public class ExterneOrganisatieContactPersoonZoekFilter extends
		AbstractZoekFilter<ExterneOrganisatieContactPersoon>
{
	private static final long serialVersionUID = 1L;

	private String naam;

	private IModel<ExterneOrganisatie> externeOrganisatie;

	private Boolean praktijkopleiderBPV;

	private Boolean contactpersoonBPV;

	private Boolean heeftAccount;

	public ExterneOrganisatieContactPersoonZoekFilter()
	{
	}

	public ExterneOrganisatieContactPersoonZoekFilter(
			IModel<ExterneOrganisatie> externeOrganisatieModel)
	{
		externeOrganisatie = externeOrganisatieModel;
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public ExterneOrganisatie getExterneOrganisatie()
	{
		return getModelObject(externeOrganisatie);
	}

	public void setExterneOrganisatie(ExterneOrganisatie externeOrganisatie)
	{
		this.externeOrganisatie = makeModelFor(externeOrganisatie);
	}

	public void setExterneOrganisatie(IModel<ExterneOrganisatie> externeOrganisatie)
	{
		this.externeOrganisatie = externeOrganisatie;
	}

	public IModel<ExterneOrganisatie> getExterneOrganisatieModel()
	{
		return externeOrganisatie;
	}

	public void setPraktijkopleiderBPV(Boolean praktijkopleiderBPV)
	{
		this.praktijkopleiderBPV = praktijkopleiderBPV;
	}

	public Boolean getPraktijkopleiderBPV()
	{
		return praktijkopleiderBPV;
	}

	public void setContactpersoonBPV(Boolean contactpersoonBPV)
	{
		this.contactpersoonBPV = contactpersoonBPV;
	}

	public Boolean getContactpersoonBPV()
	{
		return contactpersoonBPV;
	}

	public void setHeeftAccount(Boolean heeftAccount)
	{
		this.heeftAccount = heeftAccount;
	}

	public Boolean getHeeftAccount()
	{
		return heeftAccount;
	}

}
