package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import javax.persistence.Column;
import javax.persistence.Entity;

import nl.topicus.eduarte.entities.LandelijkEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author vandenbrink
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public class TaalType extends LandelijkEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 256)
	private String titel;

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
		this.titel = titel;
	}
}
