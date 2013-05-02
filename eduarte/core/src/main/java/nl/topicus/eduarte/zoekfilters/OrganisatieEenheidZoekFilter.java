/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;

import org.apache.wicket.model.IModel;

/**
 * @author loite
 */
public class OrganisatieEenheidZoekFilter extends AbstractZoekFilter<OrganisatieEenheid>

{
	private static final long serialVersionUID = 1L;

	private String afkorting;

	private String naam;

	private Boolean actief;

	private Boolean tonenBijDigitaalAanmelden;

	private IModel<OrganisatieEenheid> onlyChildrenOf;

	public OrganisatieEenheidZoekFilter()
	{
	}

	public String getAfkorting()
	{
		return afkorting;
	}

	public void setAfkorting(String afkorting)
	{
		this.afkorting = afkorting;
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public Boolean getActief()
	{
		return actief;
	}

	public void setActief(Boolean actief)
	{
		this.actief = actief;
	}

	public void setTonenBijDigitaalAanmelden(Boolean tonenBijDigitaalAanmelden)
	{
		this.tonenBijDigitaalAanmelden = tonenBijDigitaalAanmelden;
	}

	public Boolean getTonenBijDigitaalAanmelden()
	{
		return tonenBijDigitaalAanmelden;
	}

	public void setOnlyChildrenOf(OrganisatieEenheid onlyChildrenOf)
	{
		this.onlyChildrenOf = makeModelFor(onlyChildrenOf);
	}

	public OrganisatieEenheid getOnlyChildrenOf()
	{
		return getModelObject(onlyChildrenOf);
	}

}
