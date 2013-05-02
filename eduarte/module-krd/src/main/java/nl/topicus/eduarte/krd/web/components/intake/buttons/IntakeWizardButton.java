package nl.topicus.eduarte.krd.web.components.intake.buttons;

import nl.topicus.cobra.web.components.panels.bottomrow.AbstractPageBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.krd.web.pages.intake.stap0.IntakeStap0Zoeken;

/**
 * Knop die de gebruiker naar de IntakeWizard brengt.
 */
public class IntakeWizardButton extends AbstractPageBottomRowButton
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param bottomRow
	 */
	public IntakeWizardButton(BottomRowPanel bottomRow)
	{
		super(bottomRow, "Invoeren nieuwe " + EduArteApp.get().getDeelnemerTerm().toLowerCase(),
			CobraKeyAction.TOEVOEGEN, ButtonAlignment.RIGHT, IntakeStap0Zoeken.class);
	}
}
