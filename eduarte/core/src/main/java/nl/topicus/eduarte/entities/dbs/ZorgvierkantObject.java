package nl.topicus.eduarte.entities.dbs;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.eduarte.providers.DeelnemerProvider;

/**
 * @author loite
 */
public interface ZorgvierkantObject extends IdObject, DeelnemerProvider
{
	/**
	 * Geeft aan of dit object vertrouwelijk is. vertrouwlijke objecten worden alleen
	 * getoond aan medewerkers met voldoende rechten.
	 * 
	 * @return true als dit object vertrouwelijk behandeld moet worden, anders false
	 */
	public boolean isVertrouwelijk();

	/**
	 * Geeft de eventuele zorglijn aan van dit object. mbv de zorglijn word bepaald of
	 * iemand voldoende is geauthoriseerd om dit object in te zien.
	 * 
	 * @return zorglijn of null als deze er niet is
	 */
	public Integer getZorglijn();

	public String getSecurityId();

	public String getVertrouwelijkSecurityId();

	public boolean isTonenInZorgvierkant();
}
