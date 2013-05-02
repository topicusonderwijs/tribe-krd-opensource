/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import nl.topicus.cobra.web.components.choice.ActiefCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.SoortOrganisatieEenheid;

/**
 * @author loite
 */
public class SoortOrganisatieEenheidZoekFilter extends AbstractZoekFilter<SoortOrganisatieEenheid>
		implements ICodeNaamActiefZoekFilter<SoortOrganisatieEenheid>

{
	private static final long serialVersionUID = 1L;

	private String code;

	private String naam;

	@AutoForm(editorClass = ActiefCombobox.class)
	private Boolean actief;

	public SoortOrganisatieEenheidZoekFilter()
	{
	}

	/**
	 * @return Returns the code.
	 */
	@Override
	public String getCode()
	{
		return code;
	}

	/**
	 * @param code
	 *            The afkorting to set.
	 */
	@Override
	public void setCode(String code)
	{
		this.code = code;
	}

	/**
	 * @return Returns the naam.
	 */
	@Override
	public String getNaam()
	{
		return naam;
	}

	/**
	 * @param naam
	 *            The naam to set.
	 */
	@Override
	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	/**
	 * @return Returns the actief.
	 */
	@Override
	public Boolean getActief()
	{
		return actief;
	}

	/**
	 * @param actief
	 *            The actief to set.
	 */
	@Override
	public void setActief(Boolean actief)
	{
		this.actief = actief;
	}

	@Override
	public Class<SoortOrganisatieEenheid> getEntityClass()
	{
		return SoortOrganisatieEenheid.class;
	}
}
