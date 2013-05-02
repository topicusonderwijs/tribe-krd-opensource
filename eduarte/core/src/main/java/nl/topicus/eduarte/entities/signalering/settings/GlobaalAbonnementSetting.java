/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.signalering.settings;

import javax.persistence.Entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entiteit voor het instellen van signalering op instellingsniveau. Alle medewerkers van
 * de instelling krijgen automatisch deze signaleringsinstellingen. Een instelling kan een
 * defaultwaarde zijn, die door een medewerker overschreven mag worden, of het kan een
 * verplichte waarde zijn.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class GlobaalAbonnementSetting extends EventAbonnementSetting
{
	private static final long serialVersionUID = 1L;

	public GlobaalAbonnementSetting()
	{
	}
}
