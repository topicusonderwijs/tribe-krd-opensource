package nl.topicus.eduarte.app.security.authorization;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.AccountDataAccessHelper;
import nl.topicus.eduarte.entities.security.authentication.Account;

public class UsernamePasswordLogin implements LoginStrategy
{
	private String username;

	private String password;

	public UsernamePasswordLogin(String username, String password)
	{
		this.username = username;
		this.password = password;
	}

	@Override
	public Account login()
	{
		AccountDataAccessHelper accounts =
			DataAccessRegistry.getHelper(AccountDataAccessHelper.class);
		return accounts.authenticate(username, password);
	}
}
