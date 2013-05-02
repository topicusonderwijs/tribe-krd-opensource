package nl.topicus.eduarte.xml.adapters;

import nl.topicus.cobra.xml.adapters.AbstractEnumAdapter;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur.Type;

public class ResultaatstructuurTypeAdapter extends AbstractEnumAdapter<Type>
{
	public ResultaatstructuurTypeAdapter()
	{
		super(Type.class);
	}
}
