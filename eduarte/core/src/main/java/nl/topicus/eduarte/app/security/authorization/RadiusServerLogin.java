package nl.topicus.eduarte.app.security.authorization;

import java.io.IOException;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.AccountDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.SettingsDataAccessHelper;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.security.authorization.AuthorisatieNiveau;
import nl.topicus.eduarte.entities.settings.RadiusServerConfiguration;
import nl.topicus.eduarte.entities.settings.RadiusServerSetting;

import org.apache.wicket.security.authentication.LoginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinyradius.util.RadiusClient;
import org.tinyradius.util.RadiusException;

public class RadiusServerLogin implements LoginStrategy
{
	private static final Logger log = LoggerFactory.getLogger(RadiusServerLogin.class);

	private final String username;

	private final String password;

	private final boolean omzeilRadiusServer;

	private Account account;

	public RadiusServerLogin(String username, String password, boolean omzeilRadiusServer)
	{
		this.username = username;
		this.password = password;
		this.omzeilRadiusServer = omzeilRadiusServer;
	}

	@Override
	public Account login() throws LoginException
	{
		AccountDataAccessHelper accounts =
			DataAccessRegistry.getHelper(AccountDataAccessHelper.class);
		account = accounts.getAccount(username);
		if (loginOpRadiusServer())
		{
			return account;
		}
		return null;
	}

	private boolean loginOpRadiusServer() throws LoginException
	{
		SettingsDataAccessHelper settings =
			DataAccessRegistry.getHelper(SettingsDataAccessHelper.class);
		RadiusServerConfiguration server =
			settings.getSetting(RadiusServerSetting.class).getValue();

		if (!server.isActief() || account == null)
			return false;

		if (omzeilRadiusServer && account.getAuthorisatieNiveau() == AuthorisatieNiveau.REST)
		{
			throw new LoginException(
				"Enkel gebruikers met autorisatieniveau superuser of applicatiebeheerder "
					+ "mogen aanmelden zonder Radius token");
		}
		else if (omzeilRadiusServer)
		{
			return false;
		}
		try
		{
			RadiusClient rc = new RadiusClient(server.getHost(), server.getWachtwoord());
			rc.setAuthPort(server.getPoort());
			rc.setRetryCount(5);
			rc.setSocketTimeout(2000);
			return rc.authenticate(username, password);
		}
		catch (IOException e)
		{
			log.error(e.toString(), e);
			throw new LoginException("Login op radiusserver mislukt", e);
		}
		catch (RadiusException e)
		{
			log.error(e.toString(), e);
			throw new LoginException("Login op radiusserver mislukt", e);
		}
	}
}
