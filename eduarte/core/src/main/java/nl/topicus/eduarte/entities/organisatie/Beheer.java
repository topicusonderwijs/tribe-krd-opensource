/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.organisatie;

import javax.persistence.Entity;

import nl.topicus.cobra.security.RechtenSoort;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Organisatie voor instelling overkoepelend beheer. Er is maar 1 instantie van deze
 * class.
 * 
 * @author marrink
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public class Beheer extends Organisatie
{
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor voor Hibernate
	 */
	public Beheer()
	{
	}

	/**
	 * @see nl.topicus.eduarte.entities.organisatie.Organisatie#getRechtenSoort()
	 */
	@Override
	public RechtenSoort getRechtenSoort()
	{
		return RechtenSoort.BEHEER;
	}

}
