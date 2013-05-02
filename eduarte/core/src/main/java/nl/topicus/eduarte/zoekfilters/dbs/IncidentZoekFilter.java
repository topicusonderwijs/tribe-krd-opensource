package nl.topicus.eduarte.zoekfilters.dbs;

import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.eduarte.entities.dbs.gedrag.Incident;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.providers.DeelnemerProvider;

public class IncidentZoekFilter extends AbstractZorgvierkantObjectZoekFilter<Incident>
{
	private static final long serialVersionUID = 1L;

	public IncidentZoekFilter()
	{
	}

	public IncidentZoekFilter(Account account, DeelnemerProvider deelnemer)
	{
		super(account, deelnemer, Incident.VERTROUWELIJK_INCIDENT);
	}

	@Override
	public void addCriteria(CriteriaBuilder builder)
	{
		builder.createAlias("betrokkene", "betrokkene");
		builder.createAlias("betrokkene.irisIncident", "irisIncident");

		if (isMaxZorglijnSet())
		{
			if (getMaxZorglijn() == null)
				builder.addIsNull("irisIncident.zorglijn", true);
			else
				builder.addNullOrLessOrEquals("irisIncident.zorglijn", getMaxZorglijn());
		}
		if (!isVertrouwelijkAllowed())
			builder.addEquals("irisIncident.vertrouwelijk", false);
	}
}
