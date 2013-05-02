package nl.topicus.eduarte.entities.adres.filter;

import nl.topicus.eduarte.entities.adres.AdresEntiteit;

public class FysiekadresFilter implements AdresFilter
{
	public FysiekadresFilter()
	{
	}

	@Override
	public boolean matches(AdresEntiteit< ? > adres)
	{
		return adres.isFysiekadres();
	}
}
