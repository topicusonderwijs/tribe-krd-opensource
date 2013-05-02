package nl.topicus.eduarte.entities.adres.filter;

import nl.topicus.eduarte.entities.adres.AdresEntiteit;

public class OnbeeindigdAdresFilter implements AdresFilter
{
	private boolean includeUnsaved;

	public OnbeeindigdAdresFilter(boolean includeUnsaved)
	{
		this.includeUnsaved = includeUnsaved;
	}

	@Override
	public boolean matches(AdresEntiteit< ? > adres)
	{
		return (includeUnsaved || adres.isSaved()) && adres.getEinddatum() == null;
	}
}
