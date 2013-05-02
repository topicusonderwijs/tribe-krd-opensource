/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.app.security.authorization;

import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.EduArteSession;
import nl.topicus.eduarte.app.security.authentication.EduArteSubject;
import nl.topicus.eduarte.entities.organisatie.Organisatie;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.security.authentication.DeelnemerAccount;
import nl.topicus.eduarte.entities.security.authentication.MedewerkerAccount;

import org.apache.wicket.Application;
import org.apache.wicket.security.authentication.LoginException;
import org.apache.wicket.security.hive.authentication.LoginContext;
import org.apache.wicket.security.hive.authentication.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EduArteLogin extends LoginContext
{
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(EduArteLogin.class);

	/** vlag voor het bijhouden of deze login al een keer gebruikt is voor het aanmelden. */
	private boolean gebruikt = false;

	private String username;

	private String password;

	private String token;

	private boolean omzeilRadiusServer;

	private String remoteIp;

	private String aanmeldenAls;

	private transient Organisatie aanmeldenOp;

	private transient Organisatie organisatie;

	public void setToken(String token)
	{
		this.token = token;
	}

	public void setOmzeilRadiusServer(boolean omzeilRadiusServer)
	{
		this.omzeilRadiusServer = omzeilRadiusServer;
	}

	@Override
	public Subject login() throws LoginException
	{
		if (gebruikt)
		{
			throw new LoginException(
				"Kan deze EduArteLogin niet hergebruiken na een gefaalde login poging");
		}
		gebruikt = true;

		Asserts.assertNotNull("organisatie", organisatie);
		Asserts.assertNotEmpty("username", username);
		Asserts.assertNotEmpty("password", password);

		EduArteSession session = EduArteSession.get();

		LoginStrategy loginChain = getLoginChain();

		Account account = null;
		try
		{
			account = loginChain.login();
		}
		catch (LoginException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
			throw new LoginException("Aanmelden is mislukt, oorzaak: "
				+ e.getClass().getSimpleName(), e);
		}

		organisatie = null;
		username = null;
		password = null;
		remoteIp = null;

		controleerOfAccountActiefIs(account);

		if (account != null)
		{
			session.setAccountId(account.getId());
			EduArteContext.get().setAccount(account);
			return new EduArteSubject(account);
		}
		throw new LoginException(getLocalizedMessage("exception.login",
			"Illegal username password combo"));
	}

	private LoginStrategy getLoginChain()
	{
		LoginStrategy radiusServerLogin =
			new RadiusServerLogin(username, password, omzeilRadiusServer);
		LoginStrategy usernamePasswordLogin = new UsernamePasswordLogin(username, password);

		LoginStrategy vascoTokenLogin = new VascoTokenLogin(token, usernamePasswordLogin);
		LoginStrategy authenticators = new ChainedLoginStrategy(radiusServerLogin, vascoTokenLogin);

		LoginStrategy applicationIpValidatingLogin =
			new IpValidatingLogin(remoteIp, authenticators);

		LoginStrategy failedLoginCountValidatingLogin =
			new FailedLoginCountValidatingLogin(username, organisatie, applicationIpValidatingLogin);

		LoginStrategy spoofingLogin =
			new UserSpoofingLogin(aanmeldenAls, aanmeldenOp, failedLoginCountValidatingLogin);

		LoginStrategy loginChain = spoofingLogin;
		return loginChain;
	}

	private void controleerOfAccountActiefIs(Account account) throws LoginException
	{
		if (account instanceof MedewerkerAccount)
		{
			Medewerker medewerker = ((MedewerkerAccount) account).getMedewerker();
			if (medewerker == null || !medewerker.isActief())
			{
				throw new LoginException("Dit account is niet langer actief");
			}
		}
		if (account instanceof DeelnemerAccount)
		{
			Deelnemer deelnemer = ((DeelnemerAccount) account).getDeelnemer();
			if (deelnemer == null || !deelnemer.isActief(TimeUtil.getInstance().currentDate()))
			{
				throw new LoginException("Dit account is niet langer actief");
			}
		}
	}

	private String getLocalizedMessage(String key, String defaultMsg)
	{
		return Application.get().getResourceSettings().getLocalizer().getString(key, null,
			defaultMsg);
	}

	public String getUsername()
	{
		return username;
	}

	public Organisatie getOrganisatie()
	{
		return organisatie;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public void setRemoteIp(String remoteIp)
	{
		this.remoteIp = remoteIp;
	}

	public void setOrganisatie(Organisatie organisatie)
	{
		this.organisatie = organisatie;
	}

	public void setAanmeldenAls(String aanmeldenAls)
	{
		this.aanmeldenAls = aanmeldenAls;
	}

	public void setAanmeldenOp(Organisatie aanmeldenOp)
	{
		this.aanmeldenOp = aanmeldenOp;
	}
}
