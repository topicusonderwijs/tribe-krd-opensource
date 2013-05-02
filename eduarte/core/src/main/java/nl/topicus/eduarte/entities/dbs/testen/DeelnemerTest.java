/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.dbs.testen;

import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.dbs.ZorgvierkantObject;
import nl.topicus.eduarte.entities.dbs.bijlagen.TestBijlage;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.web.components.choice.ZorglijnCombobox;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

/**
 * @author maatman
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class DeelnemerTest extends InstellingEntiteit implements ZorgvierkantObject,
		IBijlageKoppelEntiteit<TestBijlage>
{
	private static final long serialVersionUID = 1L;

	public static final String DEELNEMERTEST = "DEELNEMERTEST";

	public static final String VERTROUWELIJKE_DEELNEMERTEST = "VERTROUWELIJKE_DEELNEMERTEST";

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deelnemer", nullable = false)
	@ForeignKey(name = "FK_DeelnemerTest_deelnemer")
	@Index(name = "idx_DeelnrTest_deelnemer")
	private Deelnemer deelnemer;

	@Column(columnDefinition = "date", nullable = false)
	@AutoForm(label = "Afnamedatum", htmlClasses = "unit_80")
	private Date afnameDatum;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testDefinitie", nullable = false)
	@ForeignKey(name = "FK_DeelnemerTest_testdef")
	@Index(name = "idx_DeelnemerTes_testDefiniti")
	@AutoForm(htmlClasses = "unit_max")
	private TestDefinitie testDefinitie;

	@Column(nullable = false)
	private boolean vertrouwelijk;

	@Column(nullable = true)
	@AutoForm(editorClass = ZorglijnCombobox.class)
	private Integer zorglijn;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "test")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@Sort(type = SortType.NATURAL)
	private SortedSet<Veldwaarde> veldwaarden = new TreeSet<Veldwaarde>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "test")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<TestBijlage> bijlagen = new ArrayList<TestBijlage>();

	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "test")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<DeelnemerTestNietTonenInZorgvierkant> nietTonenInZorgvierkants =
		new ArrayList<DeelnemerTestNietTonenInZorgvierkant>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "groepTest", nullable = true)
	@ForeignKey(name = "FK_DeelnemerTest_groeptest")
	@Index(name = "idx_DeelnemerTes_groeptest")
	private GroepTest groepTest;

	public DeelnemerTest()
	{
	}

	public DeelnemerTest(Deelnemer deelnemer)
	{
		setDeelnemer(deelnemer);
	}

	public String getTitel()
	{
		return getTestDefinitie().getNaam() + " - Score: " + getStandaardScore().getWaarde();
	}

	public Veldwaarde getStandaardScore()
	{
		if (getVeldwaarden() != null)
			for (Veldwaarde veldwaarde : getVeldwaarden())
			{
				if (veldwaarde.getTestVeld().isHoofdscoreVeld())
					return veldwaarde;
			}
		return null;
	}

	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
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

	public List<Veldwaarde> getVeldwaardenAsList()
	{
		return Collections.unmodifiableList(new ArrayList<Veldwaarde>(getVeldwaarden()));
	}

	public SortedSet<Veldwaarde> getVeldwaarden()
	{
		return veldwaarden;
	}

	public void setVeldwaarden(SortedSet<Veldwaarde> veldwaarden)
	{
		this.veldwaarden = veldwaarden;
	}

	public List<TestBijlage> getBijlagen()
	{
		return bijlagen;
	}

	public void setBijlagen(List<TestBijlage> bijlages)
	{
		this.bijlagen = bijlages;
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

	public GroepTest getGroepTest()
	{
		return groepTest;
	}

	public void setGroepTest(GroepTest groepTest)
	{
		this.groepTest = groepTest;
	}

	@Override
	public String toString()
	{
		String omschrijving = "";
		if (getTestDefinitie() != null)
		{
			omschrijving += getTestDefinitie().getNaam();
			if (getStandaardScore() != null)
				omschrijving += " (" + getStandaardScore().getWaarde() + ")";
		}
		else
			omschrijving = "Onbekende test";

		return omschrijving;
	}

	public String getResultatenStringFormatted()
	{
		StringBuilder res = new StringBuilder();
		for (Veldwaarde waarde : getVeldwaarden())
		{
			res.append(waarde.getTestVeld().getNaam()).append(": ");
			res.append(waarde.getWaarde()).append("\n");
		}

		return res.toString();
	}

	@Override
	public TestBijlage addBijlage(Bijlage bijlage)
	{
		TestBijlage newBijlage = new TestBijlage();
		newBijlage.setBijlage(bijlage);
		newBijlage.setDeelnemer(getDeelnemer());
		newBijlage.setTest(this);

		getBijlagen().add(newBijlage);

		return newBijlage;
	}

	@Override
	public boolean bestaatBijlage(Bijlage bijlage)
	{
		for (TestBijlage deelbijlage : getBijlagen())
		{
			if (deelbijlage.getBijlage().equals(bijlage))
				return true;
		}
		return false;
	}

	@Override
	public String getSecurityId()
	{
		return DEELNEMERTEST;
	}

	@Override
	public String getVertrouwelijkSecurityId()
	{
		return VERTROUWELIJKE_DEELNEMERTEST;
	}

	private DeelnemerTestNietTonenInZorgvierkant findNietTonen()
	{
		Medewerker medewerker = EduArteContext.get().getMedewerker();
		for (DeelnemerTestNietTonenInZorgvierkant curTonen : getNietTonenInZorgvierkants())
		{
			if (curTonen.getMedewerker().equals(medewerker))
				return curTonen;
		}
		return null;
	}

	@Override
	public boolean isTonenInZorgvierkant()
	{
		return findNietTonen() == null;
	}

	public void setTonenInZorgvierkant(boolean tonenInZorgvierkant)
	{
		DeelnemerTestNietTonenInZorgvierkant nietTonen = findNietTonen();
		if (nietTonen == null && !tonenInZorgvierkant)
		{
			DeelnemerTestNietTonenInZorgvierkant newNietTonen =
				new DeelnemerTestNietTonenInZorgvierkant(this, EduArteContext.get().getMedewerker());
			getNietTonenInZorgvierkants().add(newNietTonen);
		}
		else if (nietTonen != null && tonenInZorgvierkant)
		{
			getNietTonenInZorgvierkants().remove(nietTonen);
		}
	}

	public List<DeelnemerTestNietTonenInZorgvierkant> getNietTonenInZorgvierkants()
	{
		return nietTonenInZorgvierkants;
	}

	public void setNietTonenInZorgvierkants(
			List<DeelnemerTestNietTonenInZorgvierkant> nietTonenInZorgvierkants)
	{
		this.nietTonenInZorgvierkants = nietTonenInZorgvierkants;
	}

	public void updateVeldwaarden()
	{
		Map<TestVeld, Veldwaarde> huidigeWaarden = new HashMap<TestVeld, Veldwaarde>();
		for (Veldwaarde curWaarde : getVeldwaarden())
			huidigeWaarden.put(curWaarde.getTestVeld(), curWaarde);

		if (getTestDefinitie() != null)
		{
			for (TestVeld curVeld : getTestDefinitie().getTestVelden())
			{
				if (!huidigeWaarden.containsKey(curVeld))
				{
					Veldwaarde newWaarde = curVeld.createWaarde();
					newWaarde.setTest(this);
					getVeldwaarden().add(newWaarde);
				}
				huidigeWaarden.remove(curVeld);
			}
		}

		for (Veldwaarde curOldWaarde : huidigeWaarden.values())
			getVeldwaarden().remove(curOldWaarde);
	}

	public List<String> getNumeriekeWaarden()
	{
		List<String> numerieken = new ArrayList<String>();
		for (Veldwaarde veldwaarde : getVeldwaarden())
		{
			if (veldwaarde.getClass().isAssignableFrom(NumeriekeVeldwaarde.class))
			{
				numerieken.add(((NumeriekeVeldwaarde) veldwaarde.getWaarde()).toString());
			}
		}
		return numerieken;
	}
}
