package nl.topicus.eduarte.entities.inschrijving;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.form.AutoFormEmbedded;
import nl.topicus.eduarte.entities.VrijVeldable;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.entities.hogeronderwijs.Fase;
import nl.topicus.eduarte.entities.hogeronderwijs.InschrijvingsVorm;
import nl.topicus.eduarte.entities.hogeronderwijs.Inschrijvingsverzoek;
import nl.topicus.eduarte.entities.hogeronderwijs.OpleidingsVorm;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.vrijevelden.PlaatsingVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.providers.OrganisatieEenheidLocatieProvider;
import nl.topicus.eduarte.providers.VerbintenisProvider;
import nl.topicus.eduarte.web.components.choice.FaseCombobox;
import nl.topicus.onderwijs.duo.bron.Bron;
import nl.topicus.onderwijs.duo.criho.annot.Criho;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Een plaatsing geeft aan in welke basisgroep een deelnemer zit, en eventueel in welk
 * leerjaar. Een plaatsing wordt binnen een verbintenis gedefinieerd, en er is maar een
 * actieve plaatsing binnen een verbintenis op een gegeven datum.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Exportable
public class Plaatsing extends Groepsdeelname implements VrijVeldable<PlaatsingVrijVeld>,
		VerbintenisProvider, OrganisatieEenheidLocatieProvider
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "verbintenis")
	@Index(name = "idx_Plaatsing_verbintenis")
	@AutoFormEmbedded
	private Verbintenis verbintenis;

	@Bron
	@Column(nullable = true)
	@Index(name = "idx_Plaatsing_leerjaar")
	private Integer leerjaar;

	@Bron
	@Column(nullable = true)
	@Index(name = "idx_Plaatsing_praktijkjaar")
	private Integer jarenPraktijkonderwijs;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "plaatsing")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@BatchSize(size = 20)
	private List<PlaatsingVrijVeld> vrijVelden;

	@SuppressWarnings("unchecked")
	@Bron
	@Transient
	private Enum handmatigVersturenNaarBronMutatie = null;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "plaatsing")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@BatchSize(size = 20)
	private List<Inschrijvingsverzoek> inschrijvingsverzoeken =
		new ArrayList<Inschrijvingsverzoek>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "fase")
	@Index(name = "idx_Plaatsing_fase")
	@AutoForm(editorClass = FaseCombobox.class)
	@Criho
	private Fase fase;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@Criho
	private InschrijvingsVorm inschrijvingsVorm;

	@Column(nullable = true)
	@Enumerated(value = EnumType.STRING)
	@Criho
	private OpleidingsVorm opleidingsVorm;

	/**
	 * Indicatie of deze plaatsing een lwoo-beschikking heeft. Dit property mag alleen op
	 * true gezet worden voor plaatsingen van verbintenissen die gekoppeld zijn aan
	 * verbintenisgebieden met een lwoo-verbintenisgebied.
	 */
	@Bron
	@AutoForm(label = "LWOO")
	@Column(nullable = true)
	private boolean lwoo;

	public Plaatsing()
	{
	}

	public Verbintenis getVerbintenis()
	{
		return verbintenis;
	}

	public void setVerbintenis(Verbintenis verbintenis)
	{
		this.verbintenis = verbintenis;
	}

	@Exportable
	public Integer getLeerjaar()
	{
		return leerjaar;
	}

	public void setLeerjaar(Integer leerjaar)
	{
		this.leerjaar = leerjaar;
	}

	@AutoForm(label = "Plaatsingsgroep", htmlClasses = "unit_max")
	@Override
	@Exportable
	public Groep getGroep()
	{
		return super.getGroep();
	}

	@Override
	public List<PlaatsingVrijVeld> getVrijVelden()
	{
		if (vrijVelden == null)
			vrijVelden = new ArrayList<PlaatsingVrijVeld>();

		return vrijVelden;
	}

	@Override
	public List<PlaatsingVrijVeld> getVrijVelden(VrijVeldCategorie categorie)
	{
		List<PlaatsingVrijVeld> res = new ArrayList<PlaatsingVrijVeld>();
		for (PlaatsingVrijVeld pvv : getVrijVelden())
		{
			if (pvv.getVrijVeld().getCategorie().equals(categorie))
			{
				res.add(pvv);
			}
		}
		return res;
	}

	@Override
	public PlaatsingVrijVeld newVrijVeld()
	{
		PlaatsingVrijVeld pvv = new PlaatsingVrijVeld();
		pvv.setPlaatsing(this);

		return pvv;
	}

	@Override
	public void setVrijVelden(List<PlaatsingVrijVeld> vrijvelden)
	{
		this.vrijVelden = vrijvelden;
	}

	public void setJarenPraktijkonderwijs(Integer jarenPraktijkonderwijs)
	{
		this.jarenPraktijkonderwijs = jarenPraktijkonderwijs;
	}

	public Integer getJarenPraktijkonderwijs()
	{
		return jarenPraktijkonderwijs;
	}

	public Integer getLeerjaarOfjarenPraktijkonderwijs()
	{
		if ("0090".equals(verbintenis.getExterneCode()))
			return jarenPraktijkonderwijs;
		return leerjaar;
	}

	public Cohort getSchoolJaar()
	{
		return Cohort.getCohort(getBegindatum());
	}

	public void setHandmatigVersturenNaarBron(Enum< ? > soortMutatie)
	{
		this.handmatigVersturenNaarBronMutatie = soortMutatie;
	}

	public nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie getHandmatigeBronBveSoortMutatie()
	{
		if (handmatigVersturenNaarBronMutatie instanceof nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie)
		{
			return (nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie) handmatigVersturenNaarBronMutatie;
		}
		return null;
	}

	public nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie getHandmatigeBronVoSoortMutatie()
	{
		if (handmatigVersturenNaarBronMutatie instanceof nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie)
		{
			return (nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie) handmatigVersturenNaarBronMutatie;
		}
		return null;
	}

	public boolean isHandmatigVersturenNaarBron()
	{
		return handmatigVersturenNaarBronMutatie != null;
	}

	@Exportable
	@Override
	public String getVrijVeldWaarde(String naam)
	{
		for (PlaatsingVrijVeld vrijVeld : vrijVelden)
			if (vrijVeld.getVrijVeld().getNaam().equals(naam))
				return vrijVeld.getOmschrijving();
		return null;
	}

	@Override
	public OrganisatieEenheid getOrganisatieEenheid()
	{
		if (getVerbintenis() != null)
		{
			return getVerbintenis().getOrganisatieEenheid();
		}
		else
		{
			return null;
		}
	}

	@Override
	public Locatie getLocatie()
	{
		if (getVerbintenis() != null)
			return getVerbintenis().getLocatie();
		else
			return null;
	}

	public List<Inschrijvingsverzoek> getInschrijvingsverzoeken()
	{
		return inschrijvingsverzoeken;
	}

	public void setInschrijvingsverzoeken(List<Inschrijvingsverzoek> inschrijvingsverzoeken)
	{
		this.inschrijvingsverzoeken = inschrijvingsverzoeken;
	}

	public void setFase(Fase fase)
	{
		this.fase = fase;
	}

	public Fase getFase()
	{
		return fase;
	}

	public InschrijvingsVorm getInschrijvingsVorm()
	{
		return inschrijvingsVorm;
	}

	public void setInschrijvingsVorm(InschrijvingsVorm inschrijvingsVorm)
	{
		this.inschrijvingsVorm = inschrijvingsVorm;
	}

	public OpleidingsVorm getOpleidingsVorm()
	{
		return opleidingsVorm;
	}

	public void setOpleidingsVorm(OpleidingsVorm opleidingsVorm)
	{
		this.opleidingsVorm = opleidingsVorm;
	}

	public void setLwoo(boolean lwoo)
	{
		this.lwoo = lwoo;
	}

	public boolean isLwoo()
	{
		return lwoo;
	}

	@Exportable
	public String getLwooOmschrijving()
	{
		return isLwoo() ? "Ja" : "Nee";
	}
}
