package nl.topicus.eduarte.web.components.panels.filter.uitgebreid;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.cobra.web.components.quicksearch.ISelectListener;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.components.wiquery.CollapsablePanel;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.security.models.LocatiesPageModel;
import nl.topicus.eduarte.entities.BronEntiteitStatus;
import nl.topicus.eduarte.entities.VrijVeldable;
import nl.topicus.eduarte.entities.adres.TypeContactgegeven;
import nl.topicus.eduarte.entities.bijlage.DocumentCategorie;
import nl.topicus.eduarte.entities.bijlage.DocumentType;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.BPVStatus;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.contract.ContractOnderdeel;
import nl.topicus.eduarte.entities.contract.SoortContract;
import nl.topicus.eduarte.entities.contract.TypeFinanciering;
import nl.topicus.eduarte.entities.contract.Contract.Onderaanneming;
import nl.topicus.eduarte.entities.examen.ExamenWorkflow;
import nl.topicus.eduarte.entities.examen.Examenstatus;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.groep.Groepstype;
import nl.topicus.eduarte.entities.inschrijving.RedenUitschrijving;
import nl.topicus.eduarte.entities.inschrijving.Intakegesprek.IntakegesprekStatus;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding.SoortOnderwijs;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.Leerprofiel;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.ProfielInburgering;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.RedenInburgering;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.SoortPraktijkexamen;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.Vertrekstatus;
import nl.topicus.eduarte.entities.kenmerk.KenmerkCategorie;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.opleiding.Team;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.RelatieSoort;
import nl.topicus.eduarte.entities.taxonomie.MBOLeerweg;
import nl.topicus.eduarte.entities.taxonomie.MBONiveau;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldEntiteit;
import nl.topicus.eduarte.web.components.choice.*;
import nl.topicus.eduarte.web.components.choice.ContractOnderdeelCombobox.ContractOnderdelenListModel;
import nl.topicus.eduarte.web.components.choice.multiple.*;
import nl.topicus.eduarte.web.components.factory.KRDModuleComponentFactory;
import nl.topicus.eduarte.web.components.panels.AbstractVrijVeldEntiteitPanel;
import nl.topicus.eduarte.web.components.quicksearch.contract.ContractSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.externeorganisatie.ExterneOrganisatieSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.externeorganisatie.contactpersoon.ExterneOrganisatieContactPersoonSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.gemeente.GemeenteSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.groep.GroepSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.land.LandSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.medewerker.MedewerkerSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.nationaliteit.NationaliteitSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.onderwijsproduct.OnderwijsproductSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.opleiding.OpleidingSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.plaats.PlaatsSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.provincie.ProvincieSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.soortvooropleiding.SoortVooropleidingSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.taxonomie.TaxonomieElementSearchEditor;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.zoekfilters.*;
import nl.topicus.eduarte.zoekfilters.DeelnemerZoekFilter.TypeAdres;
import nl.topicus.eduarte.zoekfilters.RedenUitschrijvingZoekFilter.SoortRedenUitschrijvingTonen;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.NT2Vaardigheid;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.security.checks.ComponentSecurityCheck;

public class DeelnemerUitgebreidZoekFilterPanel extends TypedPanel<VerbintenisZoekFilter>
{
	private static final long serialVersionUID = 1L;

	private DatumField peilEindDatumField;

	private DatumField peildatumField;

	public DeelnemerUitgebreidZoekFilterPanel(String id, VerbintenisZoekFilter filter,
			boolean openPanels)
	{
		super(id, new CompoundPropertyModel<VerbintenisZoekFilter>(filter));

		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));

		peilEindDatumField = new DatumField("peilEindDatum")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target, Date newValue)
			{
				super.onUpdate(target, newValue);
				if (newValue != null
					&& (getFilter().getPeildatum() == null || newValue.before(getFilter()
						.getPeildatum())))
				{
					getFilter().setPeildatum(newValue);
					target.addComponent(peildatumField);
				}
			}
		};
		add(peilEindDatumField);

		peildatumField = new DatumField("peildatum")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target, Date newValue)
			{
				super.onUpdate(target, newValue);
				getFilter().setPeilEindDatum(newValue);
				if (newValue == null)
				{
					info("Let op: geen (geldige) datum ingevoerd. De peildatum wordt niet meegenomen bij de zoekopdracht.");
				}
				((SecurePage) getPage()).refreshFeedback(target);
				target.addComponent(peilEindDatumField);
			}
		};
		add(peildatumField);
		add(new JaNeeCombobox("gearchiveerd").setNullValid(true));

		add(new PersonaliaPanel("personalia", getZoekFilterModel()).setLoadAjax(!openPanels));
		add(new AdresPanel("adres", getZoekFilterModel()).setLoadAjax(!openPanels));
		add(new OnderwijsPanel("verbintenis", getZoekFilterModel()).setLoadAjax(!openPanels));
		add(new ExamendeelnamePanel("examendeelname", getZoekFilterModel())
			.setLoadAjax(!openPanels));
		add(new IntakegesprekPanel("intakegesprek", getZoekFilterModel()).setLoadAjax(!openPanels));
		add(new VooropleidingPanel("vooropleiding", getZoekFilterModel()).setLoadAjax(!openPanels));
		add(new ContractPanel("contract", getZoekFilterModel()).setLoadAjax(!openPanels));
		add(new RelatiePanel("relatie", getZoekFilterModel()).setLoadAjax(!openPanels));
		add(new BPVPanel("bpv", getZoekFilterModel()).setLoadAjax(!openPanels));
		add(new InburgeringPanel("inburgering", getZoekFilterModel()).setLoadAjax(!openPanels));
		add(new VrijVeldPanel("vrijveld", getZoekFilterModel()).setLoadAjax(!openPanels));
	}

	public VerbintenisZoekFilter getFilter()
	{
		return getModelObject();
	}

	public IModel<VerbintenisZoekFilter> getZoekFilterModel()
	{
		return getModel();
	}

	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();
		openPanels();
	}

	private void openPanels()
	{
		visitChildren(CollapsablePanel.class, new IVisitor<CollapsablePanel< ? >>()
		{
			@Override
			public Object component(final CollapsablePanel< ? > panel)
			{
				panel.visitChildren(FormComponent.class, new IVisitor<FormComponent< ? >>()
				{
					@Override
					public Object component(FormComponent< ? > inputComponent)
					{
						if (inputComponent instanceof AbstractVrijVeldEntiteitPanel< ? >)
						{
							VrijVeldable< ? > vrijveldable =
								(VrijVeldable< ? >) inputComponent.getDefaultModelObject();
							for (VrijVeldEntiteit curEntiteit : vrijveldable.getVrijVelden())
							{
								if (curEntiteit.isIngevuld())
								{
									panel.open();
									return STOP_TRAVERSAL;
								}
							}
							return CONTINUE_TRAVERSAL_BUT_DONT_GO_DEEPER;
						}
						else if (inputComponent instanceof UitgebreidZoekMultipleChoice< ? >)
						{
							UitgebreidZoekMultipleChoice< ? > uitgebreidZoekMC =
								(UitgebreidZoekMultipleChoice< ? >) inputComponent;
							if (uitgebreidZoekMC.isActive())
							{
								panel.open();
								return STOP_TRAVERSAL;
							}
							return CONTINUE_TRAVERSAL_BUT_DONT_GO_DEEPER;

						}
						else if (inputComponent.getDefaultModelObject() != null)
						{
							if (inputComponent.getDefaultModelObject() instanceof Collection< ? >)
							{
								if (!((Collection< ? >) inputComponent.getDefaultModelObject())
									.isEmpty())
								{
									panel.open();
									return STOP_TRAVERSAL;
								}
							}
							else
							{
								panel.open();
								return STOP_TRAVERSAL;
							}
						}
						return CONTINUE_TRAVERSAL;
					}
				});
				return CONTINUE_TRAVERSAL_BUT_DONT_GO_DEEPER;
			}
		});
	}

	/**
	 * Panel om zoekpanel van deelnemer personalia te tonen
	 * 
	 * @author loite
	 */
	private final class PersonaliaPanel extends CollapsablePanel<VerbintenisZoekFilter>
	{
		private static final long serialVersionUID = 1L;

		public PersonaliaPanel(String id, IModel<VerbintenisZoekFilter> content)
		{
			super(id, content, "Personalia");
		}

		@Override
		protected void createContents()
		{
			add(new TextField<Long>("deelnemernummerVanaf", Long.class));
			add(new TextField<Long>("deelnemernummerTotEnMet", Long.class));
			add(new TextField<Long>("bsn", Long.class));
			add(new JaNeeCombobox("bsnIsLeeg").setNullValid(true));
			add(new TextField<Long>("onderwijsnummer", Long.class));
			add(new JaNeeCombobox("onderwijsnummerIsLeeg").setNullValid(true));
			add(new TextField<Long>("debiteurennummer", Long.class));
			add(new TextField<String>("achternaam"));
			add(new TextField<String>("aanspreeknaam"));
			add(new TextField<String>("officieelofaanspreek"));
			add(new TextField<String>("voorvoegsel"));
			add(new TextField<String>("officieleVoorvoegsel"));
			add(new TextField<String>("roepnaam"));
			add(new JaNeeCombobox("roepnaamIsLeeg").setNullValid(true));
			add(new TextField<String>("voornamen"));
			add(new JaNeeCombobox("voornamenIsLeeg").setNullValid(true));
			add(new TextField<String>("voorletters"));
			add(new JaNeeCombobox("voorlettersIsLeeg").setNullValid(true));
			add(new EnumCombobox<Geslacht>("geslacht", Geslacht.values()).setNullValid(true));

			add(new DatumField("geboortedatum"));
			add(new JaNeeCombobox("geboortedatumIsLeeg").setNullValid(true));
			add(new DatumField("geboortedatumVanaf"));
			add(new DatumField("geboortedatumTotEnMet"));
			add(new DatumField("registratieDatum"));
			add(new JaNeeCombobox("registratieDatumIsLeeg").setNullValid(true));
			add(new DatumField("registratieDatumVanaf"));
			add(new DatumField("registratieDatumTotEnMet"));
			add(new DatumField("deelnemerZoekFilter.startkwalificatieplichtigOpDatum"));
			add(new LandSearchEditor("geboorteland"));
			add(new JaNeeCombobox("geboortelandOngelijkAanNL").setNullValid(true));
			add(new JaNeeCombobox("geboortelandIsLeeg").setNullValid(true));
			add(new LandMultipleChoice("geboortelandList"));
			add(new NationaliteitSearchEditor("nationaliteit"));
			add(new NationaliteitMultipleChoice("nationaliteitList"));
			add(new JaNeeCombobox("nationaliteitOngelijkAanNL").setNullValid(true));
			add(new JaNeeCombobox("nationaliteitIsLeeg").setNullValid(true));
			add(new JaNeeCombobox("dubbeleNationaliteit").setNullValid(true));
			add(new GemeenteSearchEditor("geboorteGemeente"));

			add(new DatumField("datumInNL"));
			add(new JaNeeCombobox("datumInNLIsLeeg").setNullValid(true));
			add(new DatumField("datumInNLVanaf"));
			add(new DatumField("datumInNLTotEnMet"));

			add(new JaNeeCombobox("allochtoon").setNullValid(true));
			add(new JaNeeCombobox("lgf").setNullValid(true));
			add(new JaNeeCombobox("nieuwkomer").setNullValid(true));
			add(new JaNeeCombobox("meerderjarig").setNullValid(true));
			add(new JaNeeCombobox("vertegenwoordiger").setNullValid(true));
			add(new JaNeeCombobox("betalingsplichtige").setNullValid(true));
			add(new JaNeeCombobox("overleden").setNullValid(true));

			add(new EnumCombobox<TypeContactgegeven>("typeContactgegeven", TypeContactgegeven
				.values()).setNullValid(true));
			add(new TextField<String>("contactgegeven"));
			add(new DocumentTypeMultipleChoice("documentTypeList",
				new PropertyModel<Collection<DocumentType>>(getModel(), "documentTypeList")));
			add(new DocumentCategorieMultipleChoice("documentCategorieList",
				new PropertyModel<Collection<DocumentCategorie>>(getModel(),
					"documentCategorieList")));
		}
	}

	/**
	 * Panel om zoekpanel van vrije velden te tonen
	 * 
	 * @author loite
	 */
	private final class VrijVeldPanel extends CollapsablePanel<VerbintenisZoekFilter>
	{
		private static final long serialVersionUID = 1L;

		public VrijVeldPanel(String id, IModel<VerbintenisZoekFilter> content)
		{
			super(id, content, "Vrije velden");
		}

		@Override
		protected void createContents()
		{
			List<KRDModuleComponentFactory> factories =
				EduArteApp.get().getPanelFactories(KRDModuleComponentFactory.class);

			if (factories.size() > 0)
			{
				KRDModuleComponentFactory factory = factories.get(0);

				AbstractVrijVeldEntiteitPanel<BPVInschrijvingZoekFilter> bpvveldenpanel =
					factory.newVrijVeldEntiteitEditPanel("bpvvelden",
						new PropertyModel<BPVInschrijvingZoekFilter>(getZoekFilterModel(),
							"bpvInschrijvingZoekFilter"), "Contract");
				bpvveldenpanel.getVrijVeldZoekFilter().setUitgebreidZoekenScherm(true);
				bpvveldenpanel.getVrijVeldZoekFilter().setCategorie(
					VrijVeldCategorie.BPV_INSCHRIJVING);
				add(bpvveldenpanel);

				AbstractVrijVeldEntiteitPanel<ContractZoekFilter> contractveldenpanel =
					factory.newVrijVeldEntiteitEditPanel("contractvelden",
						new PropertyModel<ContractZoekFilter>(getZoekFilterModel(),
							"contractZoekFilter"), "Contract");
				contractveldenpanel.getVrijVeldZoekFilter().setUitgebreidZoekenScherm(true);
				contractveldenpanel.getVrijVeldZoekFilter()
					.setCategorie(VrijVeldCategorie.CONTRACT);
				add(contractveldenpanel);

				AbstractVrijVeldEntiteitPanel<GroepZoekFilter> groepveldenpanel =
					factory
						.newVrijVeldEntiteitEditPanel("groepvelden",
							new PropertyModel<GroepZoekFilter>(getZoekFilterModel(),
								"groepZoekFilter"), "Groep");
				groepveldenpanel.getVrijVeldZoekFilter().setUitgebreidZoekenScherm(true);
				groepveldenpanel.getVrijVeldZoekFilter().setCategorie(VrijVeldCategorie.GROEP);
				add(groepveldenpanel);

				AbstractVrijVeldEntiteitPanel<IntakegesprekZoekFilter> intakeveldenpanel =
					factory.newVrijVeldEntiteitEditPanel("intakevelden",
						new PropertyModel<IntakegesprekZoekFilter>(getZoekFilterModel(),
							"intakegesprekZoekFilter"), "Intake");
				intakeveldenpanel.getVrijVeldZoekFilter().setUitgebreidZoekenScherm(true);
				intakeveldenpanel.getVrijVeldZoekFilter().setCategorie(VrijVeldCategorie.INTAKE);
				add(intakeveldenpanel);

				AbstractVrijVeldEntiteitPanel<OnderwijsproductZoekFilter> onderwijsproductpanel =
					factory.newVrijVeldEntiteitEditPanel("onderwijsproduct",
						new PropertyModel<OnderwijsproductZoekFilter>(getZoekFilterModel(),
							"onderwijsproductZoekFilter"), "Onderwijsproduct");
				onderwijsproductpanel.getVrijVeldZoekFilter().setUitgebreidZoekenScherm(true);
				onderwijsproductpanel.getVrijVeldZoekFilter().setCategorie(
					VrijVeldCategorie.ONDERWIJSPRODUCT);
				add(onderwijsproductpanel);

				AbstractVrijVeldEntiteitPanel<OpleidingZoekFilter> opleidingpanel =
					factory.newVrijVeldEntiteitEditPanel("opleiding",
						new PropertyModel<OpleidingZoekFilter>(getZoekFilterModel(),
							"opleidingZoekFilter"), "Opleiding");
				opleidingpanel.getVrijVeldZoekFilter().setUitgebreidZoekenScherm(true);
				opleidingpanel.getVrijVeldZoekFilter().setCategorie(VrijVeldCategorie.OPLEIDING);
				add(opleidingpanel);

				AbstractVrijVeldEntiteitPanel<DeelnemerZoekFilter> personaliaveldenpanel =
					factory.newVrijVeldEntiteitEditPanel("personaliavelden",
						new PropertyModel<DeelnemerZoekFilter>(getZoekFilterModel(),
							"deelnemerZoekFilter"), "Personalia");
				personaliaveldenpanel.getVrijVeldZoekFilter().setUitgebreidZoekenScherm(true);
				personaliaveldenpanel.getVrijVeldZoekFilter().setCategorie(
					VrijVeldCategorie.DEELNEMERPERSONALIA);
				add(personaliaveldenpanel);

				AbstractVrijVeldEntiteitPanel<PlaatsingZoekFilter> plaatsingveldenpanel =
					factory.newVrijVeldEntiteitEditPanel("plaatsingvelden",
						new PropertyModel<PlaatsingZoekFilter>(getZoekFilterModel(),
							"plaatsingZoekFilter"), "Plaatsing");
				plaatsingveldenpanel.getVrijVeldZoekFilter().setUitgebreidZoekenScherm(true);
				plaatsingveldenpanel.getVrijVeldZoekFilter().setCategorie(
					VrijVeldCategorie.PLAATSING);
				add(plaatsingveldenpanel);

				AbstractVrijVeldEntiteitPanel<RelatieZoekFilter> relatieveldenpanel =
					factory.newVrijVeldEntiteitEditPanel("relatievelden",
						new PropertyModel<RelatieZoekFilter>(getZoekFilterModel(),
							"relatieZoekFilter"), "Relatie");
				relatieveldenpanel.getVrijVeldZoekFilter().setUitgebreidZoekenScherm(true);
				relatieveldenpanel.getVrijVeldZoekFilter().setCategorie(VrijVeldCategorie.RELATIE);
				add(relatieveldenpanel);

				AbstractVrijVeldEntiteitPanel<UitschrijvingZoekFilter> uitschrijvingveldenpanel =
					factory.newVrijVeldEntiteitEditPanel("uitschrijvingvelden",
						new PropertyModel<UitschrijvingZoekFilter>(getZoekFilterModel(),
							"uitschrijvingZoekFilter"), "Uitschrijving");
				uitschrijvingveldenpanel.getVrijVeldZoekFilter().setUitgebreidZoekenScherm(true);
				uitschrijvingveldenpanel.getVrijVeldZoekFilter().setCategorie(
					VrijVeldCategorie.UITSCHRIJVING);
				add(uitschrijvingveldenpanel);

				AbstractVrijVeldEntiteitPanel<VerbintenisZoekFilter> verbintenisveldenpanel =
					factory.newVrijVeldEntiteitEditPanel("verbintenisvelden", getZoekFilterModel(),
						"Verbintenis");
				verbintenisveldenpanel.getVrijVeldZoekFilter().setUitgebreidZoekenScherm(true);
				verbintenisveldenpanel.getVrijVeldZoekFilter().setCategorie(
					VrijVeldCategorie.VERBINTENIS);
				add(verbintenisveldenpanel);

				AbstractVrijVeldEntiteitPanel<VooropleidingZoekFilter> vooropleidingveldenpanel =
					factory.newVrijVeldEntiteitEditPanel("vooropleidingvelden",
						new PropertyModel<VooropleidingZoekFilter>(getZoekFilterModel(),
							"vooropleidingZoekFilter"), "Vooropleiding");
				vooropleidingveldenpanel.getVrijVeldZoekFilter().setUitgebreidZoekenScherm(true);
				vooropleidingveldenpanel.getVrijVeldZoekFilter().setCategorie(
					VrijVeldCategorie.VOOROPLEIDING);
				add(vooropleidingveldenpanel);
			}
		}
	}

	/**
	 * Panel om zoekpanel van deelnemer adres te tonen
	 * 
	 * @author loite
	 */
	private final class AdresPanel extends CollapsablePanel<VerbintenisZoekFilter>
	{
		private static final long serialVersionUID = 1L;

		public AdresPanel(String id, IModel<VerbintenisZoekFilter> content)
		{
			super(id, content, "Adres");
		}

		@Override
		protected void createContents()
		{
			add(new EnumCombobox<TypeAdres>("typeAdres", TypeAdres.values()).setNullValid(true));
			add(new JaNeeCombobox("geheimAdres").setNullValid(true));
			add(new TextField<String>("postcode"));
			add(new TextField<String>("huisnummer"));
			add(new TextField<String>("huisnummerToevoeging"));
			add(new TextField<String>("straat"));
			add(new TextField<String>("postcodeVanaf"));
			add(new TextField<String>("postcodeTotEnMet"));
			add(new PlaatsSearchEditor("plaats", new PropertyModel<String>(getModel(), "plaats")));
			add(new TextField<String>("plaatsOngelijkAan"));
			add(new GemeenteSearchEditor("gemeente"));
			add(new GemeenteSearchEditor("gemeenteOngelijkAan"));
			add(new ProvincieSearchEditor("provincie"));
			add(new ProvincieSearchEditor("provincieOngelijkAan"));
			add(new LandSearchEditor("land"));
			add(new LandSearchEditor("landOngelijkAan"));
		}
	}

	private final class IntakegesprekPanel extends CollapsablePanel<VerbintenisZoekFilter>
	{
		private static final long serialVersionUID = 1L;

		public IntakegesprekPanel(String id, IModel<VerbintenisZoekFilter> content)
		{
			super(id, content, "Intakegesprek");
		}

		@Override
		protected void createContents()
		{
			add(new EnumCombobox<IntakegesprekStatus>("intakegesprekStatus", IntakegesprekStatus
				.values()).setNullValid(true));
			add(new DatumField("datumIntakegesprekVanaf"));
			add(new DatumField("datumIntakegesprekTotEnMet"));
			add(new MedewerkerSearchEditor("intaker", null));
			add(new MedewerkerMultipleChoice("intakerList",
				new PropertyModel<Collection<Medewerker>>(getModel(), "intakerList")));
			add(new OpleidingSearchEditor("gewensteOpleiding", (IModel<Opleiding>) null));
			add(new OpleidingMultipleChoice("gewensteOpleidingList",
				new PropertyModel<Collection<Opleiding>>(getModel(), "gewensteOpleidingList")));
			add(new LocatieCombobox("gewensteLocatie").setNullValid(true));
			LocatieMultipleChoice locatieList = new LocatieMultipleChoice("gewensteLocatieList");
			locatieList.setChoices(new LocatiesPageModel(new ComponentSecurityCheck(locatieList),
				null));
			add(locatieList);
			add(new DatumField("gewensteBegindatumVanaf"));
			add(new DatumField("gewensteBegindatumTotEnMet"));
			add(new DatumField("gewensteEinddatumVanaf"));
			add(new DatumField("gewensteEinddatumTotEnMet"));
		}
	}

	private final class VooropleidingPanel extends CollapsablePanel<VerbintenisZoekFilter>
	{
		private static final long serialVersionUID = 1L;

		public VooropleidingPanel(String id, IModel<VerbintenisZoekFilter> content)
		{
			super(id, content, "Vooropleiding");
		}

		@Override
		protected void createContents()
		{
			ExterneOrganisatieZoekFilter filter = new ExterneOrganisatieZoekFilter();
			filter.setBijVooropleiding(true);
			add(new ExterneOrganisatieSearchEditor("vooropleidingExterneOrganisatie", null, filter));
			add(new TextField<String>("naamOnderwijsinstellingVooropleiding"));
			add(new EnumCombobox<SoortOnderwijs>("categorieVooropleiding", SoortOnderwijs.values())
				.setNullValid(true));
			add(new SoortVooropleidingCombobox("soortVooropleiding").setNullValid(true));
			add(new DatumField("vooropleidingBegindatumVanaf"));
			add(new DatumField("vooropleidingBegindatumTotEnMet"));
			add(new DatumField("vooropleidingEinddatumVanaf"));
			add(new DatumField("vooropleidingEinddatumTotEnMet"));
			add(new TextField<Integer>("vooropleidingAantalJarenOnderwijsVanaf", Integer.class));
			add(new TextField<Integer>("vooropleidingAantalJarenOnderwijsTotEnMet", Integer.class));
			add(new JaNeeCombobox("vooropleidingAantalJarenOnderwijsIsLeeg").setNullValid(true));
			add(new JaNeeCombobox("vooropleidingDiplomaBehaald").setNullValid(true));
			add(new SchooladviesComboBox("vooropleidingSchooladvies"));
			add(new TextField<Integer>("vooropleidingCitoscore", Integer.class));
		}
	}

	private final class ExamendeelnamePanel extends CollapsablePanel<VerbintenisZoekFilter>
	{
		private static final long serialVersionUID = 1L;

		public ExamendeelnamePanel(String id, IModel<VerbintenisZoekFilter> content)
		{
			super(id, content, "Examendeelname");
		}

		@Override
		protected void createContents()
		{
			ExamenWorkflowCombobox workflowCombobox = new ExamenWorkflowCombobox("examenWorkflow");
			workflowCombobox.setNullValid(true);
			ExamenstatusCombobox statusCombobox =
				new ExamenstatusCombobox("examenstatus", new PropertyModel<Examenstatus>(
					getZoekFilterModel(), "examenstatus"), new PropertyModel<ExamenWorkflow>(
					getZoekFilterModel(), "examenWorkflow"));
			statusCombobox.setNullValid(true);
			workflowCombobox.connectListForAjaxRefresh(statusCombobox);
			add(workflowCombobox);
			add(statusCombobox);
			add(new ExamenstatusMultipleChoice("examenstatusList"));
			add(new DatumField("datumUitslagVanaf"));
			add(new DatumField("datumUitslagTotEnMet"));
			add(new JaNeeCombobox("datumUitslagIsLeeg").setNullValid(true));
			add(new TextField<Integer>("examenjaar", Integer.class));
			add(new TextField<Integer>("examennummerVanaf", Integer.class));
			add(new TextField<Integer>("examennummerTotEnMet", Integer.class));
			add(new JaNeeCombobox("examendeelnameBekostigd").setNullValid(true));
			add(new JaNeeCombobox("examendeelnameTijdvakIsLeeg").setNullValid(true));
			add(new TextField<Integer>("examendeelnameTijdvakVanaf", Integer.class));
			add(new TextField<Integer>("examendeelnameTijdvakTotEnMet", Integer.class));
		}
	}

	/**
	 * Panel om zoekpanel van deelnemer registraties te tonen
	 * 
	 * @author loite
	 */
	private final class OnderwijsPanel extends CollapsablePanel<VerbintenisZoekFilter>
	{
		private static final long serialVersionUID = 1L;

		public OnderwijsPanel(String id, IModel<VerbintenisZoekFilter> content)
		{
			super(id, content, "Verbintenis");
		}

		@Override
		protected void createContents()
		{
			add(new SecureOrganisatieEenheidLocatieUitgebreidZoekenChoicePanel<VerbintenisZoekFilter>(
				"organisatieEenheidLocatie", getZoekFilterModel()).setRenderBodyOnly(false));
			OrganisatieEenheidMultipleChoice orgEhdList =
				new OrganisatieEenheidMultipleChoice("organisatieEenheidList");
			add(orgEhdList);
			LocatieMultipleChoice locatieList = new LocatieMultipleChoice("locatieList");
			locatieList.setChoices(new LocatiesPageModel(new ComponentSecurityCheck(locatieList),
				null));
			add(locatieList);
			TaxonomieCombobox taxonomie = new TaxonomieCombobox("taxonomie");
			taxonomie.setNullValid(true);
			add(taxonomie);
			add(new TaxonomieMultipleChoice("taxonomieList"));
			TaxonomieElementTypeCombobox taxonomieElementType =
				new TaxonomieElementTypeCombobox("taxonomieElementType", taxonomie, false);
			taxonomieElementType.setNullValid(true);
			add(taxonomieElementType);
			taxonomie.connectListForAjaxRefresh(taxonomieElementType);
			TaxonomieElementSearchEditor verbintenisgebied =
				new TaxonomieElementSearchEditor("verbintenisgebied",
					new TaxonomieElementZoekFilter(Verbintenisgebied.class));
			add(verbintenisgebied);
			add(new VerbintenisgebiedMultipleChoice("verbintenisgebiedList",
				new PropertyModel<Collection<Verbintenisgebied>>(getModel(),
					"verbintenisgebiedList")));
			TaxonomieElementSearchEditor verbintenisgebiedOngelijkAan =
				new TaxonomieElementSearchEditor("verbintenisgebiedOngelijkAan",
					new TaxonomieElementZoekFilter(Verbintenisgebied.class));
			add(verbintenisgebiedOngelijkAan);
			OpleidingZoekFilter opleidingFilter = OpleidingZoekFilter.createDefaultFilter();
			opleidingFilter.setLocatieModel(new PropertyModel<Locatie>(getZoekFilterModel(),
				"locatie"));
			opleidingFilter.setOrganisatieEenheidModel(new PropertyModel<OrganisatieEenheid>(
				getZoekFilterModel(), "organisatieEenheid"));
			add(new OpleidingSearchEditor("opleiding", null, opleidingFilter));
			OpleidingZoekFilter opleidingOngelijkFilter = OpleidingZoekFilter.createDefaultFilter();
			opleidingOngelijkFilter.setLocatieModel(new PropertyModel<Locatie>(
				getZoekFilterModel(), "locatie"));
			opleidingOngelijkFilter
				.setOrganisatieEenheidModel(new PropertyModel<OrganisatieEenheid>(
					getZoekFilterModel(), "organisatieEenheid"));
			add(new OpleidingSearchEditor("opleidingOngelijkAan", null, opleidingOngelijkFilter));
			add(new TeamMultipleChoice("teamList", new PropertyModel<Collection<Team>>(getModel(),
				"teamList")));
			add(new OpleidingMultipleChoice("opleidingList",
				new PropertyModel<Collection<Opleiding>>(getModel(), "opleidingList")));
			add(new ListMultipleChoice<VerbintenisStatus>("verbintenisStatusList", Arrays
				.asList(VerbintenisStatus.values())));
			add(new ListMultipleChoice<Intensiteit>("intensiteitList", Arrays.asList(Intensiteit
				.values())));

			add(new TextField<String>("opleidingscode"));
			add(new EnumCombobox<MBOLeerweg>("leerweg", MBOLeerweg.values()).setNullValid(true));
			add(new JaNeeCombobox("indicatieGehandicapt").setNullValid(true));
			add(new EnumCombobox<Bekostigd>("bekostigd", Bekostigd.values()).setNullValid(true));
			add(new TextField<Integer>("contacturenPerWeek", Integer.class));
			add(new EnumCombobox<Vertrekstatus>("vertrekstatus", Vertrekstatus.values())
				.setNullValid(true));
			add(new CohortCombobox("cohort", new PropertyModel<Cohort>(getZoekFilterModel(),
				"cohort")));
			SoortVooropleidingSearchEditor soortVooropleiding =
				new SoortVooropleidingSearchEditor("relevanteVooropleidingSoort");
			add(soortVooropleiding);
			add(new JaNeeCombobox("relevanteVooropleidingSoortLeeg").setNullValid(true));
			OnderwijsproductSearchEditor onderwijsproduct =
				new OnderwijsproductSearchEditor("afgenomenOnderwijsproduct",
					new PropertyModel<Onderwijsproduct>(getZoekFilterModel(),
						"afgenomenOnderwijsproduct"));
			add(onderwijsproduct);
			add(new DatumField("actiefVanaf"));
			add(new DatumField("actiefTotEnMet"));
			add(new DatumField("datumInschrijvingVanaf"));
			add(new DatumField("datumInschrijvingTotEnMet"));
			add(new DatumField("datumUitschrijvingVanaf"));
			add(new DatumField("datumUitschrijvingTotEnMet"));
			add(new RedenUitschrijvingComboBox("redenUitschrijving",
				SoortRedenUitschrijvingTonen.Verbintenis).setNullValid(true));
			add(new RedenUitschrijvingMultipleChoice("redenUitschrijvingList",
				new PropertyModel<Collection<RedenUitschrijving>>(getModel(),
					"redenUitschrijvingList")));
			add(new JaNeeCombobox("deelnemerZoekFilter.heeftMeerdereVerbintenissen")
				.setNullValid(true));
			add(new DatumField("datumGeplandEindeVanaf"));
			add(new DatumField("datumGeplandEindeTotEnMet"));
			GroepstypeCombobox groepstypeCombo = new GroepstypeCombobox("groepstype");
			groepstypeCombo.setNullValid(true);
			add(groepstypeCombo);
			GroepZoekFilter groepFilter = GroepZoekFilter.createDefaultFilter();
			groepFilter.setLocatieModel(new PropertyModel<Locatie>(getModel(), "locatie"));
			groepFilter.setOrganisatieEenheidModel(new PropertyModel<OrganisatieEenheid>(
				getModel(), "organisatieEenheid"));
			groepFilter.setTypeModel(new PropertyModel<Groepstype>(getModel(), "groepstype"));
			GroepSearchEditor groep =
				new GroepSearchEditor("groep", null, GroepZoekFilter.createDefaultFilter());
			add(groep);
			add(new GroepMultipleChoice("groepList", new PropertyModel<Collection<Groep>>(
				getModel(), "groepList")));
			add(new TextField<Integer>("leerjaar", Integer.class));
			// LWOO
			add(new JaNeeCombobox("indicatieLWOO").setNullValid(true));
			add(new TextField<Integer>("praktijkjaar", Integer.class));
			KenmerkCategorieCombobox kenmerkCategorieCombo =
				new KenmerkCategorieCombobox("kenmerkCategorie");
			add(kenmerkCategorieCombo.setNullValid(true));
			KenmerkCombobox kenmerkCombo =
				new KenmerkCombobox("kenmerk", new PropertyModel<KenmerkCategorie>(getModel(),
					"kenmerkCategorie"));
			add(kenmerkCombo.setNullValid(true));
			kenmerkCategorieCombo.connectListForAjaxRefresh(kenmerkCombo);
			add(new EnumCombobox<MBONiveau>("niveau", MBONiveau.values()).setNullValid(true));

			add(new ListMultipleChoice<BronEntiteitStatus>("bronStatusList", Arrays
				.asList(BronEntiteitStatus.values())));
		}
	}

	/**
	 * Panel voor zoekopties van contracten
	 * 
	 * @author loite
	 */
	private final class ContractPanel extends CollapsablePanel<VerbintenisZoekFilter>
	{
		private static final long serialVersionUID = 1L;

		public ContractPanel(String id, IModel<VerbintenisZoekFilter> content)
		{
			super(id, content, "Contract");
		}

		@Override
		protected void createContents()
		{
			SoortContractMultipleChoice soortContract =
				new SoortContractMultipleChoice("soortContractList");
			add(soortContract);
			TypeFinancieringCombobox typeFinanciering =
				new TypeFinancieringCombobox("typeFinanciering");
			typeFinanciering.setNullValid(true);
			add(typeFinanciering);
			final ContractOnderdeelMultipleChoice contractOnderdeel =
				new ContractOnderdeelMultipleChoice("contractOnderdeelList",
					new PropertyModel<Collection<ContractOnderdeel>>(getModel(),
						"contractOnderdeelList"), new ContractOnderdelenListModel(
						new PropertyModel<Contract>(getModel(), "contract")));
			contractOnderdeel.setOutputMarkupId(true);
			add(contractOnderdeel);
			ContractZoekFilter contractFilter = new ContractZoekFilter();
			contractFilter.setSoortContractModel(new PropertyModel<SoortContract>(getModel(),
				"soortContract"));
			contractFilter.setTypeFinancieringModel(new PropertyModel<TypeFinanciering>(getModel(),
				"typeFinanciering"));
			ContractSearchEditor contract =
				new ContractSearchEditor("contract", new PropertyModel<Contract>(getModel(),
					"contract"), contractFilter);
			contract.addListener(new ISelectListener()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onUpdate(AjaxRequestTarget target)
				{
					target.addComponent(contractOnderdeel);
				}

			});
			add(contract);
			add(new ExterneOrganisatieSearchEditor("contractExterneOrganisatie", null));
			add(new ExterneOrganisatieContactPersoonSearchEditor("contractContactpersoon", null,
				new PropertyModel<ExterneOrganisatie>(getZoekFilterModel(),
					"contractExterneOrganisatie")));
			add(new MedewerkerSearchEditor("contractBeheerder", null));
			add(new TextField<BigDecimal>("contractKostprijsVanaf", BigDecimal.class));
			add(new TextField<BigDecimal>("contractKostprijsTotEnMet", BigDecimal.class));
			add(new JaNeeCombobox("contractInburgering").setNullValid(true));
			add(new EnumCombobox<Onderaanneming>("contractOnderaanneming", Onderaanneming.values())
				.setNullValid(true));
			add(new ContractMultipleChoice("contractList", new PropertyModel<Collection<Contract>>(
				getModel(), "contractList")));
			// add(new
			// ExterneOrganisatieMultipleChoice("contractExterneOrganisatieList"));
		}
	}

	private final class RelatiePanel extends CollapsablePanel<VerbintenisZoekFilter>
	{

		private static final long serialVersionUID = 1L;

		public RelatiePanel(String id, IModel<VerbintenisZoekFilter> content)
		{
			super(id, content, "Relatie");
		}

		@Override
		protected void createContents()
		{
			add(new TextField<Long>("relatieBsn", Long.class));
			add(new TextField<Long>("relatieDebiteurenNummer", Long.class));
			add(new TextField<String>("relatieAchternaam"));
			add(new EnumCombobox<Geslacht>("relatieGeslacht", Geslacht.values()).setNullValid(true));
			add(new EnumCombobox<TypeAdres>("relatieTypeAdres", TypeAdres.values())
				.setNullValid(true));
			add(new JaNeeCombobox("relatieZelfdeAdresAlsDeelnemer").setNullValid(true));
			add(new TextField<String>("relatiePostcode"));
			add(new TextField<String>("relatieStraat"));
			add(new TextField<String>("relatieHuisnummer"));
			add(new TextField<String>("relatieHuisnummerToevoeging"));
			add(new TextField<String>("relatiePlaats"));
			add(new RelatieSoortCombobox("relatieSoort", new PropertyModel<RelatieSoort>(
				getModel(), "relatieSoort")).setNullValid(true));
			add(new ExterneOrganisatieSearchEditor("relatieExterneOrganisatie",
				new PropertyModel<ExterneOrganisatie>(getModel(),
					"relatieZoekFilter.externeOrganisatie")));
			add(new JaNeeCombobox("relatieGeboortelandOngelijkAanNL").setNullValid(true));
			add(new JaNeeCombobox("relatieNationaliteitOngelijkAanNL").setNullValid(true));
		}
	}

	private final class BPVPanel extends CollapsablePanel<VerbintenisZoekFilter>
	{

		private static final long serialVersionUID = 1L;

		public BPVPanel(String id, IModel<VerbintenisZoekFilter> content)
		{
			super(id, content, "BPV");
		}

		@Override
		protected void createContents()
		{
			add(new ListMultipleChoice<BPVStatus>("bpvStatusList", Arrays
				.asList(BPVStatus.values())));
			add(new KenniscentrumCombobox("bpvKenniscentrum").setNullValid(true));
			add(new JaNeeCombobox("bpvKenniscentrumLeeg").setNullValid(true));
			ExterneOrganisatieZoekFilter bpvFilter = new ExterneOrganisatieZoekFilter();
			bpvFilter.addOrderByProperty("naam");
			bpvFilter.setBpvBedrijf(true);
			add(new ExterneOrganisatieSearchEditor("bpvBedrijf", null, bpvFilter));
			add(new ExterneOrganisatieSearchEditor("bpvContractpartner", null));
			add(new EnumCombobox<BronEntiteitStatus>("bpvBronStatus", BronEntiteitStatus.values())
				.setNullValid(true));
			add(new RedenUitschrijvingComboBox("bpvRedenBeeindiging",
				SoortRedenUitschrijvingTonen.BPV).setNullValid(true));
			add(new MedewerkerSearchEditor("praktijkBegeleider", null));
			ExterneOrganisatieContactPersoonZoekFilter filter =
				new ExterneOrganisatieContactPersoonZoekFilter();
			filter.setPraktijkopleiderBPV(true);
			add(new ExterneOrganisatieContactPersoonSearchEditor("praktijkopleiderBPVBedrijf",
				null, filter));
			add(new JaNeeCombobox("praktijkBegeleiderLeeg").setNullValid(true));
			add(new JaNeeCombobox("praktijkopleiderBPVBedrijfLeeg").setNullValid(true));
			add(new DatumField("bpvBeginDatum"));
			add(new DatumField("bpvEindDatum"));
			add(new TextField<Integer>("bpvOmvangVanaf", Integer.class));
			add(new TextField<Integer>("bpvOmvangTotEnMet", Integer.class));
			add(new TextField<BigDecimal>("bpvUrenPerWeekVanaf", BigDecimal.class));
			add(new TextField<BigDecimal>("bpvUrenPerWeekTotEnMet", BigDecimal.class));
			add(new TextField<Integer>("bpvDagenPerWeekVanaf", Integer.class));
			add(new TextField<Integer>("bpvDagenPerWeekTotEnMet", Integer.class));

		}
	}

	private final class InburgeringPanel extends CollapsablePanel<VerbintenisZoekFilter>
	{
		private static final long serialVersionUID = 1L;

		public InburgeringPanel(String id, IModel<VerbintenisZoekFilter> content)
		{
			super(id, content, "Inburgering");
		}

		@Override
		protected void createContents()
		{
			add(new EnumCombobox<RedenInburgering>("redenInburgering", RedenInburgering.values())
				.setNullValid(true));
			add(new ListMultipleChoice<ProfielInburgering>("profielInburgeringList", Arrays
				.asList(ProfielInburgering.values())));
			add(new ListMultipleChoice<Leerprofiel>("leerprofielList", Arrays.asList(Leerprofiel
				.values())));
			add(new JaNeeCombobox("deelcursus").setNullValid(true));
			add(new ListMultipleChoice<SoortPraktijkexamen>("soortPraktijkexamenList", Arrays
				.asList(SoortPraktijkexamen.values())));
			add(new DatumField("datumAanmeldenVanaf"));
			add(new DatumField("datumAanmeldenTotEnMet"));
			add(new DatumField("datumAkkoordVanaf"));
			add(new DatumField("datumAkkoordTotEnMet"));
			add(new DatumField("datumEersteActiviteitVanaf"));
			add(new DatumField("datumEersteActiviteitTotEnMet"));
			add(new ListMultipleChoice<NT2Vaardigheid>("beginNiveauSchriftelijkeVaardighedenList",
				Arrays.asList(NT2Vaardigheid.values())));
			add(new ListMultipleChoice<NT2Vaardigheid>("eindNiveauSchriftelijkeVaardighedenList",
				Arrays.asList(NT2Vaardigheid.values())));
		}
	}
}
