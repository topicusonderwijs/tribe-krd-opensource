package nl.topicus.eduarte.krd.web.components.modalwindow.contactpersoon;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ValidateModifier;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.cobra.web.validators.NullableEmailAddressValidator;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoon;

/**
 * Pagina voor het bewerken van een {@link ExterneOrganisatieContactPersoon} in een modal
 * window.
 * 
 * @author hoeve
 */
public class ExterneOrganisatieContactPersoonModalWindowPanel extends
		AbstractToevoegenBewerkenModalWindowPanel<ExterneOrganisatieContactPersoon>
{
	private static final long serialVersionUID = 1L;

	public ExterneOrganisatieContactPersoonModalWindowPanel(String id,
			AbstractToevoegenBewerkenModalWindow<ExterneOrganisatieContactPersoon> modalWindow,
			ExterneOrganisatieContactPersoonEditPanel externeOrganisatieContactPersoonEditPanel)
	{
		super(id, modalWindow, externeOrganisatieContactPersoonEditPanel);
		modalWindow.setInitialHeight(300);

		AutoFieldSet<ExterneOrganisatieContactPersoon> gegevensAutoFieldSet =
			new AutoFieldSet<ExterneOrganisatieContactPersoon>("gegevensAutoFieldSet", modalWindow
				.getModel(), "Nieuwe contactpersoon");
		gegevensAutoFieldSet.setRenderMode(RenderMode.EDIT);
		gegevensAutoFieldSet.setPropertyNames("naam", "geslacht", "rol", "telefoon", "emailadres",
			"mobiel", "begindatum", "einddatum");
		gegevensAutoFieldSet.setSortAccordingToPropertyNames(true);
		gegevensAutoFieldSet.addFieldModifier((new ValidateModifier(NullableEmailAddressValidator
			.getInstance(), "emailadres")));

		getFormContainer().add(gegevensAutoFieldSet);

		createComponents();
	}
}