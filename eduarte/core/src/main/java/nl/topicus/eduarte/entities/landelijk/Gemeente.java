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
import nl.topicus.eduarte.dao.helpers.GemeenteDataAccessHelper;
import nl.topicus.eduarte.entities.ICodeNaamEntiteit;
import nl.topicus.eduarte.entities.IsViewWhenOnNoise;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumLandelijkEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Gemeentes van Nederland
 * 
 * @author hop
 */
@Exportable
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
@IsViewWhenOnNoise
public class Gemeente extends BeginEinddatumLandelijkEntiteit implements ICodeNaamEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(length = 10, nullable = false)
	@Index(name = "idx_Gemeente_code")
	private String code;

	@Column(length = 100, nullable = false)
	@Index(name = "idx_Gemeente_naam")
	private String naam;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nieuweGemeente", nullable = true)
	@Index(name = "idx_Gemeente_nwGemeente")
	private Gemeente nieuweGemeente;

	/**
	 * Default constructor voor Hibernate.
	 */
	public Gemeente()
	{
	}

	@Exportable
	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
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
		return getNaam();
	}

	private static GemeenteDataAccessHelper getHelper()
	{
		return DataAccessRegistry.getHelper(GemeenteDataAccessHelper.class);
	}

	public static Gemeente get(String code)
	{
		return getHelper().get(code);
	}

	public void setNieuweGemeente(Gemeente nieuweGemeente)
	{
		this.nieuweGemeente = nieuweGemeente;
	}

	public Gemeente getNieuweGemeente()
	{
		return nieuweGemeente;
	}

}
