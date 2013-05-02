package nl.topicus.eduarte.dao.participatie.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.AfspraakType;
import nl.topicus.eduarte.participatie.zoekfilters.AfspraakTypeZoekFilter;

import org.hibernate.Criteria;

public interface AfspraakTypeDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<AfspraakType, AfspraakTypeZoekFilter>
{

	public Criteria addCriteria(Criteria criteria, AfspraakTypeZoekFilter filter,
			String afspraakTypeAlias);

	/**
	 * Geeft true als er een afspraak type is dat conflicteerd met het gegeven afspraak
	 * type.
	 * 
	 * @param type
	 * @return of hij bestaat
	 */
	public boolean conflicts(AfspraakType type);
}
