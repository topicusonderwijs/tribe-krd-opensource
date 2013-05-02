package nl.topicus.eduarte.web.pages.deelnemer.personalia;

import java.util.List;

import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoon;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.ExterneOrganisatieContactPersoonTable;

import org.apache.wicket.model.IModel;

public class ExterneOrganisatieContactPersoonOverzichtPanel extends
		TypedPanel<List<ExterneOrganisatieContactPersoon>>
{
	private static final long serialVersionUID = 1L;

	public ExterneOrganisatieContactPersoonOverzichtPanel(String id,
			IModel<List<ExterneOrganisatieContactPersoon>> model)
	{
		super(id, model);

		CustomDataPanel<ExterneOrganisatieContactPersoon> datapanel =
			new EduArteDataPanel<ExterneOrganisatieContactPersoon>("datapanel",
				new ListModelDataProvider<ExterneOrganisatieContactPersoon>(model),
				new ExterneOrganisatieContactPersoonTable());

		add(datapanel);
	}

}
