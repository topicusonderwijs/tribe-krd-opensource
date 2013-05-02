package nl.topicus.eduarte.entities.adres.filter;

import nl.topicus.eduarte.entities.adres.AdresEntiteit;

public class AdresCompositeFilter implements AdresFilter
{
	private AdresFilter[] filters;

	public AdresCompositeFilter(AdresFilter... filters)
	{
		this.filters = filters;
	}

	@Override
	public boolean matches(AdresEntiteit< ? > adres)
	{
		for (AdresFilter curFilter : filters)
		{
			if (!curFilter.matches(adres))
				return false;
		}
		return true;
	}
}
