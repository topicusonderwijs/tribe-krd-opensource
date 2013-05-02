package nl.topicus.eduarte.entities.sidebar;

/**
 * Interface voor objecten die als context info door een pagina geleverd kan worden.
 * 
 * @author loite
 */
public interface IContextInfoObject
{

	/**
	 * @return Een omschrijving van dit object wanneer dit als context informatie getoond
	 *         moet worden.
	 */
	public String getContextInfoOmschrijving();

}
