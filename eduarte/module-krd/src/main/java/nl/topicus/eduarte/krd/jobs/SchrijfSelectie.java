package nl.topicus.eduarte.krd.jobs;

import nl.topicus.cobra.util.StringUtil;

public enum SchrijfSelectie
{
	Criteria,
	Productregels,
	CriteriaEnProductregels;

	@Override
	public String toString()
	{
		return StringUtil.convertCamelCase(name());
	}
}