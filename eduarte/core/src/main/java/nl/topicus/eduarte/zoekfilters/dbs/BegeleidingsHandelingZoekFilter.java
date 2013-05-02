package nl.topicus.eduarte.zoekfilters.dbs;

import nl.topicus.eduarte.entities.dbs.trajecten.BegeleidingsHandeling;
import nl.topicus.eduarte.entities.dbs.trajecten.Traject;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.providers.DeelnemerProvider;

public class BegeleidingsHandelingZoekFilter extends
		AbstractBegeleidingsHandelingZoekFilter<BegeleidingsHandeling>
{
	private static final long serialVersionUID = 1L;

	public BegeleidingsHandelingZoekFilter()
	{
	}

	public BegeleidingsHandelingZoekFilter(Account account, DeelnemerProvider deelnemer)
	{
		super(account, deelnemer);
	}

	public BegeleidingsHandelingZoekFilter(Account account, Traject traject)
	{
		super(account, traject);
	}
}
