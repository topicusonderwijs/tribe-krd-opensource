package nl.topicus.eduarte.krd.web.pages.beheer.organisatie.extern;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.security.RechtenSoorten;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.LabelModifier;
import nl.topicus.cobra.web.components.form.modifier.ValidateModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.cobra.web.components.shortcut.ActionKey;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.validators.BankrekeningElfProefValidator;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.dao.helpers.NummerGeneratorDataAccessHelper;
import nl.topicus.eduarte.entities.bpv.BPVBedrijfsgegeven;
import nl.topicus.eduarte.entities.bpv.BPVCriteriaExterneOrganisatie;
import nl.topicus.eduarte.entities.bpv.BPVBedrijfsgegeven.BPVCodeHerkomst;
import nl.topicus.eduarte.entities.kenmerk.ExterneOrganisatieKenmerk;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatieAdres;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatieContactgegeven;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatieOpmerking;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatiePraktijkbegeleider;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoon;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.krd.principals.organisatie.ExterneOrganisatiesWrite;
import nl.topicus.eduarte.krd.web.components.modalwindow.bpvbedrijf.BPVBedrijfsgegevenEditPanel;
import nl.topicus.eduarte.krd.web.components.modalwindow.bpvopmerking.BPVOpmerkingEditPanel;
import nl.topicus.eduarte.krd.web.components.modalwindow.contactpersoon.ExterneOrganisatieContactPersoonEditPanel;
import nl.topicus.eduarte.krd.web.components.panels.VrijVeldEntiteitEditPanel;
import nl.topicus.eduarte.web.components.menu.RelatieBeheerMenuItem;
import nl.topicus.eduarte.web.components.modalwindow.adres.AdressenEditPanel;
import nl.topicus.eduarte.web.components.modalwindow.adres.AdressenPanel;
import nl.topicus.eduarte.web.components.modalwindow.bpvcriteria.BPVCriteriaExterneOrganisatieEditPanel;
import nl.topicus.eduarte.web.components.modalwindow.externeorganisatie.ExterneOrganisatiePraktijkbegeleiderEditPanel;
import nl.topicus.eduarte.web.components.panels.ContactgegevenEntiteitEditPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.BPVBedrijfsgegevenTable;
import nl.topicus.eduarte.web.components.panels.datapanel.table.BPVCriteriaExterneOrganisatieTable;
import nl.topicus.eduarte.web.components.panels.datapanel.table.BPVOpmerkingTable;
import nl.topicus.eduarte.web.components.panels.datapanel.table.ExterneOrganisatieContactPersoonTable;
import nl.topicus.eduarte.web.components.panels.datapanel.table.ExterneOrganisatiePraktijkbegeleiderTable;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.organisatie.ExterneOrganisatieZoekenPage;
import nl.topicus.eduarte.web.pages.beheer.relatie.AbstractRelatieBeheerPage;

import org.apache.wicket.markup.html.form.Form;

@PageInfo(title = "Externe organisatie", menu = {
	"Relatie > Externe organisaties > [Externe organisatie] > Bewerken",
	"Relatie > Externe organisaties > Toevoegen"})
@InPrincipal(ExterneOrganisatiesWrite.class)
@RechtenSoorten( {RechtenSoort.INSTELLING, RechtenSoort.BEHEER})
public class ExterneOrganisatieEditPage extends AbstractRelatieBeheerPage<ExterneOrganisatie>
		implements IModuleEditPage<ExterneOrganisatie>
{
	private final boolean brin;

	private Form<Void> form;

	private ExterneOrganisatieModel externeOrganisatieModel;

	private BestaandAdresValidator bestaandAdresValidator;

	public ExterneOrganisatieEditPage(ExterneOrganisatie externeOrganisatie, SecurePage returnPage)
	{
		this(new ExterneOrganisatieModel(externeOrganisatie), returnPage);
	}

	public ExterneOrganisatieEditPage(ExterneOrganisatieModel externeOrganisatieModel,
			SecurePage returnPage)
	{
		super(externeOrganisatieModel.getEntiteitModel(), RelatieBeheerMenuItem.ExterneOrganisaties);
		this.externeOrganisatieModel = externeOrganisatieModel;
		externeOrganisatieModel.getExterneOrganisatie().checkAndCreateDebiteurNummer();
		this.brin = externeOrganisatieModel.getExterneOrganisatie().doUnproxy() instanceof Brin;
		setReturnPage(returnPage);
		createForm();
	}

	private void createForm()
	{
		form = new Form<Void>("form");
		add(form);

		bestaandAdresValidator = new BestaandAdresValidator(externeOrganisatieModel);
		form.add(bestaandAdresValidator);

		AutoFieldSet<ExterneOrganisatie> fieldSet =
			new AutoFieldSet<ExterneOrganisatie>("externeOrganisatie", getContextModel(),
				"Externe organisatie");
		if (brin)
		{
			fieldSet.setPropertyNames("code", "onderwijssector", "naam", "verkorteNaam",
				"soortExterneOrganisatie", "begindatum", "einddatum", "bpvBedrijf",
				"ondertekeningBPVOdoor", "debiteurennummer", "bankrekeningnummer",
				"betalingstermijn", "omschrijving");
		}
		else
		{
			fieldSet.setPropertyNames("naam", "verkorteNaam", "soortExterneOrganisatie",
				"begindatum", "einddatum", "bpvBedrijf", "ondertekeningBPVOdoor",
				"nietGeschiktVoorBPVDeelnemers", "toelichtingNietGeschiktVoorBPV",
				"nietGeschiktVoorBPVMatch", "debiteurennummer", "bankrekeningnummer",
				"verzamelfacturen", "betalingstermijn", "omschrijving");

		}
		fieldSet.addFieldModifier(new LabelModifier("bpvBedrijf", "BPV-bedrijf"));

		fieldSet.setRenderMode(externeOrganisatieModel.getObject().isLandelijk()
			? RenderMode.DISPLAY : RenderMode.EDIT);
		fieldSet.setSortAccordingToPropertyNames(true);
		fieldSet.addFieldModifier(new ValidateModifier(new BankrekeningElfProefValidator(),
			"bankrekeningnummer"));
		form.add(fieldSet);

		form
			.add(new ContactgegevenEntiteitEditPanel<ExterneOrganisatieContactgegeven, ExterneOrganisatie>(
				"contactgegevens", getContextModel())
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected boolean isDeletable(Object o)
				{
					return !((ExterneOrganisatieContactgegeven) o).isInGebruik();
				}
			});

		form.add(new ExterneOrganisatieContactPersoonEditPanel("contactpersonen",
			externeOrganisatieModel.getContPersListModel(), externeOrganisatieModel
				.getEntiteitManager(), new ExterneOrganisatieContactPersoonTable())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public ExterneOrganisatieContactPersoon createNewT()
			{
				return new ExterneOrganisatieContactPersoon(externeOrganisatieModel
					.getExterneOrganisatie());
			}

			@Override
			protected boolean isDeletable()
			{
				// ExterneOrganisatieContactPersoonAccount,BPVInschrijving en Contract
				// hebben een verwijzing naar deze entitiet, bovendien heeft deze entiteit
				// een begin en einddatum. Verwijderen lijkt me daarom overbodig.
				return false;
			}
		});
		form.add(new BPVBedrijfsgegevenEditPanel("bpvBedrijfsgegevens", externeOrganisatieModel
			.getBPVBedrGegListModel(), externeOrganisatieModel.getEntiteitManager(),
			new BPVBedrijfsgegevenTable(false))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public BPVBedrijfsgegeven createNewT()
			{
				BPVBedrijfsgegeven gegeven =
					new BPVBedrijfsgegeven(externeOrganisatieModel.getExterneOrganisatie());
				gegeven.setHerkomstCode(BPVCodeHerkomst.BRON);
				return gegeven;
			}

		}.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));

		form.add(new BPVOpmerkingEditPanel("bpvOpmerkingen", externeOrganisatieModel
			.getBPVOpmerkingListModel(), externeOrganisatieModel.getEntiteitManager(),
			new BPVOpmerkingTable())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public ExterneOrganisatieOpmerking createNewT()
			{
				ExterneOrganisatieOpmerking opmerking =
					new ExterneOrganisatieOpmerking(externeOrganisatieModel.getExterneOrganisatie());
				opmerking.setAuteur(getIngelogdeMedewerker());
				opmerking.setDatum(TimeUtil.getInstance().currentDate());
				return opmerking;
			}

		}.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));

		form.add(new BPVCriteriaExterneOrganisatieEditPanel("bpvCriteria", externeOrganisatieModel
			.getBPVCriteriaListModel(), externeOrganisatieModel.getEntiteitManager(),
			new BPVCriteriaExterneOrganisatieTable())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public BPVCriteriaExterneOrganisatie createNewT()
			{
				return new BPVCriteriaExterneOrganisatie(externeOrganisatieModel
					.getExterneOrganisatie());
			}

		}.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));

		form.add(new ExterneOrganisatiePraktijkbegeleiderEditPanel("bpvPraktijkbegeleiders",
			externeOrganisatieModel.getExterneOrganisatiePraktijkbegeleiderListModel(),
			externeOrganisatieModel.getEntiteitManager(),
			new ExterneOrganisatiePraktijkbegeleiderTable())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public ExterneOrganisatiePraktijkbegeleider createNewT()
			{
				return new ExterneOrganisatiePraktijkbegeleider(externeOrganisatieModel
					.getExterneOrganisatie());
			}

		}.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));

		VrijVeldEntiteitEditPanel<ExterneOrganisatie> VVEEPanel =
			new VrijVeldEntiteitEditPanel<ExterneOrganisatie>("vrijVelden", getContextModel());
		VVEEPanel.getVrijVeldZoekFilter().setDossierScherm(true);
		VVEEPanel.getVrijVeldZoekFilter().setCategorie(VrijVeldCategorie.EXTERNEORGANISATIE);
		form.add(VVEEPanel);

		addAdresTable();

		createComponents();
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
				externeOrganisatieModel.save();
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
				setResponsePage(getReturnPage());
			}
		});
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				externeOrganisatieModel.save();
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();

				NummerGeneratorDataAccessHelper generator =
					DataAccessRegistry.getHelper(NummerGeneratorDataAccessHelper.class);
				ExterneOrganisatie org = brin ? new Brin() : new ExterneOrganisatie();

				if (org.getDebiteurennummer() == null)
				{
					org.setDebiteurennummer(generator.newOrganisatieDebiteurnummer());
				}

				ExterneOrganisatieEditPage page =
					new ExterneOrganisatieEditPage(org, getReturnPage());
				EduArteRequestCycle.get().setResponsePage(page);
			}

			@Override
			public String getLabel()
			{
				return "Opslaan en nieuwe toevoegen";
			}

			@Override
			public ActionKey getAction()
			{
				ActionKey action = CobraKeyAction.VOLGENDE;
				return action;
			}
		});
		panel.addButton(new AnnulerenButton(panel, getReturnPage()));

		panel.addButton(new VerwijderButton(panel, "Verwijderen",
			"Weet u zeker dat u deze externe organisatie wilt verwijderen?")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				ExterneOrganisatie eo = externeOrganisatieModel.getExterneOrganisatie();
				for (BPVBedrijfsgegeven b : eo.getBpvBedrijfsgegevens())
					b.delete();
				for (ExterneOrganisatieAdres adres : eo.getAdressen())
					adres.delete();
				for (ExterneOrganisatieContactgegeven con : eo.getContactgegevens())
					con.delete();
				for (ExterneOrganisatieContactPersoon pson : eo.getContactPersonen())
					pson.delete();
				for (ExterneOrganisatieKenmerk kenmerk : eo.getKenmerken())
					kenmerk.delete();
				eo.delete();
				eo.commit();
				setResponsePage(ExterneOrganisatieZoekenPage.class);
			}

			@Override
			public boolean isVisible()
			{
				ExterneOrganisatie eo = externeOrganisatieModel.getExterneOrganisatie();
				return !eo.isLandelijk() && eo.isSaved() && !eo.isInGebruik();
			}
		});
	}

	private void addAdresTable()
	{
		if (externeOrganisatieModel.getObject().isLandelijk())
		{
			form.add(new AdressenPanel<ExterneOrganisatieAdres>("inputFieldsAdres",
				externeOrganisatieModel.getEntiteitModel()));
		}
		else
		{
			form.add(new AdressenEditPanel<ExterneOrganisatieAdres, ExterneOrganisatie>(
				"inputFieldsAdres", externeOrganisatieModel.getEntiteitModel(),
				externeOrganisatieModel.getEntiteitManager()));
		}
	}

	@Override
	public void onDetach()
	{
		ComponentUtil.detachQuietly(externeOrganisatieModel);
		super.onDetach();
	}

}
