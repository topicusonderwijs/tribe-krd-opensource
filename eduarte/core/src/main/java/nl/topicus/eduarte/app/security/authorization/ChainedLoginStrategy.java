package nl.topicus.eduarte.app.security.authorization;

import nl.topicus.eduarte.entities.security.authentication.Account;

import org.apache.wicket.security.authentication.LoginException;

public class ChainedLoginStrategy implements LoginStrategy
{
	private LoginStrategy[] strategies;

	public ChainedLoginStrategy(LoginStrategy... strategies)
	{
		this.strategies = strategies;
	}

	@Override
	public Account login() throws LoginException
	{
		for (LoginStrategy strategy : strategies)
		{
			Account login = strategy.login();
			if (login != null)
				return login;
		}
		return null;
	}
}
