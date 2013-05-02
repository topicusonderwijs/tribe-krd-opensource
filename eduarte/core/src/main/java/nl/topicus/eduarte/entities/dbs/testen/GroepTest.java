/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.dbs.testen;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

/**
 * @author vandekamp
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class GroepTest extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "groep", nullable = false)
	@ForeignKey(name = "FK_GroepTest_groep")
	@Index(name = "idx_GroepTest_groep")
	private Groep groep;

	@Column(columnDefinition = "date")
	private Date afnameDatum;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testDefinitie", nullable = false)
	@ForeignKey(name = "FK_GroepTest_testdef")
	@Index(name = "idx_GroepTest_testDefiniti")
	private TestDefinitie testDefinitie;

	@Column(nullable = false)
	private boolean vertrouwelijk;

	@Column(nullable = true)
	private Integer zorglijn;

	@Column(nullable = false)
	private boolean tonen;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "groepTest")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<DeelnemerTest> deelnemerTesten = new ArrayList<DeelnemerTest>();

	public GroepTest()
	{
	}

	public Groep getGroep()
	{
		return groep;
	}

	public void setGroep(Groep groep)
	{
		this.groep = groep;
	}

	public Date getAfnameDatum()
	{
		return afnameDatum;
	}

	public void setAfnameDatum(Date afnameDatum)
	{
		this.afnameDatum = afnameDatum;
	}

	public TestDefinitie getTestDefinitie()
	{
		return testDefinitie;
	}

	public void setTestDefinitie(TestDefinitie testDefinitie)
	{
		this.testDefinitie = testDefinitie;
	}

	public List<DeelnemerTest> getDeelnemerTesten()
	{
		return deelnemerTesten;
	}

	public void setDeelnemerTesten(List<DeelnemerTest> deenemerTesten)
	{
		this.deelnemerTesten = deenemerTesten;
	}

	public boolean isVertrouwelijk()
	{
		return vertrouwelijk;
	}

	public void setVertrouwelijk(boolean vertrouwelijk)
	{
		this.vertrouwelijk = vertrouwelijk;
	}

	public Integer getZorglijn()
	{
		return zorglijn;
	}

	public void setZorglijn(Integer zorglijn)
	{
		this.zorglijn = zorglijn;
	}

	public boolean isTonen()
	{
		return tonen;
	}

	public void setTonen(boolean tonen)
	{
		this.tonen = tonen;
	}
}
