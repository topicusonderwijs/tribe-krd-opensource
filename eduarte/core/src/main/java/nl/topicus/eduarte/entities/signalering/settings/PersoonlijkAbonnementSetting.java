/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.signalering.settings;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.personen.Persoon;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Entiteit voor het vasthouden van de specifieke signaleringsinstellingen van een
 * persoon. Een persoon krijgt eventuele default settings vanuit
 * {@link GlobaalAbonnementSetting}, maar kan deze zelf overschrijven (zolang de
 * waarde op instellingsniveau niet op Verplicht gezet is).
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class PersoonlijkAbonnementSetting extends EventAbonnementSetting
{
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "persoon", nullable = true)
	@Index(name = "idx_SignaleringS_persoon")
	private Persoon persoon;

	public PersoonlijkAbonnementSetting()
	{
	}

	public PersoonlijkAbonnementSetting(Persoon persoon)
	{
		setPersoon(persoon);
	}

	public Persoon getPersoon()
	{
		return persoon;
	}

	public void setPersoon(Persoon persoon)
	{
		this.persoon = persoon;
	}
}
