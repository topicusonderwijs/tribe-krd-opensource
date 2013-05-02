package nl.topicus.eduarte.entities.jobs.logging;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * jobrun met extra's om bij te houden of een jobrun terug gedraaid is
 * 
 * @see JobRun
 * @author Henzen
 */
@Entity()
public abstract class TerugdraaibareJobRun extends JobRun
{

	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	@Basic(optional = false)
	private boolean teruggedraaid;

	/**
	 * constructor voor hibernate
	 */
	public TerugdraaibareJobRun()
	{

	}

	/**
	 * @return Returns the teruggedraaid.
	 */
	public boolean isTeruggedraaid()
	{
		return teruggedraaid;
	}

	/**
	 * @param teruggedraaid
	 *            The teruggedraaid to set.
	 */
	public void setTeruggedraaid(boolean teruggedraaid)
	{
		this.teruggedraaid = teruggedraaid;
	}

	/**
	 * @return Ja indien teruggedraaid en anders Nee
	 */
	public String getTeruggedraaidOmschrijving()
	{
		return isTeruggedraaid() ? "Ja" : "Nee";
	}

}
