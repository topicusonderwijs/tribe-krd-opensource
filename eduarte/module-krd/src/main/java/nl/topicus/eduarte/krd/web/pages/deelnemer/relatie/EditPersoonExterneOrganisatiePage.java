/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.web.pages.deelnemer.relatie;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.EnableModifier;
import nl.topicus.cobra.web.components.form.modifier.VisibilityModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.bpv.BPVBedrijfsgegeven;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatieAdres;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatieContactgegeven;
import nl.topicus.eduarte.entities.personen.AbstractRelatie;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoon;
import nl.topicus.eduarte.entities.personen.PersoonExterneOrganisatie;
import nl.topicus.eduarte.entities.personen.PersoonExterneOrganisatieContactPersoon;
import nl.topicus.eduarte.entities.vrijevelden.RelatieVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldOptieKeuze;
import nl.topicus.eduarte.krd.principals.deelnemer.relatie.DeelnemerRelatieOrganisatieWrite;
import nl.topicus.eduarte.krd.web.components.modalwindow.bpvbedrijf.BPVBedrijfsgegevenEditPanel;
import nl.topicus.eduarte.krd.web.components.modalwindow.contactpersoon.ExterneOrganisatieContactPersoonEditPanel;
import nl.topicus.eduarte.krd.web.components.modalwindow.contactpersoon.PersoonExterneOrganisatieContactPersoonEditPanel;
import nl.topicus.eduarte.krd.web.components.panels.VrijVeldEntiteitEditPanel;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.modalwindow.adres.AdressenEditPanel;
import nl.topicus.eduarte.web.components.modalwindow.adres.AdressenPanel;
import nl.topicus.eduarte.web.components.panels.ContactgegevenEntiteitEditPanel;
import nl.topicus.eduarte.web.components.panels.ContactgegevenEntiteitPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.BPVBedrijfsgegevenTable;
import nl.topicus.eduarte.web.components.panels.datapanel.table.ExterneOrganisatieContactPersoonTable;
import nl.topicus.eduarte.web.components.panels.datapanel.table.PersoonExterneOrganisatieContactPersoonTable;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.web.pages.deelnemer.personalia.DeelnemerRelatiesOverzichtPage;
import nl.topicus.eduarte.web.pages.deelnemer.personalia.ExterneOrganisatieContactPersoonOverzichtPanel;
import nl.topicus.eduarte.zoekfilters.RelatieSoortZoekFilter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

/**
 * @author idserda
 */
@PageInfo(title = "Organsatie als relatie bewerken", menu = {"Deelnemer > [deelnemer]",
	"Groep > [groep] > [deelnemer]"})
@InPrincipal(DeelnemerRelatieOrganisatieWrite.class)
public class EditPersoonExterneOrganisatiePage extends AbstractDeelnemerPage implements
		IModuleEditPage<PersoonExterneOrganisatie>

{
	private Form<Void> form;

	private AbstractDeelnemerPage returnToPage;

	private PersoonExterneOrganisatieContactPersoonEditPanel persoonExterneOrganisatieContactPersoonEditPanel;

	public EditPersoonExterneOrganisatiePage(PersoonExterneOrganisatie persoonExterneOrganisatie,
			AbstractDeelnemerPage returnToPage)
	{
		super(DeelnemerMenuItem.Relaties, returnToPage.getContextDeelnemer(), returnToPage
			.getContextVerbintenis());
		this.returnToPage = returnToPage;

		setDefaultModel(ModelFactory.getCompoundChangeRecordingModel(persoonExterneOrganisatie,
			new DefaultModelManager(PersoonExterneOrganisatieContactPersoon.class,
				PersoonExterneOrganisatie.class, ExterneOrganisatieAdres.class,
				ExterneOrganisatieContactgegeven.class, ExterneOrganisatieContactPersoon.class,
				BPVBedrijfsgegeven.class, VrijVeldOptieKeuze.class, RelatieVrijVeld.class,
				Adres.class, Brin.class, ExterneOrganisatie.class)));

		form = new Form<Void>("form");

		voegPersonaliaAutoFieldsetToe();

		if (getPersoonExterneOrganisatie().getRelatie().isSaved())
			voegOverzichtDatapanelsToe();
		else
			voegBewerkenDatapanelsToe();

		voegBPVGegevensEditpanelToe();
		voegVrijeVeldenEditpanelToe();
		voegSpecifiekeContactpersonenEditpanelToe();

		add(form);

		createComponents();
	}

	private void voegPersonaliaAutoFieldsetToe()
	{
		AutoFieldSet<PersoonExterneOrganisatie> fieldsetpersonalia =
			new AutoFieldSet<PersoonExterneOrganisatie>("inputFieldsAlgemeneGegevens",
				getChangeRecordingModel(), "Algemene gegevens");
		fieldsetpersonalia.setRenderMode(RenderMode.EDIT);
		fieldsetpersonalia.setSortAccordingToPropertyNames(true);
		fieldsetpersonalia.setPropertyNames("relatie.naam", "relatie.verkorteNaam",
			"relatie.soortExterneOrganisatie", "relatie.begindatum", "relatie.einddatum",
			"relatie.bpvBedrijf", "relatie.omschrijving", "relatieSoort",
			"relatie.debiteurennummer", "relatie.bankrekeningnummer",
			"wettelijkeVertegenwoordiger", "betalingsplichtige");

		RelatieSoortZoekFilter filter = new RelatieSoortZoekFilter();
		filter.setOrganisatieOpname(true);
		fieldsetpersonalia.addFieldModifier(new ConstructorArgModifier("relatieSoort", filter));
		fieldsetpersonalia.addFieldModifier(new EnableModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return !getPersoonExterneOrganisatie().getRelatie().isSaved();
			}

		}, "relatie.naam", "relatie.verkorteNaam", "relatie.soortExterneOrganisatie",
			"relatie.bpvBedrijf", "relatie.actief", "relatie.omschrijving",
			"relatie.bankrekeningnummer", "wettelijkeVertegenwoordiger"));

		fieldsetpersonalia.addFieldModifier(new VisibilityModifier(
			new AbstractReadOnlyModel<Boolean>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Boolean getObject()
				{
					return !getPersoonExterneOrganisatie().getPersoon().isMeerderjarig();
				}
			}, "wettelijkeVertegenwoordiger"));

		if (!getPersoonExterneOrganisatie().getRelatie().isSaved())
		{
			fieldsetpersonalia.addModifier("relatie.bpvBedrijf", new OnChangeAjaxBehavior()
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target)
				{
					target.addComponent(form.get("bpvBedrijfsgegevens"));
				}

			});
		}

		form.add(fieldsetpersonalia);
	}

	private void voegOverzichtDatapanelsToe()
	{
		form.add(new AdressenPanel<ExterneOrganisatieAdres>("adresEditTabs",
			new PropertyModel<ExterneOrganisatie>(getDefaultModel(), "relatie")));

		form.add(new ContactgegevenEntiteitPanel<ExterneOrganisatieContactgegeven>(
			"contactgegevens", new PropertyModel<List<ExterneOrganisatieContactgegeven>>(
				getDefaultModel(), "relatie.contactgegevens"), false));

		form.add(new ExterneOrganisatieContactPersoonOverzichtPanel("contactpersonen",
			new PropertyModel<List<ExterneOrganisatieContactPersoon>>(getDefaultModel(),
				"relatie.contactPersonen")));
	}

	private void voegBewerkenDatapanelsToe()
	{
		form
			.add(new ContactgegevenEntiteitEditPanel<ExterneOrganisatieContactgegeven, ExterneOrganisatie>(
				"contactgegevens", new LoadableDetachableModel<ExterneOrganisatie>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected ExterneOrganisatie load()
					{
						return getPersoonExterneOrganisatie().getRelatie();
					}
				}));

		form.add(new AdressenEditPanel<ExterneOrganisatieAdres, ExterneOrganisatie>(
			"adresEditTabs", new PropertyModel<ExterneOrganisatie>(getDefaultModel(), "relatie"),
			getManager()));

		form.add(new ExterneOrganisatieContactPersoonEditPanel("contactpersonen",
			new PropertyModel<List<ExterneOrganisatieContactPersoon>>(getDefaultModel(),
				"relatie.contactPersonen"), getManager(),
			new ExterneOrganisatieContactPersoonTable())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public ExterneOrganisatieContactPersoon createNewT()
			{
				return new ExterneOrganisatieContactPersoon(getPersoonExterneOrganisatie()
					.getRelatie());
			}

			@Override
			public void onDelete(ExterneOrganisatieContactPersoon object, AjaxRequestTarget target)
			{
				super.onDelete(object, target);
				getPersoonExterneOrganisatie().verwijderGekoppeldeContactPersoon(object);
				target.addComponent(persoonExterneOrganisatieContactPersoonEditPanel);
			}

			@Override
			protected void onSaveCurrent(AjaxRequestTarget target,
					ExterneOrganisatieContactPersoon object)
			{
				super.onSaveCurrent(target, object);
				target.addComponent(persoonExterneOrganisatieContactPersoonEditPanel);
			}
		});
	}

	private void voegVrijeVeldenEditpanelToe()
	{
		VrijVeldEntiteitEditPanel<PersoonExterneOrganisatie> VVEEPanel =
			new VrijVeldEntiteitEditPanel<PersoonExterneOrganisatie>("vrijVelden",
				getChangeRecordingModel());
		VVEEPanel.getVrijVeldZoekFilter().setDossierScherm(true);
		VVEEPanel.getVrijVeldZoekFilter().setCategorie(VrijVeldCategorie.RELATIE);
		form.add(VVEEPanel);
	}

	private void voegBPVGegevensEditpanelToe()
	{
		form.add(new BPVBedrijfsgegevenEditPanel("bpvBedrijfsgegevens",
			new PropertyModel<List<BPVBedrijfsgegeven>>(getDefaultModel(),
				"relatie.bpvBedrijfsgegevens"), getManager(), new BPVBedrijfsgegevenTable(false))
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

			@Override
			public BPVBedrijfsgegeven createNewT()
			{
				return new BPVBedrijfsgegeven(getPersoonExterneOrganisatie().getRelatie());
			}

		}.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));
	}

	private void voegSpecifiekeContactpersonenEditpanelToe()
	{
		IModel<List<PersoonExterneOrganisatieContactPersoon>> persoonExtOrgContactPersoonModel =
			new PropertyModel<List<PersoonExterneOrganisatieContactPersoon>>(
				getChangeRecordingModel(), "persoonExterneOrganisatieContactPersonen");

		IModel<List<ExterneOrganisatieContactPersoon>> contactPersonenModel =
			getBeschikbareContactPersonenModel();

		persoonExterneOrganisatieContactPersoonEditPanel =
			new PersoonExterneOrganisatieContactPersoonEditPanel("contactpersonenRelatie",
				persoonExtOrgContactPersoonModel, contactPersonenModel, getManager(),
				new PersoonExterneOrganisatieContactPersoonTable(),
				new ExterneOrganisatieContactPersoonTable())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public PersoonExterneOrganisatieContactPersoon createNewT()
				{
					return new PersoonExterneOrganisatieContactPersoon(
						getPersoonExterneOrganisatie());
				}
			};

		form.add(persoonExterneOrganisatieContactPersoonEditPanel);
	}

	private IModel<List<ExterneOrganisatieContactPersoon>> getBeschikbareContactPersonenModel()
	{
		return new LoadableDetachableModel<List<ExterneOrganisatieContactPersoon>>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected List<ExterneOrganisatieContactPersoon> load()
			{
				List<ExterneOrganisatieContactPersoon> ret =
					new ArrayList<ExterneOrganisatieContactPersoon>();

				for (ExterneOrganisatieContactPersoon cp : getPersoonExterneOrganisatie()
					.getRelatie().getContactPersonen())
				{
					if (!getPersoonExterneOrganisatie().isGekoppeldMetContactPersoon(cp))
						ret.add(cp);
				}
				return ret;
			}
		};
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

				List<AbstractRelatie> relaties =
					persoonExterneOrganisatie.getPersoon().getRelaties();
				if (!relaties.contains(persoonExterneOrganisatie))
					relaties.add(persoonExterneOrganisatie);
				getChangeRecordingModel().saveObject();
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
				setResponsePage(returnToPage);
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
							relatie.save();
						}
					}
				}
			}

		});

		if (getPersoonExterneOrganisatie().isSaved())
		{
			AbstractBottomRowButton ontkoppelen =
				new AbstractBottomRowButton(panel, "Externe organisatie ontkoppelen",
					CobraKeyAction.VERWIJDEREN, ButtonAlignment.RIGHT)
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
								PersoonExterneOrganisatie persoonExterneOrganisatie =
									getPersoonExterneOrganisatie();

								List<AbstractRelatie> relaties =
									getPersoonExterneOrganisatie().getPersoon().getRelaties();
								relaties.remove(getPersoonExterneOrganisatie());

								persoonExterneOrganisatie.delete();
								persoonExterneOrganisatie.commit();

								setResponsePage(new DeelnemerRelatiesOverzichtPage(returnToPage
									.getContextDeelnemerModel()));
							}
						};
					}
				};

			panel.addButton(ontkoppelen);
		}

		panel.addButton(new AnnulerenButton(panel, returnToPage));
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		returnToPage.detach();
	}

	@SuppressWarnings("unchecked")
	private IChangeRecordingModel<PersoonExterneOrganisatie> getChangeRecordingModel()
	{
		return (IChangeRecordingModel<PersoonExterneOrganisatie>) getDefaultModel();
	}

	private ModelManager getManager()
	{
		return getChangeRecordingModel().getManager();
	}

	private PersoonExterneOrganisatie getPersoonExterneOrganisatie()
	{
		return (PersoonExterneOrganisatie) getDefaultModelObject();
	}

}
