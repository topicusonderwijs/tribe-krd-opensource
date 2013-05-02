package nl.topicus.eduarte.krdparticipatie.web.pages.verzuimloket;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.entities.ibgverzuimloket.IbgVerzuimmelding;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.entities.personen.PersoonContactgegeven;
import nl.topicus.eduarte.krdparticipatie.principals.deelnemer.DeelnemerVerzuimloketWrite;
import nl.topicus.eduarte.krdparticipatie.web.components.menu.enums.ParticipatieDeelnemerMenuItem;
import nl.topicus.eduarte.web.components.modalwindow.adres.AdressenPanel;
import nl.topicus.eduarte.web.components.panels.ContactgegevenEntiteitPanel;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;

@PageInfo(title = "Melding toevoegen", menu = "Verzuimloket")
@InPrincipal(DeelnemerVerzuimloketWrite.class)
public class EditIbgVerzuimmeldingPersonaliaPage extends AbstractVerzuimmeldingPage implements
		IEditPage
{

	private static final long serialVersionUID = 1L;

	private SecurePage returnPage;

	private Form<IbgVerzuimmelding> form;

	public EditIbgVerzuimmeldingPersonaliaPage(Verbintenis inschrijving, SecurePage returnPage)
	{
		this(inschrijving, getDefaultVerzuimmelding(inschrijving), returnPage);
	}

	public EditIbgVerzuimmeldingPersonaliaPage(Verbintenis inschrijving,
			IModel<IbgVerzuimmelding> melding, SecurePage returnPage)
	{
		super(ParticipatieDeelnemerMenuItem.Verzuimloket, inschrijving.getDeelnemer(),
			inschrijving, melding);
		this.returnPage = returnPage;
		createPage(inschrijving);
	}

	public EditIbgVerzuimmeldingPersonaliaPage(Verbintenis inschrijving, IbgVerzuimmelding melding,
			SecurePage returnPage)
	{
		super(ParticipatieDeelnemerMenuItem.Verzuimloket, inschrijving.getDeelnemer(),
			inschrijving, melding);

		this.returnPage = returnPage;
		createPage(inschrijving);
	}

	public void createPage(Verbintenis inschrijving)
	{
		form = new Form<IbgVerzuimmelding>("form", verzuimmeldingModel);

		AutoFieldSet<Deelnemer> fieldsetpersonalia =
			new AutoFieldSet<Deelnemer>("personalia", deelnemerModel, "Personalia");
		fieldsetpersonalia.setPropertyNames("persoon.officieleAchternaam", "persoon.voorvoegsel",
			"persoon.voorletters", "persoon.bsn", "onderwijsnummer");
		form.add(fieldsetpersonalia);

		form.add(new AdressenPanel<PersoonAdres>("adresPanel",
			new LoadableDetachableModel<Persoon>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected Persoon load()
				{
					return getContextDeelnemer().getPersoon();
				}
			}));

		form.add(new ContactgegevenEntiteitPanel<PersoonContactgegeven>("contactGegevensPanel",
			new LoadableDetachableModel<List<PersoonContactgegeven>>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected List<PersoonContactgegeven> load()
				{
					return getContextDeelnemer().getPersoon().getContactgegevens();
				}
			}, false));
		AutoFieldSet<Verbintenis> fieldsetOpleidingsgegevens =
			new AutoFieldSet<Verbintenis>("opleidingsgegevens",
				ModelFactory.getModel(inschrijving), "Opleidingsgegevens");
		fieldsetOpleidingsgegevens.setPropertyNames("opleiding.naam", "intensiteit");

		form.add(fieldsetOpleidingsgegevens);

		AutoFieldSet<IbgVerzuimmelding> fieldsetMeldersgegevens =
			new AutoFieldSet<IbgVerzuimmelding>("meldersgegevens", verzuimmeldingModel,
				"Gegevens melder");
		fieldsetMeldersgegevens.setPropertyNames("functieMelder", "abonneenummerMelder",
			"netnummermelder", "emailadresMelder", "aanduidingContactpersoon", "ccEmailontvanger");
		fieldsetMeldersgegevens.setRenderMode(RenderMode.EDIT);
		fieldsetMeldersgegevens
			.addModifier("emailadresMelder", EmailAddressValidator.getInstance());
		form.add(fieldsetMeldersgegevens);

		add(form);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		addVolgendeButton(panel);
		panel.addButton(new AnnulerenButton(panel, returnPage));
	}

	public void addVolgendeButton(BottomRowPanel panel)
	{
		OpslaanButton opslaanButton = new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				setResponsePage(new EditIbgVerzuimmeldingMeldingPage(getContextVerbintenis(),
					verzuimmeldingModel, returnPage, EditIbgVerzuimmeldingPersonaliaPage.this));
			}
		};
		opslaanButton.setLabel("Volgende stap");
		panel.addButton(opslaanButton);
	}
}
