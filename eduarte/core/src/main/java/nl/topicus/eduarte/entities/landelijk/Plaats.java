/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.landelijk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.eduarte.dao.helpers.PlaatsDataAccessHelper;
import nl.topicus.eduarte.entities.LandelijkEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Plaatsnamen van Nederland
 * 
 * @author hop
 */
@Exportable
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public class Plaats extends LandelijkEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(length = 100, nullable = false)
	@Index(name = "idx_Plaats_naam")
	private String naam;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gemeente", nullable = true)
	@Index(name = "idx_Plaats_gemeente")
	private Gemeente gemeente;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "provincie", nullable = true)
	@Index(name = "idx_Plaats_provincie")
	private Provincie provincie;

	@Column(length = 100, nullable = false)
	@Index(name = "idx_Plaats_sorteernaam")
	private String sorteerNaam;

	@Column(nullable = false)
	private boolean uniek = true;

	@Column(nullable = false)
	private boolean uniekMetProvincie = true;

	/**
	 * Default constructor voor Hibernate.
	 */
	public Plaats()
	{
	}

	public Plaats(String naam)
	{
		this.naam = naam;
	}

	@Exportable
	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	@Override
	public String toString()
	{
		return naam;
	}

	public boolean isUniek()
	{
		return uniek;
	}

	public void setUniek(boolean uniek)
	{
		this.uniek = uniek;
	}

	public boolean isUniekMetProvincie()
	{
		return uniekMetProvincie;
	}

	public void setUniekMetProvincie(boolean uniekMetProvincie)
	{
		this.uniekMetProvincie = uniekMetProvincie;
	}

	private static PlaatsDataAccessHelper getHelper()
	{
		return DataAccessRegistry.getHelper(PlaatsDataAccessHelper.class);
	}

	public static Plaats get(String naam, Gemeente gemeente, Provincie provincie)
	{
		return getHelper().get(naam, provincie, gemeente);
	}

	public void setGemeente(Gemeente gemeente)
	{
		this.gemeente = gemeente;
	}

	public Gemeente getGemeente()
	{
		return gemeente;
	}

	public void setProvincie(Provincie provincie)
	{
		this.provincie = provincie;
	}

	public Provincie getProvincie()
	{
		return provincie;
	}

	public void setSorteerNaam(String sorteerNaam)
	{
		this.sorteerNaam = sorteerNaam;
	}

	public String getSorteerNaam()
	{
		return sorteerNaam;
	}

}
