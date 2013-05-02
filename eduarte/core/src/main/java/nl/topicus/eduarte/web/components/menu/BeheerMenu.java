/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.menu;

import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.DropdownMenuItem;
import nl.topicus.cobra.web.components.menu.HorizontalSeperator;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.eduarte.web.pages.beheer.AlgemeneInstellingenPage;
import nl.topicus.eduarte.web.pages.beheer.BeheerHomePage;
import nl.topicus.eduarte.web.pages.beheer.CachesPage;
import nl.topicus.eduarte.web.pages.beheer.CentraalBeheerSignalenPage;
import nl.topicus.eduarte.web.pages.beheer.ModuleAfnamesPage;
import nl.topicus.eduarte.web.pages.beheer.ReplicatedConnectionsPage;
import nl.topicus.eduarte.web.pages.beheer.account.GebruikersoverzichtPage;
import nl.topicus.eduarte.web.pages.beheer.account.RollenEnRechtenPage;
import nl.topicus.eduarte.web.pages.beheer.documenten.DocumentCategorieZoekenPage;
import nl.topicus.eduarte.web.pages.beheer.documenten.DocumentTypeZoekenPage;
import nl.topicus.eduarte.web.pages.beheer.landelijk.ModuleAfnameZoekenPage;
import nl.topicus.eduarte.web.pages.beheer.organisatie.GroepstypeZoekenPage;
import nl.topicus.eduarte.web.pages.beheer.organisatie.SoortOrganisatieEenheidZoekenPage;
import nl.topicus.eduarte.web.pages.beheer.participatie.AfspraakTypePage;
import nl.topicus.eduarte.web.pages.beheer.rapportage.DocumentTemplateZoekenPage;
import nl.topicus.eduarte.web.pages.beheer.rapportage.MergeFieldsPage;
import nl.topicus.eduarte.web.pages.shared.jobs.RunningJobsPage;

/**
 * @author loite
 */
public class BeheerMenu extends AbstractMenuBar
{

	private static final long serialVersionUID = 1L;

	/**
	 * Wordt gebruikt om een menuitem in participatie aan het menu toe te voegen
	 */
	public static final String ONDERWIJSMENUNAME = "Onderwijs";

	public static final String BEHEERTABELLENMENUNAME = "Beheer tabellen";

	public static final String EXTERNEORGANISATIESMENUNAME = "Externe organisaties";

	public static final String SYSTEEMNAME = "Systeem";

	public static final String ACCOUNTBEHEERNAME = "Accountbeheer";

	public static final String AANWEZIGHEIDMENUNAME = "Aanwezigheid";

	public static final String HOMENUNAME = "Hoger Onderwijs";

	public static final String BEGELEIDING = "Begeleiding";

	public BeheerMenu(String id, MenuItemKey selectedTab)
	{
		super(id, selectedTab);

		createSysteemMenu();
		createAccountbeheerMenu();
		createOrganisatiemodelMenu();
		createOnderwijsMenu();
		createRapportageMenu();
		createBeheertabellenMenu();
		createAanwezigheidMenu();
		createHOMenu();

		addModuleMenuItems();
	}

	private void createAanwezigheidMenu()
	{
		DropdownMenuItem participatie = new DropdownMenuItem(AANWEZIGHEIDMENUNAME);
		participatie.add(new MenuItem(AfspraakTypePage.class, BeheerMenuItem.AfspraakTypes));
		addItem(participatie);
	}

	private void createHOMenu()
	{
		DropdownMenuItem ho = new DropdownMenuItem(HOMENUNAME);
		addItem(ho);
	}

	private void createSysteemMenu()
	{
		DropdownMenuItem systeem = new DropdownMenuItem(SYSTEEMNAME);
		systeem.add(new MenuItem(BeheerHomePage.class, BeheerMenuItem.Beheer));
		systeem.add(new MenuItem(ModuleAfnamesPage.class, BeheerMenuItem.ModuleAfnames));
		systeem.add(new MenuItem(AlgemeneInstellingenPage.class,
			BeheerMenuItem.AlgemeneInstellingen));
		systeem.add(new MenuItem(CentraalBeheerSignalenPage.class, BeheerMenuItem.Signalen));
		systeem.add(new MenuItem(RunningJobsPage.class, BeheerMenuItem.LopendeTaken));
		systeem.add(new MenuItem(CachesPage.class, BeheerMenuItem.Caches));
		systeem
			.add(new MenuItem(ReplicatedConnectionsPage.class, BeheerMenuItem.Databaseconnecties));
		systeem.add(new MenuItem(ModuleAfnameZoekenPage.class, BeheerMenuItem.Modules));

		addItem(systeem);
	}

	private void createAccountbeheerMenu()
	{
		DropdownMenuItem accountbeheer = new DropdownMenuItem(ACCOUNTBEHEERNAME);
		accountbeheer.add(new MenuItem(GebruikersoverzichtPage.class, BeheerMenuItem.Gebruikers));
		accountbeheer.add(new MenuItem(RollenEnRechtenPage.class, BeheerMenuItem.RollenEnRechten));
		addItem(accountbeheer);
	}

	private void createOrganisatiemodelMenu()
	{
		DropdownMenuItem organisatiemodelbeheer = new DropdownMenuItem("Organisatiemodel");
		addItem(organisatiemodelbeheer);
	}

	private void createOnderwijsMenu()
	{
		DropdownMenuItem onderwijs = new DropdownMenuItem(ONDERWIJSMENUNAME);
		addItem(onderwijs);
	}

	private void createRapportageMenu()
	{
		DropdownMenuItem rapportages = new DropdownMenuItem("Rapportage");
		rapportages.add(new MenuItem(DocumentTemplateZoekenPage.class,
			BeheerMenuItem.Samenvoegdocumenten));
		rapportages
			.add(new MenuItem(MergeFieldsPage.class, BeheerMenuItem.OverzichtSamenvoegvelden));
		addItem(rapportages);
	}

	private void createBeheertabellenMenu()
	{
		DropdownMenuItem beheertabellen = new DropdownMenuItem(BEHEERTABELLENMENUNAME);
		beheertabellen.add(new MenuItem(SoortOrganisatieEenheidZoekenPage.class,
			BeheerMenuItem.SoortOrganisatie_Eenheden));
		beheertabellen.add(new MenuItem(GroepstypeZoekenPage.class, BeheerMenuItem.Groepstype));
		beheertabellen.add(new HorizontalSeperator());
		beheertabellen.add(new MenuItem(DocumentCategorieZoekenPage.class,
			BeheerMenuItem.DocumentCategorien));
		beheertabellen
			.add(new MenuItem(DocumentTypeZoekenPage.class, BeheerMenuItem.DocumentTypes));
		addItem(beheertabellen);
	}
}
