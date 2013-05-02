package nl.topicus.eduarte.entities.adres.filter;

import nl.topicus.eduarte.entities.adres.AdresEntiteit;

public class PostadresFilter implements AdresFilter
{
	public PostadresFilter()
	{
	}

	@Override
	public boolean matches(AdresEntiteit< ? > adres)
	{
		return adres.isPostadres();
	}
}
