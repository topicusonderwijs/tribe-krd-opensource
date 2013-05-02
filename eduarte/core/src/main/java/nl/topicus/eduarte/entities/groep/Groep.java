package nl.topicus.eduarte.entities.groep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.FieldPersistance;
import nl.topicus.cobra.entities.FieldPersistenceMode;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.StringUtil.StringConverter;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.BeoordelingDataAccessHelper;
import nl.topicus.eduarte.entities.IsViewWhenOnNoise;
import nl.topicus.eduarte.entities.VrijVeldable;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.sidebar.IContextInfoObject;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Groepsbeoordeling;
import nl.topicus.eduarte.entities.vrijevelden.GroepVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.providers.GroepProvider;
import nl.topicus.eduarte.providers.OrganisatieEenheidLocatieProvider;
import nl.topicus.eduarte.rapportage.model.GroepFotosRapportageModel;
import nl.topicus.eduarte.rapportage.model.GroepFotosRapportageModel.DeelnemerFoto;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

/**
 * Een groep van deelnemers. Kan een klas, een lesgroep, een projectgroep, een
 * aanmeldgroep of wat dan ook voor groep zijn.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Exportable
@Table(appliesTo = "Groep", indexes = {
	@Index(name = "GENERATED_NAME_beg_eind_arch", columnNames = {"organisatie", "gearchiveerd",
		"einddatumNotNull", "begindatum"}),
	@Index(name = "GENERATED_NAME_beg_eind", columnNames = {"organisatie", "einddatumNotNull",
		"begindatum"})})
@IsViewWhenOnNoise
public class Groep extends BeginEinddatumInstellingEntiteit implements IContextInfoObject,
		GroepProvider, IBijlageKoppelEntiteit<GroepBijlage>, VrijVeldable<GroepVrijVeld>,
		OrganisatieEenheidLocatieProvider
{
	private static final long serialVersionUID = 1L;

	public static final String DEELNAME_REMOVE = "DEELNAME_REMOVE";

	public static final String GROEP_WRITE = "GROEP_WRITE";

	public static final String EDIT_DEELNAME = "EDIT_DEELNAME";

	@Column(length = 20, nullable = false)
	@Index(name = "idx_Groep_code")
	@AutoForm(label = "Code", htmlClasses = "unit_60")
	private String code;

	@Column(nullable = true)
	@Index(name = "idx_Groep_leerjaar")
	@AutoForm(htmlClasses = "unit_60")
	private Integer leerjaar;

	@Column(length = 100, nullable = false)
	@Index(name = "idx_Groep_naam")
	@AutoForm(htmlClasses = "unit_max")
	private String naam;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "groepstype", nullable = false)
	@Index(name = "idx_Groep_groepstype")
	@AutoForm(htmlClasses = "unit_max")
	private Groepstype groepstype;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organisatieEenheid", nullable = false)
	@Index(name = "idx_Groep_orgEhd")
	@AutoForm(htmlClasses = "unit_max")
	private OrganisatieEenheid organisatieEenheid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "locatie", nullable = true)
	@Index(name = "idx_Groep_locatie")
	@AutoForm(htmlClasses = "unit_max")
	private Locatie locatie;

	/**
	 * Lijst met alle mentoren voor deze groep.
	 */
	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "groep")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@OrderBy("begindatum DESC, einddatum DESC")
	private List<GroepMentor> groepMentoren = new ArrayList<GroepMentor>();

	/**
	 * Lijst met alle docenten voor deze groep.
	 */
	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "groep")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@OrderBy("begindatum DESC, einddatum DESC")
	private List<GroepDocent> groepDocenten = new ArrayList<GroepDocent>();

	/**
	 * Lijst met alle deelnemers voor deze groep.
	 */
	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "groep")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@OrderBy("begindatum DESC, einddatum DESC")
	private List<Groepsdeelname> deelnemers = new ArrayList<Groepsdeelname>();

	/**
	 * Unordered, uncached, alleen voor joins.
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "groep")
	@FieldPersistance(FieldPersistenceMode.SKIP)
	private List<Groepsdeelname> deelnamesUnordered;

	/**
	 * De bijlages van deze goep
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "groep")
	private List<GroepBijlage> bijlagen;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "groep")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@BatchSize(size = 20)
	private List<GroepVrijVeld> vrijVelden;

	public Groep()
	{
	}

	@Exportable
	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	@Exportable
	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	@Override
	@Exportable
	public Date getBegindatum()
	{
		return super.getBegindatum();
	}

	@Exportable
	public Groepstype getGroepstype()
	{
		return groepstype;
	}

	public void setGroepstype(Groepstype groepstype)
	{
		this.groepstype = groepstype;
	}

	@Override
	public String getContextInfoOmschrijving()
	{
		return toString();
	}

	@Exportable
	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return organisatieEenheid;
	}

	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		this.organisatieEenheid = organisatieEenheid;
	}

	@Exportable
	public Locatie getLocatie()
	{
		return locatie;
	}

	public void setLocatie(Locatie locatie)
	{
		this.locatie = locatie;
	}

	public List<GroepMentor> getGroepMentoren()
	{
		return groepMentoren;
	}

	public void setGroepMentoren(List<GroepMentor> groepMentoren)
	{
		this.groepMentoren = groepMentoren;
	}

	public String getGroepDocentenVolledigeNaam()
	{
		return StringUtil.toString(getGroepMentoren(), "", new StringConverter<GroepMentor>()
		{
			@Override
			public String getSeparator(int listIndex)
			{
				return ", ";
			}

			@Override
			public String toString(GroepMentor object, int listIndex)
			{
				return object.getMedewerker().getPersoon().getVolledigeNaam();
			}
		});
	}

	public List<GroepDocent> getGroepDocenten()
	{
		return groepDocenten;
	}

	public void setGroepDocenten(List<GroepDocent> groepDocenten)
	{
		this.groepDocenten = groepDocenten;
	}

	@SuppressWarnings("unchecked")
	public void addDeelnemer(Groepsdeelname deelnemer)
	{
		List<Groepsdeelname> deelnames = (List<Groepsdeelname>) getDeelnemers();
		deelnames.add(deelnemer);
	}

	public List< ? extends Groepsdeelname> getDeelnemers()
	{
		return deelnemers;
	}

	public void setDeelnemers(List<Groepsdeelname> deelnemers)
	{
		this.deelnemers = deelnemers;
	}

	@Exportable
	public List<GroepMentor> getGroepMentorenOpPeildatum()
	{
		Date peildatum = EduArteContext.get().getPeildatum();
		List<GroepMentor> res = new ArrayList<GroepMentor>(2);
		for (GroepMentor mentor : getGroepMentoren())
		{
			if (peildatum == null || mentor.isActief(peildatum))
			{
				res.add(mentor);
			}
		}
		return res;
	}

	@Exportable
	public List<GroepDocent> getGroepDocentenOpPeildatum()
	{
		Date peildatum = EduArteContext.get().getPeildatum();
		List<GroepDocent> res = new ArrayList<GroepDocent>(2);
		for (GroepDocent docent : getGroepDocenten())
		{
			if (peildatum == null || docent.isActief(peildatum))
			{
				res.add(docent);
			}
		}
		return res;
	}

	@Exportable
	public List< ? extends Groepsdeelname> getDeelnemersOpPeildatum()
	{
		Date peildatum = EduArteContext.get().getPeildatum();
		List<Groepsdeelname> res = new ArrayList<Groepsdeelname>(getDeelnemers().size());
		for (Groepsdeelname deelnemer : getDeelnemers())
		{
			if (peildatum == null || deelnemer.isActief(peildatum))
			{
				res.add(deelnemer);
			}
		}
		Collections.sort(res);
		return res;
	}

	/**
	 * Functie om 'gebatched' deelnemer fotos op te halen en te printen.
	 */
	@Exportable
	public List<DeelnemerFoto> getDeelnemerFotosOpPeildatum()
	{
		return new GroepFotosRapportageModel(this).list();
	}

	public List< ? extends Groepsdeelname> getDeelnemersOpDatum(Date datum)
	{
		List<Groepsdeelname> res = new ArrayList<Groepsdeelname>(getDeelnemers().size());
		for (Groepsdeelname deelnemer : getDeelnemers())
		{
			if (datum == null || deelnemer.isActief(datum))
			{
				res.add(deelnemer);
			}
		}
		Collections.sort(res);
		return res;
	}

	public List<Deelnemer> getDeelnemersOpPeildatumAsDeelnemer()
	{
		List<Deelnemer> ret = new ArrayList<Deelnemer>();
		for (Groepsdeelname curDeelname : getDeelnemersOpPeildatum())
		{
			ret.add(curDeelname.getDeelnemer());
		}
		return ret;
	}

	public List<Deelnemer> getDeelnemersOpDatumAsDeelnemer(Date datum)
	{
		List<Deelnemer> ret = new ArrayList<Deelnemer>();
		for (Groepsdeelname curDeelname : getDeelnemersOpDatum(datum))
		{
			ret.add(curDeelname.getDeelnemer());
		}
		return ret;
	}

	@Exportable
	public List< ? extends Groepsdeelname> getDeelnemersOpPeildatumEnToekomst()
	{
		Date peildatum = EduArteContext.get().getPeildatum();
		List<Groepsdeelname> res = new ArrayList<Groepsdeelname>(getDeelnemers().size());
		for (Groepsdeelname deelnemer : getDeelnemers())
		{
			if (peildatum == null || deelnemer.isActief(peildatum)
				|| deelnemer.getBegindatum().after(peildatum))
			{
				res.add(deelnemer);
			}
		}
		Collections.sort(res);
		return res;
	}

	@Exportable
	public String getGroepMentorenAfkortingenOpPeildatum()
	{
		StringBuilder namen = new StringBuilder();
		int count = 0;
		for (GroepMentor mentor : getGroepMentorenOpPeildatum())
		{
			if (count > 0)
			{
				namen.append(", ");
			}
			namen.append(mentor.getMedewerker().getAfkorting());
			count++;
		}

		return namen.toString();
	}

	@Exportable
	public String getGroepDocentenAfkortingenOpPeildatum()
	{
		StringBuilder namen = new StringBuilder();
		int count = 0;
		for (GroepDocent docent : getGroepDocentenOpPeildatum())
		{
			if (count > 0)
			{
				namen.append(", ");
			}
			namen.append(docent.getMedewerker().getAfkorting());
			count++;
		}

		return namen.toString();
	}

	@Override
	public Groep getGroep()
	{
		return this;
	}

	@Override
	public String toString()
	{
		return getCode() + " - " + getNaam();
	}

	@Override
	public GroepBijlage addBijlage(Bijlage bijlage)
	{
		GroepBijlage newBijlage = new GroepBijlage();
		newBijlage.setBijlage(bijlage);
		newBijlage.setGroep(this);

		getBijlagen().add(newBijlage);

		return newBijlage;
	}

	@Override
	public boolean bestaatBijlage(Bijlage bijlage)
	{
		for (GroepBijlage groepbijlage : getBijlagen())
		{
			if (groepbijlage.getBijlage().equals(bijlage))
				return true;
		}
		return false;
	}

	@Override
	public List<GroepBijlage> getBijlagen()
	{
		if (bijlagen == null)
			bijlagen = new ArrayList<GroepBijlage>();

		return bijlagen;
	}

	@Override
	public void setBijlagen(List<GroepBijlage> bijlagen)
	{
		this.bijlagen = bijlagen;
	}

	public Integer getLeerjaar()
	{
		return leerjaar;
	}

	public void setLeerjaar(Integer leerjaar)
	{
		this.leerjaar = leerjaar;
	}

	/**
	 * Deze is alleen voor joins, gebruik getDeelnemers()
	 * 
	 * @return groepsdeelnames
	 */
	public List< ? extends Groepsdeelname> getDeelnamesUnordered()
	{
		return deelnamesUnordered;
	}

	public void setDeelnamesUnordered(List<Groepsdeelname> deelnamesUnordered)
	{
		this.deelnamesUnordered = deelnamesUnordered;
	}

	@Override
	public List<GroepVrijVeld> getVrijVelden()
	{
		if (vrijVelden == null)
			vrijVelden = new ArrayList<GroepVrijVeld>();

		return vrijVelden;
	}

	@Override
	public List<GroepVrijVeld> getVrijVelden(VrijVeldCategorie categorie)
	{
		List<GroepVrijVeld> res = new ArrayList<GroepVrijVeld>();
		for (GroepVrijVeld pvv : getVrijVelden())
		{
			if (pvv.getVrijVeld().getCategorie().equals(categorie))
			{
				res.add(pvv);
			}
		}
		return res;
	}

	@Override
	public GroepVrijVeld newVrijVeld()
	{
		GroepVrijVeld pvv = new GroepVrijVeld();
		pvv.setGroep(this);

		return pvv;
	}

	@Override
	public void setVrijVelden(List<GroepVrijVeld> vrijvelden)
	{
		this.vrijVelden = vrijvelden;
	}

	@Exportable
	@Override
	public String getVrijVeldWaarde(String vrijVeldNaam)
	{
		for (GroepVrijVeld vrijVeld : vrijVelden)
			if (vrijVeld.getVrijVeld().getNaam().equals(vrijVeldNaam))
				return vrijVeld.getOmschrijving();
		return null;
	}

	public List<Groepsbeoordeling> getBeoordelingen()
	{
		return DataAccessRegistry.getHelper(BeoordelingDataAccessHelper.class).getBeoordelingen(
			this);
	}

	public Groepsbeoordeling getEersteBeoordeling()
	{
		for (Groepsbeoordeling beoordeling : getBeoordelingen())
		{
			return beoordeling;
		}
		return null;
	}

}
