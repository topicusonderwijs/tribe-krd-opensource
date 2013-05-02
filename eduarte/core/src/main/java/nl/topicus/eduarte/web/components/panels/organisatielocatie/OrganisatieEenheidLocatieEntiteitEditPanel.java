package nl.topicus.eduarte.web.components.panels.organisatielocatie;

import java.util.List;

import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelEntiteit;
import nl.topicus.eduarte.web.components.modalwindow.AbstractEduArteToevoegenBewerkenPanel;

import org.apache.wicket.model.IModel;

/**
 * Panel voor het toevoegen & bewerken van een
 * {@link IOrganisatieEenheidLocatieKoppelEntiteit}.
 * 
 * @author hoeve
 */
public abstract class OrganisatieEenheidLocatieEntiteitEditPanel<T extends IOrganisatieEenheidLocatieKoppelEntiteit<T>>
		extends AbstractEduArteToevoegenBewerkenPanel<T>
{
	private static final long serialVersionUID = 1L;

	private IModel< ? extends List<T>> model;

	private boolean locatieRequired;

	public OrganisatieEenheidLocatieEntiteitEditPanel(String id, IModel<List<T>> model,
			ModelManager manager, CustomDataPanelContentDescription<T> table,
			boolean locatieRequired)
	{
		super(id, model, manager, table);
		this.locatieRequired = locatieRequired;
		this.model = model;
	}

	@Override
	public AbstractToevoegenBewerkenModalWindowPanel<T> createModalWindowPanel(String id,
			AbstractToevoegenBewerkenModalWindow<T> modalWindow)
	{
		modalWindow.setInitialWidth(500);
		modalWindow.setInitialHeight(200);
		OrganisatieEenheidLocatieEntiteitModalWindowPanel<T> ret =
			new OrganisatieEenheidLocatieEntiteitModalWindowPanel<T>(id, modalWindow, this, model,
				locatieRequired);
		processPanelBeforeSubmit(ret);
		return ret;
	}

	@SuppressWarnings("unused")
	protected void processPanelBeforeSubmit(
			OrganisatieEenheidLocatieEntiteitModalWindowPanel<T> panel)
	{
	}

	@Override
	public String getToevoegenLabel()
	{
		return "Organisatie-eenheid / locatie toevoegen";
	}

	@Override
	public String getModalWindowTitle()
	{
		return "Organisatie-eenheid / Locatie";
	}
}
