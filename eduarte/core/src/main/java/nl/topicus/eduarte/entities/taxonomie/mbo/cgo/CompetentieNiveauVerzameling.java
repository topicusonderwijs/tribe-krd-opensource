package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.SessionDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.MeeteenheidDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.providers.DeelnemerProvider;

import org.hibernate.Session;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;
import org.hibernate.proxy.HibernateProxy;

/**
 * @author vandebrink
 * @author vanharen
 * 
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(appliesTo = "CompetentieNiveauVerzameling")
public abstract class CompetentieNiveauVerzameling extends InstellingEntiteit implements
		Comparable<CompetentieNiveauVerzameling>, DeelnemerProvider
{
	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.DATE)
	@Column(nullable = true)
	private Date datum;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "deelnemer", nullable = true)
	@Index(name = "idx_competentieNV_deelnemer")
	private Deelnemer deelnemer;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "matrix", nullable = false)
	@Index(name = "idx_competentieNV_matrix")
	private CompetentieMatrix matrix;

	@OneToMany(mappedBy = "niveauVerzameling")
	protected List<CompetentieNiveau> competentieNiveaus = new ArrayList<CompetentieNiveau>();

	@Column(length = 100, nullable = false)
	private String naam;

	@Lob
	@Column(nullable = true)
	private String commentaar;

	@ManyToOne
	@Basic(optional = false)
	@JoinColumn(name = "meeteenheid", nullable = false)
	@Index(name = "idx_competentieNV_meeteenheid")
	private Meeteenheid meeteenheid;

	@ManyToOne(optional = true)
	@JoinColumn(name = "opleiding", nullable = true)
	@Index(name = "idx_competentieNV_OA")
	private Opleiding opleiding;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "cohort")
	@Index(name = "idx_compNivVerz_cohort")
	private Cohort cohort;

	public Opleiding getOpleiding()
	{
		return opleiding;
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = opleiding;
	}

	public Meeteenheid getMeeteenheid()
	{
		return meeteenheid;
	}

	public void setMeeteenheid(Meeteenheid meeteenheid)
	{
		this.meeteenheid = meeteenheid;
	}

	public CompetentieNiveauVerzameling()
	{
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public String getCommentaar()
	{
		return commentaar;
	}

	public void setCommentaar(String commentaar)
	{
		this.commentaar = commentaar;
	}

	public Date getDatum()
	{
		return datum;
	}

	public void setDatum(Date datum)
	{
		this.datum = datum;
	}

	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
	}

	public CompetentieMatrix getMatrix()
	{
		return matrix;
	}

	public void setMatrix(CompetentieMatrix matrix)
	{
		this.matrix = matrix;
	}

	public List<CompetentieNiveau> getCompetentieNiveaus()
	{
		return competentieNiveaus;
	}

	public void setCompetentieNiveaus(List<CompetentieNiveau> competentieNiveaus)
	{
		this.competentieNiveaus = competentieNiveaus;
	}

	public void addCompetentieNiveau(CompetentieNiveau competentieNiveau)
	{
		competentieNiveaus.add(competentieNiveau);
	}

	/**
	 * Zet de score op een leerpunt. Als score == null, dan wordt deze weggehaald.
	 * 
	 * @param leerpunt
	 * @param score
	 */
	public void setScore(Leerpunt leerpunt, MeeteenheidWaarde score)
	{
		Iterator<CompetentieNiveau> it = getCompetentieNiveaus().iterator();
		if (score == null)
		{
			while (it.hasNext())
			{
				if (it.next().getLeerpunt().equals(leerpunt))
				{
					it.remove();
					return;
				}
			}
		}
		else
		{
			while (it.hasNext())
			{
				CompetentieNiveau curNiveau = it.next();
				if (curNiveau.getLeerpunt().equals(leerpunt))
				{
					curNiveau.setScore(score);
					return;
				}
			}
			CompetentieNiveau newNiveau = new CompetentieNiveau();
			newNiveau.setLeerpunt(leerpunt);
			newNiveau.setScore(score);
			newNiveau.setNiveauVerzameling(this);
			competentieNiveaus.add(newNiveau);
		}
	}

	/**
	 * De ids van de competentieniveaus uit deze verzameling, wordt gebruikt voor opslaan
	 * van de verzameling.
	 * 
	 * @return de niveau ids
	 */
	public Set<Serializable> getNiveauIds()
	{
		Set<Serializable> ret = new HashSet<Serializable>();
		for (CompetentieNiveau curNiveau : getCompetentieNiveaus())
		{
			if (curNiveau.isSaved())
			{
				ret.add(curNiveau.getIdAsSerializable());
			}
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public void batchSave(Set<Serializable> niveauIds)
	{

		saveOrUpdate();
		for (CompetentieNiveau curNiveau : new ArrayList<CompetentieNiveau>(getCompetentieNiveaus()))
		{
			niveauIds.remove(curNiveau.getIdAsSerializable());
			curNiveau.saveOrUpdate();
		}
		BatchDataAccessHelper dah = DataAccessRegistry.getHelper(BatchDataAccessHelper.class);
		Session session =
			DataAccessRegistry.getHelper(SessionDataAccessHelper.class)
				.getHibernateSessionProvider().getSession();
		for (Serializable curId : niveauIds)
		{
			dah.delete(session.load(CompetentieNiveau.class, curId));
		}

		CompetentieMatrix theMatrix = getMatrix();
		MeeteenheidKoppelType type;
		@SuppressWarnings("hiding")
		Opleiding opleiding = getOpleiding();
		boolean updateMeeteenheidKoppel = true;
		if (theMatrix instanceof HibernateProxy)
		{
			theMatrix =
				(CompetentieMatrix) ((HibernateProxy) theMatrix).getHibernateLazyInitializer()
					.getImplementation();
		}
		if (theMatrix instanceof Uitstroom)
		{
			type = MeeteenheidKoppelType.Algemeen;
		}
		else if (theMatrix instanceof Kwalificatiedossier)
		{
			type = MeeteenheidKoppelType.Algemeen;
		}
		else if (theMatrix instanceof LLBMatrix)
		{
			type = MeeteenheidKoppelType.LLB;
		}
		else if (theMatrix instanceof VrijeMatrix)
		{
			type = MeeteenheidKoppelType.Vrij;
			if (((VrijeMatrix) theMatrix).getDeelnemer() == null)
				updateMeeteenheidKoppel = false;
		}
		else
		{
			throw new IllegalArgumentException("Cannot determine Meeteenheid for "
				+ theMatrix.getClass() + " yet");
		}

		if (updateMeeteenheidKoppel)
		{
			MeeteenheidKoppel koppel =
				DataAccessRegistry.getHelper(MeeteenheidDataAccessHelper.class)
					.getMeeteenheidKoppeling(opleiding, cohort, type);
			if (koppel != null)
			{
				koppel.setVastgezet(true);
			}
			else
			{
				koppel = new MeeteenheidKoppel();
				koppel.setAutomatischAangemaakt(true);
				koppel.setVastgezet(true);
				koppel.setMeeteenheid(getMeeteenheid());
				koppel.setOpleiding(getOpleiding());
				koppel.setType(type);
				koppel.setCohort(cohort);
			}
			koppel.saveOrUpdate();
		}

	}

	/**
	 * De competentieniveaus in de vorm van een map, dit is handig als je bijv. bij het
	 * populaten van een CompetentieMatrixScorePanel wilt weten welk niveau bij een
	 * bepaald leerpunt uit een {@link CompetentieMatrix} hoort.
	 * 
	 * @return de niveaus
	 */
	public Map<Leerpunt, CompetentieNiveau> getCompetentieNiveauAsMap()
	{
		Map<Leerpunt, CompetentieNiveau> ret = new HashMap<Leerpunt, CompetentieNiveau>();
		for (CompetentieNiveau curNiveau : getCompetentieNiveaus())
		{
			ret.put(curNiveau.getLeerpunt(), curNiveau);
		}
		return ret;
	}

	/**
	 * Geeft de naam van het type, voor de meeste subclasses is dit de class name, maar
	 * dit kan overschreven worden (bijv. uitstromen geven hier "Dossier uitstroom" of
	 * "Standaard uitstroom" naar gelang het uitstroomtype.)
	 * 
	 * @return de naam
	 */
	public abstract String getTypeNaam();

	@Override
	public int compareTo(CompetentieNiveauVerzameling o)
	{
		return o.getId().compareTo(o.getId());
	}

	public Cohort getCohort()
	{
		return cohort;
	}

	public void setCohort(Cohort cohort)
	{
		this.cohort = cohort;
	}

}
