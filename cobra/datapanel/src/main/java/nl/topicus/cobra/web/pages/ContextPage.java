package nl.topicus.cobra.web.pages;

import java.awt.Image;
import java.util.List;

import nl.topicus.cobra.web.components.datapanel.CriteriaBean;
import nl.topicus.cobra.web.components.datapanel.IContextProvider;

/**
 * Bevat methodes voor het doorgeven van context informatie aan CustomDataPanel
 * 
 * @author papegaaij
 */
public interface ContextPage extends IContextProvider
{
	public String getContextOmschrijving();

	/**
	 * @return De naam van de ingelogde gebruiker.
	 */
	public String getIngelogdeGebruikersNaam();

	/**
	 * @return De naam van de organisatie van de ingelogde gebruiker.
	 */
	public String getOrganisatieNaam();

	/**
	 * @return De criteria waar op gezocht wordt.
	 */
	public List<CriteriaBean> getZoekCriteria();

	/**
	 * @return Het logo van de instelling
	 */
	public Image getInstellingsLogo();
}
