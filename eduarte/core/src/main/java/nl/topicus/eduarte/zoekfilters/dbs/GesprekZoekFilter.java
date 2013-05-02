package nl.topicus.eduarte.zoekfilters.dbs;

import nl.topicus.eduarte.entities.dbs.trajecten.Gesprek;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.providers.DeelnemerProvider;

public class GesprekZoekFilter extends AbstractBegeleidingsHandelingZoekFilter<Gesprek>
{
	private static final long serialVersionUID = 1L;

	public GesprekZoekFilter()
	{
	}

	public GesprekZoekFilter(Account account, DeelnemerProvider deelnemer)
	{
		super(account, deelnemer);
	}
}
