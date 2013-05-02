package nl.topicus.eduarte.web.components.menu;

import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.web.pages.beheer.organisatie.AbstractExterneOrganisatiePage;
import nl.topicus.eduarte.web.pages.beheer.organisatie.ExterneOrganisatieKenmerkenPage;
import nl.topicus.eduarte.web.pages.beheer.organisatie.ExterneOrganisatieOverzichtPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IModel;

/**
 * Menu voor externeOrganisatiepagina's.
 * 
 * @author loite
 */
public class ExterneOrganisatieMenu extends AbstractMenuBar
{
	private static final long serialVersionUID = 1L;

	private final IModel<ExterneOrganisatie> externeOrganisatieModel;

	public ExterneOrganisatieMenu(String id, IModel<ExterneOrganisatie> externeOrganisatieModel,
			MenuItemKey selected)
	{
		super(id, selected);
		setDefaultModel(externeOrganisatieModel);
		this.externeOrganisatieModel = externeOrganisatieModel;
		addItem(new MenuItem(createPageLink(ExterneOrganisatieOverzichtPage.class),
			ExterneOrganisatieMenuItem.ExterneOrganisatiekaart));
		addItem(new MenuItem(createPageLink(ExterneOrganisatieKenmerkenPage.class),
			ExterneOrganisatieMenuItem.Kenmerken));
		addModuleMenuItems();
	}

	public IPageLink createPageLink(Class< ? extends AbstractExterneOrganisatiePage> pageClass)
	{
		return new ExterneOrganisatiePageLink(pageClass);
	}

	private final class ExterneOrganisatiePageLink implements IPageLink
	{
		private static final long serialVersionUID = 1L;

		private final Class< ? extends AbstractExterneOrganisatiePage> pageClass;

		private ExterneOrganisatiePageLink(
				Class< ? extends AbstractExterneOrganisatiePage> pageClass)
		{
			this.pageClass = pageClass;
		}

		@Override
		public Page getPage()
		{
			ExterneOrganisatie externeOrganisatie = externeOrganisatieModel.getObject();
			return ReflectionUtil.invokeConstructor(pageClass, externeOrganisatie);
		}

		@Override
		public Class< ? extends AbstractExterneOrganisatiePage> getPageIdentity()
		{
			return pageClass;
		}

	}

	@Override
	public void detachModels()
	{
		super.detachModels();
		ComponentUtil.detachQuietly(externeOrganisatieModel);
	}

	/**
	 * @return De ExterneOrganisatie die gekoppeld is aan het menu.
	 */
	public ExterneOrganisatie getExterneOrganisatie()
	{
		return externeOrganisatieModel.getObject();
	}

}
