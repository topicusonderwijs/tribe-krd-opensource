/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.personen;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.cobra.web.components.form.AutoFormEmbedded;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@DiscriminatorValue("P")
public class Relatie extends AbstractRelatie
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "verzorger", nullable = true)
	@Index(name = "idx_Relatie_verzorger")
	@AutoFormEmbedded
	private Persoon relatie;

	public Relatie()
	{
	}

	@Override
	public Persoon getRelatie()
	{
		return relatie;
	}

	public void setRelatie(Persoon verzorger)
	{
		this.relatie = verzorger;
	}

	public PersoonAdres getEersteAdresOpPeildatum()
	{
		if (getRelatie() == null)
			return null;

		return getRelatie().getEerstePersoonAdresOpPeildatum();
	}

	public Geslacht getGeslacht()
	{
		if (getRelatie() == null)
			return null;

		return getRelatie().getGeslacht();
	}

	public String getNaam()
	{
		if (getRelatie() == null)
			return null;

		return getRelatie().getFormeleNaam();
	}
}
