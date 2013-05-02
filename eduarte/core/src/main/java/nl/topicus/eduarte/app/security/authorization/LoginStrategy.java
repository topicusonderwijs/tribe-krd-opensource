package nl.topicus.eduarte.app.security.authorization;

import nl.topicus.eduarte.entities.security.authentication.Account;

import org.apache.wicket.security.authentication.LoginException;

public interface LoginStrategy
{
	public Account login() throws LoginException;
}
