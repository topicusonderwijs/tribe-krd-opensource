package nl.topicus.eduarte.entities;

import nl.topicus.cobra.entities.IdObject;

/**
 * Interface voor alle entiteiten die een code, naam en actief kolom hebben
 * 
 * @author loite
 */
public interface ICodeNaamEntiteit extends IdObject
{

	/**
	 * @return Returns the code.
	 */
	public String getCode();

	/**
	 * @param code
	 *            The code to set.
	 */
	public void setCode(String code);

	/**
	 * @return Returns the naam.
	 */
	public String getNaam();

	/**
	 * @param naam
	 *            The naam to set.
	 */
	public void setNaam(String naam);
}
