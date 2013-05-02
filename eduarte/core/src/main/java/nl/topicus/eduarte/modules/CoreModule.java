/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.modules;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.entities.IOrganisatie;
import nl.topicus.cobra.security.IPrincipalSourceResolver;
import nl.topicus.cobra.security.SpringPrincipalSourceResolver;
import nl.topicus.cobra.web.components.menu.main.CobraMainMenu;
import nl.topicus.cobra.web.components.menu.main.MainMenuItemPanel;
import nl.topicus.eduarte.app.AbstractEduArteModule;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.EduArtePrincipal;
import nl.topicus.eduarte.entities.settings.*;
import nl.topicus.eduarte.rapportage.creators.DeelnemerkaartTemplateCreator;
import nl.topicus.eduarte.rapportage.creators.GroepPasfotosTemplateCreator;
import nl.topicus.eduarte.rapportage.creators.OpleidingkaartTemplateCreator;
import nl.topicus.eduarte.web.components.factory.*;
import nl.topicus.eduarte.web.components.menu.main.*;
import nl.topicus.eduarte.web.pages.deelnemer.DeelnemerZoekenPage;
import nl.topicus.eduarte.web.pages.groep.GroepZoekenPage;
import nl.topicus.eduarte.web.pages.medewerker.MedewerkerZoekenPage;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.target.coding.IndexedHybridUrlCodingStrategy;

/**
 * De basismodule.
 * 
 * @author marrink
 */
public class CoreModule extends AbstractEduArteModule
{
	public CoreModule()
	{
		super(EduArteModuleKey.BASISFUNCTIONALITEIT);
	}

	@Override
	public List<MainMenuItemPanel> createMainMenuItems(CobraMainMenu menu)
	{
		List<MainMenuItemPanel> ret = new ArrayList<MainMenuItemPanel>();
		ret.add(new DeelnemerMainMenuItemPanel(menu));
		ret.add(new GroepMainMenuItemPanel(menu));
		ret.add(new MedewerkerMainMenuItemPanel(menu));
		ret.add(new OnderwijsMainMenuItemPanel(menu));
		ret.add(new BeheerMainMenuItemPanel(menu));
		ret.add(new HomeMainMenuItemPanel(menu));
		ret.add(new RelatieMainMenuitemPanel(menu));
		ret.add(new HelpMainMenuItemPanel(menu));
		ret.add(new DeelnemerportaalHomeMainMenuItemPanel(menu));
		ret.add(new DeelnemerportaalDossierMainMenuItemPanel(menu));
		return ret;
	}

	@Override
	protected void registerSignaalHandlers()
	{
		super.registerSignaalHandlers();
	}

	@Override
	protected void registerModulePanels()
	{
		super.registerModulePanels();
		addModulePanelFactory(ZorgvierkantPresentiePanelFactory.class,
			new CoreZorgvierkantPresentiePanelFactoryImpl());
		addModulePanelFactory(HomePageHandelingenPanelFactory.class,
			new CoreHomePageHandelingenPanelFactoryImpl());
		addModulePanelFactory(HomePageUitnodigingenPanelFactory.class,
			new CoreHomePageUitnodigingenPanelFactoryImpl());
		addModulePanelFactory(StructuurLinkFactory.class, new ResultatenDummyComponentFactory());
		addModulePanelFactory(InleverenLinkFactory.class, new ResultatenDummyComponentFactory());
	}

	@Override
	public IPrincipalSourceResolver<EduArtePrincipal> getPrincipalSourceResolver()
	{
		return new SpringPrincipalSourceResolver<EduArtePrincipal>(
			"nl.topicus.eduarte.core.principals", "nl.topicus.eduarte.web.pages");
	}

	@Override
	public boolean isModuleActive(IOrganisatie instelling)
	{
		return true;
	}

	@Override
	public String getVersion()
	{
		return EduArteApp.get().getVersion();
	}

	@Override
	public void registerBookmarkablePageMounts(WebApplication application)
	{
		super.registerBookmarkablePageMounts(application);

		application.mount(new IndexedHybridUrlCodingStrategy("/deelnemer/zoeken",
			DeelnemerZoekenPage.class));
		application
			.mount(new IndexedHybridUrlCodingStrategy("/groep/zoeken", GroepZoekenPage.class));
		application.mount(new IndexedHybridUrlCodingStrategy("/medewerker/zoeken",
			MedewerkerZoekenPage.class));
	}

	@Override
	protected void registerOrganisatieSettings()
	{
		addSetting(ScreenSaverSetting.class);
		addSetting(LoginSetting.class);
		addSetting(PasswordSetting.class);
		addSetting(RadiusServerSetting.class);
		addSetting(GebruikLandelijkeExterneOrganisatiesSetting.class);
		addSetting(OrganisatieIpAdresSetting.class);
		addSetting(APIKeySetting.class);
		addSetting(MutatieBlokkedatumSetting.class);
	}

	@Override
	protected void registerDocumentTemplateCreators()
	{
		addDocumentTemplateCreator(new DeelnemerkaartTemplateCreator());
		addDocumentTemplateCreator(new GroepPasfotosTemplateCreator());
		addDocumentTemplateCreator(new OpleidingkaartTemplateCreator());
	}
}
