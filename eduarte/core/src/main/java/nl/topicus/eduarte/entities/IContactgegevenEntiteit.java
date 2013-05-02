package nl.topicus.eduarte.entities;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.eduarte.entities.adres.SoortContactgegeven;

/**
 * Entiteit welke een enkel contactgegeven bevat.
 * 
 * @author hoeve
 */
public interface IContactgegevenEntiteit extends IdObject
{

	/**
	 * @return Returns the contactgegeven.
	 */
	public String getContactgegeven();

	/**
	 * @param contactgegeven
	 *            The contactgegeven to set.
	 */
	public void setContactgegeven(String contactgegeven);

	/**
	 * @return Returns the geheim.
	 */
	public boolean isGeheim();

	/**
	 * @param geheim
	 *            The geheim to set.
	 */
	public void setGeheim(boolean geheim);

	/**
	 * @return Returns the soortContactgegeven.
	 */
	public SoortContactgegeven getSoortContactgegeven();

	/**
	 * @param soortContactgegeven
	 *            The soortContactgegeven to set.
	 */
	public void setSoortContactgegeven(SoortContactgegeven soortContactgegeven);

	/**
	 * @return Returns the volgorde.
	 */
	public int getVolgorde();

	/**
	 * @param volgorde
	 *            The volgorde to set.
	 */
	public void setVolgorde(int volgorde);

	/**
	 * @return Het contactgegeven of '**********' als het geheim is.
	 */
	public String getFormattedContactgegeven();
}
