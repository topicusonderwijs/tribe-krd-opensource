/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.dbs.testen;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.dao.helpers.IgnoreInGebruik;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

/**
 * Klasse als superclass voor de numerieke- en tekstuele veldwaarden
 * 
 * @author maatman
 */
@Entity()
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public abstract class Veldwaarde extends InstellingEntiteit implements Comparable<Veldwaarde>
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "testVeld")
	@ForeignKey(name = "FK_Veldwaarde_testVeld")
	@Index(name = "idx_Veldwaarde_testVeld")
	private TestVeld testVeld;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "test")
	@ForeignKey(name = "FK_Veldwaarde_test")
	@Index(name = "idx_Veldwaarde_test")
	@IgnoreInGebruik
	private DeelnemerTest test;

	public Veldwaarde()
	{
	}

	public abstract Serializable getWaarde();

	public abstract Class< ? extends Serializable> getVeldType();

	public TestVeld getTestVeld()
	{
		return testVeld;
	}

	public void setTestVeld(TestVeld testVeld)
	{
		this.testVeld = testVeld;
	}

	public DeelnemerTest getTest()
	{
		return test;
	}

	public void setTest(DeelnemerTest deelnemerTest)
	{
		this.test = deelnemerTest;
	}

	public String getWaardeRangeMelding(Object waarde)
	{
		if (getTestVeld() == null || !getVeldType().equals(Integer.class) || waarde == null)
			return null;

		Integer max = getTestVeld().getMaximumWaarde();
		Integer min = getTestVeld().getMinimumWaarde();

		int intWaarde = (Integer) waarde;
		if (max != null && min != null)
		{
			if (intWaarde > max || intWaarde < min)
				return "De waarde moet liggen tussen " + getTestVeld().getMinimumWaarde() + " en "
					+ getTestVeld().getMaximumWaarde() + ".";
			return null;
		}

		if (max != null && intWaarde > max)
			return "De waarde mag niet groter zijn dan " + getTestVeld().getMaximumWaarde() + ".";
		else if (min != null && intWaarde < min)
			return "De waarde moet minstens " + getTestVeld().getMinimumWaarde() + " zijn.";

		return null;
	}

	@Override
	public int compareTo(Veldwaarde o)
	{
		return getTestVeld().compareTo(o.getTestVeld());
	}
}
