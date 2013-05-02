package nl.topicus.eduarte.web.components.menu;

import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;
import nl.topicus.eduarte.web.pages.onderwijs.taxonomie.AbstractTaxonomieElementPage;
import nl.topicus.eduarte.web.pages.onderwijs.taxonomie.TaxonomieElementDeelgebiedenPage;
import nl.topicus.eduarte.web.pages.onderwijs.taxonomie.TaxonomieElementVerbintenisgebiedenPage;
import nl.topicus.eduarte.web.pages.onderwijs.taxonomie.TaxonomieElementkaartPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IModel;

/**
 * Menu voor taxonomieelementpagina's.
 * 
 * @author loite
 */
public class TaxonomieElementMenu extends AbstractMenuBar
{
	private static final long serialVersionUID = 1L;

	private final IModel<TaxonomieElement> taxonomieElementModel;

	public TaxonomieElementMenu(String id, IModel<TaxonomieElement> taxonomieElementModel,
			MenuItemKey selected)
	{
		super(id, selected);
		setDefaultModel(taxonomieElementModel);
		this.taxonomieElementModel = taxonomieElementModel;
		addItem(new MenuItem(createPageLink(TaxonomieElementkaartPage.class),
			TaxonomieElementMenuItem.Algemeen));
		addItem(new MenuItem(createPageLink(TaxonomieElementVerbintenisgebiedenPage.class),
			TaxonomieElementMenuItem.Verbintenisgebieden));
		addItem(new MenuItem(createPageLink(TaxonomieElementDeelgebiedenPage.class),
			TaxonomieElementMenuItem.Deelgebieden));
		addModuleMenuItems();
	}

	/**
	 * Maakt een pagelink naar een AbstractTaxonomieElementPage. Kan ook gebruikt worden
	 * door menu-extenders.
	 */
	public IPageLink createPageLink(Class< ? extends AbstractTaxonomieElementPage> pageClass)
	{
		return new TaxonomieElementPageLink(pageClass, taxonomieElementModel);
	}

	private static final class TaxonomieElementPageLink implements IPageLink
	{
		private static final long serialVersionUID = 1L;

		private final Class< ? extends AbstractTaxonomieElementPage> pageClass;

		private final IModel<TaxonomieElement> taxonomieElementModel;

		private TaxonomieElementPageLink(Class< ? extends AbstractTaxonomieElementPage> pageClass,
				IModel<TaxonomieElement> taxonomieElementModel)
		{
			this.pageClass = pageClass;
			this.taxonomieElementModel = taxonomieElementModel;
		}

		@Override
		public Page getPage()
		{
			TaxonomieElement taxonomieElement = taxonomieElementModel.getObject();
			return ReflectionUtil.invokeConstructor(pageClass, taxonomieElement);
		}

		@Override
		public Class< ? extends AbstractTaxonomieElementPage> getPageIdentity()
		{
			return pageClass;
		}

	}

	@Override
	public void detachModels()
	{
		super.detachModels();
		ComponentUtil.detachQuietly(taxonomieElementModel);
	}

	public IModel<TaxonomieElement> getTaxonomieElementModel()
	{
		return taxonomieElementModel;
	}

}
