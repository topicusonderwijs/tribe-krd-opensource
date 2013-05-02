package nl.topicus.eduarte.web.components.modalwindow.contract;

import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.contract.ContractVerplichting;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.model.IModel;

public class MyContractVerplichtingEditPanel extends CobraModalWindow<ContractVerplichting>
// AbstractToevoegenBewerkenPanel<ContractVerplichting>
{

	private static final long serialVersionUID = 1L;

	public MyContractVerplichtingEditPanel(String id, IModel<ContractVerplichting> model,
			final EduArteDataPanel< ? > parent)
	{
		super(id, model);
		setWindowClosedCallback(new WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				target.addComponent(parent);
			}
		});
	}

	@Override
	protected CobraModalWindowBasePanel<ContractVerplichting> createContents(String id)
	{
		return new MyContractVerplichtingModalWindow(id, getModel(), this);
	}

	// private static final long serialVersionUID = 1L;
	//
	// public MyContractVerplichtingEditPanel(String id, IModel model, ModelManager
	// manager,
	// CustomDataPanelContentDescription table)
	// {
	// super(id, model, manager, table);
	// }
	//
	// @Override
	// public AbstractToevoegenBewerkenModalWindowPanel<ContractVerplichting>
	// createModalWindowPanel(
	// String id, AbtractToevoegenBewerkenModalWindow modalWindow)
	// {
	// modalWindow.setInitialHeight(250);
	// modalWindow.setInitialWidth(450);
	// return new MyContractVerplichtingModalWindow(id, modalWindow, this);
	// }
	//
	// @Override
	// public boolean isEditable()
	// {
	// return false;
	// }
	//
	// @Override
	// public ContractVerplichting createNewT()
	// {
	// return null;
	// }
	//
	// @Override
	// public String getModalWindowTitle()
	// {
	// return "Contract verplichting";
	// }
	//
	// @Override
	// public String getToevoegenLabel()
	// {
	// return null;
	// }

}
