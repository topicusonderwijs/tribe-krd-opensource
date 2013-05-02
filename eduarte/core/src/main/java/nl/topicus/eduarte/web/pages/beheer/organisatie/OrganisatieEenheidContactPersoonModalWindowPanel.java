package nl.topicus.eduarte.web.pages.beheer.organisatie;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ValidateModifier;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.cobra.web.validators.NullableEmailAddressValidator;
import nl.topicus.eduarte.entities.personen.OrganisatieEenheidContactPersoon;

/**
 * Pagina voor het bewerken van een {@link OrganisatieEenheidContactPersoon} in een modal
 * window.
 * 
 * @author hoeve
 */
public class OrganisatieEenheidContactPersoonModalWindowPanel extends
		AbstractToevoegenBewerkenModalWindowPanel<OrganisatieEenheidContactPersoon>
{
	private static final long serialVersionUID = 1L;

	public OrganisatieEenheidContactPersoonModalWindowPanel(String id,
			AbstractToevoegenBewerkenModalWindow<OrganisatieEenheidContactPersoon> modalWindow,
			OrganisatieEenheidContactPersoonEditPanel contactPersoonEditPanel)
	{
		super(id, modalWindow, contactPersoonEditPanel);

		AutoFieldSet<OrganisatieEenheidContactPersoon> gegevensAutoFieldSet =
			new AutoFieldSet<OrganisatieEenheidContactPersoon>("gegevensAutoFieldSet", modalWindow
				.getModel(), "Nieuwe contactpersoon");
		gegevensAutoFieldSet.setRenderMode(RenderMode.EDIT);
		gegevensAutoFieldSet.setPropertyNames("naam", "geslacht", "rol", "telefoon", "emailadres",
			"mobiel");
		gegevensAutoFieldSet.setSortAccordingToPropertyNames(true);
		gegevensAutoFieldSet.addFieldModifier((new ValidateModifier(NullableEmailAddressValidator
			.getInstance(), "emailadres")));

		getFormContainer().add(gegevensAutoFieldSet);

		createComponents();
	}
}