/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.web.pages.intake.stap2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatieAdres;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatieContactgegeven;
import nl.topicus.eduarte.entities.personen.AbstractRelatie;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoon;
import nl.topicus.eduarte.entities.personen.PersoonExterneOrganisatie;
import nl.topicus.eduarte.krd.web.pages.intake.IntakeWizardModel;
import nl.topicus.eduarte.krd.web.pages.intake.IntakeWizardPage;
import nl.topicus.eduarte.web.components.modalwindow.adres.AdressenPanel;
import nl.topicus.eduarte.web.components.panels.ContactgegevenEntiteitPanel;
import nl.topicus.eduarte.web.pages.deelnemer.personalia.ExterneOrganisatieContactPersoonOverzichtPanel;
import nl.topicus.eduarte.zoekfilters.RelatieSoortZoekFilter;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * @author hop
 */
@PageInfo(title = "Intake stap 2 - Organisatie als relatie toevoegen", menu = {"Deelnemer > intake"})
public class IntakeStap2BewerkOrganisatieRelatiePage extends IntakeWizardPage
{
	private IModel<AbstractRelatie> relatieModel;

	private Form<Void> form;

	private boolean nieuw;

	public IntakeStap2BewerkOrganisatieRelatiePage(IntakeWizardModel wizard,
			PersoonExterneOrganisatie persoonExterneOrganisatie, boolean nieuw)
	{
		setWizard(wizard);
		relatieModel = wizard.getModel(persoonExterneOrganisatie);
		this.nieuw = nieuw;

		form = new Form<Void>("form");

		form.add(createPersonaliaAutoFieldset("inputFieldsAlgemeneGegevens"));

		form.add(createRelatieAutoFieldset("inputFieldsRelatieGegevens").setVisible(
			!persoonExterneOrganisatie.getPersoon().isMeerderjarig()));

		form.add(new AdressenPanel<ExterneOrganisatieAdres>("adresEditTabs",
			new PropertyModel<ExterneOrganisatie>(relatieModel, "relatie")));

		form.add(new ContactgegevenEntiteitPanel<ExterneOrganisatieContactgegeven>(
			"contactgegevens", new PropertyModel<List<ExterneOrganisatieContactgegeven>>(
				relatieModel, "relatie.contactgegevens"), false));

		form.add(new ExterneOrganisatieContactPersoonOverzichtPanel("contactpersonen",
			new PropertyModel<List<ExterneOrganisatieContactPersoon>>(relatieModel,
				"relatie.contactPersonen")));

		add(form);

		createComponents();
	}

	private AutoFieldSet<AbstractRelatie> createPersonaliaAutoFieldset(String id)
	{
		ArrayList<String> fields = new ArrayList<String>();
		fields.addAll(Arrays.asList("relatie.naam", "relatie.verkorteNaam",
			"relatie.soortExterneOrganisatie", "relatieSoort"));

		fields.add("relatie.debiteurennummer");
		fields.add("relatie.bankrekeningnummer");

		AutoFieldSet<AbstractRelatie> fieldsetpersonalia =
			new AutoFieldSet<AbstractRelatie>(id, relatieModel, "Algemene gegevens");
		fieldsetpersonalia.setPropertyNames(fields);
		fieldsetpersonalia.setSortAccordingToPropertyNames(true);
		fieldsetpersonalia.setRenderMode(RenderMode.DISPLAY);

		return fieldsetpersonalia;
	}

	private AutoFieldSet<AbstractRelatie> createRelatieAutoFieldset(String id)
	{
		ArrayList<String> fields = new ArrayList<String>();

		// dit zijn de enige velden die kunnen worden geedit, en alleen als niet
		// meerderjarig
		// wettelijkeVertegenwoordiger is alleen beschikbaar indien meerderjarig
		// betalingsplichtige is true als niet meerderjarig en geen andere
		// betalingsplichtige geregistreerd
		fields.add("wettelijkeVertegenwoordiger");
		fields.add("betalingsplichtige");
		fields.add("relatieSoort");

		AutoFieldSet<AbstractRelatie> fieldsetrelatie =
			new AutoFieldSet<AbstractRelatie>(id, relatieModel);
		fieldsetrelatie.setPropertyNames(fields);
		fieldsetrelatie.setSortAccordingToPropertyNames(true);
		fieldsetrelatie.setRenderMode(RenderMode.EDIT);
		RelatieSoortZoekFilter filter = new RelatieSoortZoekFilter();
		filter.setOrganisatieOpname(true);
		fieldsetrelatie.addFieldModifier(new ConstructorArgModifier("relatieSoort", filter));

		return fieldsetrelatie;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit()
			{
				PersoonExterneOrganisatie persoonExterneOrganisatie =
					getPersoonExterneOrganisatie();
				checkBetalingsplichtige(persoonExterneOrganisatie);

				setResponsePage(new IntakeStap2Achtergrond(getWizard()));
			}

			private void checkBetalingsplichtige(PersoonExterneOrganisatie persoonExterneOrganisatie)
			{
				if (persoonExterneOrganisatie.isBetalingsplichtige())
				{
					for (AbstractRelatie relatie : persoonExterneOrganisatie.getDeelnemer()
						.getRelaties())
					{
						if (relatie.isBetalingsplichtige()
							&& !relatie.equals(persoonExterneOrganisatie))
						{
							relatie.setBetalingsplichtige(false);
						}
					}
				}
			}

		});

		if (!nieuw)
		{
			AbstractBottomRowButton ontkoppelen =
				new AbstractBottomRowButton(panel, "Externe organisatie ontkoppelen",
					CobraKeyAction.VERWIJDEREN, ButtonAlignment.LEFT)
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected WebMarkupContainer getLink(String linkId)
					{
						return new Link<Void>(linkId)
						{
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick()
							{
								getWizard().removeRelatie(getPersoonExterneOrganisatie());
								setResponsePage(new IntakeStap2Achtergrond(getWizard()));
							}
						};
					}
				};

			panel.addButton(ontkoppelen);
		}

		panel.addButton(new AbstractLinkButton(panel, "Annuleren", CobraKeyAction.ANNULEREN,
			ButtonAlignment.RIGHT)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				if (nieuw)
				{
					PersoonExterneOrganisatie relatie = getPersoonExterneOrganisatie();
					// model op null zetten voorkomt probleem bij dubbelklik
					// (mantis 43834)
					relatieModel = new Model<AbstractRelatie>(null);
					getWizard().removeRelatie(relatie);
				}
				setResponsePage(new IntakeStap2Achtergrond(getWizard()));
			}
		});
	}

	private PersoonExterneOrganisatie getPersoonExterneOrganisatie()
	{
		return (PersoonExterneOrganisatie) relatieModel.getObject();
	}

	@Override
	public void detachModels()
	{
		super.detachModels();
		ComponentUtil.detachFields(this);
	}

	@Override
	protected int getStapNummer()
	{
		return 2;
	}

	@Override
	protected String getStapTitel()
	{
		return "Relatie bewerken (organisatie)";
	}
}
