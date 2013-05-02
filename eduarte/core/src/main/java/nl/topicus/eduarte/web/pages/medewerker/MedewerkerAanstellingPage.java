/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.medewerker;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.core.principals.medewerker.MedewerkerAanstelling;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.OrganisatieMedewerker;
import nl.topicus.eduarte.entities.vrijevelden.MedewerkerVrijVeld;
import nl.topicus.eduarte.providers.MedewerkerProvider;
import nl.topicus.eduarte.web.components.menu.MedewerkerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.VrijVeldEntiteitPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OrganisatieLocatieTable;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.PropertyModel;

/**
 * @author hoeve
 */
@PageInfo(title = "Aanstelling", menu = {"Medewerker > [Medewerker] > Aanstelling"})
@InPrincipal(MedewerkerAanstelling.class)
public class MedewerkerAanstellingPage extends AbstractMedewerkerPage
{
	private static final long serialVersionUID = 1L;

	public MedewerkerAanstellingPage(MedewerkerProvider provider)
	{
		this(provider.getMedewerker());
	}

	public MedewerkerAanstellingPage(Medewerker medewerker)
	{
		super(MedewerkerMenuItem.Aanstelling, medewerker);

		add(createFieldsetAanstelling());
		add(createFieldsetOrganisatieEenheid());
		add(createVrijVeldPanel());

		createComponents();
	}

	private AutoFieldSet<Medewerker> createFieldsetAanstelling()
	{
		AutoFieldSet<Medewerker> fieldset =
			new AutoFieldSet<Medewerker>("inputFieldsAanstelling", getContextMedewerkerModel(),
				"Aanstelling");
		fieldset.setPropertyNames("functie", "begindatum", "einddatum", "redenUitDienst",
			"uitgeslotenVanCorrespondentie", "redenUitgeslotenVanCorrespondentie");
		fieldset.setRenderMode(RenderMode.DISPLAY);
		fieldset.setSortAccordingToPropertyNames(true);

		return fieldset;
	}

	private WebMarkupContainer createFieldsetOrganisatieEenheid()
	{
		final WebMarkupContainer datapanelContainer =
			new WebMarkupContainer("panelOrganisatieMedewerker");
		CustomDataPanel<OrganisatieMedewerker> datapanel =
			new EduArteDataPanel<OrganisatieMedewerker>("datapanelOrganisatieMedewerker",
				new ListModelDataProvider<OrganisatieMedewerker>(
					new PropertyModel<List<OrganisatieMedewerker>>(getDefaultModel(),
						"organisatieMedewerkers")),
				new OrganisatieLocatieTable<OrganisatieMedewerker>(true));

		datapanelContainer.setOutputMarkupId(true);
		datapanelContainer.add(datapanel);

		return datapanelContainer;
	}

	private VrijVeldEntiteitPanel<MedewerkerVrijVeld, Medewerker> createVrijVeldPanel()
	{
		VrijVeldEntiteitPanel<MedewerkerVrijVeld, Medewerker> vrijVeldenPanel =
			new VrijVeldEntiteitPanel<MedewerkerVrijVeld, Medewerker>("vrijveldenPanel",
				getContextMedewerkerModel());
		vrijVeldenPanel.setDossierScherm(true);
		return vrijVeldenPanel;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new ModuleEditPageButton<Medewerker>(panel, "Bewerken",
			CobraKeyAction.BEWERKEN, Medewerker.class, MedewerkerMenuItem.Aanstelling,
			MedewerkerAanstellingPage.this, getContextMedewerkerModel())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible()
					&& (!ingelogdeMedewerkerGelijkAanGetoondeMedewerker() || ingelogdeMedewerkerIsApplicatiebeheer());
			}

		});
	}

	private boolean ingelogdeMedewerkerGelijkAanGetoondeMedewerker()
	{
		return (EduArteContext.get().getMedewerker() != null && EduArteContext.get()
			.getMedewerker().equals(getContextMedewerker()));
	}

	private boolean ingelogdeMedewerkerIsApplicatiebeheer()
	{
		return (EduArteContext.get().getAccount() != null && EduArteContext.get().getAccount()
			.heeftRol("Standaard applicatiebeheerder"));
	}
}