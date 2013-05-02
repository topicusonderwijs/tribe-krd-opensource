package nl.topicus.eduarte.converters;

import nl.topicus.cobra.util.StringUtil.StringConverter;
import nl.topicus.eduarte.entities.NaamEntiteit;

public class NaamEntiteitStringConverter implements StringConverter<NaamEntiteit>
{
	@Override
	public String getSeparator(int listIndex)
	{
		if (listIndex <= 0)
		{
			return null;
		}
		return ", ";
	}

	@Override
	public String toString(NaamEntiteit object, int listIndex)
	{
		return object != null ? object.getNaam() : "";
	}
}
