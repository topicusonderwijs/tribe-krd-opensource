package nl.topicus.eduarte.entities.adres.filter;

import nl.topicus.eduarte.entities.adres.AdresEntiteit;

public interface AdresFilter
{
	public boolean matches(AdresEntiteit< ? > adres);
}
