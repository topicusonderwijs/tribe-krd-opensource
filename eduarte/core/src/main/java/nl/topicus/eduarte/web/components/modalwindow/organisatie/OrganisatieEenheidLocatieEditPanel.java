package nl.topicus.eduarte.web.components.modalwindow.organisatie;

import java.util.List;

import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelEntiteit;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheidLocatie;
import nl.topicus.eduarte.web.components.modalwindow.AbstractEduArteToevoegenBewerkenPanel;
import nl.topicus.eduarte.web.pages.beheer.organisatie.OrganisatieEenheidModel;

import org.apache.wicket.model.IModel;

/**
 * Panel voor het toevoegen & bewerken van een {@link OrganisatieEenheidLocatie}. Niet te
 * verwarren met een {@link IOrganisatieEenheidLocatieKoppelEntiteit}.
 * 
 * @author hoeve
 */
public abstract class OrganisatieEenheidLocatieEditPanel extends
		AbstractEduArteToevoegenBewerkenPanel<OrganisatieEenheidLocatie>
{
	private OrganisatieEenheidModel organisatieDeelnemerModel;

	private static final long serialVersionUID = 1L;

	public OrganisatieEenheidLocatieEditPanel(String id,
			IModel<List<OrganisatieEenheidLocatie>> model, ModelManager manager,
			CustomDataPanelContentDescription<OrganisatieEenheidLocatie> table)
	{
		super(id, model, manager, table);
	}

	@Override
	public AbstractToevoegenBewerkenModalWindowPanel<OrganisatieEenheidLocatie> createModalWindowPanel(
			String id, AbstractToevoegenBewerkenModalWindow<OrganisatieEenheidLocatie> modalWindow)
	{
		modalWindow.setInitialWidth(500);
		modalWindow.setInitialHeight(200);
		return new OrganisatieEenheidLocatieModalWindowPanel(id, modalWindow, this,
			getOrganisatieDeelnemerModel());
	}

	@Override
	public String getToevoegenLabel()
	{
		return "Locatie toevoegen";
	}

	@Override
	public String getModalWindowTitle()
	{
		return "Locatie";
	}

	public OrganisatieEenheidModel getOrganisatieDeelnemerModel()
	{
		return organisatieDeelnemerModel;
	}
}
