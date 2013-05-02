package nl.topicus.eduarte.entities.adres.filter;

import nl.topicus.eduarte.entities.adres.AdresEntiteit;

public class FactuuradresFilter implements AdresFilter
{
	public FactuuradresFilter()
	{
	}

	@Override
	public boolean matches(AdresEntiteit< ? > adres)
	{
		return adres.isFactuuradres();
	}
}
