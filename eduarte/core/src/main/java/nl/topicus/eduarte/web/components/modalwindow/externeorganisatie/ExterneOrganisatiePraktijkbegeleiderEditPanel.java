package nl.topicus.eduarte.web.components.modalwindow.externeorganisatie;

import java.util.List;

import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatiePraktijkbegeleider;
import nl.topicus.eduarte.web.components.modalwindow.AbstractEduArteToevoegenBewerkenPanel;

import org.apache.wicket.model.IModel;

public abstract class ExterneOrganisatiePraktijkbegeleiderEditPanel extends
		AbstractEduArteToevoegenBewerkenPanel<ExterneOrganisatiePraktijkbegeleider>
{
	private static final long serialVersionUID = 1L;

	public ExterneOrganisatiePraktijkbegeleiderEditPanel(String id,
			IModel<List<ExterneOrganisatiePraktijkbegeleider>> model, ModelManager manager,
			CustomDataPanelContentDescription<ExterneOrganisatiePraktijkbegeleider> table)
	{
		super(id, model, manager, table);
	}

	@Override
	public AbstractToevoegenBewerkenModalWindowPanel<ExterneOrganisatiePraktijkbegeleider> createModalWindowPanel(
			String id,
			AbstractToevoegenBewerkenModalWindow<ExterneOrganisatiePraktijkbegeleider> modalWindow)
	{
		modalWindow.setInitialHeight(300);
		modalWindow.setInitialWidth(650);

		return new ExterneOrganisatiePraktijkbegeleiderModalWindowPanel(id, modalWindow, this);
	}

	@Override
	public String getToevoegenLabel()
	{
		return "Begeleider toevoegen";
	}

	@Override
	public String getModalWindowTitle()
	{
		return "Begeleiders";
	}

	@Override
	public boolean isDeletable(ExterneOrganisatiePraktijkbegeleider gegeven)
	{
		return !gegeven.isInGebruik();
	}
}
