package nl.topicus.eduarte.web.components.menu;

import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.DropdownMenuItem;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus.*;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IModel;

/**
 * Menu voor onderwijsproductpagina's.
 * 
 * @author loite
 */
public class OnderwijsproductMenu extends AbstractMenuBar
{
	private static final long serialVersionUID = 1L;

	private final IModel<Onderwijsproduct> onderwijsproductModel;

	public static final String RESULTATEN_MENU_NAME = "Resultaten";

	public OnderwijsproductMenu(String id, IModel<Onderwijsproduct> onderwijsproductModel,
			MenuItemKey selected)
	{
		super(id, selected);
		setDefaultModel(onderwijsproductModel);
		this.onderwijsproductModel = onderwijsproductModel;
		DropdownMenuItem algemeen = new DropdownMenuItem("Algemeen");
		addItem(algemeen);
		algemeen.add(new MenuItem(createPageLink(OnderwijsproductKaartPage.class),
			OnderwijsproductMenuItem.Algemeen));
		algemeen.add(new MenuItem(createPageLink(OnderwijsproductEigenaarPage.class),
			OnderwijsproductMenuItem.Eigenaar));
		algemeen.add(new MenuItem(createPageLink(OnderwijsproductOpvolgersPage.class),
			OnderwijsproductMenuItem.Opvolgers));
		addItem(new MenuItem(createPageLink(OnderwijsproductVoorwaardenPage.class),
			OnderwijsproductMenuItem.Voorwaarden));
		addItem(new MenuItem(createPageLink(OnderwijsproductPaklijstPage.class),
			OnderwijsproductMenuItem.Paklijst));
		addItem(new MenuItem(createPageLink(OnderwijsproductKalenderPage.class),
			OnderwijsproductMenuItem.Kalender));
		addItem(new MenuItem(createPageLink(OnderwijsproductToegankelijkheidPage.class),
			OnderwijsproductMenuItem.Toegankelijkheid));
		addItem(new MenuItem(createPageLink(OnderwijsproductPersoneelPage.class),
			OnderwijsproductMenuItem.Personeel));
		DropdownMenuItem middelen = new DropdownMenuItem("Middelen");
		addItem(middelen);
		middelen.add(new MenuItem(createPageLink(OnderwijsproductGebruiksmiddelPage.class),
			OnderwijsproductMenuItem.Gebruiksmiddelen));
		middelen.add(new MenuItem(createPageLink(OnderwijsproductVerbruiksmiddelPage.class),
			OnderwijsproductMenuItem.Verbruiksmiddelen));
		addItem(new MenuItem(createPageLink(OnderwijsproductBijlagePage.class),
			OnderwijsproductMenuItem.Bijlagen));
		addItem(new DropdownMenuItem(OnderwijsproductMenu.RESULTATEN_MENU_NAME));
		addModuleMenuItems();
	}

	public IPageLink createPageLink(Class< ? extends SecurePage> pageClass)
	{
		return new OnderwijsproductPageLink(pageClass);
	}

	private final class OnderwijsproductPageLink implements IPageLink
	{
		private static final long serialVersionUID = 1L;

		private final Class< ? extends SecurePage> pageClass;

		private OnderwijsproductPageLink(Class< ? extends SecurePage> pageClass)
		{
			this.pageClass = pageClass;
		}

		@Override
		public Page getPage()
		{
			Onderwijsproduct onderwijsproduct = onderwijsproductModel.getObject();
			return ReflectionUtil.invokeConstructor(pageClass, onderwijsproduct);
		}

		@Override
		public Class< ? extends SecurePage> getPageIdentity()
		{
			return pageClass;
		}
	}

	public Onderwijsproduct getOnderwijsproduct()
	{
		return onderwijsproductModel.getObject();
	}

	@Override
	public void detachModels()
	{
		super.detachModels();
		ComponentUtil.detachQuietly(onderwijsproductModel);
	}

}
