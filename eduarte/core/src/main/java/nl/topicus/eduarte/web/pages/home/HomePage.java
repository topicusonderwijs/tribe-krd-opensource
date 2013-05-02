/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.home;

import nl.topicus.cobra.app.INavigationBasePage;
import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.security.RechtenSoorten;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.core.principals.Always;
import nl.topicus.eduarte.web.components.factory.HomePageHandelingenPanelFactory;
import nl.topicus.eduarte.web.components.factory.HomePageUitnodigingenPanelFactory;
import nl.topicus.eduarte.web.components.menu.HomeMenuItem;
import nl.topicus.eduarte.web.components.panels.signalen.OverzichtSignalenPanel;

import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * Eerste pagina die een gebruiker ziet na het inloggen. Tenzij er direct geforward wordt
 * naar een bookmarkablepage ofzo.
 * 
 * @author marrink
 */
@PageInfo(title = "Home", menu = "Home")
@InPrincipal(Always.class)
@RechtenSoorten( {RechtenSoort.INSTELLING, RechtenSoort.BEHEER, RechtenSoort.EXTERNEORGANISATIE})
public class HomePage extends AbstractHomePage<Void> implements INavigationBasePage
{

	private static final long serialVersionUID = 1L;

	public HomePage()
	{
		super(HomeMenuItem.Home);

		if (getIngelogdeMedewerker() != null)
		{
			add(new OverzichtSignalenPanel("signalen", getIngelogdeMedewerker().getPersoon(), 10));
			HomePageHandelingenPanelFactory handelingenFactory =
				EduArteApp.get().getFirstPanelFactory(HomePageHandelingenPanelFactory.class,
					EduArteContext.get().getOrganisatie());
			HomePageUitnodigingenPanelFactory uitnodigingenFactory =
				EduArteApp.get().getFirstPanelFactory(HomePageUitnodigingenPanelFactory.class,
					EduArteContext.get().getOrganisatie());
			add(handelingenFactory.create("handelingenPanel"));
			add(uitnodigingenFactory.create("uitnodigingenPanel"));
		}
		else
		{
			add(new WebMarkupContainer("signalen").setVisible(false));
			add(new WebMarkupContainer("handelingenPanel").setVisible(false));
			add(new WebMarkupContainer("uitnodigingenPanel").setVisible(false));
		}
		createComponents();
	}
}
