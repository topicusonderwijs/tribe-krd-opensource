package nl.topicus.eduarte.app.security.authorization;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.dao.helpers.SettingsDataAccessHelper;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.settings.OrganisatieIpAdresSetting;
import nl.topicus.eduarte.util.IpAddressAuthorizationUtil;

import org.apache.wicket.security.authentication.LoginException;

public class IpValidatingLogin implements LoginStrategy
{
	private String ipAdres;

	private LoginStrategy wrapped;

	public IpValidatingLogin(String ipAdres, LoginStrategy wrapped)
	{
		this.ipAdres = ipAdres;
		this.wrapped = wrapped;
	}

	@Override
	public Account login() throws LoginException
	{
		Account account = wrapped.login();

		if (heeftBeperkingIpAdresAccount(account))
		{
			IpAddressAuthorizationUtil ipValidatingUtil =
				new IpAddressAuthorizationUtil(account.getIpAdressen(), ipAdres);

			if (ipValidatingUtil.isAuthorized())
				return account;

			if (!heeftBeperkingIpAdresInstelling())
				throw new LoginException("Dit account mag niet van dit ip-adres inloggen");
		}

		if (heeftBeperkingIpAdresInstelling())
		{
			OrganisatieIpAdresSetting setting =
				DataAccessRegistry.getHelper(SettingsDataAccessHelper.class).getSetting(
					OrganisatieIpAdresSetting.class);

			IpAddressAuthorizationUtil ipValidatingUtil =
				new IpAddressAuthorizationUtil(setting.getValue().getIpAdressen(), ipAdres);

			if (ipValidatingUtil.isAuthorized())
				return account;

			throw new LoginException(
				"Voor deze instelling mag niet vanaf dit ip-adres ingelogd worden");
		}

		return account;
	}

	private boolean heeftBeperkingIpAdresAccount(Account account)
	{
		return account != null && StringUtil.isNotEmpty(account.getIpAdressen());
	}

	private boolean heeftBeperkingIpAdresInstelling()
	{
		OrganisatieIpAdresSetting setting =
			DataAccessRegistry.getHelper(SettingsDataAccessHelper.class).getSetting(
				OrganisatieIpAdresSetting.class);
		return (setting != null && setting.getValue() != null && StringUtil.isNotEmpty(setting
			.getValue().getIpAdressen()));
	}
}
