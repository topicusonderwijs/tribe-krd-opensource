/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.app;

import java.io.Serializable;

import nl.topicus.cobra.app.HibernateRequestCycle;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.util.ExceptionUtil;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.entities.organisatie.Instelling;
import nl.topicus.eduarte.entities.organisatie.Organisatie;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.web.pages.EduArteErrorPage;
import nl.topicus.eduarte.web.pages.ErrorPageMetaData;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.home.HomePage;
import nl.topicus.eduarte.web.pages.login.LoginPage;
import nl.topicus.onderwijs.duo.bron.BronException;

import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.protocol.http.PageExpiredException;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * @author loite
 */
public class EduArteRequestCycle extends HibernateRequestCycle
{
	/**
	 * @param application
	 * @param request
	 * @param response
	 * @param sessionFactory
	 */
	public EduArteRequestCycle(WebApplication application, WebRequest request, Response response,
			SessionFactory sessionFactory)
	{
		super(application, request, response, sessionFactory);
	}

	public static EduArteRequestCycle get()
	{
		return (EduArteRequestCycle) RequestCycle.get();
	}

	@Override
	public RechtenSoort getAccountRechtenSoort()
	{
		Account account = EduArteContext.get().getAccount();
		return account == null ? null : account.getRechtenSoort();
	}

	/**
	 * @return De organisatie van het huidige account.
	 */
	@Override
	public Organisatie getOrganisatie()
	{
		Account account = EduArteContext.get().getAccount();
		return account == null ? null : account.getOrganisatie();
	}

	@Override
	public boolean isGebruikerAccount()
	{
		return EduArteContext.get().isGebruikerAccount();
	}

	@Override
	public Serializable getAccountId()
	{
		Account account = EduArteContext.get().getAccount();
		return account == null ? null : account.getIdAsSerializable();
	}

	@Override
	protected void onBeginRequest()
	{
		super.onBeginRequest();

		EduArteSession session = EduArteSession.get();

		EduArteContext.get().setAccountById(session.getAccountId());
		EduArteContext.get().setOrganisatie(getOrganisatie());
		EduArteContext.get().setPeildatumModel(session.getPeildatumModel());

		setUserDataOnSessionInfo(session.getSessionInfo());
	}

	private void setUserDataOnSessionInfo(EduArteSessionInfo sessionInfo)
	{
		setUsernameOnSessionInfo(sessionInfo);
		setOrganizationOnSessionInfo(sessionInfo);
	}

	private void setUsernameOnSessionInfo(EduArteSessionInfo sessionInfo)
	{
		Account account = EduArteContext.get().getAccount();
		if (account != null)
		{
			sessionInfo.setGebruiker(account.getGebruikersnaam());
		}
	}

	private void setOrganizationOnSessionInfo(EduArteSessionInfo sessionInfo)
	{
		Organisatie organisatie = EduArteContext.get().getOrganisatie();
		if (organisatie != null)
		{
			String naam = organisatie.getNaam();
			if (organisatie instanceof Instelling)
			{
				Instelling instelling = (Instelling) organisatie;
				naam =
					String.format("%s %s", instelling.getBrincode().getCode(), instelling
						.getBrincode().getVerkorteNaam());
			}
			sessionInfo.setOrganisatie(naam);
		}
	}

	@Override
	protected void onEndRequest()
	{
		try
		{
			super.onEndRequest();
		}
		finally
		{
			EduArteContext.clearContext();
		}
	}

	@Override
	public Page onRuntimeException(Page page, RuntimeException e)
	{
		if (e instanceof PageExpiredException)
		{
			return handlePageExpiredException();
		}
		if (ExceptionUtil.isCausedBy(e, BronException.class))
		{
			return handleBronException(page, e);
		}
		if (page != null)
		{
			return handlePageException(page, e);
		}
		return new EduArteErrorPage(page, e);
	}

	private Page handlePageExpiredException()
	{
		Page p = new LoginPage(new PageParameters());
		p.error("De sessie is be\u00EBindigd. Meld opnieuw aan.");
		return p;
	}

	private Page handleBronException(Page page, RuntimeException e)
	{
		BronException bronException = ExceptionUtil.getCause(e, BronException.class);
		page.detach();
		page.error(bronException.getMessage());
		return page;
	}

	private Page handlePageException(Page page, RuntimeException e)
	{
		ErrorPageMetaData data = page.getMetaData(ErrorPageMetaData.KEY);
		if (data != null && data.isVorigeRenderingFout())
		{
			return handleTerugGingFout(page);
		}
		return new EduArteErrorPage(page, e);
	}

	private Page handleTerugGingFout(Page page)
	{
		Page responsePage = null;
		page.setMetaData(ErrorPageMetaData.KEY, null);
		if (page instanceof SecurePage)
		{
			SecurePage securePage = (SecurePage) page;
			responsePage = securePage.createDefaultContextPage();
		}
		if (responsePage == null)
		{
			responsePage = new HomePage();
		}
		else
		{
			responsePage.setMetaData(ErrorPageMetaData.KEY, null);
		}
		getSession().warn(
			"Kon de fout van de vorige pagina (" + getUserUnderstandablePageName(page)
				+ ") niet herstellen, en daarom de " + getUserUnderstandablePageName(responsePage)
				+ " getoond.");
		return responsePage;
	}

	private static String getUserUnderstandablePageName(Page page)
	{
		String naam = page.getClass().getSimpleName();
		if (naam.endsWith("Page"))
		{
			naam = naam.substring(0, naam.lastIndexOf("Page"));
		}
		return StringUtil.convertCamelCase(naam).toLowerCase();
	}

	/**
	 * Override zodat de flush mode gezet wordt op Commit. Hibernate doet nu alleen een
	 * flush als een commit wordt aangeroepen.
	 * 
	 * @see nl.topicus.cobra.app.HibernateRequestCycle#getHibernateSession()
	 */
	@Override
	public Session getHibernateSession()
	{
		Session session = super.getHibernateSession();
		session.setFlushMode(FlushMode.COMMIT);
		return session;
	}

	public String getImageDirectory()
	{
		return getRequest().getRelativePathPrefixToContextRoot() + "assets/img/";
	}

	public String getImageDirectoryWithoutLastSlash()
	{
		return getRequest().getRelativePathPrefixToContextRoot() + "assets/img";
	}
}
