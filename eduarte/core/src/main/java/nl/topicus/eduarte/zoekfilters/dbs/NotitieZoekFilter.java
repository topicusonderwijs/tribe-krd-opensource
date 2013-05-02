package nl.topicus.eduarte.zoekfilters.dbs;

import nl.topicus.eduarte.entities.dbs.gedrag.Notitie;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.providers.DeelnemerProvider;

public class NotitieZoekFilter extends AbstractZorgvierkantObjectZoekFilter<Notitie>
{
	private static final long serialVersionUID = 1L;

	public NotitieZoekFilter()
	{
	}

	public NotitieZoekFilter(Account account, DeelnemerProvider deelnemer)
	{
		super(account, deelnemer, Notitie.VERTROUWELIJKE_NOTITIES);
	}
}
