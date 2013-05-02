package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

public class NaamActiefZoekFilter<T extends InstellingEntiteit> extends
		AbstractNaamActiefZoekFilter<T>
{

	private static final long serialVersionUID = 1L;

	public NaamActiefZoekFilter(Class<T> entityClass)
	{
		super(entityClass);
	}

}
