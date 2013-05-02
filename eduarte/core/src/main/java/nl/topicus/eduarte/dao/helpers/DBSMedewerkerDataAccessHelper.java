/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.zoekfilters.IVerantwoordelijkeUitvoerendeZoekFilter;

import org.hibernate.criterion.Criterion;

/**
 * Uitbreidingen op de MedewerkerDataAccessHelper. De implementatie bevindt zich in
 * module-dbs.
 * 
 * @author papegaaij
 */
public interface DBSMedewerkerDataAccessHelper extends BatchDataAccessHelper<Medewerker>
{
	/**
	 * Controle of een medewerker verantwoordelijke is van een traject van de deelnemer.
	 */
	public boolean isVerantwoordelijkeVan(Medewerker medewerker, Deelnemer deelnemer, Date peilDatum);

	/**
	 * Controle of een medewerker uitvoerende is van een traject van de deelnemer.
	 */
	public boolean isUitvoerendeVan(Medewerker medewerker, Deelnemer deelnemer, Date peilDatum);

	public List<Medewerker> getVerantwoordelijkenVan(Deelnemer deelnemer, Date peilDatum);

	public List<Medewerker> getUitvoerendenVan(Deelnemer deelnemer, Date peilDatum);

	public List<Criterion> createVerantwoordelijkeUitvoerende(
			IVerantwoordelijkeUitvoerendeZoekFilter< ? > filter, String deelnemerAlias);
}
