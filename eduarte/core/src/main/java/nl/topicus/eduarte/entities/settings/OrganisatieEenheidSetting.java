/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.settings;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;

import org.hibernate.annotations.Index;

/**
 * Setting per organisatie eenheid.
 * 
 * @author marrink
 * @param <T>
 */
@Entity()
public abstract class OrganisatieEenheidSetting<T> extends OrganisatieSetting<T>
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organisatieEenheid", nullable = true)
	@Basic(optional = false)
	@Index(name = "idx_OrganisatieS_organisatieE")
	private OrganisatieEenheid organisatieEenheid;

	public OrganisatieEenheidSetting()
	{
	}

	public OrganisatieEenheidSetting(OrganisatieEenheid eenheid)
	{
		setOrganisatieEenheid(eenheid);
	}

	/**
	 * @return Returns the organisatieEenheid.
	 */
	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return organisatieEenheid;
	}

	/**
	 * @param organisatieEenheid
	 *            The organisatieEenheid to set.
	 */
	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		this.organisatieEenheid = organisatieEenheid;
	}

}
