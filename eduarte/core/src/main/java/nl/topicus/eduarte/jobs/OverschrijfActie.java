package nl.topicus.eduarte.jobs;

import nl.topicus.cobra.util.StringUtil;

public enum OverschrijfActie
{
	Overslaan,
	Overschrijven;

	@Override
	public String toString()
	{
		return StringUtil.convertCamelCase(name());
	}
}