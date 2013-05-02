/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import nl.topicus.cobra.web.components.choice.ActiefCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoonRol;

public class ExterneOrganisatieContactPersoonRolZoekFilter extends
		AbstractZoekFilter<ExterneOrganisatieContactPersoonRol> implements
		INaamActiefZoekFilter<ExterneOrganisatieContactPersoonRol>
{
	private static final long serialVersionUID = 1L;

	private String naam;

	private Boolean praktijkopleiderBPV;

	private Boolean contactpersoonBPV;

	@AutoForm(editorClass = ActiefCombobox.class)
	private Boolean actief;

	public ExterneOrganisatieContactPersoonRolZoekFilter()
	{
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public Boolean getPraktijkopleiderBPV()
	{
		return praktijkopleiderBPV;
	}

	public void setPraktijkopleiderBPV(Boolean praktijkopleiderBPV)
	{
		this.praktijkopleiderBPV = praktijkopleiderBPV;
	}

	public Boolean getContactpersoonBPV()
	{
		return contactpersoonBPV;
	}

	public void setContactpersoonBPV(Boolean contactpersoonBPV)
	{
		this.contactpersoonBPV = contactpersoonBPV;
	}

	public void setActief(Boolean actief)
	{
		this.actief = actief;
	}

	public Boolean getActief()
	{
		return actief;
	}
}
