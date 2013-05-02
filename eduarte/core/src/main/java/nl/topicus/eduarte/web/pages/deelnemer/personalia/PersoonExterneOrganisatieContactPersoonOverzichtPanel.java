package nl.topicus.eduarte.web.pages.deelnemer.personalia;

import java.util.List;

import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.personen.PersoonExterneOrganisatieContactPersoon;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.PersoonExterneOrganisatieContactPersoonTable;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class PersoonExterneOrganisatieContactPersoonOverzichtPanel extends
		TypedPanel<List<PersoonExterneOrganisatieContactPersoon>>
{
	private static final long serialVersionUID = 1L;

	public PersoonExterneOrganisatieContactPersoonOverzichtPanel(String id,
			IModel<List<PersoonExterneOrganisatieContactPersoon>> model)
	{
		super(id, model);

		CustomDataPanel<PersoonExterneOrganisatieContactPersoon> datapanel =
			new EduArteDataPanel<PersoonExterneOrganisatieContactPersoon>("datapanel",
				new ListModelDataProvider<PersoonExterneOrganisatieContactPersoon>(model),
				new PersoonExterneOrganisatieContactPersoonTable());

		datapanel.setTitleModel(Model.of("Gekoppelde contactpersonen"));

		add(datapanel);
	}
}