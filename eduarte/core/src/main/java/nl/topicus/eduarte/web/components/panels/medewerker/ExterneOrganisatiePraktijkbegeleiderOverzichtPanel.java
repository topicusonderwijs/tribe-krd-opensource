package nl.topicus.eduarte.web.components.panels.medewerker;

import java.util.List;

import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatiePraktijkbegeleider;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.ExterneOrganisatiePraktijkbegeleiderTable;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

public class ExterneOrganisatiePraktijkbegeleiderOverzichtPanel extends
		TypedPanel<List<ExterneOrganisatiePraktijkbegeleider>>
{
	private static final long serialVersionUID = 1L;

	WebMarkupContainer data;

	public ExterneOrganisatiePraktijkbegeleiderOverzichtPanel(String id,
			IModel<List<ExterneOrganisatiePraktijkbegeleider>> model)
	{
		super(id, model);

		EduArteDataPanel<ExterneOrganisatiePraktijkbegeleider> datapanel =
			new EduArteDataPanel<ExterneOrganisatiePraktijkbegeleider>("datapanel",
				new ListModelDataProvider<ExterneOrganisatiePraktijkbegeleider>(model),
				new ExterneOrganisatiePraktijkbegeleiderTable());

		add(datapanel);
	}
}
