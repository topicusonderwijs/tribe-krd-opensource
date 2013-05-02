/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.deelnemer.deelnemerkaart;

import java.util.Date;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.labels.JaNeeLabel;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.core.principals.deelnemer.DeelnemerInzien;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.web.components.image.DeelnemerImage;
import nl.topicus.eduarte.web.components.label.PostcodeWoonplaatsLabel;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.panels.bottomrow.JasperReportBottomRowButton;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

/**
 * @author loite
 */
@PageInfo(title = "Deelnemerkaart", menu = {"Deelnemer > [deelnemer]",
	"Groep > [groep] > [deelnemer]"})
@InPrincipal(DeelnemerInzien.class)
public class DeelnemerkaartPage extends AbstractDeelnemerPage
{
	private static final long serialVersionUID = 1L;

	private final WebMarkupContainer mboContainer;

	public DeelnemerkaartPage(PageParameters parameters)
	{
		this(AbstractDeelnemerPage.getDeelnemerFromPageParameters(DeelnemerkaartPage.class,
			parameters));
	}

	public DeelnemerkaartPage(DeelnemerProvider provider)
	{
		this(provider.getDeelnemer(), provider.getDeelnemer()
			.getEersteInschrijvingOpPeildatum(true));
	}

	public DeelnemerkaartPage(Deelnemer deelnemer)
	{
		this(deelnemer, deelnemer.getEersteInschrijvingOpPeildatum(true));
	}

	public DeelnemerkaartPage(Verbintenis inschrijving)
	{
		this(inschrijving.getDeelnemer());
	}

	public DeelnemerkaartPage(Deelnemer deelnemer, Verbintenis inschrijving)
	{
		super(DeelnemerMenuItem.Deelnemerkaart, deelnemer, inschrijving);
		setDefaultModel(ModelFactory.getCompoundModelForModel(getContextDeelnemerModel()));
		IModel<Verbintenis> inschrijvingModel =
			ModelFactory.getCompoundModelForModel(getContextVerbintenisModel());

		add(new Label("caption", EduArteApp.get().getDeelnemerTerm() + "kaart"));
		add(new DeelnemerImage("foto", getContextDeelnemerModel()));

		add(new Label("deelnemernummer").setRenderBodyOnly(true));
		add(new Label("persoon.voorvoegselAchternaam").setRenderBodyOnly(true));
		add(new Label("persoon.roepnaam").setRenderBodyOnly(true));
		add(new Label("persoon.geboortedatum").setRenderBodyOnly(true));
		add(new Label("persoon.geslacht").setRenderBodyOnly(true));

		WebMarkupContainer straatHuisnummerFormatted =
			new WebMarkupContainer("straatHuisnummerFormatted");
		straatHuisnummerFormatted.setRenderBodyOnly(true);

		straatHuisnummerFormatted.add(new Label(
			"persoon.eerstePersoonAdresOpPeildatum.adres.straatHuisnummerFormatted")
			.setRenderBodyOnly(true));
		straatHuisnummerFormatted.add(new PostcodeWoonplaatsLabel(
			"persoon.eerstePersoonAdresOpPeildatum.adres.postcodePlaatsFormatted")
			.setRenderBodyOnly(true));

		add(straatHuisnummerFormatted);

		WebMarkupContainer telefoonnummerFormatted =
			new WebMarkupContainer("telefoonnummerFormatted");
		telefoonnummerFormatted.add(new Label("persoon.eersteTelefoon.formattedContactgegeven")
			.setRenderBodyOnly(true));
		telefoonnummerFormatted.setRenderBodyOnly(true);
		add(telefoonnummerFormatted);

		WebMarkupContainer inschrijvingContainer =
			new WebMarkupContainer("inschrijvingContainer", inschrijvingModel);
		inschrijvingContainer.setRenderBodyOnly(true);
		inschrijvingContainer.add(new Label("opleiding.naam").setRenderBodyOnly(true));
		inschrijvingContainer.add(new Label("externeCode").setRenderBodyOnly(true));
		inschrijvingContainer.add(new Label("externeCodeLabel"));
		inschrijvingContainer.add(new Label("begindatum", getBegindatumModel())
			.setRenderBodyOnly(true));
		inschrijvingContainer.add(new Label("plaatsingOpPeildatum.groep").setRenderBodyOnly(true));
		inschrijvingContainer.add(new JaNeeLabel("Lwoo").setRenderBodyOnly(true));
		inschrijvingContainer.add(new Label(
			"relevanteVooropleidingVooropleiding.externeOrganisatie.naam").setRenderBodyOnly(true));

		WebMarkupContainer einddatumContainer = createEinddatumContainer();
		einddatumContainer.setRenderBodyOnly(true);

		WebMarkupContainer registratieDatumContainer =
			createRegistratieDatumContainer(getContextDeelnemerModel());
		registratieDatumContainer.setRenderBodyOnly(true);
		registratieDatumContainer.add(new Label("registratieDatum", new PropertyModel<Date>(
			getContextDeelnemerModel(), "registratieDatum")).setRenderBodyOnly(true));
		inschrijvingContainer.add(registratieDatumContainer);

		einddatumContainer.add(new Label("einddatum").setRenderBodyOnly(true));
		einddatumContainer.add(new Label("redenUitschrijving"));

		inschrijvingContainer.add(einddatumContainer);
		inschrijvingContainer.add(new Label("geplandeEinddatum").setRenderBodyOnly(true));

		add(inschrijvingContainer);

		mboContainer = new WebMarkupContainer("mboContainer");
		mboContainer.setRenderBodyOnly(true);
		mboContainer.add(new Label("datumEindeLeerplicht").setRenderBodyOnly(true));
		WebMarkupContainer kwalificatieplichtContainer =
			new WebMarkupContainer("kwalificatieplicht");
		kwalificatieplichtContainer.add(new Label("datumEindeKwalificatieplicht")
			.setRenderBodyOnly(true));
		kwalificatieplichtContainer.setVisible(deelnemer.isKwalificatieplichtVanToepassing());
		mboContainer.add(kwalificatieplichtContainer);
		add(mboContainer);

		add(new DeelnemerkaartDetailPanel("detailPanel", getContextDeelnemerModel(),
			getContextVerbintenisModel()));

		createComponents();
	}

	private IModel<Date> getBegindatumModel()
	{
		return new LoadableDetachableModel<Date>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Date load()
			{
				if (getContextVerbintenis() == null
					|| VerbintenisStatus.Intake.equals(getContextVerbintenis().getStatus()))
					return null;
				else
					return getContextVerbintenis().getBegindatum();
			}
		};
	}

	private WebMarkupContainer createEinddatumContainer()
	{
		return new WebMarkupContainer("einddatumContainer")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && getContextVerbintenis() != null
					&& getContextVerbintenis().getEinddatum() != null;
			}

		};
	}

	private WebMarkupContainer createRegistratieDatumContainer(IModel<Deelnemer> model)
	{
		return new WebMarkupContainer("registratieDatumContainer", model)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && getContextDeelnemer().getRegistratieDatum() != null;
			}

		};
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);

		panel.addButton(new JasperReportBottomRowButton<Verbintenis>(panel, "deelnemerkaart.jrxml",
			getClass(), "verbintenis")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<Verbintenis> getContextModel()
			{
				return getContextVerbintenisModel();
			}
		});
	}
}
