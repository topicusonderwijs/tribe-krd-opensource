package nl.topicus.eduarte.krd.web.components.modalwindow.contactpersoon;

import java.util.List;

import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoon;
import nl.topicus.eduarte.web.components.modalwindow.AbstractEduArteToevoegenBewerkenPanel;

import org.apache.wicket.model.IModel;

public abstract class ExterneOrganisatieContactPersoonEditPanel extends
		AbstractEduArteToevoegenBewerkenPanel<ExterneOrganisatieContactPersoon>
{
	private static final long serialVersionUID = 1L;

	public ExterneOrganisatieContactPersoonEditPanel(String id,
			IModel<List<ExterneOrganisatieContactPersoon>> model, ModelManager manager,
			CustomDataPanelContentDescription<ExterneOrganisatieContactPersoon> table)
	{
		super(id, model, manager, table);
	}

	@Override
	public AbstractToevoegenBewerkenModalWindowPanel<ExterneOrganisatieContactPersoon> createModalWindowPanel(
			String id,
			AbstractToevoegenBewerkenModalWindow<ExterneOrganisatieContactPersoon> modalWindow)
	{
		modalWindow.setTitle("Contactpersoon toevoegen");
		modalWindow.setInitialHeight(260);
		modalWindow.setInitialWidth(500);

		return new ExterneOrganisatieContactPersoonModalWindowPanel(id, modalWindow, this);
	}

	@Override
	public String getToevoegenLabel()
	{
		return "Contactpersoon toevoegen";
	}

	@Override
	public String getModalWindowTitle()
	{
		return "Contactpersoon";
	}

	@Override
	public boolean isDeletable(ExterneOrganisatieContactPersoon persoon)
	{
		return !persoon.isInGebruik();
	}
}
