/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.IHierarchischeOrganisatieEenheidEntiteit;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.organisatie.SoortOrganisatieEenheid;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidZoekFilter;

/**
 * @author loite
 */
public interface OrganisatieEenheidDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<OrganisatieEenheid, OrganisatieEenheidZoekFilter>
{
	/**
	 * Haalt organisatie eenheid op per id.
	 * 
	 * @param id
	 * @return eenheid of null als deze niet gevonden wordt
	 */
	public OrganisatieEenheid get(Long id);

	/**
	 * Geeft alle actieve organisatie-eenheden van de gegeven instelling. Dit zijn de
	 * organisatie-eenheden die onder de instelling vallen, en niet de externe
	 * organisatie-eenheden.
	 * 
	 * @return Een lijst met alle active organisatie-eenheden van de gegeven instelling.
	 */
	public List<OrganisatieEenheid> list();

	/**
	 * De overkoepelende organisatie eenheid behorende bij de instelling (die zelf dus
	 * geen parent organisatie meer heeft)
	 * 
	 * @return de top-most overkoepelende organisatie eenheid
	 */
	public OrganisatieEenheid getRootOrganisatieEenheid();

	/**
	 * Geeft de organisatie-eenheid met de gegeven code
	 * 
	 * @param code
	 *            De code die gezocht moet worden.
	 * @return De organisatie-eenheid met de gegeven code
	 */
	public SoortOrganisatieEenheid getSoortOrganisatieEenheid(String code);

	/**
	 * Geeft de organisatie-eenheid met de gegeven naam
	 * 
	 * @param naam
	 *            De naam, case insensitive.
	 * @return De organisatie-eenheid met de gegeven naam
	 */
	public OrganisatieEenheid getByNaam(String naam);

	/**
	 * Een lijst van organisatie-eenheden die hierarchisch gezien lager staan dan de
	 * opgegeven {@link OrganisatieEenheid}. De lijst bevat tevens de opgegeven
	 * {@link OrganisatieEenheid}.
	 * 
	 * @param organisatieEenheid
	 * @return lijst (volgorde niet gedefinieerd)
	 */
	public List<OrganisatieEenheid> getChildren(OrganisatieEenheid organisatieEenheid,
			Date peilDatum);

	/**
	 * Een lijst van organisatie-eenheden die een niveau lager in de hierarchie staan. De
	 * lijst bevat NIET de opgegeven organisatie-eenheid.
	 * 
	 * @param organisatieEenheid
	 * @return lijst
	 */
	public List<OrganisatieEenheid> getDirectChildren(OrganisatieEenheid organisatieEenheid,
			Date peilDatum);

	/**
	 * Een lijst met alle hoger gelegen organisatieeenheden, inclusief de gegeven eenheid.
	 * De hoogste eenheid is de eerste in de lijst.
	 * 
	 * @param organisatieEenheid
	 * @return ljst
	 */
	public List<OrganisatieEenheid> getAncestors(OrganisatieEenheid organisatieEenheid);

	/**
	 * Verwijderd alle entiteiten uit de lijst die worden overschreven in de
	 * organisatieeenheid hierarchie.
	 * 
	 * @param entiteiten
	 * @param organisatieEenheid
	 *            De diepst gelegen organisatieeenheid.
	 */
	public void removeOverrides(
			List< ? extends IHierarchischeOrganisatieEenheidEntiteit> entiteiten,
			OrganisatieEenheid organisatieEenheid);

	/**
	 * @param afkorting
	 * @return de organisatieeenheid met de meegegeven afkorting
	 */
	public OrganisatieEenheid get(String afkorting);
}
