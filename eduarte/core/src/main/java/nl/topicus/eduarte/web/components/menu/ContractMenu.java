package nl.topicus.eduarte.web.components.menu;

import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.web.pages.beheer.contract.AbstractContractPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IModel;

/**
 * Menu voor contract pagina's.
 * 
 * @author schimmel
 */
public class ContractMenu extends AbstractMenuBar
{
	private static final long serialVersionUID = 1L;

	private final IModel<Contract> contractModel;

	public ContractMenu(String id, IModel<Contract> contractModel, MenuItemKey selected)
	{
		super(id, selected);
		setDefaultModel(contractModel);
		this.contractModel = contractModel;
		addModuleMenuItems();
	}

	public IPageLink createPageLink(Class< ? extends AbstractContractPage> pageClass)
	{
		return new ContractPageLink(pageClass);
	}

	private final class ContractPageLink implements IPageLink
	{
		private static final long serialVersionUID = 1L;

		private final Class< ? extends AbstractContractPage> pageClass;

		private ContractPageLink(Class< ? extends AbstractContractPage> pageClass)
		{
			this.pageClass = pageClass;
		}

		@Override
		public Page getPage()
		{
			Contract contract = contractModel.getObject();
			return ReflectionUtil.invokeConstructor(pageClass, contract);
		}

		@Override
		public Class< ? extends AbstractContractPage> getPageIdentity()
		{
			return pageClass;
		}

	}

	/**
	 * @see org.apache.wicket.Component#detachModels()
	 */
	@Override
	public void detachModels()
	{
		super.detachModels();
		ComponentUtil.detachQuietly(contractModel);
	}

	public Contract getContract()
	{
		return contractModel.getObject();
	}

}
