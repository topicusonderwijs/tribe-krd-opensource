package nl.topicus.eduarte.web.components.choice;

import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.providers.OrganisatieEenheidLocatieProvider;

import org.apache.wicket.model.IModel;

/**
 * Versie van het panel om te gebruiken op forms.
 * 
 * @author loite
 */
public class SecureOrganisatieEenheidLocatieFormChoicePanel<T extends OrganisatieEenheidLocatieProvider>
		extends SecureOrganisatieEenheidLocatieChoicePanel<T>
{
	private static final long serialVersionUID = 1L;

	public SecureOrganisatieEenheidLocatieFormChoicePanel(String id, IModel<T> model,
			OrganisatieEenheidLocatieRequired requiredMode)
	{
		this(id, model, requiredMode, null);
	}

	public SecureOrganisatieEenheidLocatieFormChoicePanel(String id, IModel<T> model,
			OrganisatieEenheidLocatieRequired requiredMode, IModel<Opleiding> opleidingModel)
	{
		super(id, model, requiredMode, opleidingModel);
	}
}
