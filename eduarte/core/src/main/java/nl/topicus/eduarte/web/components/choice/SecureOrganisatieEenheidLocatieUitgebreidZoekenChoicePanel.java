package nl.topicus.eduarte.web.components.choice;

import nl.topicus.eduarte.providers.OrganisatieEenheidLocatieProvider;

import org.apache.wicket.model.IModel;

/**
 * Versie van het panel om te gebruiken in filterbalken.
 * 
 * @author loite
 */
public class SecureOrganisatieEenheidLocatieUitgebreidZoekenChoicePanel<T extends OrganisatieEenheidLocatieProvider>
		extends SecureOrganisatieEenheidLocatieChoicePanel<T>
{
	private static final long serialVersionUID = 1L;

	public SecureOrganisatieEenheidLocatieUitgebreidZoekenChoicePanel(String id, IModel<T> model)
	{
		this(id, model, OrganisatieEenheidLocatieRequired.Geen);
	}

	public SecureOrganisatieEenheidLocatieUitgebreidZoekenChoicePanel(String id, IModel<T> model,
			OrganisatieEenheidLocatieRequired requiredMode)
	{
		super(id, model, requiredMode);
	}
}
