package nl.topicus.eduarte.web.pages.beheer.organisatie;

import java.util.List;

import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatieOpmerking;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.BPVOpmerkingTable;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

public class BPVOpmerkingOverzichtPanel extends TypedPanel<List<ExterneOrganisatieOpmerking>>
{
	private static final long serialVersionUID = 1L;

	WebMarkupContainer data;

	public BPVOpmerkingOverzichtPanel(String id, IModel<List<ExterneOrganisatieOpmerking>> model)
	{
		super(id, model);
		CustomDataPanel<ExterneOrganisatieOpmerking> datapanel =
			new EduArteDataPanel<ExterneOrganisatieOpmerking>("datapanel",
				new ListModelDataProvider<ExterneOrganisatieOpmerking>(model),
				new BPVOpmerkingTable());
		add(datapanel);
	}

}
