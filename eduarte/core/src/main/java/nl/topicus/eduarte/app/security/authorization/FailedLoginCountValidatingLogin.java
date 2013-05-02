package nl.topicus.eduarte.app.security.authorization;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.dao.helpers.AccountDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.SettingsDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Organisatie;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.settings.LoginConfiguration;
import nl.topicus.eduarte.entities.settings.LoginSetting;

import org.apache.wicket.Application;
import org.apache.wicket.security.authentication.LoginException;

public class FailedLoginCountValidatingLogin implements LoginStrategy
{
	private String username;

	private LoginStrategy wrapped;

	private Organisatie organisatie;

	public FailedLoginCountValidatingLogin(String username, Organisatie organisatie,
			LoginStrategy wrapped)
	{
		this.username = username;
		this.organisatie = organisatie;
		this.wrapped = wrapped;
	}

	@Override
	public Account login() throws LoginException
	{
		int attempts = EduArteApp.get().getLoginAttempts(username, organisatie);

		AccountDataAccessHelper accounts =
			DataAccessRegistry.getHelper(AccountDataAccessHelper.class);
		SettingsDataAccessHelper settings =
			DataAccessRegistry.getHelper(SettingsDataAccessHelper.class);
		LoginConfiguration loginConf = settings.getSetting(LoginSetting.class).getValue();

		if (loginConf.isActief() && attempts >= loginConf.getPogingen())
		{
			Account account = accounts.getAccount(username);
			if (account != null)
			{
				// als we hier een ander veld voor gebruiken kunnen we bij latere pogingen
				// vertellen dat account geblocked is.
				account.setActief(false);
				account.save();
				account.commit();
			}
			throw new LoginException(getLocalizedMessage("exception.login.tomany.attempts",
				"To many failed login attemps, account blocked")).setLoginContext(this);
		}
		EduArteApp.get().incrementLoginAttempt(username, organisatie);
		return wrapped.login();
	}

	private String getLocalizedMessage(String key, String defaultMsg)
	{
		return Application.get().getResourceSettings().getLocalizer().getString(key, null,
			defaultMsg);
	}
}
