package nl.topicus.eduarte.web.pages.onderwijs.taxonomie;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.eduarte.entities.sidebar.IContextInfoObject;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;
import nl.topicus.eduarte.web.components.menu.TaxonomieElementMenu;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Baseclass voor alle taxonomieelementpagina's.
 * 
 * @author loite
 */
public abstract class AbstractTaxonomieElementPage extends SecurePage
{
	private final MenuItemKey selectedMenuItem;

	/**
	 * Constructor
	 * 
	 * @param selectedMenuItem
	 * @param taxonomieElementModel
	 */
	public AbstractTaxonomieElementPage(MenuItemKey selectedMenuItem,
			IModel<TaxonomieElement> taxonomieElementModel)
	{
		super(taxonomieElementModel, CoreMainMenuItem.Onderwijs);
		this.selectedMenuItem = selectedMenuItem;
	}

	/**
	 * @return Het taxonomieelement van deze pagina.
	 */
	public TaxonomieElement getContextTaxonomieElement()
	{
		return getContextTaxonomieElementModel().getObject();
	}

	/**
	 * @return De taxonomie van het taxonomie-element dat getoond wordt.
	 */
	public Taxonomie getContextTaxonomie()
	{
		return getContextTaxonomieElement().getTaxonomie();
	}

	@SuppressWarnings("unchecked")
	public IModel<TaxonomieElement> getContextTaxonomieElementModel()
	{
		return (IModel<TaxonomieElement>) getDefaultModel();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new TaxonomieElementMenu(id, getContextTaxonomieElementModel(), selectedMenuItem);
	}

	@Override
	public final Component createTitle(String id)
	{
		return new Label(id, new PropertyModel<String>(getContextTaxonomieElementModel(),
			"toString"));
	}

	@Override
	protected Class< ? extends Page> getBasePageForContext()
	{
		return TaxonomieElementkaartPage.class;
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(TaxonomieElement.class);
		ctorArgValues.add(getContextTaxonomieElementModel());
	}

	private static final List<Class< ? extends IContextInfoObject>> CONTEXT_PARAMETER_TYPES =
		new ArrayList<Class< ? extends IContextInfoObject>>(2);
	static
	{
		CONTEXT_PARAMETER_TYPES.add(TaxonomieElement.class);
	}

	@Override
	public List<Class< ? extends IContextInfoObject>> getContextParameterTypes()
	{
		return CONTEXT_PARAMETER_TYPES;
	}

	@Override
	public IContextInfoObject getContextValue(Class< ? extends IContextInfoObject> clazz)
	{
		if (clazz == TaxonomieElement.class)
		{
			return getContextTaxonomieElement();
		}
		return null;
	}

	public MenuItemKey getSelectedMenuItem()
	{
		return selectedMenuItem;
	}

}
