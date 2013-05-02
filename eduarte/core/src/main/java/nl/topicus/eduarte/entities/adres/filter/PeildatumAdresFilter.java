package nl.topicus.eduarte.entities.adres.filter;

import java.util.Date;

import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.entities.adres.AdresEntiteit;

public class PeildatumAdresFilter implements AdresFilter
{
	private Date peildatum;

	public PeildatumAdresFilter()
	{
		peildatum = EduArteContext.get().getPeildatumOfVandaag();
	}

	public PeildatumAdresFilter(Date peildatum)
	{
		this.peildatum = peildatum;
	}

	@Override
	public boolean matches(AdresEntiteit< ? > adres)
	{
		return adres.isActief(peildatum);
	}
}
