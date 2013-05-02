package nl.topicus.eduarte.entities.dbs.trajecten.templates;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.entities.IActiefEntiteit;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.dbs.trajecten.Traject;
import nl.topicus.eduarte.entities.dbs.trajecten.TrajectSoort;
import nl.topicus.eduarte.entities.dbs.trajecten.TrajectUitvoerder;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.security.authorization.Rol;
import nl.topicus.eduarte.providers.OrganisatieEenheidLocatieProvider;
import nl.topicus.eduarte.web.components.choice.ZorglijnCombobox;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Where;

/**
 * @author maatman, henzen
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class TrajectTemplate extends InstellingEntiteit implements IActiefEntiteit,
		OrganisatieEenheidLocatieProvider, IBijlageKoppelEntiteit<TrajectTemplateBijlage>
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "organisatieEenheid", nullable = false)
	@Index(name = "idx_trajectT_organisatieE")
	@AutoForm(label = "Organisatie-eenheid", htmlClasses = "unit_max")
	private OrganisatieEenheid organisatieEenheid;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "locatie", nullable = true)
	@Index(name = "idx_trajectT_Locatie")
	@AutoForm(label = "Locatie", htmlClasses = "unit_max")
	private Locatie locatie;

	@Column(length = 128, nullable = false)
	@AutoForm(htmlClasses = "unit_160")
	private String naam;

	@Column(length = 512, nullable = false)
	@AutoForm(htmlClasses = "unit_max")
	private String omschrijving;

	@Column(nullable = false)
	private boolean actief = true;

	@Column(nullable = false)
	@AutoForm(htmlClasses = "unit_25")
	private Integer tijdsduurAantal;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@AutoForm(htmlClasses = "unit_120")
	private TijdEenheid tijdsduurEenheid;

	@Column(nullable = false)
	private boolean opEersteDagSchooljaar;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "trajectSoort", nullable = false)
	@ForeignKey(name = "FK_TrajTempl_soort")
	@Index(name = "idx_TrajTempl_soort")
	@AutoForm(htmlClasses = "unit_max", label = "Trajectsoort")
	private TrajectSoort trajectSoort;

	@Column(nullable = true)
	@AutoForm(editorClass = ZorglijnCombobox.class)
	private Integer zorglijn;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "trajectTemplate")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@Where(clause = "koppelingsRol = 'UITVOERENDE'")
	private List<GekoppeldeTemplate> uitvoerenden = new ArrayList<GekoppeldeTemplate>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "trajectTemplate")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@Where(clause = "koppelingsRol = 'VERANTWOORDELIJKE'")
	private List<GekoppeldeTemplate> verantwoordelijken = new ArrayList<GekoppeldeTemplate>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "trajectTemplate")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<TrajectBegeleidingsHandelingTemplate> handelingen =
		new ArrayList<TrajectBegeleidingsHandelingTemplate>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "trajectTemplate")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<AanleidingTemplate> aanleidingen = new ArrayList<AanleidingTemplate>();

	@Index(name = "idx_TrajectT_eindHandeling")
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EindHandelingTemplate", nullable = true)
	private BegeleidingsHandelingTemplate eindHandelingTemplate;

	@Index(name = "idx_TrajectT_autoKopp")
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "automatischeKoppeling", nullable = true)
	private TrajectTemplateAutomatischeKoppeling automatischeKoppeling;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "trajectTemplate")
	private List<TrajectTemplateBijlage> bijlagen;

	public TrajectTemplate()
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

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public Integer getTijdsduurAantal()
	{
		return tijdsduurAantal;
	}

	public void setTijdsduurAantal(Integer tijdsduurAantal)
	{
		this.tijdsduurAantal = tijdsduurAantal;
	}

	public TijdEenheid getTijdsduurEenheid()
	{
		return tijdsduurEenheid;
	}

	public void setTijdsduurEenheid(TijdEenheid tijdsduurEenheid)
	{
		this.tijdsduurEenheid = tijdsduurEenheid;
	}

	public boolean isOpEersteDagSchooljaar()
	{
		return opEersteDagSchooljaar;
	}

	public void setOpEersteDagSchooljaar(boolean opEersteDagSchooljaar)
	{
		this.opEersteDagSchooljaar = opEersteDagSchooljaar;
	}

	public TrajectSoort getTrajectSoort()
	{
		return trajectSoort;
	}

	public void setTrajectSoort(TrajectSoort trajectSoort)
	{
		this.trajectSoort = trajectSoort;
	}

	public Integer getZorglijn()
	{
		return zorglijn;
	}

	public void setZorglijn(Integer zorglijn)
	{
		this.zorglijn = zorglijn;
	}

	public List<GekoppeldeTemplate> getUitvoerenden()
	{
		return uitvoerenden;
	}

	public void setUitvoerenden(List<GekoppeldeTemplate> uitvoerenden)
	{
		this.uitvoerenden = uitvoerenden;
	}

	public List<GekoppeldeTemplate> getVerantwoordelijken()
	{
		return verantwoordelijken;
	}

	public void setVerantwoordelijken(List<GekoppeldeTemplate> verantwoordelijken)
	{
		this.verantwoordelijken = verantwoordelijken;
	}

	public List<TrajectBegeleidingsHandelingTemplate> getHandelingen()
	{
		return handelingen;
	}

	public void setHandelingen(List<TrajectBegeleidingsHandelingTemplate> handelingen)
	{
		this.handelingen = handelingen;
	}

	public List<AanleidingTemplate> getAanleidingen()
	{
		return aanleidingen;
	}

	public void setAanleidingen(List<AanleidingTemplate> aanleidingen)
	{
		this.aanleidingen = aanleidingen;
	}

	public BegeleidingsHandelingTemplate getEindHandelingTemplate()
	{
		return eindHandelingTemplate;
	}

	public void setEindHandelingTemplate(BegeleidingsHandelingTemplate eindHandelingTemplate)
	{
		this.eindHandelingTemplate = eindHandelingTemplate;
	}

	@Override
	public String toString()
	{
		return getNaam();
	}

	@Override
	public boolean isActief()
	{
		return actief;
	}

	public void setActief(boolean actief)
	{
		this.actief = actief;
	}

	public List<Rol> getVerantwoordelijkeRollen()
	{
		List<Rol> rollen = new ArrayList<Rol>();

		for (GekoppeldeTemplate gekoppeldeTemplate : getVerantwoordelijken())
			if (gekoppeldeTemplate.getRol() != null)
				rollen.add(gekoppeldeTemplate.getRol());

		return rollen;
	}

	public List<Rol> getUitvoerendeRollen()
	{
		List<Rol> rollen = new ArrayList<Rol>();

		for (GekoppeldeTemplate gekoppeldeTemplate : getUitvoerenden())
			if (gekoppeldeTemplate.getRol() != null)
				rollen.add(gekoppeldeTemplate.getRol());

		return rollen;
	}

	public TrajectTemplateAutomatischeKoppeling getAutomatischeKoppeling()
	{
		return automatischeKoppeling;
	}

	public void setAutomatischeKoppeling(TrajectTemplateAutomatischeKoppeling automatischeKoppeling)
	{
		this.automatischeKoppeling = automatischeKoppeling;
	}

	public boolean isAutomatischeKoppelingAanwezig()
	{
		return getAutomatischeKoppeling() != null;
	}

	public void fillTraject(Traject traject)
	{
		traject.setTitel(getNaam());
		traject.setOmschrijving(getOmschrijving());
		traject.setTrajectSoort(getTrajectSoort());
		getTrajectSoort().fillTraject(traject);
		traject.setZorglijn(getZorglijn());
		if (isOpEersteDagSchooljaar())
		{
			if (traject.getVerbintenis() != null)
			{
				if (traject.getVerbintenis().getBegindatum().before(
					TimeUtil.getInstance().currentDate()))
					traject.setBegindatum(Cohort.getHuidigCohort().getBegindatum());
				else
					traject.setBegindatum(Cohort
						.getCohort(traject.getVerbintenis().getBegindatum()).getBegindatum());
			}
			else
				traject.setBegindatum(Cohort.getHuidigCohort().getBegindatum());
		}
		else
			traject.setBegindatum(TimeUtil.getInstance().currentDate());
		traject.setBeoogdeEinddatum(getTijdsduurEenheid().add(traject.getBegindatum(),
			getTijdsduurAantal()));

		if (getVerantwoordelijken() != null && !getVerantwoordelijken().isEmpty())
		{
			List<Medewerker> verantwoordelijkeMedewerkers =
				getVerantwoordelijken().get(0).getGekoppeldeMedewerkers(traject);
			if (!verantwoordelijkeMedewerkers.isEmpty())
				traject.setVerantwoordelijke(verantwoordelijkeMedewerkers.get(0));
		}

		if (!getUitvoerenden().isEmpty())
		{
			traject.getUitvoerders().clear();
			for (GekoppeldeTemplate curGekoppelde : getUitvoerenden())
				for (Medewerker curUitvoerende : curGekoppelde.getGekoppeldeMedewerkers(traject))
					if (!traject.isUitvoerende(curUitvoerende))
						traject.getUitvoerders()
							.add(new TrajectUitvoerder(traject, curUitvoerende));
		}
		traject.setEindHandelingTemplate(getEindHandelingTemplate() == null ? null
			: getEindHandelingTemplate().copy());
		if (!getAanleidingen().isEmpty())
		{
			traject.getAanleidingen().clear();
			for (AanleidingTemplate curAanleidingTemplate : getAanleidingen())
			{
				traject.getAanleidingen().addAll(curAanleidingTemplate.createAanleidingen(traject));
			}
		}
	}

	public void createHandelingen(Traject traject)
	{
		for (TrajectBegeleidingsHandelingTemplate trajactHandeling : getHandelingen())
			trajactHandeling.getBegeleidingsHandeling().createHandelingen(traject);
	}

	@Override
	public TrajectTemplateBijlage addBijlage(Bijlage bijlage)
	{
		TrajectTemplateBijlage newBijlage = new TrajectTemplateBijlage();
		newBijlage.setBijlage(bijlage);
		newBijlage.setTrajectTemplate(this);

		getBijlagen().add(newBijlage);

		return newBijlage;
	}

	@Override
	public boolean bestaatBijlage(Bijlage bijlage)
	{
		for (TrajectTemplateBijlage templateBijlage : getBijlagen())
		{
			if (templateBijlage.getBijlage().equals(bijlage))
				return true;
		}
		return false;
	}

	@Override
	public List<TrajectTemplateBijlage> getBijlagen()
	{
		if (bijlagen == null)
			bijlagen = new ArrayList<TrajectTemplateBijlage>();

		return bijlagen;
	}

	@Override
	public void setBijlagen(List<TrajectTemplateBijlage> bijlagen)
	{
		this.bijlagen = bijlagen;
	}
}
