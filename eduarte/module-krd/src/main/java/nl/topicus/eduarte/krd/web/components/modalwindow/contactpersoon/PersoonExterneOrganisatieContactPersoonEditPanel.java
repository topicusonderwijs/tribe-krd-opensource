package nl.topicus.eduarte.krd.web.components.modalwindow.contactpersoon;

import java.util.List;

import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoon;
import nl.topicus.eduarte.entities.personen.PersoonExterneOrganisatieContactPersoon;
import nl.topicus.eduarte.web.components.modalwindow.AbstractEduarteSelectieToevoegenBewerkenPanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public abstract class PersoonExterneOrganisatieContactPersoonEditPanel
		extends
		AbstractEduarteSelectieToevoegenBewerkenPanel<PersoonExterneOrganisatieContactPersoon, ExterneOrganisatieContactPersoon>
{
	private static final long serialVersionUID = 1L;

	public PersoonExterneOrganisatieContactPersoonEditPanel(String id,
			IModel<List<PersoonExterneOrganisatieContactPersoon>> model,
			IModel<List<ExterneOrganisatieContactPersoon>> choicesModel, ModelManager manager,
			CustomDataPanelContentDescription<PersoonExterneOrganisatieContactPersoon> table,
			CustomDataPanelContentDescription<ExterneOrganisatieContactPersoon> tableChoices)
	{
		super(id, model, manager, table, tableChoices, choicesModel);
		getCustomDataPanel().setTitleModel(Model.of("Gekoppelde contactpersonen"));
	}

	@Override
	protected void onItemSelect(AjaxRequestTarget target,
			Item<ExterneOrganisatieContactPersoon> item)
	{
		getModalWindow().getModelObject()
			.setExterneOrganisatieContactPersoon(item.getModelObject());
	}

	@Override
	public String getModalWindowTitle()
	{
		return "Contactpersoon selecteren";
	}

	@Override
	public String getToevoegenLabel()
	{
		return "Contactpersoon koppelen";
	}

}
