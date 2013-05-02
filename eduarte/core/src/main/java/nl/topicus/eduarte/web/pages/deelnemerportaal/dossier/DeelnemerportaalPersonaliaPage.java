package nl.topicus.eduarte.web.pages.deelnemerportaal.dossier;

import java.util.Date;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.eduarte.core.principals.deelnemerportaal.DeelnemerportaalPersonalia;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.web.components.menu.deelnemerPortaal.DeelnemerportaalDossierMenuItem;

import org.apache.wicket.model.PropertyModel;

/**
 * @author ambrosius
 */
@PageInfo(title = "Personalia", menu = "")
// TODO nick - Deelneemer portaalRecht toevoegen
@InPrincipal(DeelnemerportaalPersonalia.class)
public class DeelnemerportaalPersonaliaPage extends AbstractDeelnemerportaalDossierPage
{
	private static final long serialVersionUID = 1L;

	public DeelnemerportaalPersonaliaPage()
	{
		this(getDefaultVerbintenis());
	}

	public DeelnemerportaalPersonaliaPage(Verbintenis verbintenis)
	{
		this(verbintenis.getDeelnemer(), verbintenis);
	}

	public DeelnemerportaalPersonaliaPage(Deelnemer deelnemer, Verbintenis verbintenis)
	{
		super(DeelnemerportaalDossierMenuItem.Personalia, verbintenis);
		setDefaultModel(ModelFactory.getModel(deelnemer));

		AutoFieldSet<Deelnemer> fieldsetpersonalia =
			new AutoFieldSet<Deelnemer>("inputFieldsPersonalia", getContextDeelnemerModel(),
				"Personalia");
		// TODO Paul: Juiste gegevens??
		fieldsetpersonalia.setPropertyNames("deelnemernummer", "persoon.voorletters",
			"persoon.voornamen", "persoon.voorvoegsel", "persoon.achternaam", "persoon.roepnaam",
			"persoon.geboortedatum", "persoon.geslacht", "persoon.bsn", "onderwijsnummer",
			"persoon.geboorteGemeente", "persoon.geboorteland",
			"persoon.nationaliteit1.omschrijving", "persoon.nationaliteit2.omschrijving",
			"allochtoon");

		fieldsetpersonalia.addFieldModifier(new ConstructorArgModifier("persoon.geboortedatum",
			new PropertyModel<Date>(getDefaultModel(), "persoon.toepassingGeboortedatum")));

		add(fieldsetpersonalia);

		createComponents();
	}
}
