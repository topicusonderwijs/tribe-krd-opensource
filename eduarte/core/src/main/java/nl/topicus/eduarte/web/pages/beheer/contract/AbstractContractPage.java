package nl.topicus.eduarte.web.pages.beheer.contract;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.sidebar.IContextInfoObject;
import nl.topicus.eduarte.web.components.menu.ContractMenu;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Baseclass voor externe contract pagina's.
 * 
 * @author schimmel
 */
public abstract class AbstractContractPage extends SecurePage
{
	private final MenuItemKey selectedMenuItem;

	public AbstractContractPage(MenuItemKey selectedMenuItem, Contract contract)
	{
		super(CoreMainMenuItem.Relatie);
		setDefaultModel(ModelFactory.getCompoundModel(contract));
		this.selectedMenuItem = selectedMenuItem;
	}

	/**
	 * @return van deze pagina.
	 */
	public Contract getContextContract()
	{
		return getContextContractModel().getObject();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new ContractMenu(id, getContextContractModel(), selectedMenuItem);
	}

	public MenuItemKey getSelectedMenuItem()
	{
		return selectedMenuItem;
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, new PropertyModel<String>(getContextContractModel(), "naam"));
	}

	@Override
	protected Class< ? extends Page> getBasePageForContext()
	{
		return ContractOverzichtPage.class;
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(Contract.class);
		ctorArgValues.add(getContextContractModel());
	}

	private static final List<Class< ? extends IContextInfoObject>> CONTEXT_PARAMETER_TYPES =
		new ArrayList<Class< ? extends IContextInfoObject>>(2);
	static
	{
		CONTEXT_PARAMETER_TYPES.add(Contract.class);
	}

	@Override
	public List<Class< ? extends IContextInfoObject>> getContextParameterTypes()
	{
		return CONTEXT_PARAMETER_TYPES;
	}

	@Override
	public IContextInfoObject getContextValue(Class< ? extends IContextInfoObject> clazz)
	{
		if (clazz == Contract.class)
		{
			return getContextContract();
		}
		return null;
	}

	@Override
	public String getContextOmschrijving()
	{
		return getContextValue(Contract.class).getContextInfoOmschrijving();
	}

	@SuppressWarnings("unchecked")
	public IModel<Contract> getContextContractModel()
	{
		return (IModel<Contract>) getDefaultModel();
	}
}
