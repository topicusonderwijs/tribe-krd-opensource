/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.deelnemer.personalia;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.VisibilityModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.core.principals.deelnemer.DeelnemerRelaties;
import nl.topicus.eduarte.entities.bpv.BPVBedrijfsgegeven;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatieContactgegeven;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoon;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.entities.personen.PersoonExterneOrganisatie;
import nl.topicus.eduarte.entities.personen.PersoonExterneOrganisatieContactPersoon;
import nl.topicus.eduarte.entities.vrijevelden.RelatieVrijVeld;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.modalwindow.adres.AdressenPanel;
import nl.topicus.eduarte.web.components.panels.ContactgegevenEntiteitPanel;
import nl.topicus.eduarte.web.components.panels.VrijVeldEntiteitPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.organisatie.BPVBedrijfsgegevenOverzichtPanel;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * @author idserda
 */
@PageInfo(title = "Personalia", menu = {"Deelnemer > [deelnemer]", "Groep > [groep] > [deelnemer]"})
@InPrincipal(DeelnemerRelaties.class)
public class PersoonExterneOrganisatieDetailPage extends AbstractDeelnemerPage
{
	private static final long serialVersionUID = 1L;

	IModel<PersoonExterneOrganisatie> persoonExterneOrganisatieModel;

	public PersoonExterneOrganisatieDetailPage(Deelnemer deelnemer, Verbintenis verbintenis,
			PersoonExterneOrganisatie persoonExterneOrganisatie)
	{
		super(DeelnemerMenuItem.Relaties, deelnemer, verbintenis);

		persoonExterneOrganisatieModel = ModelFactory.getModel(persoonExterneOrganisatie);

		add(new Label("titel", "Details van \"" + persoonExterneOrganisatie.getNaam() + "\""));

		AutoFieldSet<PersoonExterneOrganisatie> algemeenFieldSet =
			new AutoFieldSet<PersoonExterneOrganisatie>("personaliaFieldSet",
				persoonExterneOrganisatieModel, "Algemene gegevens");
		algemeenFieldSet.setPropertyNames("relatie.naam", "relatie.verkorteNaam", "soort",
			"begindatum", "einddatum", "relatie.bpvBedrijf", "relatie.omschrijving",
			"relatieSoort", "relatie.debiteurennummer", "relatie.bankrekeningnummer",
			"wettelijkeVertegenwoordiger", "betalingsplichtige");
		algemeenFieldSet.setRenderMode(RenderMode.DISPLAY);
		algemeenFieldSet.setSortAccordingToPropertyNames(true);

		algemeenFieldSet.addFieldModifier(new VisibilityModifier(
			new AbstractReadOnlyModel<Boolean>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Boolean getObject()
				{
					return !getContextDeelnemer().getPersoon().isMeerderjarig();
				}
			}, "wettelijkeVertegenwoordiger"));

		add(algemeenFieldSet);

		add(new AdressenPanel<PersoonAdres>("adresPanel", new PropertyModel<Persoon>(
			persoonExterneOrganisatieModel, "relatie")));

		add(new ContactgegevenEntiteitPanel<ExterneOrganisatieContactgegeven>(
			"contactGegevensPanel", new PropertyModel<List<ExterneOrganisatieContactgegeven>>(
				persoonExterneOrganisatieModel, "relatie.contactgegevens"), false));

		add(new ExterneOrganisatieContactPersoonOverzichtPanel("contactPersonenPanel",
			new PropertyModel<List<ExterneOrganisatieContactPersoon>>(
				persoonExterneOrganisatieModel, "relatie.contactPersonen")));

		add(new PersoonExterneOrganisatieContactPersoonOverzichtPanel(
			"contactPersonenSelectiePanel",
			new PropertyModel<List<PersoonExterneOrganisatieContactPersoon>>(
				persoonExterneOrganisatieModel, "persoonExterneOrganisatieContactPersonen")));

		add(new BPVBedrijfsgegevenOverzichtPanel("bpvBedrijfsgegevenPanel",
			new PropertyModel<List<BPVBedrijfsgegeven>>(persoonExterneOrganisatieModel,
				"relatie.bpvBedrijfsgegevens"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				if (getPersoonExterneOrganisatie().getRelatie() != null
					&& getPersoonExterneOrganisatie().getRelatie().isBpvBedrijf())
					return true;
				return false;
			}

		});

		VrijVeldEntiteitPanel<RelatieVrijVeld, PersoonExterneOrganisatie> vrijVeldenPanel =
			new VrijVeldEntiteitPanel<RelatieVrijVeld, PersoonExterneOrganisatie>(
				"vrijVeldenPanel", persoonExterneOrganisatieModel);
		add(vrijVeldenPanel);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		ModuleEditPageButton<PersoonExterneOrganisatie> bewerken =
			new ModuleEditPageButton<PersoonExterneOrganisatie>(panel, "Bewerken",
				CobraKeyAction.BEWERKEN, PersoonExterneOrganisatie.class, getSelectedMenuItem(),
				PersoonExterneOrganisatieDetailPage.this, persoonExterneOrganisatieModel);
		panel.addButton(bewerken);

		panel.addButton(new TerugButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new DeelnemerRelatiesOverzichtPage(getContextDeelnemer(),
					getContextVerbintenis());
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return PersoonExterneOrganisatieDetailPage.class;
			}

		}));
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(PersoonExterneOrganisatie.class);
		ctorArgValues.add(getContextPersoonExterneOrganisatieModel());
	}

	private IModel<PersoonExterneOrganisatie> getContextPersoonExterneOrganisatieModel()
	{
		return persoonExterneOrganisatieModel;
	}

	private PersoonExterneOrganisatie getPersoonExterneOrganisatie()
	{
		return persoonExterneOrganisatieModel.getObject();
	}
}
