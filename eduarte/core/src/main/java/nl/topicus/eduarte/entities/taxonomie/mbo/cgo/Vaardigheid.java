package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.organisatie.LandelijkOfInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Vaardigheden behorende bij een kwalificatiedossier.
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public class Vaardigheid extends LandelijkOfInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private int nummer;

	@Column(length = 256, nullable = false)
	private String titel;

	@ManyToOne(optional = true)
	@JoinColumn(name = "dossier", nullable = true)
	@Index(name = "idx_vaardigheid_dossier")
	private Kwalificatiedossier dossier;

	protected Vaardigheid()
	{
	}

	public Vaardigheid(EntiteitContext context)
	{
		super(context);
	}

	/**
	 * @return the nummer
	 */
	public int getNummer()
	{
		return nummer;
	}

	/**
	 * @return the titel
	 */
	public String getTitel()
	{
		return titel;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return nummer + " " + titel;
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
	 * @param titel
	 *            the titel to set
	 */
	public void setTitel(String titel)
	{
		this.titel = titel;
	}

	/**
	 * Geeft het dossier.
	 * 
	 * @return het dossier waartoe deze vaardigheid behoort
	 */
	public Kwalificatiedossier getDossier()
	{
		return dossier;
	}

	/**
	 * Zet het dossier.
	 * 
	 * @param dossier
	 *            het dossier waartoe deze vaardigheid behoort.
	 */
	public void setDossier(Kwalificatiedossier dossier)
	{
		this.dossier = dossier;
	}
}
