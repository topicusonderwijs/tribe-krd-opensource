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
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.DeelnemerRapportagesPage;
import nl.topicus.eduarte.web.pages.deelnemer.DeelnemerUitgebreidZoekenPage;
import nl.topicus.eduarte.web.pages.deelnemer.DeelnemerZoekenPage;
import nl.topicus.eduarte.web.pages.deelnemer.MijnDeelnemerPage;
import nl.topicus.eduarte.web.pages.deelnemer.zoekopdrachten.OpgeslagenZoekOpdrachtenPage;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.markup.html.link.IPageLink;

/**
 * @author loite
 */
public class DeelnemerCollectiefMenu extends AbstractMenuBar
{
	private static final long serialVersionUID = 1L;

	public static final String RAPPORTAGESNAME = "Rapportages";

	public static final String PARTICIPATIENAME = "Participatie";

	public static final String ZOEKENNAME = "Zoeken";

	public final String MIJNDEELNEMERSNAME = getMijnDeelnemersName();

	public static String getMijnDeelnemersName()
	{
		return "Mijn " + EduArteApp.get().getDeelnemerTermMeervoud();
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 * @param selectedItem
	 */
	public DeelnemerCollectiefMenu(String id, MenuItemKey selectedItem)
	{
		super(id, selectedItem);
		DropdownMenuItem zoeken = new DropdownMenuItem(ZOEKENNAME);
		addItem(zoeken);
		zoeken.add(new MenuItem(DeelnemerZoekenPage.class,
			DeelnemerCollectiefMenuItem.DeelnemerZoeken));
		zoeken.add(new MenuItem(DeelnemerUitgebreidZoekenPage.class,
			DeelnemerCollectiefMenuItem.DeelnemerUitgebreidZoeken));
		zoeken.add(new HorizontalSeperator());
		zoeken.add(new MenuItem(OpgeslagenZoekOpdrachtenPage.class,
			DeelnemerCollectiefMenuItem.OpgeslagenZoekopdrachten));

		DropdownMenuItem mijnDeelnemers = new DropdownMenuItem(MIJNDEELNEMERSNAME);
		addItem(mijnDeelnemers);

		mijnDeelnemers.add(new MenuItem(new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public SecurePage getPage()
			{
				VerbintenisZoekFilter filter = DeelnemerZoekenPage.getDefaultFilter();
				filter.setMentorOfDocent(EduArteContext.get().getMedewerker());
				filter.setMentorOfDocentRequired(true);
				return new MijnDeelnemerPage(filter);
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return MijnDeelnemerPage.class;
			}
		}, DeelnemerCollectiefMenuItem.MijnDeelnemers));

		DropdownMenuItem rapportages = new DropdownMenuItem(RAPPORTAGESNAME);
		addItem(rapportages);
		rapportages.add(new MenuItem(DeelnemerRapportagesPage.class,
			DeelnemerCollectiefMenuItem.Rapportages));

		addModuleMenuItems();
	}
}
