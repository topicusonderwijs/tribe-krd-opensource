package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.entities.LandelijkEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public class CompetentieComponent extends LandelijkEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private int nummer;

	@Column(length = 128, nullable = false)
	private String titel;

	@ManyToOne(optional = false)
	@JoinColumn(name = "competentie", nullable = false)
	@Index(name = "idx_competentie_component")
	private Competentie competentie;

	public Competentie getCompetentie()
	{
		return competentie;
	}

	public void setCompetentie(Competentie competentie)
	{
		Asserts.assertNotNull("competentie", competentie);
		this.competentie = competentie;
	}

	/**
	 * @return the nummer
	 */
	public int getNummer()
	{
		return nummer;
	}

	/**
	 * @param nummer
	 *            the nummer to set
	 */
	public void setNummer(int nummer)
	{
		this.nummer = nummer;
	}

	/**
	 * @return the titel
	 */
	public String getTitel()
	{
		return titel;
	}

	/**
	 * @param titel
	 *            the titel to set
	 */
	public void setTitel(String titel)
	{
		Asserts.assertNotEmpty("titel", titel);
		this.titel = titel;
	}
}
