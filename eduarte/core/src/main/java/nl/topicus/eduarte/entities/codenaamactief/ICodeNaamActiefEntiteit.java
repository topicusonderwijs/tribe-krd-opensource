package nl.topicus.eduarte.entities.codenaamactief;

import nl.topicus.cobra.entities.IActiefEntiteit;
import nl.topicus.eduarte.entities.ICodeNaamEntiteit;

/**
 * Interface voor alle entiteiten die een code, naam en actief kolom hebben
 * 
 * @author loite
 */
public interface ICodeNaamActiefEntiteit extends ICodeNaamEntiteit, IActiefEntiteit
{
	/**
	 * @return Returns the actief.
	 */
	public boolean isActief();

	/**
	 * @param actief
	 *            The actief to set.
	 */
	public void setActief(boolean actief);
}
