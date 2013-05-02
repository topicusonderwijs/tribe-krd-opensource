package nl.topicus.eduarte.krd.web.pages.beheer.contract;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.cobra.web.components.link.ConfirmationLink;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.web.pages.beheer.contract.ContractOverzichtPage;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectieTarget;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

public class ContractKoppelenSelectieTarget extends
		AbstractSelectieTarget<Verbintenis, Verbintenis>
{
	private static final long serialVersionUID = 1L;

	private IModel<Contract> contractModel;

	public ContractKoppelenSelectieTarget(IModel<Contract> contractModel)
	{
		super(ContractOverzichtPage.class, "Koppelen");
		this.contractModel = contractModel;
	}

	@Override
	public Link<Void> createLink(String linkId,
			final ISelectionComponent<Verbintenis, Verbintenis> base)
	{
		return new ConfirmationLink<Void>(linkId,
			"Weet u zeker dat u het contract aan de geselecteerde verbintenissen wilt toekennen?")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				ContractCollectiefAanmakenPage page =
					new ContractCollectiefAanmakenPage(contractModel, ModelFactory
						.getListModel(base.getSelectedElements()));
				setResponsePage(page);
			}
		};
	}

	@Override
	public void detach()
	{
		super.detach();
		contractModel.detach();
	}
}