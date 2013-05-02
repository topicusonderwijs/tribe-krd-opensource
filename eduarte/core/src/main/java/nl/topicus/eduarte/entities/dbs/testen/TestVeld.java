/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.dbs.testen;

import javax.persistence.*;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.LandelijkOfInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"volgnummer", "testDefinitie"})})
public class TestVeld extends LandelijkOfInstellingEntiteit implements Comparable<TestVeld>
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private int volgnummer;

	@Column(nullable = false, length = 100)
	private String naam;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Veldtype type;

	@Column(nullable = false)
	private boolean verplicht;

	@Column(nullable = true)
	private Integer minimumWaarde;

	@Column(nullable = true)
	private Integer maximumWaarde;

	@Column(nullable = false)
	@AutoForm(include = false)
	private boolean hoofdscoreVeld;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "testDefinitie")
	@Index(name = "idx_TestVeld_testDefinitie")
	@AutoForm(include = false)
	private TestDefinitie testDefinitie;

	public TestVeld()
	{

	}

	public TestVeld(TestDefinitie definitie)
	{
		setTestDefinitie(definitie);
	}

	public boolean isVerplicht()
	{
		return verplicht;
	}

	public void setVerplicht(boolean verplicht)
	{
		this.verplicht = verplicht;
	}

	public Integer getMinimumWaarde()
	{
		return minimumWaarde;
	}

	public void setMinimumWaarde(Integer minimumWaarde)
	{
		this.minimumWaarde = minimumWaarde;
	}

	public Integer getMaximumWaarde()
	{
		return maximumWaarde;
	}

	public void setMaximumWaarde(Integer maximumWaarde)
	{
		this.maximumWaarde = maximumWaarde;
	}

	public int getVolgnummer()
	{
		return volgnummer;
	}

	public void setVolgnummer(int volgnummer)
	{
		this.volgnummer = volgnummer;
	}

	public boolean isHoofdscoreVeld()
	{
		return hoofdscoreVeld;
	}

	public void setHoofdscoreVeld(boolean hoofdscoreVeld)
	{
		this.hoofdscoreVeld = hoofdscoreVeld;
	}

	public Veldtype getType()
	{
		return type;
	}

	public void setType(Veldtype type)
	{
		this.type = type;
	}

	public TestDefinitie getTestDefinitie()
	{
		return testDefinitie;
	}

	public void setTestDefinitie(TestDefinitie testDefinitie)
	{
		this.testDefinitie = testDefinitie;
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public Veldwaarde createWaarde()
	{
		Veldwaarde ret = getType().createWaarde();
		ret.setTestVeld(this);
		return ret;
	}

	@Override
	public int compareTo(TestVeld o)
	{
		return getVolgnummer() - o.getVolgnummer();
	}

	public boolean isVolgnummerInGebruik(int value)
	{
		for (TestVeld curVeld : getTestDefinitie().getTestVelden())
		{
			if (!curVeld.equals(this) && curVeld.getVolgnummer() == value)
				return true;
		}
		return false;
	}
}
