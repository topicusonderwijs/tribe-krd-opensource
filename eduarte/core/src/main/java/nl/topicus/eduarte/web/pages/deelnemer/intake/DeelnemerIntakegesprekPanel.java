package nl.topicus.eduarte.web.pages.deelnemer.intake;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.inschrijving.Intakegesprek;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.model.IModel;

public class DeelnemerIntakegesprekPanel extends TypedPanel<Intakegesprek>
{
	private static final long serialVersionUID = 1L;

	private AutoFieldSet<Intakegesprek> detailsLinks;

	private AutoFieldSet<Intakegesprek> detailsRechts;

	public DeelnemerIntakegesprekPanel(String id, IModel<Intakegesprek> model, SecurePage returnPage)
	{
		super(id, model);

		detailsLinks = new AutoFieldSet<Intakegesprek>("detailsLinks", model);
		detailsLinks.setPropertyNames("datumTijd", "intaker", "locatie", "kamer", "status",
			"uitkomst");
		detailsLinks.setSortAccordingToPropertyNames(true);
		detailsLinks.setRenderMode(RenderMode.DISPLAY);
		add(detailsLinks);

		detailsRechts = new AutoFieldSet<Intakegesprek>("detailsRechts", model);
		detailsRechts.setPropertyNames("organisatieEenheid", "gewensteOpleiding",
			"gewensteLocatie", "gewensteBegindatum", "gewensteEinddatum", "gewensteBPV",
			"opmerking");
		detailsRechts.setSortAccordingToPropertyNames(true);
		detailsRechts.setRenderMode(RenderMode.DISPLAY);
		detailsRechts.addFieldModifier(new ConstructorArgModifier("gewensteBPV", returnPage));
		add(detailsRechts);
	}
}