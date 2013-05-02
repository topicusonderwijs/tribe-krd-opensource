/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.menu.deelnemerPortaal;

import nl.topicus.cobra.web.components.menu.DropdownMenuItem;
import nl.topicus.cobra.web.components.menu.HorizontalSeperator;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.web.pages.deelnemerportaal.dossier.DeelnemerportaalGroepenPage;
import nl.topicus.eduarte.web.pages.deelnemerportaal.dossier.DeelnemerportaalPersonaliaPage;
import nl.topicus.eduarte.web.pages.deelnemerportaal.dossier.DeelnemerportaalVerbintenisPage;

import org.apache.wicket.model.IModel;

/**
 * @author ambrosius, papegaaij
 */
public class DeelnemerportaalDossierMenu extends AbstractDeelnemerportaalMenu
{
	private static final long serialVersionUID = 1L;

	public DeelnemerportaalDossierMenu(String id, MenuItemKey selectedTab,
			IModel<Verbintenis> inschrijvingModel)
	{
		super(id, selectedTab, inschrijvingModel);

		addItem(new MenuItem(createPageLink(DeelnemerportaalPersonaliaPage.class),
			DeelnemerportaalDossierMenuItem.Personalia));
		DropdownMenuItem registraties = new DropdownMenuItem("Inschrijvingen");
		addItem(registraties);
		registraties.add(new MenuItem(createPageLink(DeelnemerportaalGroepenPage.class),
			DeelnemerportaalDossierMenuItem.Groepen));
		registraties.add(new HorizontalSeperator());
		registraties.add(new MenuItem(createPageLink(DeelnemerportaalVerbintenisPage.class),
			DeelnemerportaalDossierMenuItem.Inschrijving));

		addModuleMenuItems();
	}
}
