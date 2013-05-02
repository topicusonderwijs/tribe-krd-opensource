package nl.topicus.eduarte.app.security.authorization;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.AccountDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Organisatie;
import nl.topicus.eduarte.entities.security.authentication.Account;

import org.apache.wicket.security.authentication.LoginException;

/**
 * Login voor systeembeheer waarmee je als een andere gebruiker van een andere organisatie
 * kan aanmelden. Dit kan natuurlijk alleen als je zelf wel succesvol geauthenticeerd bent
 * voor het landelijk beheer.
 */
public class UserSpoofingLogin implements LoginStrategy
{
	private final String aanmeldenAls;

	private final Organisatie aanmeldenOp;

	private final LoginStrategy wrapped;

	public UserSpoofingLogin(String aanmeldenAls, Organisatie aanmeldenOp, LoginStrategy wrapped)
	{
		this.aanmeldenAls = aanmeldenAls;
		this.aanmeldenOp = aanmeldenOp;
		this.wrapped = wrapped;
	}

	@Override
	public Account login() throws LoginException
	{
		Account account = wrapped.login();

		if (account == null)
			return null;

		boolean spoofAccount = aanmeldenAls != null && aanmeldenOp != null;

		boolean isLandelijkBeheerder =
			account.getOrganisatie().getRechtenSoort() == RechtenSoort.BEHEER;
		if (spoofAccount && isLandelijkBeheerder)
		{
			AanmeldenAlsLoginRunner runner = new AanmeldenAlsLoginRunner();
			EduArteContext.get().runInContext(runner, aanmeldenOp);
			return runner.account;
		}
		return account;
	}

	private class AanmeldenAlsLoginRunner implements Runnable
	{
		private Account account;

		@Override
		public void run()
		{
			AccountDataAccessHelper helper =
				DataAccessRegistry.getHelper(AccountDataAccessHelper.class);
			account = helper.getAccount(aanmeldenAls);
		}
	}
}
