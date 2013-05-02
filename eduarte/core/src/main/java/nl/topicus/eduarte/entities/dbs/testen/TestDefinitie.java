/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.dbs.testen;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.entities.IActiefEntiteit;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.participatie.AfspraakType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"naam",
	"organisatie"})})
public class TestDefinitie extends InstellingEntiteit implements IActiefEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "categorie")
	@ForeignKey(name = "FK_Testdefinitie_categorie")
	@Index(name = "idx_TestDefinitie_categorie")
	@AutoForm(htmlClasses = "unit_max")
	private TestCategorie categorie;

	@Column(length = 100, nullable = false)
	@AutoForm(htmlClasses = "unit_max")
	private String naam;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "afspraakType", nullable = false)
	@Index(name = "idx_testdefinitie_afspraakT")
	@AutoForm(htmlClasses = "unit_max")
	private AfspraakType afspraakType;

	@Column(nullable = false)
	private boolean actief = true;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "testDefinitie")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@AutoForm(include = false)
	@OrderBy("volgnummer")
	private List<TestVeld> testVelden = new ArrayList<TestVeld>();

	@Column(nullable = false)
	@AutoForm(label = "'Bespreken' tonen")
	private boolean besprekenTonen;

	public TestDefinitie()
	{
	}

	public TestCategorie getCategorie()
	{
		return categorie;
	}

	public void setCategorie(TestCategorie categorie)
	{
		this.categorie = categorie;
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public boolean isActief()
	{
		return actief;
	}

	public void setActief(boolean actief)
	{
		this.actief = actief;
	}

	public List<TestVeld> getTestVelden()
	{
		return testVelden;
	}

	public void setTestVelden(List<TestVeld> testVelden)
	{
		this.testVelden = testVelden;
	}

	public boolean isBesprekenTonen()
	{
		return besprekenTonen;
	}

	public void setBesprekenTonen(boolean besprekenTonen)
	{
		this.besprekenTonen = besprekenTonen;
	}

	public AfspraakType getAfspraakType()
	{
		return afspraakType;
	}

	public void setAfspraakType(AfspraakType afspraakType)
	{
		this.afspraakType = afspraakType;
	}

	@Override
	public String toString()
	{
		return getNaam();
	}
}
