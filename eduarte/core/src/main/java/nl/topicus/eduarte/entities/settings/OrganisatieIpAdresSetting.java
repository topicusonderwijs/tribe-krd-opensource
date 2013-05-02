/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.settings;

import javax.persistence.Embedded;
import javax.persistence.Entity;

import nl.topicus.cobra.web.components.form.AutoFormEmbedded;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author idserda
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class OrganisatieIpAdresSetting extends OrganisatieSetting<OrganisatieIpAdresConfiguration>
{
	private static final long serialVersionUID = 1L;

	@Embedded
	@AutoFormEmbedded
	private OrganisatieIpAdresConfiguration value;

	public OrganisatieIpAdresSetting()
	{
		value = new OrganisatieIpAdresConfiguration();
	}

	@Override
	public String getOmschrijving()
	{
		return "Toegang beperken";
	}

	@Override
	public OrganisatieIpAdresConfiguration getValue()
	{
		return value;
	}

	@Override
	public void setValue(OrganisatieIpAdresConfiguration value)
	{
		this.value = value;
	}
}
