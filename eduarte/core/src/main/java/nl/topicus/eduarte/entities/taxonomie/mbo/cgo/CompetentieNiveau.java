package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.UniqueConstraint;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

/**
 * Een door een Deelnemer op een bepaalde datum behaald niveau van een competentie.
 * 
 * @author vandenbrink
 */
@Entity
@BatchSize(size = 1000)
@Table(appliesTo = "CompetentieNiveau")
@javax.persistence.Table(uniqueConstraints = @UniqueConstraint(columnNames = {"leerpunt",
	"niveauVerzameling"}))
public class CompetentieNiveau extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "score", nullable = true)
	@Index(name = "idx_competentieniveau_score")
	private MeeteenheidWaarde score;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "leerpunt", nullable = false)
	@Index(name = "idx_competentieniveau_leerpunt")
	private Leerpunt leerpunt;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "niveauVerzameling", nullable = false)
	@Index(name = "idx_competentieN_niveauV")
	private CompetentieNiveauVerzameling niveauVerzameling;

	public MeeteenheidWaarde getScore()
	{
		return score;
	}

	public void setScore(MeeteenheidWaarde score)
	{
		this.score = score;
	}

	public Leerpunt getLeerpunt()
	{
		return leerpunt;
	}

	public void setLeerpunt(Leerpunt leerpunt)
	{
		this.leerpunt = leerpunt;
	}

	public CompetentieNiveauVerzameling getNiveauVerzameling()
	{
		return niveauVerzameling;
	}

	public void setNiveauVerzameling(CompetentieNiveauVerzameling niveauVerzameling)
	{
		this.niveauVerzameling = niveauVerzameling;
	}
}
