package nl.topicus.eduarte.zoekfilters.dbs;

import nl.topicus.eduarte.entities.dbs.trajecten.Traject;
import nl.topicus.eduarte.entities.dbs.trajecten.ZorgvierkantKwadrant;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.providers.DeelnemerProvider;

public class TrajectZoekFilter extends AbstractZorgvierkantObjectZoekFilter<Traject>
{
	private static final long serialVersionUID = 1L;

	private ZorgvierkantKwadrant kwadrant;

	public TrajectZoekFilter()
	{
	}

	public TrajectZoekFilter(Account account, DeelnemerProvider deelnemer)
	{
		super(account, deelnemer, Traject.VERTROUWELIJK_TRAJECT);
	}

	public ZorgvierkantKwadrant getKwadrant()
	{
		return kwadrant;
	}

	public void setKwadrant(ZorgvierkantKwadrant kwadrant)
	{
		this.kwadrant = kwadrant;
	}

}
