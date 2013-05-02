/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.inschrijving;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;
import nl.topicus.onderwijs.duo.bron.Bron;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

/**
 * @author idserda
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Table(appliesTo = "Bekostigingsperiode", indexes = {
	@Index(name = "GENERATED_NAME_beg_eind_arch", columnNames = {"organisatie", "gearchiveerd",
		"einddatumNotNull", "begindatum"}),
	@Index(name = "GENERATED_NAME_beg_eind", columnNames = {"organisatie", "einddatumNotNull",
		"begindatum"})})
public class Bekostigingsperiode extends BeginEinddatumInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "verbintenis")
	@Index(name = "idx_Bekostp_verbintenis")
	private Verbintenis verbintenis;

	@Column(nullable = false)
	@Bron
	private boolean bekostigd;

	@SuppressWarnings("unchecked")
	@Bron
	@Transient
	private Enum handmatigVersturenNaarBronMutatie = null;

	public Bekostigingsperiode()
	{
	}

	/**
	 * @param verbintenis
	 *            The verbintenis to set.
	 */
	public void setVerbintenis(Verbintenis verbintenis)
	{
		this.verbintenis = verbintenis;
	}

	/**
	 * @return Returns the verbintenis.
	 */
	public Verbintenis getVerbintenis()
	{
		return verbintenis;
	}

	/**
	 * @param bekostigd
	 *            The bekostigd to set.
	 */
	public void setBekostigd(boolean bekostigd)
	{
		this.bekostigd = bekostigd;
	}

	/**
	 * @return Returns the bekostigd.
	 */
	public boolean isBekostigd()
	{
		return bekostigd;
	}

	public void setHandmatigVersturenNaarBron(Enum< ? > soortMutatie)
	{
		this.handmatigVersturenNaarBronMutatie = soortMutatie;
	}

	public nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie getHandmatigeBronBveSoortMutatie()
	{
		if (handmatigVersturenNaarBronMutatie instanceof nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie)
		{
			return (nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie) handmatigVersturenNaarBronMutatie;
		}
		return null;
	}

	public nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie getHandmatigeBronVoSoortMutatie()
	{
		if (handmatigVersturenNaarBronMutatie instanceof nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie)
		{
			return (nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie) handmatigVersturenNaarBronMutatie;
		}
		return null;
	}

	public boolean isHandmatigVersturenNaarBron()
	{
		return handmatigVersturenNaarBronMutatie != null;
	}

}
