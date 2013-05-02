package nl.topicus.eduarte.onderwijscatalogus;

import nl.topicus.cobra.security.IPrincipalSourceResolver;
import nl.topicus.cobra.security.SpringPrincipalSourceResolver;
import nl.topicus.eduarte.app.AbstractEduArteModule;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.EduArtePrincipal;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductBijlage;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductOpvolger;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductSamenstelling;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductTaxonomie;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductVoorwaarde;
import nl.topicus.eduarte.onderwijscatalogus.web.components.menu.OnderwijscatalogusBeheerMenuExtender;
import nl.topicus.eduarte.onderwijscatalogus.web.components.menu.OnderwijscatalogusOnderwijsCollectiefMenuExtender;
import nl.topicus.eduarte.onderwijscatalogus.web.pages.onderwijsproduct.*;
import nl.topicus.eduarte.web.components.menu.BeheerMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.OnderwijsproductMenuItem;

/**
 * OnderwijsCatalogus module (wijzigschermen voor onderwijsproducten etc)
 * 
 * @author loite
 */
public class OnderwijscatalogusModule extends AbstractEduArteModule
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public OnderwijscatalogusModule()
	{
		super(EduArteModuleKey.ONDERWIJSCATALOGUS);
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
		addMenuExtender(BeheerMenu.class, new OnderwijscatalogusBeheerMenuExtender());
		addMenuExtender(OnderwijsCollectiefMenu.class,
			new OnderwijscatalogusOnderwijsCollectiefMenuExtender());
	}

	@Override
	protected void registerModuleEditPages()
	{
		super.registerModuleEditPages();
		addModuleEditPage(Onderwijsproduct.class, OnderwijsproductMenuItem.Algemeen,
			OnderwijsproductKaartWijzigenPage.class);
		addModuleEditPage(Onderwijsproduct.class, OnderwijsproductMenuItem.Eigenaar,
			OnderwijsproductEigenaarWijzigenPage.class);
		addModuleEditPage(Onderwijsproduct.class,
			OnderwijsCollectiefMenuItem.OnderwijsproductenZoeken,
			OnderwijsproductKaartWijzigenPage.class);
		addModuleEditPage(Onderwijsproduct.class, OnderwijsproductMenuItem.Personeel,
			OnderwijsproductPersoneelWijzigenPage.class);
		addModuleEditPage(Onderwijsproduct.class, OnderwijsproductMenuItem.Kalender,
			OnderwijsproductKalenderWijzigenPage.class);
		addModuleEditPage(Onderwijsproduct.class, OnderwijsproductMenuItem.Toegankelijkheid,
			OnderwijsproductToegankelijkheidWijzigenPage.class);
		addModuleEditPage(OnderwijsproductTaxonomie.class,
			OnderwijsproductTaxonomieToevoegenPage.class);
		addModuleEditPage(OnderwijsproductVoorwaarde.class,
			OnderwijsproductVoorwaardenToevoegenPage.class);
		addModuleEditPage(OnderwijsproductOpvolger.class,
			OnderwijsproductOpvolgerToevoegenPage.class);
		addModuleEditPage(OnderwijsproductSamenstelling.class,
			OnderwijsproductPaklijstToevoegenPage.class);
		addModuleEditPage(Onderwijsproduct.class, OnderwijsproductMenuItem.Gebruiksmiddelen,
			OnderwijsproductGebruiksmiddelToevoegenPage.class);
		addModuleEditPage(Onderwijsproduct.class, OnderwijsproductMenuItem.Verbruiksmiddelen,
			OnderwijsproductVerbruiksmiddelToevoegenPage.class);
		addModuleEditPage(OnderwijsproductBijlage.class, OnderwijsproductBijlageToevoegenPage.class);
	}

	@Override
	public IPrincipalSourceResolver<EduArtePrincipal> getPrincipalSourceResolver()
	{
		return new SpringPrincipalSourceResolver<EduArtePrincipal>(
			"nl.topicus.eduarte.onderwijscatalogus.principals",
			"nl.topicus.eduarte.onderwijscatalogus.web.pages");
	}
}
