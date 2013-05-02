package nl.topicus.eduarte.web.pages.beheer.organisatie;

import java.util.List;

import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.personen.OrganisatieEenheidContactPersoon;
import nl.topicus.eduarte.web.components.modalwindow.AbstractEduArteToevoegenBewerkenPanel;

import org.apache.wicket.model.IModel;

public abstract class OrganisatieEenheidContactPersoonEditPanel extends
		AbstractEduArteToevoegenBewerkenPanel<OrganisatieEenheidContactPersoon>
{
	private static final long serialVersionUID = 1L;

	public OrganisatieEenheidContactPersoonEditPanel(String id,
			IModel<List<OrganisatieEenheidContactPersoon>> model, ModelManager manager,
			CustomDataPanelContentDescription<OrganisatieEenheidContactPersoon> table)
	{
		super(id, model, manager, table);
	}

	@Override
	public AbstractToevoegenBewerkenModalWindowPanel<OrganisatieEenheidContactPersoon> createModalWindowPanel(
			String id,
			AbstractToevoegenBewerkenModalWindow<OrganisatieEenheidContactPersoon> modalWindow)
	{
		modalWindow.setTitle("Contactpersoon toevoegen");
		modalWindow.setInitialHeight(260);
		modalWindow.setInitialWidth(500);

		return new OrganisatieEenheidContactPersoonModalWindowPanel(id, modalWindow, this);
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
}
