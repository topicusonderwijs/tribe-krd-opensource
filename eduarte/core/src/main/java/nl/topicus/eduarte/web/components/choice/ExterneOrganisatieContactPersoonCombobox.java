package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoon;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitPropertyRenderer;

import org.apache.wicket.model.IModel;

public class ExterneOrganisatieContactPersoonCombobox extends
		AbstractAjaxDropDownChoice<ExterneOrganisatieContactPersoon>
{
	private static final long serialVersionUID = 1L;

	public ExterneOrganisatieContactPersoonCombobox(String id,
			IModel<ExterneOrganisatieContactPersoon> model,
			IModel<List<ExterneOrganisatieContactPersoon>> choices)
	{
		super(id, model, choices, new EntiteitPropertyRenderer("naamInactief"));
	}
}
