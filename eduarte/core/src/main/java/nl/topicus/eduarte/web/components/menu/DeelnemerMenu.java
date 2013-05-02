/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.menu;

import nl.topicus.cobra.reflection.InvocationFailedException;
import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.DropdownMenuItem;
import nl.topicus.cobra.web.components.menu.HorizontalSeperator;
import nl.topicus.cobra.web.components.menu.IMenuItem;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.deelnemerkaart.DeelnemerkaartPage;
import nl.topicus.eduarte.web.pages.deelnemer.documenten.DeelnemerDocumentenPage;
import nl.topicus.eduarte.web.pages.deelnemer.groep.DeelnemerGroepenPage;
import nl.topicus.eduarte.web.pages.deelnemer.intake.DeelnemerIntakePage;
import nl.topicus.eduarte.web.pages.deelnemer.kenmerk.DeelnemerKenmerkenPage;
import nl.topicus.eduarte.web.pages.deelnemer.personalia.DeelnemerPersonaliaPage;
import nl.topicus.eduarte.web.pages.deelnemer.personalia.DeelnemerRelatiesOverzichtPage;
import nl.topicus.eduarte.web.pages.deelnemer.verbintenis.DeelnemerVerbintenisPage;
import nl.topicus.eduarte.web.pages.deelnemer.verbintenis.DeelnemerVooropleidingenPage;
import nl.topicus.eduarte.web.pages.deelnemer.verbintenis.bpv.DeelnemerBPVPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author loite
 */
public class DeelnemerMenu extends AbstractMenuBar
{
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(DeelnemerMenu.class);

	private final IModel<Deelnemer> deelnemerModel;

	private final IModel<Verbintenis> inschrijvingModel;

	public static final String AANWEZIGHEIDMENUNAME = "Aanwezigheid";

	public static final String VERBINTENISSENMENUNAME = "Verbintenissen";

	public static final String STUDIELINKMENUNAME = "Studielink";

	public static final String RESULTATEN_MENU = "Resultaten";

	public DeelnemerMenu(String id, MenuItemKey selectedItem, IModel<Deelnemer> deelnemer)
	{
		this(id, selectedItem, deelnemer, new Model<Verbintenis>(deelnemer.getObject()
			.getVerbintenisOpPeildatum()));
	}

	public DeelnemerMenu(String id, MenuItemKey selectedItem, IModel<Deelnemer> deelnemer,
			IModel<Verbintenis> inschrijving)
	{
		super(id, selectedItem);
		setDefaultModel(deelnemer);
		this.deelnemerModel = deelnemer;
		this.inschrijvingModel = inschrijving;
		Verbintenis verbintenis = inschrijving.getObject();

		// Deelnemerkaart
		addItem(new MenuItem(createPageLink(DeelnemerkaartPage.class),
			DeelnemerMenuItem.Deelnemerkaart));

		// Personalia
		DeelnemerDropDownMenu dropdownPersonalia = new DeelnemerDropDownMenu("Personalia");
		dropdownPersonalia.add(new MenuItem(createPageLink(DeelnemerPersonaliaPage.class),
			DeelnemerMenuItem.Personalia));
		dropdownPersonalia.add(new MenuItem(createPageLink(DeelnemerRelatiesOverzichtPage.class),
			DeelnemerMenuItem.Relaties));
		dropdownPersonalia.add(new MenuItem(createPageLink(DeelnemerKenmerkenPage.class),
			DeelnemerMenuItem.Kenmerken));
		dropdownPersonalia.add(new HorizontalSeperator());
		dropdownPersonalia.add(new MenuItem(createPageLink(DeelnemerVooropleidingenPage.class),
			DeelnemerMenuItem.Vooropleidingen));
		addItem(dropdownPersonalia);
		dropdownPersonalia.add(new HorizontalSeperator());
		dropdownPersonalia.add(new MenuItem(createPageLink(DeelnemerDocumentenPage.class),
			DeelnemerMenuItem.Documenten));

		// Verbintenis
		DeelnemerDropDownMenu dropdownVerbintenis =
			new DeelnemerDropDownMenu(VERBINTENISSENMENUNAME);
		if (verbintenis == null || VerbintenisStatus.Aangemeld != verbintenis.getStatus())
			dropdownVerbintenis.add(new MenuItem(createPageLink(DeelnemerVerbintenisPage.class),
				DeelnemerMenuItem.Verbintenis));
		dropdownVerbintenis.add(new MenuItem(createPageLink(DeelnemerBPVPage.class),
			DeelnemerMenuItem.BPV));
		dropdownVerbintenis.add(new MenuItem(createPageLink(DeelnemerIntakePage.class),
			DeelnemerMenuItem.Intake));
		dropdownVerbintenis.add(new HorizontalSeperator());
		dropdownVerbintenis.add(new MenuItem(createPageLink(DeelnemerGroepenPage.class),
			DeelnemerMenuItem.Groepen));
		addItem(dropdownVerbintenis);

		addModuleMenuItems();
	}

	public IPageLink createPageLink(Class< ? extends SecurePage> pageClass)
	{
		return new DeelnemerPageLink(pageClass);
	}

	public DropdownMenuItem createDropdown(IModel<String> label, IMenuItem... items)
	{
		return new DeelnemerDropDownMenu(label, items);
	}

	public DropdownMenuItem createDropdown(String label)
	{
		return new DeelnemerDropDownMenu(label);
	}

	public IPageLink createDeelnemerAanmeldingPageLink(Class< ? extends SecurePage> pageClass)
	{
		return new DeelnemerAanmeldingPageLink(pageClass);
	}

	private final class DeelnemerAanmeldingPageLink implements IPageLink
	{
		private static final long serialVersionUID = 1L;

		private final Class< ? extends SecurePage> pageClass;

		public DeelnemerAanmeldingPageLink(Class< ? extends SecurePage> pageClass)
		{
			this.pageClass = pageClass;
		}

		@Override
		public Page getPage()
		{
			Verbintenis inschrijving = inschrijvingModel.getObject();
			try
			{
				return ReflectionUtil.invokeConstructor(pageClass, inschrijving.getAanmelding());
			}
			catch (InvocationFailedException e)
			{
				log.error(e.toString(), e);
			}
			return null;

		}

		@Override
		public Class< ? extends Page> getPageIdentity()
		{
			return pageClass;
		}
	}

	private final class DeelnemerPageLink implements IPageLink
	{
		private static final long serialVersionUID = 1L;

		private final Class< ? extends SecurePage> pageClass;

		public DeelnemerPageLink(Class< ? extends SecurePage> pageClass)
		{
			this.pageClass = pageClass;
		}

		@Override
		public Page getPage()
		{
			Verbintenis inschrijving = inschrijvingModel.getObject();
			Deelnemer deelnemer = getDeelnemer();
			if (inschrijving != null)
			{
				try
				{
					return ReflectionUtil.invokeConstructor(pageClass, deelnemer, inschrijving);
				}
				catch (InvocationFailedException e)
				{
					log.error(e.toString(), e);
				}
			}
			// Geen pagina op basis van inschrijving. Probeer op basis van deelnemer.
			return ReflectionUtil.invokeConstructor(pageClass, deelnemer);
		}

		@Override
		public Class< ? extends Page> getPageIdentity()
		{
			return pageClass;
		}
	}

	@Override
	public void detachModels()
	{
		super.detachModels();
		deelnemerModel.detach();
		inschrijvingModel.detach();
	}

	/**
	 * @return De deelnemer die gekoppeld is aan het menu.
	 */
	public Deelnemer getDeelnemer()
	{
		return deelnemerModel.getObject();
	}

	/**
	 * @return De inschrijving die gekoppeld is aan het menu
	 */
	public Verbintenis getInschrijving()
	{
		return inschrijvingModel.getObject();
	}

	private final class DeelnemerDropDownMenu extends DropdownMenuItem
	{
		private static final long serialVersionUID = 1L;

		public DeelnemerDropDownMenu(IModel<String> label, IMenuItem... items)
		{
			super(label, items);
		}

		public DeelnemerDropDownMenu(String label)
		{
			super(label);
		}
	}

	/**
	 * @return Het model met daarin de deelnemer die gekoppeld is aan het menu.
	 */
	public IModel<Deelnemer> getDeelnemerModel()
	{
		return deelnemerModel;
	}
}
