package nl.topicus.eduarte.krd.web.components.contracten.buttons;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractPageBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.krd.web.pages.beheer.contract.ContractCollectiefKoppelenPage;
import nl.topicus.eduarte.web.pages.beheer.contract.ContractOverzichtPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IModel;

public class ContractCollectiefKoppelenButton extends AbstractPageBottomRowButton
{
	private static final long serialVersionUID = 1L;

	private IModel<Contract> contractModel;

	public ContractCollectiefKoppelenButton(BottomRowPanel parent,
			final IModel<Contract> contractModel, final ContractOverzichtPage returnPage)
	{
		super(parent, "Deelnemers koppelen", CobraKeyAction.GEEN, ButtonAlignment.LEFT,
			new IPageLink()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Page getPage()
				{
					return new ContractCollectiefKoppelenPage(returnPage, contractModel);
				}

				@SuppressWarnings("unchecked")
				@Override
				public Class getPageIdentity()
				{
					return ContractCollectiefKoppelenPage.class;
				}

			});
		this.contractModel = contractModel;
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(contractModel);
	}

}
