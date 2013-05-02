/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.signalering.settings;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import nl.topicus.eduarte.app.signalering.EventAbonnementConfiguration;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "EventAbonnementConfiguration")
public abstract class AbstractEventAbonnementConfiguration<T> extends InstellingEntiteit implements
		EventAbonnementConfiguration<T>
{
	private static final long serialVersionUID = 1L;

	public AbstractEventAbonnementConfiguration()
	{
	}

	public abstract AbstractEventAbonnementConfiguration< ? > copy();
}
