package nl.topicus.eduarte.web.components.panels;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoon;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

public class ExterneOrganisatieContactPersoonPanel extends
		TypedPanel<ExterneOrganisatieContactPersoon>
{
	private static final long serialVersionUID = 1L;

	public ExterneOrganisatieContactPersoonPanel(String id,
			IModel<ExterneOrganisatieContactPersoon> contactgegevenModel)
	{
		super(id, ModelFactory.getCompoundModelForModel(contactgegevenModel));
		setRenderBodyOnly(true);
		add(new Label("naam"));
		add(new Label("emailadres"));
		add(new Label("telefoon"));
	}
}