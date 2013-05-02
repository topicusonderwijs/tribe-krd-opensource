package nl.topicus.eduarte.krdparticipatie;

import nl.topicus.cobra.entities.IOrganisatie;
import nl.topicus.cobra.security.IPrincipalSourceResolver;
import nl.topicus.cobra.security.SpringPrincipalSourceResolver;
import nl.topicus.eduarte.app.AbstractEduArteModule;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.EduArtePrincipal;
import nl.topicus.eduarte.krdparticipatie.rapportage.creators.JaaroverzichtTemplateCreator;
import nl.topicus.eduarte.krdparticipatie.rapportage.creators.WeekoverzichtTemplateCreator;
import nl.topicus.eduarte.krdparticipatie.web.components.menu.extenders.KrdParticipatieBeheerMenuExtender;
import nl.topicus.eduarte.krdparticipatie.web.components.menu.extenders.KrdParticipatieDeelnemerCollectiefMenuExtender;
import nl.topicus.eduarte.krdparticipatie.web.components.menu.extenders.KrdParticipatieDeelnemerMenuExtender;
import nl.topicus.eduarte.krdparticipatie.web.components.menu.extenders.KrdParticipatieGroepMenuExtender;
import nl.topicus.eduarte.krdparticipatie.web.components.panels.factory.ParticipatieZorgvierkantPresentiePanelFactoryImpl;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.rapportage.creator.DeelnemeractviteitTotalenTemplateCreator;
import nl.topicus.eduarte.web.components.factory.ZorgvierkantPresentiePanelFactory;
import nl.topicus.eduarte.web.components.menu.BeheerMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenu;
import nl.topicus.eduarte.web.components.menu.GroepMenu;

import org.apache.wicket.protocol.http.WebApplication;

/**
 * Gecombineerde KRD / Participatie module voor aanwezigheidsanalyse
 * 
 * @author papegaaij
 * @author loite
 */
public class KRDParticipatieModule extends AbstractEduArteModule
{
	private static final long serialVersionUID = 1L;

	public KRDParticipatieModule()
	{
		super(EduArteModuleKey.KRD_PARTICIPATIE);
	}

	@Override
	public String getVersion()
	{
		return "1.0";
	}

	@Override
	protected void registerMenuExtenders()
	{
		super.registerMenuExtenders();
		addMenuExtender(BeheerMenu.class, new KrdParticipatieBeheerMenuExtender());
		addMenuExtender(DeelnemerCollectiefMenu.class,
			new KrdParticipatieDeelnemerCollectiefMenuExtender());
		addMenuExtender(DeelnemerMenu.class, new KrdParticipatieDeelnemerMenuExtender());
		addMenuExtender(GroepMenu.class, new KrdParticipatieGroepMenuExtender());
	}

	@Override
	protected void registerModulePanels()
	{
		addModulePanelFactory(ZorgvierkantPresentiePanelFactory.class,
			new ParticipatieZorgvierkantPresentiePanelFactoryImpl());
	}

	@Override
	protected void registerModuleEditPages()
	{
		super.registerModuleEditPages();
	}

	@Override
	public void registerBookmarkablePageMounts(WebApplication application)
	{
	}

	@Override
	public IPrincipalSourceResolver<EduArtePrincipal> getPrincipalSourceResolver()
	{
		return new SpringPrincipalSourceResolver<EduArtePrincipal>(
			"nl.topicus.eduarte.krdparticipatie.principals",
			"nl.topicus.eduarte.krdparticipatie.web.pages");
	}

	@Override
	public boolean isModuleActive(IOrganisatie organization)
	{
		return EduArteApp.get().isModuleActive(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
			|| EduArteApp.get().isModuleActive(EduArteModuleKey.PARTICIPATIE);
	}

	@Override
	protected void registerDocumentTemplateCreators()
	{
		addDocumentTemplateCreator(new WeekoverzichtTemplateCreator());
		addDocumentTemplateCreator(new JaaroverzichtTemplateCreator());
		addDocumentTemplateCreator(new DeelnemeractviteitTotalenTemplateCreator());

	}
}
