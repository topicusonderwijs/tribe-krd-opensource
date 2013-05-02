/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import java.util.Date;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.app.signalering.EventAbonnementType;
import nl.topicus.eduarte.app.signalering.EventReceiver;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.OrganisatieMedewerker;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.zoekfilters.MedewerkerZoekFilter;

import org.hibernate.criterion.Conjunction;

public interface MedewerkerDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Medewerker, MedewerkerZoekFilter>
{
	/**
	 * Haal medewerker met een bepaald id op.
	 * 
	 * @param id
	 * @return medewerker met dit id of null als die niet bestaat.
	 */
	public Medewerker get(Long id);

	/**
	 * Haalt medewerker op mbv persoonsgegevens.
	 * 
	 * @param persoon
	 * @return medewerker of null als deze niet gevonden wordt
	 */
	public Medewerker get(Persoon persoon);

	/**
	 * @param afkorting
	 * @return de medewerker met de gegeven afkorting
	 */
	public Medewerker batchGetByAfkorting(String afkorting);

	/**
	 * Controle of een medewerker docent is van een deelnemer.
	 * 
	 * @param docent
	 * @param deelnemer
	 * @param peilDtaum
	 * @return true als de medewerker docent is van de deelnemer, anders false
	 */
	public boolean isDocentVan(Medewerker docent, Deelnemer deelnemer, Date peilDtaum);

	/**
	 * Controle of een medewerker begeleider / mentor is van een deelnemer.
	 * 
	 * @param begeleider
	 * @param deelnemer
	 * @param peilDatum
	 * @return true als de medewerker begeleider is van de deelnemer, anders false
	 */
	public boolean isBegeleiderVan(Medewerker begeleider, Deelnemer deelnemer, Date peilDatum);

	public List<Medewerker> getDocentenVan(Deelnemer deelnemer, Date peilDatum);

	public List<Medewerker> getBegeleidersVan(Deelnemer deelnemer, Date peilDatum);

	/**
	 * @param multiZoek
	 * @return criteria met de multizoekcriteria
	 */
	public Conjunction addMultiZoekCriteria(String multiZoek);

	public List<OrganisatieMedewerker> getOrganisatieMedewerkers(Medewerker medewerker,
			boolean instellingAuthorized, boolean orgEhdAuthorized);

	public Map<EventAbonnementType, List< ? extends EventReceiver>> getEventReceivers(
			Deelnemer deelnemer);
}
