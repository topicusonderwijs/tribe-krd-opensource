package nl.topicus.eduarte.entities.dbs.trajecten;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.entities.IActiefEntiteit;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.wiquery.TextEditorPanel;
import nl.topicus.eduarte.entities.dbs.testen.TestDefinitie;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.providers.OrganisatieEenheidLocatieProvider;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class TrajectSoort extends InstellingEntiteit implements IActiefEntiteit,
		OrganisatieEenheidLocatieProvider
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "organisatieEenheid", nullable = false)
	@Index(name = "idx_trajectS_organisatieE")
	@AutoForm(label = "Organisatie-eenheid", htmlClasses = "unit_max")
	private OrganisatieEenheid organisatieEenheid;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "locatie", nullable = true)
	@Index(name = "idx_trajectS_Locatie")
	@AutoForm(label = "Locatie", htmlClasses = "unit_max")
	private Locatie locatie;

	@Column(nullable = false, length = 255)
	@AutoForm(htmlClasses = "unit_max")
	private String naam;

	@Column(nullable = false)
	private boolean actief = true;

	@Column(nullable = false)
	private boolean handelingsplan;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "defaultGesprekSoort", nullable = true)
	@Index(name = "idx_Trajectsoort_stdGesprekS")
	@AutoForm(label = "Standaard gespreksoort", htmlClasses = "unit_max")
	private GesprekSoort standaardGesprekSoort;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "defaultTaakSoort", nullable = true)
	@Index(name = "idx_Trajectsoort_stdTaakSoort")
	@AutoForm(label = "Standaard taaksoort", htmlClasses = "unit_max")
	private TaakSoort standaardTaakSoort;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "defaultTestDefinitie", nullable = true)
	@Index(name = "idx_Trajectsoort_stdTestDef")
	@AutoForm(label = "Standaard testdefinitie", htmlClasses = "unit_max")
	private TestDefinitie standaardTestDefinitie;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ZorgvierkantKwadrant kwadrant = ZorgvierkantKwadrant.Ontwikkeling;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "trajectsoort")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@AutoForm(include = false)
	private List<ToegestaneStatusSoort> toegestaneStatussoorten =
		new ArrayList<ToegestaneStatusSoort>();

	@Lob
	@Column(nullable = true)
	@AutoForm(label = "Beginsituatie-template", editorClass = TextEditorPanel.class)
	private String beginSituatieTemplate;

	@Lob
	@Column(nullable = true)
	@AutoForm(label = "Doelen-template", editorClass = TextEditorPanel.class)
	private String doelenTemplate;

	@Lob
	@Column(nullable = true)
	@AutoForm(label = "Handelingen-template", editorClass = TextEditorPanel.class)
	private String handelingenTemplate;

	public TrajectSoort()
	{
	}

	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return organisatieEenheid;
	}

	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		this.organisatieEenheid = organisatieEenheid;
	}

	public Locatie getLocatie()
	{
		return locatie;
	}

	public void setLocatie(Locatie locatie)
	{
		this.locatie = locatie;
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

	public boolean isHandelingsplan()
	{
		return handelingsplan;
	}

	public void setHandelingsplan(boolean handelingsplan)
	{
		this.handelingsplan = handelingsplan;
	}

	public List<ToegestaneStatusSoort> getToegestaneStatussoorten()
	{
		return toegestaneStatussoorten;
	}

	public void setToegestaneStatussoorten(List<ToegestaneStatusSoort> toegestaneStatussoorten)
	{
		this.toegestaneStatussoorten = toegestaneStatussoorten;
	}

	public ZorgvierkantKwadrant getKwadrant()
	{
		return kwadrant;
	}

	public void setKwadrant(ZorgvierkantKwadrant kwadrant)
	{
		this.kwadrant = kwadrant;
	}

	public String getDoelenTemplate()
	{
		return doelenTemplate;
	}

	public void setDoelenTemplate(String doelenTemplate)
	{
		this.doelenTemplate = doelenTemplate;
	}

	public String getBeginSituatieTemplate()
	{
		return beginSituatieTemplate;
	}

	public void setBeginSituatieTemplate(String beginSituatieTemplate)
	{
		this.beginSituatieTemplate = beginSituatieTemplate;
	}

	public String getHandelingenTemplate()
	{
		return handelingenTemplate;
	}

	public void setHandelingenTemplate(String handelingenTemplate)
	{
		this.handelingenTemplate = handelingenTemplate;
	}

	public GesprekSoort getStandaardGesprekSoort()
	{
		return standaardGesprekSoort;
	}

	public void setStandaardGesprekSoort(GesprekSoort standaardGesprekSoort)
	{
		this.standaardGesprekSoort = standaardGesprekSoort;
	}

	public TaakSoort getStandaardTaakSoort()
	{
		return standaardTaakSoort;
	}

	public void setStandaardTaakSoort(TaakSoort standaardTaakSoort)
	{
		this.standaardTaakSoort = standaardTaakSoort;
	}

	public TestDefinitie getStandaardTestDefinitie()
	{
		return standaardTestDefinitie;
	}

	public void setStandaardTestDefinitie(TestDefinitie standaardTestDefinitie)
	{
		this.standaardTestDefinitie = standaardTestDefinitie;
	}

	public TrajectStatusSoort getEersteStatus()
	{
		for (ToegestaneStatusSoort curSoort : getToegestaneStatussoorten())
		{
			if (curSoort.isDefaultStatus())
				return curSoort.getTrajectStatusSoort();
		}
		return null;
	}

	@Override
	public String toString()
	{
		return naam;
	}

	public void fillTraject(Traject traject)
	{
		if (isHandelingsplan())
		{
			traject.setDoelen(getDoelenTemplate());
			traject.setHandelingen(getHandelingenTemplate());
			traject.setBeginSituatie(getBeginSituatieTemplate());
		}
		else
		{
			traject.setDoelen(null);
			traject.setHandelingen(null);
			traject.setBeginSituatie(null);
		}
		TrajectStatusSoort status = getEersteStatus();
		if (status == null)
		{
			List<ToegestaneStatusSoort> statussen = getToegestaneStatussoorten();
			if (!statussen.isEmpty())
			{
				status = statussen.get(0).getTrajectStatusSoort();
			}
			else
			{
				return;
			}
		}
		traject.setTrajectStatusSoort(getEersteStatus());
	}
}
