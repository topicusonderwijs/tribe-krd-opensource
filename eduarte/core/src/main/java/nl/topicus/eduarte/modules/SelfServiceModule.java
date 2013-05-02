package nl.topicus.eduarte.modules;

import nl.topicus.cobra.entities.IOrganisatie;
import nl.topicus.cobra.modules.AbstractCobraModule;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.web.components.factory.NieuweAccountButtonFactory;
import nl.topicus.eduarte.web.components.factory.NieuweRolButtonFactory;
import nl.topicus.eduarte.web.components.factory.SelfServiceButtonFactory;
import nl.topicus.eduarte.web.pages.deelnemerportaal.login.SelfServiceLoginPage;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.target.coding.IndexedHybridUrlCodingStrategy;

public class SelfServiceModule extends AbstractCobraModule
{
	public SelfServiceModule()
	{
		super(EduArteModuleKey.SELFSERVICE);
	}

	@Override
	public String getVersion()
	{
		return "0.1";
	}

	@Override
	public boolean isModuleActive(IOrganisatie instelling)
	{
		return EduArteApp.get().isModuleActive(EduArteModuleKey.BPV)
			|| EduArteApp.get().isModuleActive(EduArteModuleKey.PARTICIPATIE);
	}

	@Override
	protected void registerModulePanels()
	{
		addModulePanelFactory(NieuweAccountButtonFactory.class, new SelfServiceButtonFactory());
		addModulePanelFactory(NieuweRolButtonFactory.class, new SelfServiceButtonFactory());
	}

	@Override
	public void registerBookmarkablePageMounts(WebApplication application)
	{
		mount(application, "/selfservice", SelfServiceLoginPage.class);
	}

	private void mount(WebApplication application, String path, Class< ? extends WebPage> page)
	{
		application.mount(new IndexedHybridUrlCodingStrategy(path, page));
	}
}
