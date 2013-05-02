package nl.topicus.cobra.commons.interfaces;

import java.util.List;

/**
 * Basis klasse voor alle zoek filters. Deze klasse houdt vast wat het start item is dat
 * wordt opgehaald, en het maximum aantal resultaten dat opgehaald moet worden.
 * 
 * @param <T>
 */
public interface ZoekFilter<T>
{
	/**
	 * Voegt het property toe aan het begin van de order by list. Indien het property al
	 * het eerste element in de lijst is, wordt de sorteervolgorde geflipt.
	 * 
	 * @param property
	 *            Het property dat aan het begin van de order by list moet komen.
	 */
	public void addOrderByProperty(String property);

	/**
	 * @return Het eerste property in de order by list, of null indien de lijst leeg is.
	 */
	public String getOrderBy();

	public boolean isAscending();

	public void setAscending(boolean ascending);

	public List<String> getOrderByList();

	public void setOrderByList(List<String> orderByList);

	/**
	 * @param searchString
	 *            originele opgegeven searchcriteria inclusief wildcard '*';
	 * @return string welke de wildcards in de searchcriteria aanpast.
	 */
	public String likeGeneration(String searchString);

	/**
	 * Het filter bepaald of het resultaat wel of niet cachable is. Dit ivm
	 * {@DetachedCriteria} die soms toegevoegd worden waardoor het
	 * resultaat niet meer klopt als er gecached word. Default mogen resultaten gecached
	 * worden.
	 * 
	 * @return true als er gecached mag worden, anders false.
	 */
	public boolean isResultCacheable();

	/**
	 * Bepaal of het resultaat gecached mag worden.
	 * 
	 * @param cacheable
	 */
	public void setResultCacheable(boolean cacheable);
}
