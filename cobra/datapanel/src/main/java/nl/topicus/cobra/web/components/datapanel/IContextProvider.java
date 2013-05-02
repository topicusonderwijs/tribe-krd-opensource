package nl.topicus.cobra.web.components.datapanel;

import java.awt.Image;
import java.io.Serializable;
import java.util.List;

public interface IContextProvider extends Serializable
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
