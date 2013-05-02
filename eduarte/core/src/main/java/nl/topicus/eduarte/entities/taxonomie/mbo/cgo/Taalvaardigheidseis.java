package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Index;

/**
 * Eis voor een specifieke taal voor een uitstroom of andere competentiematrix.
 * 
 * @author loite
 */
@Entity
public class Taalvaardigheidseis extends TaalscoreNiveauVerzameling
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "taalType", nullable = true)
	@Index(name = "idx_taalVE_taalType")
	private TaalType taalType;

	/**
	 * @return Returns the taalType.
	 */
	public TaalType getTaalType()
	{
		return taalType;
	}

	/**
	 * @param taalType
	 *            The taalType to set.
	 */
	public void setTaalType(TaalType taalType)
	{
		this.taalType = taalType;
	}

}
