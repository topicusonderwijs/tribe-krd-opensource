package nl.topicus.eduarte.zoekfilters.dbs;

import nl.topicus.eduarte.entities.dbs.testen.DeelnemerTest;
import nl.topicus.eduarte.entities.dbs.testen.TestDefinitie;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.providers.DeelnemerProvider;

import org.apache.wicket.model.IModel;

public class DeelnemerTestZoekFilter extends AbstractZorgvierkantObjectZoekFilter<DeelnemerTest>
{
	private static final long serialVersionUID = 1L;

	private IModel<TestDefinitie> testDefinitie;

	public DeelnemerTestZoekFilter()
	{
	}

	public DeelnemerTestZoekFilter(Account account, DeelnemerProvider deelnemer)
	{
		super(account, deelnemer, DeelnemerTest.VERTROUWELIJKE_DEELNEMERTEST);
	}

	public TestDefinitie getTestDefinitie()
	{
		return getModelObject(testDefinitie);
	}

	public void setTestDefinitie(TestDefinitie testDefinitie)
	{
		this.testDefinitie = makeModelFor(testDefinitie);
	}
}
