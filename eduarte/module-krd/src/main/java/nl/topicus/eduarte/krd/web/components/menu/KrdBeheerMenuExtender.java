package nl.topicus.eduarte.krd.web.components.menu;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.menu.AbstractMenuExtender;
import nl.topicus.cobra.web.components.menu.DropdownMenuItem;
import nl.topicus.cobra.web.components.menu.HorizontalSeperator;
import nl.topicus.cobra.web.components.menu.IMenuItem;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.eduarte.krd.web.pages.beheer.*;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.BronAlgemeenPage;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.BronInstellingenPage;
import nl.topicus.eduarte.krd.web.pages.beheer.contract.SoortContractZoekenPage;
import nl.topicus.eduarte.krd.web.pages.beheer.contract.TypeFinancieringZoekenPage;
import nl.topicus.eduarte.krd.web.pages.beheer.mutatielog.MutatieLogJobPage;
import nl.topicus.eduarte.krd.web.pages.beheer.mutatielog.MutatieLogVerwerkersTesterPage;
import nl.topicus.eduarte.krd.web.pages.beheer.organisatie.extern.ExterneOrganisatieContactPersoonRolZoekenPage;
import nl.topicus.eduarte.krd.web.pages.beheer.organisatie.extern.SoortExterneOrganisatieZoekenPage;
import nl.topicus.eduarte.krd.web.pages.beheer.soortproductregel.SoortProductregelZoekenPage;
import nl.topicus.eduarte.krd.web.pages.beheer.vrijveld.VrijVeldZoekenPage;
import nl.topicus.eduarte.web.components.menu.BeheerMenu;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.menu.BronMenuItem;
import nl.topicus.eduarte.web.pages.beheer.onderwijs.TeamZoekenPage;
import nl.topicus.eduarte.web.pages.beheer.organisatie.LocatieZoekenPage;
import nl.topicus.eduarte.web.pages.beheer.organisatie.OrganisatieEenhedenPage;
import nl.topicus.eduarte.web.pages.beheer.organisatie.OrganisatieEenheidZoekenPage;

/**
 * Extender voor het beheer gedeelte.
 * 
 * @author hoeve
 */
public class KrdBeheerMenuExtender extends AbstractMenuExtender<BeheerMenu>
{

	@Override
	public List<IMenuItem> getMenuExtension(BeheerMenu menu)
	{
		List<IMenuItem> ret = new ArrayList<IMenuItem>();

		createOrganisatiemodelMenu(menu);

		DropdownMenuItem systeemMenu = findSubmenu(menu, BeheerMenu.SYSTEEMNAME);
		systeemMenu.add(new MenuItem(UploadFotosPage.class, BeheerMenuItem.FotosUploaden));
		systeemMenu.add(new MenuItem(MutatieLogJobPage.class, BeheerMenuItem.MutatieLogVerwerkers));
		systeemMenu.add(new MenuItem(MutatieLogVerwerkersTesterPage.class,
			BeheerMenuItem.MutatieLogVerwerkersTester));

		DropdownMenuItem beheerTabellenMenu = findSubmenu(menu, BeheerMenu.BEHEERTABELLENMENUNAME);
		beheerTabellenMenu.add(new HorizontalSeperator());
		beheerTabellenMenu.add(new MenuItem(FunctieZoekenPage.class, BeheerMenuItem.Functies));
		beheerTabellenMenu.add(new MenuItem(RedenUitDienstZoekenPage.class,
			BeheerMenuItem.RedenUitDienst));
		beheerTabellenMenu.add(new HorizontalSeperator());
		beheerTabellenMenu.add(new MenuItem(RelatieSoortZoekenPage.class,
			BeheerMenuItem.Relatiesoort));
		beheerTabellenMenu.add(new MenuItem(SoortContactgegevenZoekenPage.class,
			BeheerMenuItem.SoortContactgegevens));
		beheerTabellenMenu.add(new HorizontalSeperator());
		beheerTabellenMenu.add(new MenuItem(VrijVeldZoekenPage.class, BeheerMenuItem.VrijeVelden));
		beheerTabellenMenu.add(new HorizontalSeperator());
		beheerTabellenMenu.add(new MenuItem(UitkomstIntakegesprekZoekenPage.class,
			BeheerMenuItem.UitkomstIntakegesprek));
		beheerTabellenMenu.add(new MenuItem(RedenUitschrijvingZoekenPage.class,
			BeheerMenuItem.RedenUitschrijving));
		beheerTabellenMenu.add(new MenuItem(SchooladviesZoekenPage.class,
			BeheerMenuItem.Schooladvies));
		beheerTabellenMenu.add(new MenuItem(SoortVooropleidingZoekenPage.class,
			BeheerMenuItem.SoortVooropleiding));
		beheerTabellenMenu.add(new HorizontalSeperator());
		beheerTabellenMenu.add(new MenuItem(SoortContractZoekenPage.class,
			BeheerMenuItem.SoortContracten));
		beheerTabellenMenu.add(new MenuItem(TypeFinancieringZoekenPage.class,
			BeheerMenuItem.TypeFinancieringen));
		beheerTabellenMenu.add(new HorizontalSeperator());
		beheerTabellenMenu.add(new MenuItem(SoortExterneOrganisatieZoekenPage.class,
			BeheerMenuItem.SoortExterneOrganisaties));
		beheerTabellenMenu.add(new MenuItem(ExterneOrganisatieContactPersoonRolZoekenPage.class,
			BeheerMenuItem.ExterneOrganisatieContactPersoonRol));
		beheerTabellenMenu.add(new HorizontalSeperator());
		beheerTabellenMenu.add(new MenuItem(KenmerkCategorieZoekenPage.class,
			BeheerMenuItem.Kenmerkcategorien));
		beheerTabellenMenu.add(new MenuItem(KenmerkZoekenPage.class, BeheerMenuItem.Kenmerken));

		DropdownMenuItem onderwijsMenu = findSubmenu(menu, BeheerMenu.ONDERWIJSMENUNAME);
		onderwijsMenu.add(0, new MenuItem(SoortProductregelZoekenPage.class,
			BeheerMenuItem.SoortProductregels));
		onderwijsMenu.add(new MenuItem(TeamZoekenPage.class, BeheerMenuItem.Teams));

		DropdownMenuItem bron = new DropdownMenuItem("BRON");
		bron.add(new MenuItem(BronAlgemeenPage.class, BronMenuItem.BRON));
		bron.add(new MenuItem(BronInstellingenPage.class, BronMenuItem.Instellingen));
		ret.add(bron);

		return ret;
	}

	private void createOrganisatiemodelMenu(BeheerMenu menu)
	{
		DropdownMenuItem organisatiemodelbeheer = findSubmenu(menu, "Organisatiemodel");
		organisatiemodelbeheer.add(new MenuItem(OrganisatieEenhedenPage.class,
			BeheerMenuItem.Organisatieboom));
		organisatiemodelbeheer.add(new MenuItem(LocatieZoekenPage.class, BeheerMenuItem.Locaties));
		organisatiemodelbeheer.add(new MenuItem(OrganisatieEenheidZoekenPage.class,
			BeheerMenuItem.Organisatie_eenheden));
	}

}
