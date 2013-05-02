/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.dbs.testen;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.dao.helpers.IgnoreInGebruik;
import nl.topicus.eduarte.entities.dbs.NietTonenInZorgvierkant;
import nl.topicus.eduarte.entities.personen.Medewerker;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class DeelnemerTestNietTonenInZorgvierkant extends NietTonenInZorgvierkant
{
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "test", nullable = true)
	@Index(name = "idx_TonenInZorgv_test")
	@IgnoreInGebruik
	private DeelnemerTest test;

	public DeelnemerTestNietTonenInZorgvierkant()
	{
	}

	public DeelnemerTestNietTonenInZorgvierkant(DeelnemerTest test, Medewerker medewerker)
	{
		super(medewerker);
		setTest(test);
	}

	public DeelnemerTest getTest()
	{
		return test;
	}

	public void setTest(DeelnemerTest test)
	{
		this.test = test;
	}
}
