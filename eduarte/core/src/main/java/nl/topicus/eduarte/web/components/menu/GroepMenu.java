/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.menu;

import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.DropdownMenuItem;
import nl.topicus.cobra.web.components.menu.IMenuItem;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.groep.GroepDossieroverzichtPage;
import nl.topicus.eduarte.web.pages.groep.GroepHokjeslijstPage;
import nl.topicus.eduarte.web.pages.groep.GroepKaartPage;
import nl.topicus.eduarte.web.pages.groep.GroepPasfotoPage;

import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IModel;

/**
 * @author hoeve
 */
public class GroepMenu extends AbstractMenuBar
{
	private static final long serialVersionUID = 1L;

	public static String RESULTATEN_MENU_NAME = "Resultaten";

	private final IModel<Groep> groepModel;

	public GroepMenu(String id, MenuItemKey selectedItem, IModel<Groep> groep)
	{
		super(id, selectedItem);
		setDefaultModel(groep);
		this.groepModel = groep;

		addItem(new MenuItem(createPageLink(GroepKaartPage.class), GroepMenuItem.Groepkaart));
		addItem(new MenuItem(createPageLink(GroepPasfotoPage.class), GroepMenuItem.Pasfotos));
		DropdownMenuItem overzichten = new DropdownMenuItem(GroepMenuItem.Overzichten.toString());
		overzichten.add(new MenuItem(createPageLink(GroepDossieroverzichtPage.class),
			GroepMenuItem.Dossieroverzicht));
		overzichten.add(new MenuItem(createPageLink(GroepHokjeslijstPage.class),
			GroepMenuItem.Hokjeslijst));
		addItem(overzichten);

		addItem(new DropdownMenuItem(RESULTATEN_MENU_NAME));

		addModuleMenuItems();
	}

	public IPageLink createPageLink(Class< ? extends SecurePage> pageClass)
	{
		return new GroepPageLink(pageClass, groepModel);
	}

	public DropdownMenuItem createDropdown(IModel<String> label, IMenuItem... items)
	{
		return new GroepDropDownMenu(label, items);
	}

	public DropdownMenuItem createDropdown(String label)
	{
		return new GroepDropDownMenu(label);
	}

	@Override
	public void detachModels()
	{
		super.detachModels();
		groepModel.detach();
	}

	private final class GroepDropDownMenu extends DropdownMenuItem
	{
		private static final long serialVersionUID = 1L;

		public GroepDropDownMenu(IModel<String> label, IMenuItem... items)
		{
			super(label, items);
		}

		public GroepDropDownMenu(String label)
		{
			super(label);
		}
	}

	public IModel<Groep> getGroepModel()
	{
		return groepModel;
	}

}
