/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import java.util.Collection;
import java.util.List;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.SoortExterneOrganisatie;
import nl.topicus.eduarte.entities.personen.PersoonExterneOrganisatie;
import nl.topicus.eduarte.entities.personen.RelatieSoort;
import nl.topicus.eduarte.web.components.choice.dropdownchecklist.SoortExterneOrganisatieDropDownCheckList;

import org.apache.wicket.model.IModel;

/**
 * @author idserda
 */
public class PersoonExterneOrganisatieZoekFilter extends
		AbstractZoekFilter<PersoonExterneOrganisatie>
{
	private static final long serialVersionUID = 1L;

	private String naam;

	private String afkorting;

	private String plaats;

	private String postcode;

	private IModel<RelatieSoort> relatieSoort;

	private Long persoonIdNot;

	@AutoForm(editorClass = SoortExterneOrganisatieDropDownCheckList.class)
	private IModel<List<SoortExterneOrganisatie>> soortExterneOrganisaties;

	private Collection<Long> idNotIn;

	private IModel<ExterneOrganisatie> relatie;

	public PersoonExterneOrganisatieZoekFilter()
	{
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public String getNaam()
	{
		return naam;
	}

	public void setAfkorting(String afkorting)
	{
		this.afkorting = afkorting;
	}

	public String getAfkorting()
	{
		return afkorting;
	}

	public void setPlaats(String plaats)
	{
		this.plaats = plaats;
	}

	public String getPlaats()
	{
		return plaats;
	}

	public void setPostcode(String postcode)
	{
		this.postcode = postcode;
	}

	public String getPostcode()
	{
		return postcode;
	}

	public boolean heeftAdresCriteria()
	{
		return (postcode != null || plaats != null);
	}

	public void setPersoonIdNot(Long persoonIdNot)
	{
		this.persoonIdNot = persoonIdNot;
	}

	public Long getPersoonIdNot()
	{
		return persoonIdNot;
	}

	public void setIdNotIn(Collection<Long> idNotIn)
	{
		this.idNotIn = idNotIn;

	}

	public Collection<Long> getIdNotIn()
	{
		return idNotIn;
	}

	public void setSoortExterneOrganisaties(List<SoortExterneOrganisatie> soortExterneOrganisaties)
	{
		this.soortExterneOrganisaties = makeModelFor(soortExterneOrganisaties);
	}

	public List<SoortExterneOrganisatie> getSoortExterneOrganisaties()
	{
		return getModelObject(soortExterneOrganisaties);
	}

	public void setRelatieSoort(RelatieSoort relatieSoort)
	{
		this.relatieSoort = makeModelFor(relatieSoort);
	}

	public RelatieSoort getRelatieSoort()
	{
		return getModelObject(relatieSoort);
	}

	public ExterneOrganisatie getRelatie()
	{
		return getModelObject(relatie);
	}

	public void setRelatie(ExterneOrganisatie relatie)
	{
		this.relatie = makeModelFor(relatie);
	}

}
