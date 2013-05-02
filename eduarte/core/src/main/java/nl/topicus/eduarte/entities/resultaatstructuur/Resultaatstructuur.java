package nl.topicus.eduarte.entities.resultaatstructuur;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.codenaamactief.CodeNaamActiefInstellingEntiteit;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.SoortToets;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.ToetsCodePathMode;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Een onderwijsproduct dat een summatief resultaat levert heeft een resultaatstructuur
 * per cohort.
 * 
 * @author loite
 */
@Exportable()
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"code",
	"organisatie", "cohort", "onderwijsproduct"})})
public class Resultaatstructuur extends CodeNaamActiefInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	public static final String RES_SUMMATIEF = "RES_SUMMATIEF";

	public static final String RES_FORMATIEF = "RES_FORMATIEF";

	public static final String VERWIJDEREN = "_VERWIJDEREN";

	public static final String KOPIEREN = "_KOPIEREN";

	public static final String IMPORTEREN = "_IMPORTEREN";

	public static final String ANDERMANS_STRUCTUREN = "ANDERMANS_STRUCTUREN";

	public static enum Status
	{
		IN_HERBEREKENING,
		BESCHIKBAAR,
		IN_ONDERHOUD,
		FOUTIEF;

		@Override
		public String toString()
		{
			return StringUtil.firstCharUppercase(StringUtil.convertCamelCase(name().toLowerCase()));
		}
	}

	public static enum Type
	{
		SUMMATIEF(RES_SUMMATIEF),
		FORMATIEF(RES_FORMATIEF);

		Type(String securityId)
		{
			this.securityId = securityId;
		}

		public String securityId;

		public String getSecurityId()
		{
			return securityId;
		}

		@Override
		public String toString()
		{
			return StringUtil.firstCharUppercase(name());
		}
	}

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Type type;

	@Column(nullable = false)
	private boolean specifiek;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "categorie")
	@Index(name = "idx_Resstructuur_categorie")
	@AutoForm(required = true, htmlClasses = "unit_max")
	private ResultaatstructuurCategorie categorie;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "cohort")
	@Index(name = "idx_Resstructuur_cohort")
	private Cohort cohort;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "onderwijsproduct")
	@Index(name = "idx_Resstructuur_ondprod")
	@AutoForm(htmlClasses = "unit_max")
	private Onderwijsproduct onderwijsproduct;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "auteur")
	@Index(name = "idx_Resstructuur_auteur")
	@AutoForm(readOnly = true)
	private Medewerker auteur;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;

	/**
	 * Nullable omdat het een 1-op-1 relatie is die eigenlijk aan allebei kanten not-null
	 * zouden moeten zijn. Dit is echter niet mogelijk omdat de twee records dan
	 * tegelijkertijd inserted zouden moeten worden.
	 */
	@Basic(optional = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "eindresultaat")
	@Index(name = "idx_Resstructuur_eindres")
	private Toets eindresultaat;

	/**
	 * Alle toetsen waar de resultaatstructuur uit bestaat
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "resultaatstructuur")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@BatchSize(size = 100)
	private List<Toets> toetsen = new ArrayList<Toets>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "resultaatstructuur")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<DeelnemerResultaatVersie> versies = new ArrayList<DeelnemerResultaatVersie>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "resultaatstructuur")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<ResultaatstructuurMedewerker> medewerkers =
		new ArrayList<ResultaatstructuurMedewerker>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "resultaatstructuur")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<ResultaatstructuurDeelnemer> deelnemers =
		new ArrayList<ResultaatstructuurDeelnemer>();

	public Resultaatstructuur()
	{
	}

	public Resultaatstructuur(Onderwijsproduct onderwijsproduct, Cohort cohort)
	{
		setOnderwijsproduct(onderwijsproduct);
		setCohort(cohort);
	}

	public Type getType()
	{
		return type;
	}

	public void setType(Type type)
	{
		this.type = type;
	}

	public boolean isSpecifiek()
	{
		return specifiek;
	}

	public void setSpecifiek(boolean specifiek)
	{
		this.specifiek = specifiek;
	}

	public ResultaatstructuurCategorie getCategorie()
	{
		return categorie;
	}

	public void setCategorie(ResultaatstructuurCategorie categorie)
	{
		this.categorie = categorie;
	}

	public Cohort getCohort()
	{
		return cohort;
	}

	public void setCohort(Cohort cohort)
	{
		this.cohort = cohort;
	}

	@Exportable()
	public Onderwijsproduct getOnderwijsproduct()
	{
		return onderwijsproduct;
	}

	public void setOnderwijsproduct(Onderwijsproduct onderwijsproduct)
	{
		this.onderwijsproduct = onderwijsproduct;
	}

	public Medewerker getAuteur()
	{
		return auteur;
	}

	public void setAuteur(Medewerker auteur)
	{
		this.auteur = auteur;
	}

	public Toets getEindresultaat()
	{
		return eindresultaat;
	}

	public void setEindresultaat(Toets eindresultaat)
	{
		this.eindresultaat = eindresultaat;
	}

	public void setToetsen(List<Toets> toetsen)
	{
		this.toetsen = toetsen;
	}

	public List<Toets> getToetsen()
	{
		return toetsen;
	}

	public int getDepth()
	{
		int ret = 0;
		for (Toets curToets : getToetsen())
		{
			int curDepth = curToets.getDepth();
			if (curDepth > ret)
				ret = curDepth;
		}
		return ret + 1;
	}

	public void setStatus(Status status)
	{
		this.status = status;
	}

	public Status getStatus()
	{
		return status;
	}

	public List<DeelnemerResultaatVersie> getVersies()
	{
		return versies;
	}

	public void setVersies(List<DeelnemerResultaatVersie> versies)
	{
		this.versies = versies;
	}

	public List<ResultaatstructuurMedewerker> getMedewerkers()
	{
		return medewerkers;
	}

	public void setMedewerkers(List<ResultaatstructuurMedewerker> medewerkers)
	{
		this.medewerkers = medewerkers;
	}

	public List<ResultaatstructuurDeelnemer> getDeelnemers()
	{
		return deelnemers;
	}

	public void setDeelnemers(List<ResultaatstructuurDeelnemer> deelnemers)
	{
		this.deelnemers = deelnemers;
	}

	public boolean isBewerkbaar()
	{
		return status.equals(Status.IN_ONDERHOUD);
	}

	public boolean isBeschikbaar()
	{
		return status.equals(Status.BESCHIKBAAR);
	}

	public boolean isInBerekening()
	{
		return status.equals(Status.IN_HERBEREKENING);
	}

	/**
	 * @return de toets met het gegeven toetscodepad. Voorbeeld van een toetscodepad:
	 *         EIND/SE/309
	 */
	@Exportable
	public Toets findToets(String toetsCodePath)
	{
		for (Toets curToets : getToetsen())
			if (curToets.getToetscodePath().equals(toetsCodePath))
				return curToets;
		return null;
	}

	public Toets findToets(String toetsCodePath, ToetsCodePathMode mode)
	{
		for (Toets curToets : getToetsen())
			if (curToets.getToetscodePath(mode).equals(toetsCodePath))
				return curToets;
		return null;
	}

	/**
	 * @return De unieke toets binnen deze resultaatstructuur van de gegeven soort. Soort
	 *         moet aangegeven zijn als uniek binnen de resultaatstructuur.
	 */
	public Toets getToets(SoortToets soort)
	{
		if (!soort.isUniekBinnenResultaatstructuur())
			throw new IllegalArgumentException("Soort moet uniek binnen de resultaatstructuur zijn");
		if (getEindresultaat() == null)
			return null;
		return getEindresultaat().getChild(soort);
	}

	@Override
	public String toString()
	{
		return "(" + getCohort() + ") " + onderwijsproduct;
	}

	public boolean isEindResultaatCompleet()
	{
		return getEindresultaat().getCode() != null && getEindresultaat().getNaam() != null
			&& getEindresultaat().getSchaal() != null
			&& getEindresultaat().getScoreschaal() != null;
	}

	public static ModelManager createModelManager()
	{
		return new DefaultModelManager(ResultaatstructuurDeelnemer.class,
			ResultaatstructuurMedewerker.class, Scoreschaalwaarde.class, ToetsVerwijzing.class,
			PersoonlijkeToetscode.class, Toets.class, Resultaatstructuur.class);
	}

	public boolean isGekoppeld(Medewerker medewerker)
	{
		if (!isSpecifiek())
			return true;

		for (ResultaatstructuurMedewerker curMedewerker : getMedewerkers())
			if (curMedewerker.getMedewerker().equals(medewerker))
				return true;
		return false;
	}
}
