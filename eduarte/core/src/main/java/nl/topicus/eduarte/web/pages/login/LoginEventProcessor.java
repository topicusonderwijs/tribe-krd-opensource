package nl.topicus.eduarte.web.pages.login;

import nl.topicus.eduarte.app.security.authorization.EduArteLogin;

/**
 * Interface voor het verwerken van een login event.
 */
public interface LoginEventProcessor
{
	public void onLogin(EduArteLogin login);
}
