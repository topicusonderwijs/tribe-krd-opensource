package nl.topicus.eduarte.dao.participatie.helpers;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.helpers.ZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.olc.OlcLocatie;
import nl.topicus.eduarte.entities.participatie.olc.OlcWaarneming;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.participatie.zoekfilters.OlcWaarnemingZoekFilter;

public interface OlcWaarnemingDataAccessHelper extends
		ZoekFilterDataAccessHelper<OlcWaarneming, OlcWaarnemingZoekFilter>
{

	public List<OlcWaarneming> getWaarnemingen(OrganisatieEenheid organisatieEenheid,
			OlcLocatie locatie);

	// public List<OlcWaarneming> getWaarnemingenOpDatum(OrganisatieEenheid
	// organisatieEenheid,
	// OlcLocatie locatie, Date datum);

	public List<OlcWaarneming> getActieveWaarneming(OlcLocatie locatie, Deelnemer deelnemer);

	public boolean hasOlcWaarnemingen(OlcLocatie locatie);

	public boolean heeftOlcWaarneming(Deelnemer deelnemer, Date datum, Date tijd);

}
