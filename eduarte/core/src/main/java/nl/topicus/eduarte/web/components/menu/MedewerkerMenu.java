package nl.topicus.eduarte.web.components.menu;

import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.DropdownMenuItem;
import nl.topicus.cobra.web.components.menu.IMenuItem;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.web.pages.medewerker.AbstractMedewerkerPage;
import nl.topicus.eduarte.web.pages.medewerker.MedewerkerAanstellingPage;
import nl.topicus.eduarte.web.pages.medewerker.MedewerkerAccountPage;
import nl.topicus.eduarte.web.pages.medewerker.MedewerkerGroepZoekenPage;
import nl.topicus.eduarte.web.pages.medewerker.MedewerkerKenmerkenPage;
import nl.topicus.eduarte.web.pages.medewerker.MedewerkerPersonaliaPage;
import nl.topicus.eduarte.web.pages.medewerker.MedewerkerkaartPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IModel;

/**
 * Menu voor medewerkerpagina's.
 * 
 * @author loite
 */
public class MedewerkerMenu extends AbstractMenuBar
{
	private static final long serialVersionUID = 1L;

	private final IModel<Medewerker> medewerkerModel;

	public MedewerkerMenu(String id, IModel<Medewerker> medewerkerModel, MenuItemKey selected)
	{
		super(id, selected);
		setDefaultModel(medewerkerModel);
		this.medewerkerModel = medewerkerModel;
		addItem(new MenuItem(createPageLink(MedewerkerkaartPage.class),
			MedewerkerMenuItem.Medewerkerkaart));

		// Personalia
		MedewerkerDropDownMenu dropdownPersonalia = new MedewerkerDropDownMenu("Personalia");
		dropdownPersonalia.add(new MenuItem(createPageLink(MedewerkerPersonaliaPage.class),
			MedewerkerMenuItem.Personalia));
		dropdownPersonalia.add(new MenuItem(createPageLink(MedewerkerKenmerkenPage.class),
			MedewerkerMenuItem.Kenmerken));
		addItem(dropdownPersonalia);

		addItem(new MenuItem(createPageLink(MedewerkerAccountPage.class),
			MedewerkerMenuItem.Account));
		addItem(new MenuItem(createPageLink(MedewerkerAanstellingPage.class),
			MedewerkerMenuItem.Aanstelling));
		addItem(new MenuItem(createPageLink(MedewerkerGroepZoekenPage.class),
			MedewerkerMenuItem.Groepen));

		addModuleMenuItems();
	}

	public IPageLink createPageLink(Class< ? extends AbstractMedewerkerPage> pageClass)
	{
		return new MedewerkerPageLink(pageClass);
	}

	private final class MedewerkerPageLink implements IPageLink
	{
		private static final long serialVersionUID = 1L;

		private final Class< ? extends AbstractMedewerkerPage> pageClass;

		private MedewerkerPageLink(Class< ? extends AbstractMedewerkerPage> pageClass)
		{
			this.pageClass = pageClass;
		}

		@Override
		public Page getPage()
		{
			Medewerker medewerker = medewerkerModel.getObject();
			return ReflectionUtil.invokeConstructor(pageClass, medewerker);
		}

		@Override
		public Class< ? extends AbstractMedewerkerPage> getPageIdentity()
		{
			return pageClass;
		}

	}

	private final class MedewerkerDropDownMenu extends DropdownMenuItem
	{
		private static final long serialVersionUID = 1L;

		public MedewerkerDropDownMenu(IModel<String> label, IMenuItem... items)
		{
			super(label, items);
		}

		public MedewerkerDropDownMenu(String label)
		{
			super(label);
		}
	}

	@Override
	public void detachModels()
	{
		super.detachModels();
		ComponentUtil.detachQuietly(medewerkerModel);
	}

}
