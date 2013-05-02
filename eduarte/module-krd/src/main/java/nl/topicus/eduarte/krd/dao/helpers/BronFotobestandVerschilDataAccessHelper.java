package nl.topicus.eduarte.krd.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.ZoekFilterDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.foto.BronFotobestand;
import nl.topicus.eduarte.krd.entities.bron.foto.BronFotobestandVerschil;
import nl.topicus.eduarte.krd.zoekfilters.BronFotobestandVerschilZoekFilter;

public interface BronFotobestandVerschilDataAccessHelper extends
		ZoekFilterDataAccessHelper<BronFotobestandVerschil, BronFotobestandVerschilZoekFilter>
{
	public List<BronFotobestandVerschil> getVerschillenList(BronFotobestand bronFotobestand);
}
