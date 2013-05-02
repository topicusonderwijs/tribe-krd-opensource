/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.web.pages.medewerker;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.security.RequiredSecurityCheck;
import nl.topicus.cobra.web.validators.BegindatumVoorEinddatumValidator;
import nl.topicus.cobra.web.validators.DatumGroterOfGelijkDatumValidator;
import nl.topicus.cobra.web.validators.DatumKleinerOfGelijkDatumValidator;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.app.security.checks.NietOverledenSecurityCheck;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.OrganisatieMedewerker;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.krd.principals.medewerker.MedewerkerAanstellingWrite;
import nl.topicus.eduarte.krd.web.components.panels.VrijVeldEntiteitEditPanel;
import nl.topicus.eduarte.web.components.menu.MedewerkerMenuItem;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OrganisatieLocatieTable;
import nl.topicus.eduarte.web.components.panels.organisatielocatie.OrganisatieEenheidLocatieEntiteitEditPanel;
import nl.topicus.eduarte.web.components.panels.organisatielocatie.OrganisatieEenheidLocatieEntiteitModalWindowPanel;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.medewerker.AbstractMedewerkerPage;
import nl.topicus.eduarte.web.pages.medewerker.MedewerkerAanstellingPage;

import org.apache.wicket.markup.html.form.Form;

/**
 * Edit pagina voor de medewerker specifieke informatie, Interface is getyped op Functie
 * omdat deze pagina niet op een specifiek object.
 * 
 * @author hoeve
 */
@PageInfo(title = "Aanstelling", menu = {"Medewerker > [Medewerker] > Aanstelling"})
@InPrincipal(MedewerkerAanstellingWrite.class)
@RequiredSecurityCheck(NietOverledenSecurityCheck.class)
public class MedewerkerAanstellingEditPage extends AbstractMedewerkerPage implements
		IModuleEditPage<Medewerker>
{
	private static final long serialVersionUID = 1L;

	private MedewerkerModel medewerkerModel;

	private SecurePage returnPage;

	private Form<Void> form;

	private AutoFieldSet<Medewerker> fieldset;

	public MedewerkerAanstellingEditPage(Medewerker medewerker, SecurePage returnPage)
	{
		super(MedewerkerMenuItem.Aanstelling, null);
		this.medewerkerModel = new MedewerkerModel(medewerker);
		setDefaultModel(medewerkerModel);
		this.returnPage = returnPage;

		form = new Form<Void>("form");
		form.add(createFieldsetAanstelling());
		form.add(new OrganisatieEenheidLocatieEntiteitEditPanel<OrganisatieMedewerker>(
			"panelOrganisatieMedewerkers", medewerkerModel.getOrganisatieMedewerkerListModel(),
			medewerkerModel.getEntiteitManager(),
			new OrganisatieLocatieTable<OrganisatieMedewerker>(true), false)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public OrganisatieMedewerker createNewT()
			{
				return new OrganisatieMedewerker(medewerkerModel.getObject());
			}

			@Override
			protected void processPanelBeforeSubmit(
					OrganisatieEenheidLocatieEntiteitModalWindowPanel<OrganisatieMedewerker> panel)
			{
				panel.getForm().add(
					new DatumGroterOfGelijkDatumValidator("Geldig vanaf", panel
						.getBegindatumField(), getMedewerker().getBegindatum()));

				panel.getForm().add(
					new DatumKleinerOfGelijkDatumValidator("Geldig vanaf", panel
						.getBegindatumField(), getMedewerker().getEinddatum()));

				panel.getForm().add(
					new DatumKleinerOfGelijkDatumValidator("Geldig tot", panel.getEinddatumField(),
						getMedewerker().getEinddatum()));
			}

		});

		VrijVeldEntiteitEditPanel<Medewerker> vrijveldPanel =
			new VrijVeldEntiteitEditPanel<Medewerker>("vrijVelden", medewerkerModel);
		vrijveldPanel.getVrijVeldZoekFilter().setDossierScherm(true);
		vrijveldPanel.getVrijVeldZoekFilter().setCategorie(VrijVeldCategorie.MEDEWERKERAANSTELLING);
		form.add(vrijveldPanel);

		add(form);

		createComponents();
	}

	private AutoFieldSet<Medewerker> createFieldsetAanstelling()
	{
		fieldset =
			new AutoFieldSet<Medewerker>("inputFieldsAanstelling", medewerkerModel
				.getEntiteitModel(), "Aanstelling");
		fieldset.setPropertyNames("functie", "begindatum", "einddatum", "redenUitDienst",
			"uitgeslotenVanCorrespondentie", "redenUitgeslotenVanCorrespondentie");

		fieldset.setRenderMode(RenderMode.EDIT);
		fieldset.setSortAccordingToPropertyNames(true);

		return fieldset;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{

				MedewerkerAanstellingEditPage.this.medewerkerModel.save();

				EduArteRequestCycle.get().setResponsePage(
					new MedewerkerAanstellingPage(
						MedewerkerAanstellingEditPage.this.medewerkerModel.getObject()));
			}
		});
		panel.addButton(new AnnulerenButton(panel, returnPage));
	}

	@Override
	public void detachModels()
	{
		super.detachModels();

		if (medewerkerModel != null)
			medewerkerModel.detach();
		if (returnPage != null)
			returnPage.detach();
	}

	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();
		DatumField begindatumField = (DatumField) fieldset.findFieldComponent("begindatum");
		DatumField einddatumField = (DatumField) fieldset.findFieldComponent("einddatum");

		form.add(new DatumGroterOfGelijkDatumValidator(begindatumField, getMedewerker()
			.getPersoon().getGeboortedatum()));

		form.add(new BegindatumVoorEinddatumValidator(begindatumField, einddatumField));
	}

	public Medewerker getMedewerker()
	{
		return medewerkerModel.getObject();
	}

}
