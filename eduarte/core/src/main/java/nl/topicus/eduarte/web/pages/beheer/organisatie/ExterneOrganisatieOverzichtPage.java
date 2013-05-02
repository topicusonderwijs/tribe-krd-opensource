/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.beheer.organisatie;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.LabelModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.core.principals.beheer.organisatie.ExterneOrganisatiesRead;
import nl.topicus.eduarte.entities.bpv.BPVBedrijfsgegeven;
import nl.topicus.eduarte.entities.bpv.BPVCriteriaExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatieAdres;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatieContactgegeven;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatieOpmerking;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatiePraktijkbegeleider;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoon;
import nl.topicus.eduarte.entities.vrijevelden.ExterneOrganisatieVrijVeld;
import nl.topicus.eduarte.web.components.menu.ExterneOrganisatieMenuItem;
import nl.topicus.eduarte.web.components.menu.RelatieBeheerMenuItem;
import nl.topicus.eduarte.web.components.modalwindow.adres.AdressenPanel;
import nl.topicus.eduarte.web.components.panels.ContactgegevenEntiteitPanel;
import nl.topicus.eduarte.web.components.panels.VrijVeldEntiteitPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.panels.criteria.BPVCriteriaExterneOrganisatieOverzichtPanel;
import nl.topicus.eduarte.web.components.panels.medewerker.ExterneOrganisatiePraktijkbegeleiderOverzichtPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.personalia.ExterneOrganisatieContactPersoonOverzichtPanel;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.PropertyModel;

/**
 * @author vandekamp
 */
@PageInfo(title = "Externe organisatie", menu = {"Relatie > Externe organisatie -> [Externe organisatie"})
@InPrincipal(ExterneOrganisatiesRead.class)
public class ExterneOrganisatieOverzichtPage extends AbstractExterneOrganisatiePage
{
	private static final long serialVersionUID = 1L;

	private SecurePage securePage;

	public ExterneOrganisatieOverzichtPage(ExterneOrganisatie externeOrganisatie)
	{
		this(externeOrganisatie, new ExterneOrganisatieZoekenPage());
	}

	public ExterneOrganisatieOverzichtPage(ExterneOrganisatie externeOrganisatie,
			SecurePage securePage)
	{
		super(ExterneOrganisatieMenuItem.ExterneOrganisatiekaart, externeOrganisatie);
		this.securePage = securePage;

		AutoFieldSet<ExterneOrganisatie> fieldSet =
			new AutoFieldSet<ExterneOrganisatie>("externeOrganisatie",
				getContextExterneOrganisatieModel(), "Externe organisatie");
		fieldSet.setPropertyNames("naam", "verkorteNaam", "soortExterneOrganisatie", "begindatum",
			"einddatum", "bpvBedrijf", "ondertekeningBPVOdoor", "nietGeschiktVoorBPVDeelnemers",
			"toelichtingNietGeschiktVoorBPV", "nietGeschiktVoorBPVMatch", "debiteurennummer",
			"bankrekeningnummer", "verzamelfacturen", "betalingstermijn", "omschrijving");
		fieldSet.addFieldModifier(new LabelModifier("bpvBedrijf", "BPV-bedrijf"));
		fieldSet.setRenderMode(RenderMode.DISPLAY);
		fieldSet.setSortAccordingToPropertyNames(true);
		add(fieldSet);

		add(new AdressenPanel<ExterneOrganisatieAdres>("adresPanel",
			getContextExterneOrganisatieModel()));

		add(new ContactgegevenEntiteitPanel<ExterneOrganisatieContactgegeven>(
			"contactGegevensPanel", new PropertyModel<List<ExterneOrganisatieContactgegeven>>(
				getDefaultModel(), "contactgegevens"), false));

		add(new ExterneOrganisatieContactPersoonOverzichtPanel("contactPersonenPanel",
			new PropertyModel<List<ExterneOrganisatieContactPersoon>>(getDefaultModel(),
				"contactPersonen")));

		add(new BPVBedrijfsgegevenOverzichtPanel("bpvBedrijfsgegevenPanel",
			new PropertyModel<List<BPVBedrijfsgegeven>>(getDefaultModel(), "bpvBedrijfsgegevens")));

		add(new BPVOpmerkingOverzichtPanel("bpvOpmerkingPanel",
			new PropertyModel<List<ExterneOrganisatieOpmerking>>(getDefaultModel(), "opmerkingen")));

		add(new BPVCriteriaExterneOrganisatieOverzichtPanel(
			"bpvCriteriaPanel",
			new PropertyModel<List<BPVCriteriaExterneOrganisatie>>(getDefaultModel(), "bpvCriteria")));

		add(new ExterneOrganisatiePraktijkbegeleiderOverzichtPanel("bpvBegeleiderPanel",
			new PropertyModel<List<ExterneOrganisatiePraktijkbegeleider>>(getDefaultModel(),
				"praktijkbegeleiders")));

		add(new VrijVeldEntiteitPanel<ExterneOrganisatieVrijVeld, ExterneOrganisatie>(
			"vrijveldenPanel", getContextExterneOrganisatieModel()));

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new ModuleEditPageButton<ExterneOrganisatie>(panel, "Bewerken",
			CobraKeyAction.BEWERKEN, ExterneOrganisatie.class,
			RelatieBeheerMenuItem.ExterneOrganisaties, ExterneOrganisatieOverzichtPage.this,
			getContextExterneOrganisatieModel()));

		panel.addButton(new TerugButton(panel, securePage));
		panel.addButton(new PageLinkButton(panel, getBPVDeelnemerButtonTitle(), new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new BPVDeelnemersPage((ExterneOrganisatie) getDefaultModelObject());
			}

			@Override
			public Class<BPVDeelnemersPage> getPageIdentity()
			{
				return BPVDeelnemersPage.class;
			}
		})
		{
			private static final long serialVersionUID = 1L;

			@Override
			public ButtonAlignment getAlignment()
			{
				return ButtonAlignment.LEFT;
			}
		});
	}

	private String getBPVDeelnemerButtonTitle()
	{
		if (EduArteApp.get().isModuleActive(EduArteModuleKey.HOGER_ONDERWIJS))
		{
			return "Stage-studenten";
		}
		else
		{
			return "BPV-deelnemers";
		}
	}

	@Override
	public void onDetach()
	{
		ComponentUtil.detachQuietly(securePage);
		super.onDetach();
	}
}
