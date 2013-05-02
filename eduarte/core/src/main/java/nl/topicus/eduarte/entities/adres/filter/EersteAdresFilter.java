package nl.topicus.eduarte.entities.adres.filter;

import nl.topicus.eduarte.entities.adres.AdresEntiteit;

public class EersteAdresFilter implements AdresFilter
{
	private boolean first = true;

	public EersteAdresFilter()
	{
	}

	@Override
	public boolean matches(AdresEntiteit< ? > adres)
	{
		boolean ret = first;
		first = false;
		return ret;
	}
}
