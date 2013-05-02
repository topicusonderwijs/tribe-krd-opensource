/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.participatie.settings;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

import nl.topicus.eduarte.entities.settings.OrganisatieSetting;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Setting om per organisatie de OneindigeAbsentieMeldingen toe te staan.
 * 
 * @author marrink, ambrosius
 */
@Entity(name = "DpOneindigeMeldingSetting")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class DeelnemerportaalOneindigeAbsentieMeldingEnabledSetting extends
		OrganisatieSetting<Boolean>
{
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@Column(name = "booleanValue", nullable = true)
	private Boolean value = false;

	/**
	 * 
	 */
	public DeelnemerportaalOneindigeAbsentieMeldingEnabledSetting()
	{
	}

	@Override
	public String getOmschrijving()
	{
		return "Sta voor deelnemers oneindige absentiemeldingen toe";
	}

	@Override
	public Boolean getValue()
	{
		return value;
	}

	@Override
	public void setValue(Boolean value)
	{
		this.value = value;
	}

}
