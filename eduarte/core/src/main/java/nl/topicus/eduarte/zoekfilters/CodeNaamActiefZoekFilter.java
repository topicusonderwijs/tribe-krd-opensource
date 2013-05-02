package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.codenaamactief.ICodeNaamActiefEntiteit;

/**
 * Zoekfilter voor entiteiten met een code en naam en actief kolom
 * 
 * @author loite
 * @param <T>
 */
public class CodeNaamActiefZoekFilter<T extends ICodeNaamActiefEntiteit> extends
		AbstractCodeNaamActiefZoekFilter<T>
{
	private static final long serialVersionUID = 1L;

	public CodeNaamActiefZoekFilter(Class<T> entityClass)
	{
		super(entityClass);
	}
}
