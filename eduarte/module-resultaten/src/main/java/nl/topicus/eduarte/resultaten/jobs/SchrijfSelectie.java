package nl.topicus.eduarte.resultaten.jobs;

import nl.topicus.cobra.util.StringUtil;

public enum SchrijfSelectie
{
	Resultaatstructuren,
	Toetsverwijzingen,
	StructurenEnVerwijzingen;

	@Override
	public String toString()
	{
		return StringUtil.convertCamelCase(name());
	}
}