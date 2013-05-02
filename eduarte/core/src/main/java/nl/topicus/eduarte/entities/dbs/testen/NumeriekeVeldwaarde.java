/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.dbs.testen;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author maatman
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class NumeriekeVeldwaarde extends Veldwaarde
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true, name = "intWaarde")
	private Integer waarde;

	public NumeriekeVeldwaarde()
	{
	}

	@Override
	public Integer getWaarde()
	{
		return waarde;
	}

	public void setWaarde(Integer waarde)
	{
		this.waarde = waarde;
	}

	@Override
	public Class< ? extends Serializable> getVeldType()
	{
		return Integer.class;
	}
}
