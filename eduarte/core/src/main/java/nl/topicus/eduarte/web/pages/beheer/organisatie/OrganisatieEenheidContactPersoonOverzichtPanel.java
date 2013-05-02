package nl.topicus.eduarte.web.pages.beheer.organisatie;

import java.util.List;

import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.personen.OrganisatieEenheidContactPersoon;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OrganisatieEenheidContactPersoonTable;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

public class OrganisatieEenheidContactPersoonOverzichtPanel extends
		TypedPanel<List<OrganisatieEenheidContactPersoon>>
{
	private static final long serialVersionUID = 1L;

	WebMarkupContainer data;

	public OrganisatieEenheidContactPersoonOverzichtPanel(String id,
			IModel<List<OrganisatieEenheidContactPersoon>> model)
	{
		super(id, model);
		CustomDataPanel<OrganisatieEenheidContactPersoon> datapanel =
			new EduArteDataPanel<OrganisatieEenheidContactPersoon>("datapanel",
				new ListModelDataProvider<OrganisatieEenheidContactPersoon>(model),
				new OrganisatieEenheidContactPersoonTable());
		add(datapanel);
	}

}
