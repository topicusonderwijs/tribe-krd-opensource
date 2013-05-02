/*
 * Copyright (c) 2007, Topicus B.V. All rights
 * reserved.
 */

package nl.topicus.eduarte.web.pages.deelnemerportaal.login;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.pages.BasePage;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.app.EduArteSession;
import nl.topicus.eduarte.app.security.authorization.EduArteLogin;
import nl.topicus.eduarte.dao.helpers.AccountDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.OrganisatieDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.SettingsDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Organisatie;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.security.authentication.Sessie;
import nl.topicus.eduarte.entities.settings.LoginConfiguration;
import nl.topicus.eduarte.entities.settings.LoginSetting;
import nl.topicus.eduarte.web.pages.login.LoginEventProcessor;
import nl.topicus.eduarte.web.pages.login.UsernamePasswordSignInPanel;

import org.apache.wicket.Application;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.security.authentication.LoginException;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.commons.CoreJavaScriptHeaderContributor;

public class SelfServiceLoginPage extends BasePage implements LoginEventProcessor
{
	private static final long serialVersionUID = 1L;

	private IModel<Organisatie> organisatieModel = new OrganisatieParametersModel();

	@SpringBean
	private AccountDataAccessHelper accounts;

	public SelfServiceLoginPage(PageParameters parameters)
	{
		super(parameters);

		if (isUserLoggedIn())
		{
			getSession().info("U bent al ingelogd");
			throw new RestartResponseException(Application.get().getHomePage());
		}

		add(new Label("pageTitle", EduArteApp.get().getLoginTitle()));
		add(new Label("version", getVersion()));
		add(new Label("build", getBuild()));

		setStatelessHint(true);

		add(new FeedbackPanel("feedback")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return anyMessage();
			}
		});

		String panelId = "signInPanel";

		Organisatie organisatie = getOrganisatie();
		if (organisatie != null)
		{
			// if(deelnemerportaal afgenomen)
			info("Aanmelden voor " + organisatie.getNaam());
			newUserPasswordSignInPanel(panelId, organisatie);
		}
		add(new HeaderContributor(new CoreJavaScriptHeaderContributor()));
	}

	@Override
	public String getNavigationStackName()
	{
		return "Login";
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(organisatieModel);
		super.onDetach();
	}

	private Organisatie getOrganisatie()
	{
		return organisatieModel.getObject();
	}

	private void newUserPasswordSignInPanel(String panelId, Organisatie organisatie)
	{
		add(new UsernamePasswordSignInPanel(panelId, organisatie, this));
	}

	public void onLogin(EduArteLogin login)
	{
		EduArteContext.get().runInContext(new LoginBinnenOrganisatieContext(login),
			getOrganisatie());
	}

	/**
	 * Aparte class voor het uitvoeren van de login actie binnen de context van de
	 * geselecteerde organisatie. Om de hibernate queries goed te laten lopen moeten we
	 * tijdelijk de current organisatie zetten op de geselecteerde organisatie, en daarna
	 * weer terugzetten op de oorspronkelijke.
	 */
	private class LoginBinnenOrganisatieContext implements Runnable
	{
		private final EduArteLogin login;

		private LoginBinnenOrganisatieContext(EduArteLogin login)
		{
			this.login = login;
		}

		@Override
		public void run()
		{
			String username = login.getUsername();
			Organisatie organisatie = getOrganisatie();

			if (isGebruikerAlAangemeld(username))
				return;

			login.setRemoteIp(getRemoteIp());
			login.setOrganisatie(organisatie);
			try
			{
				EduArteSession session = EduArteSession.get();
				session.login(login);

				onLoginSuccess(username, organisatie);
			}
			catch (LoginException exception)
			{
				onLoginFailed(username, organisatie, exception);
			}
		}

		private boolean isGebruikerAlAangemeld(String username)
		{
			EduArteSession session = EduArteSession.get();
			if (session.isUserAuthenticated())
			{
				String currentUsername = EduArteContext.get().getAccount().getGebruikersnaam();
				if (!currentUsername.equals(username))
				{
					session
						.error("U was reeds ingelogd als '" + currentUsername
							+ "'. Om aan te melden als '" + username
							+ "' dient u eerst uit te loggen.");
				}
				setResponsePage(Application.get().getHomePage());
				return true;
			}
			return false;
		}

		private String getRemoteIp()
		{
			HttpServletRequest request =
				EduArteRequestCycle.get().getWebRequest().getHttpServletRequest();
			String ip = request.getRemoteAddr();
			return ip;
		}

		private void onLoginSuccess(String username, Organisatie organisatie)
		{
			EduArteApp.get().removeLoginAttempts(username, organisatie);

			// Inloggen gelukt. Maak een nieuwe sessie aan.
			Sessie sessie =
				new Sessie(accounts.get(Account.class, EduArteSession.get().getAccountId()));
			sessie.save();
			sessie.commit();

			makeUsernameAvailableForContainer(username);

			// als er al een doel pagina beschikbaar is, daar naar toe doorgaan,
			// anders naar de home page
			if (!getPage().continueToOriginalDestination())
			{
				setResponsePage(Application.get().getHomePage());
			}
		}

		private void makeUsernameAvailableForContainer(String username)
		{
			getWebRequestCycle().getWebRequest().getHttpServletRequest().getSession().setAttribute(
				"Login", username);
		}

		private void onLoginFailed(String username, Organisatie organisatie, LoginException e)
		{
			error(e.getMessage());

			// maak ff misbruik van een veld dat al aanwezig is op de exceptie
			// :)
			if (e.getLoginContext() == null)
			{
				LoginConfiguration loginConf =
					DataAccessRegistry.getHelper(SettingsDataAccessHelper.class).getSetting(
						LoginSetting.class).getValue();
				if (loginConf.isActief())
				{
					Integer max = loginConf.getPogingen();
					warn((max - EduArteApp.get().getLoginAttempts(username, organisatie))
						+ " poging(en) over.");
				}
			}
		}
	}

	private class OrganisatieParametersModel extends LoadableDetachableModel<Organisatie>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected Organisatie load()
		{
			PageParameters parameters = getPageParameters();
			String key = "0";
			if (parameters.containsKey("organisatie"))
			{
				key = "organisatie";
			}
			String naam = parameters.getString(key, null);

			if (StringUtil.isNotEmpty(naam))
			{
				OrganisatieDataAccessHelper helper =
					DataAccessRegistry.getHelper(OrganisatieDataAccessHelper.class);
				List< ? extends Organisatie> organisaties = helper.getInlogDomeinen();
				for (Organisatie organisatie : organisaties)
					if (organisatie.getNaam().equalsIgnoreCase(naam))
						return organisatie;
			}
			return null;
		}
	}
}
