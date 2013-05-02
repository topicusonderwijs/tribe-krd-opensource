package nl.topicus.eduarte.web.components.resultaat;

import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;

public class StructuurToetsComparator extends AbstractToetsBoomComparator
{
	private static final long serialVersionUID = 1L;

	public StructuurToetsComparator(boolean ascending)
	{
		super(ascending);
	}

	@Override
	protected String getPathPostfix(Resultaatstructuur structuur)
	{
		return "";
	}

	@Override
	protected String getPathPrefix(Resultaatstructuur structuur)
	{
		return getResultaatstructuurPrefix(structuur);
	}

	@Override
	protected String getPathSegment(Toets toets)
	{
		return volgnummerToString(toets.getVolgnummer());
	}
}
