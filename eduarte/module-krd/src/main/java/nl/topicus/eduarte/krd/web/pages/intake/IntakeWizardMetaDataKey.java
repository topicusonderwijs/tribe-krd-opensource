package nl.topicus.eduarte.krd.web.pages.intake;

import org.apache.wicket.MetaDataKey;

/**
 * Key voor het opzoeken van IntakeWizardModel gegevens in de sessie. Deze worden
 * opgeslagen in de metadata van de sessie om zo versie conflicten te voorkomen tussen
 * pagina-overgangen (een gebruiker kan van voor naar achter gaan in de wizard).
 */
public class IntakeWizardMetaDataKey extends MetaDataKey<IntakeWizardMetaDataValues>
{
	private static final long serialVersionUID = 1L;

	public IntakeWizardMetaDataKey()
	{
	}
}
