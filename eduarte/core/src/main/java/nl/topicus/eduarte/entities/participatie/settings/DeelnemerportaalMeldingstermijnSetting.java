/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.participatie.settings;

import javax.persistence.Column;
import javax.persistence.Entity;

import nl.topicus.eduarte.entities.settings.OrganisatieSetting;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Houd per organisatie bij wat de termijn is waarop deelnemers nog absentiemeldingen
 * kunnen aanmaken, nadat de afsrpaak geweest is
 * 
 * @author marrink, ambrosius
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class DeelnemerportaalMeldingstermijnSetting extends OrganisatieSetting<Integer>
{
	private static final long serialVersionUID = 1L;

	@Column(name = "intValue", nullable = true)
	private Integer value;

	/**
	 * Hibernate constructor
	 */
	public DeelnemerportaalMeldingstermijnSetting()
	{
		setValue(0);
	}

	@Override
	public String getOmschrijving()
	{
		return "Meldingstermijnsetting";
	}

	@Override
	public Integer getValue()
	{
		return value;
	}

	@Override
	public void setValue(Integer meldingstermijn)
	{
		this.value = meldingstermijn;
		// voor de veiligheid
		if (meldingstermijn != null && meldingstermijn < 0)
			this.value = 0;
	}

}
