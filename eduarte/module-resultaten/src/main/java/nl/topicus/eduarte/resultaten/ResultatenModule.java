package nl.topicus.eduarte.resultaten;

import nl.topicus.cobra.security.IPrincipalSourceResolver;
import nl.topicus.cobra.security.SpringPrincipalSourceResolver;
import nl.topicus.eduarte.app.AbstractEduArteModule;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.EduArtePrincipal;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.settings.ResultaatControleSetting;
import nl.topicus.eduarte.resultaten.web.components.factory.ResultatenDummyComponentFactory;
import nl.topicus.eduarte.resultaten.web.components.factory.ToetsWizardButtonFactory;
import nl.topicus.eduarte.resultaten.web.components.factory.ToetscodesBeherenButtonFactory;
import nl.topicus.eduarte.resultaten.web.components.menu.ResultatenBeheerMenuExtender;
import nl.topicus.eduarte.resultaten.web.components.menu.ResultatenDeelnemerCollectiefMenuExtender;
import nl.topicus.eduarte.resultaten.web.components.menu.ResultatenDeelnemerMenuExtender;
import nl.topicus.eduarte.resultaten.web.components.menu.ResultatenGroepMenuExtender;
import nl.topicus.eduarte.resultaten.web.components.menu.ResultatenOnderwijsCollectiefMenuExtender;
import nl.topicus.eduarte.resultaten.web.components.menu.ResultatenOnderwijsproductMenuExtender;
import nl.topicus.eduarte.resultaten.web.pages.deelnemer.DeelnemerResultatenboomEditPage;
import nl.topicus.eduarte.resultaten.web.pages.deelnemer.DeelnemerResultatenmatrixEditPage;
import nl.topicus.eduarte.resultaten.web.pages.groep.GroepResultatenmatrixEditPage;
import nl.topicus.eduarte.web.components.menu.*;

public class ResultatenModule extends AbstractEduArteModule
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public ResultatenModule()
	{
		super(EduArteModuleKey.SUMMATIEVE_RESULTATEN);
	}

	@Override
	public String getVersion()
	{
		return "1.0";
	}

	@Override
	public IPrincipalSourceResolver<EduArtePrincipal> getPrincipalSourceResolver()
	{
		return new SpringPrincipalSourceResolver<EduArtePrincipal>(
			"nl.topicus.eduarte.resultaten.principals", "nl.topicus.eduarte.resultaten.web.pages");
	}

	@Override
	protected void registerMenuExtenders()
	{
		addMenuExtender(GroepMenu.class, new ResultatenGroepMenuExtender());
		addMenuExtender(DeelnemerMenu.class, new ResultatenDeelnemerMenuExtender());
		addMenuExtender(DeelnemerCollectiefMenu.class,
			new ResultatenDeelnemerCollectiefMenuExtender());
		addMenuExtender(OnderwijsCollectiefMenu.class,
			new ResultatenOnderwijsCollectiefMenuExtender());
		addMenuExtender(OnderwijsproductMenu.class, new ResultatenOnderwijsproductMenuExtender());
		addMenuExtender(BeheerMenu.class, new ResultatenBeheerMenuExtender());
	}

	@Override
	protected void registerModuleEditPages()
	{
		addModuleEditPage(Deelnemer.class, DeelnemerMenuItem.Resultatenboom,
			DeelnemerResultatenboomEditPage.class);
		addModuleEditPage(Deelnemer.class, DeelnemerMenuItem.Resultatenmatrix,
			DeelnemerResultatenmatrixEditPage.class);
		addModuleEditPage(Groep.class, GroepMenuItem.Invoeren, GroepResultatenmatrixEditPage.class);
	}

	@Override
	protected void registerModulePanels()
	{
		addModulePanelFactory(ToetsWizardButtonFactory.class,
			new ResultatenDummyComponentFactory());
		addModulePanelFactory(ToetscodesBeherenButtonFactory.class,
			new ResultatenDummyComponentFactory());
	}

	@Override
	protected void registerOrganisatieSettings()
	{
		addSetting(ResultaatControleSetting.class);
	}
}
