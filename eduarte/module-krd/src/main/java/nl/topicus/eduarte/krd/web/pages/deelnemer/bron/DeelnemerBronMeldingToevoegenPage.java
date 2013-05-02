/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.web.pages.deelnemer.bron;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.choice.AjaxRadioChoice;
import nl.topicus.cobra.web.components.datapanel.CollapsableGroupRowFactoryDecorator;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.eduarte.entities.Entiteit;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.inschrijving.Bekostigingsperiode;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.krd.bron.BronController;
import nl.topicus.eduarte.krd.principals.beheer.bron.BronOverzichtWrite;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.BronBPVGegevensBOSelecterenPanel;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.BronBekostigingsperiodeBOSelecterenPanel;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.BronNT2ResultaatEDSelecteerPanel;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.BronVakgegevensEDSelecteerPanel;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.overzichten.DeelnemerBronPage;
import nl.topicus.eduarte.krd.web.validators.BronValidator;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.providers.VerbintenisProvider;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.VerbintenisPlaatsingBronTable;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.web.pages.deelnemer.verbintenis.VerbintenisMetPlaatsingenBronDataProvider;
import nl.topicus.onderwijs.duo.bron.BronException;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.BronBveAanleverRecord;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * @author idserda
 */
@PageInfo(title = "Bron melding", menu = "Deelnemer > Bron")
@InPrincipal(BronOverzichtWrite.class)
public class DeelnemerBronMeldingToevoegenPage extends AbstractDeelnemerPage implements
		VerbintenisProvider
{
	private Form<Void> form;

	private List<Class< ? >> recordTypesBO = new ArrayList<Class< ? >>();

	private List<Class< ? >> recordTypesBOZonderPeriode = new ArrayList<Class< ? >>();

	private List<Class< ? >> recordTypesED = new ArrayList<Class< ? >>();

	private List<Class< ? >> recordTypesVAVO = new ArrayList<Class< ? >>();

	private List<Class< ? >> recordTypesVO = new ArrayList<Class< ? >>();

	private List<Class< ? >> soortGegevensList;

	private Class< ? extends Enum< ? >> soortMutatieClass;

	private IModel<HandmatigeBronMelding> meldingmodel;

	private RadioChoice<Class< ? >> soortGegevensChoice;

	private WebMarkupContainer soortGegevensContainer;

	private RadioChoice<Enum< ? >> soortMutatieChoice;

	private WebMarkupContainer soortMutatieContainer;

	private BronVakgegevensEDSelecteerPanel subselectVakgegevensED;

	private BronBPVGegevensBOSelecterenPanel subselectBPVGegevensBO;

	private BronBekostigingsperiodeBOSelecterenPanel subselectBekostigingsperiodeBO;

	private BronNT2ResultaatEDSelecteerPanel subselectNT2ResultaatED;

	private RadioGroup<IdObject> geselecteerdeVerbintenisOfPlaatsing;

	public DeelnemerBronMeldingToevoegenPage(DeelnemerProvider deelnemer)
	{
		this(deelnemer.getDeelnemer());
	}

	public DeelnemerBronMeldingToevoegenPage(Deelnemer deelnemer)
	{
		this(deelnemer, deelnemer.getEersteInschrijvingOpPeildatum());
	}

	public DeelnemerBronMeldingToevoegenPage(Deelnemer deelnemer, Verbintenis verbintenis)
	{
		super(DeelnemerMenuItem.Bron, deelnemer, verbintenis);

		meldingmodel = new Model<HandmatigeBronMelding>(new HandmatigeBronMelding());

		form = createForm();

		geselecteerdeVerbintenisOfPlaatsing = getGeselecteerdeVerbintenisOfPlaatsingRadioGroup();
		geselecteerdeVerbintenisOfPlaatsing.add(new BronValidator<IdObject>(this));
		geselecteerdeVerbintenisOfPlaatsing.add(createVerbintenisPlaatsingDatapanel());

		setupRecordTypes();

		createSoortMutatieChoice();
		createSoortGegevensChoice();

		createSubselectVakgegevensED();
		createSubselectBPVGegevensBO();
		createSubselectBekostiginsperiodeBO();
		createSubselectNT2ResultaatED();

		addValidators();

		form.add(geselecteerdeVerbintenisOfPlaatsing);

		add(form);
		createComponents();
	}

	private RadioGroup<IdObject> getGeselecteerdeVerbintenisOfPlaatsingRadioGroup()
	{
		return new RadioGroup<IdObject>("gewijzigdeVerbintenisOfPlaatsing",
			new PropertyModel<IdObject>(meldingmodel, "gewijzigdeVerbintenisOfPlaatsing"));
	}

	private CustomDataPanel<IdObject> createVerbintenisPlaatsingDatapanel()
	{
		IDataProvider<IdObject> dataprovider =
			new VerbintenisMetPlaatsingenBronDataProvider(getContextDeelnemerModel());

		CollapsableGroupRowFactoryDecorator<IdObject> rowFactory =
			new CollapsableGroupRowFactoryDecorator<IdObject>(
				new CustomDataPanelRowFactory<IdObject>());

		VerbintenisPlaatsingBronTable verbintenisPlaatsingBronTable =
			new VerbintenisPlaatsingBronTable(rowFactory)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onChoiceUpdate(AjaxRequestTarget target,
						IModel<IdObject> selectedModel)
				{
					updateNaVerbintenisOfPlaatsingSelect(target, selectedModel);
				}
			};

		EduArteDataPanel<IdObject> datapanel =
			new EduArteDataPanel<IdObject>("datapanel", dataprovider, verbintenisPlaatsingBronTable)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected IModel<String> createTitleModel(String title)
				{
					return Model
						.of("Stap 1. Selecteer de verbintenis (BO en ED) of de plaatsing (VO en VAVO)");
				}

				@Override
				protected String getGeengegevensTekst()
				{
					return "Er zijn geen BRON communiceerbare verbintenissen gevonden.";
				}
			};

		datapanel.setButtonsVisible(false);
		datapanel.setRowFactory(rowFactory);
		return datapanel;
	}

	private void updateNaVerbintenisOfPlaatsingSelect(AjaxRequestTarget target,
			IModel<IdObject> selectedModel)
	{
		IdObject obj = selectedModel.getObject();

		geselecteerdeVerbintenisOfPlaatsing.setModelObject(obj);

		if (obj instanceof Verbintenis)
		{
			Verbintenis geselecteerdeVerbintenis = (Verbintenis) obj;

			soortMutatieClass = nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie.class;

			if (geselecteerdeVerbintenis.isEducatieVerbintenis())
			{
				soortGegevensList = recordTypesED;
			}
			else if (geselecteerdeVerbintenis.isBOVerbintenis())
			{
				if (geselecteerdeVerbintenis.getBekostigd() == Bekostigd.Gedeeltelijk)
					soortGegevensList = recordTypesBO;
				else
					soortGegevensList = recordTypesBOZonderPeriode;
			}

		}
		else if (obj instanceof Plaatsing)
		{
			Verbintenis geselecteerdeVerbintenis = ((Plaatsing) obj).getVerbintenis();

			if (geselecteerdeVerbintenis.isVAVOVerbintenis())
			{
				soortMutatieClass =
					nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie.class;

				soortGegevensList = recordTypesVAVO;
			}
			else if (geselecteerdeVerbintenis.isVOVerbintenis())
			{
				soortMutatieClass =
					nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie.class;

				soortGegevensList = recordTypesVO;
			}
		}

		target.addComponent(soortGegevensContainer);
		target.addComponent(soortMutatieContainer);

		target.addComponent(subselectVakgegevensED);
		target.addComponent(subselectBPVGegevensBO);
		target.addComponent(subselectBekostigingsperiodeBO);
		target.addComponent(subselectNT2ResultaatED);
	}

	private void createSoortMutatieChoice()
	{
		soortMutatieContainer = new WebMarkupContainer("soortMutatieContainer")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && soortMutatieClass != null;
			}
		};
		soortMutatieContainer.setOutputMarkupPlaceholderTag(true);

		soortMutatieChoice =
			new RadioChoice<Enum< ? >>("soortMutatie", new PropertyModel<Enum< ? >>(meldingmodel,
				"soortMutatie"), new SoortMutatieModel())
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onBeforeRender()
				{
					// Om te voorkomen dat de huidige optie geselecteerd blijft terwijl
					// deze niet in de nieuwe lijst van opties voorkomt
					setModelObject(null);

					super.onBeforeRender();
				}
			};
		soortMutatieContainer.add(soortMutatieChoice);

		form.add(soortMutatieContainer);
	}

	private void createSoortGegevensChoice()
	{
		soortGegevensContainer = new WebMarkupContainer("soortGegevensContainer")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && soortGegevensList != null
					&& !soortGegevensList.isEmpty();
			}
		};
		soortGegevensContainer.setOutputMarkupPlaceholderTag(true);

		soortGegevensChoice =
			new AjaxRadioChoice<Class< ? >>("soortGegevens", new PropertyModel<Class< ? >>(
				meldingmodel, "soortGegevens"), new SoortGegevensModel(),
				new ChoiceRenderer<Class< ? >>("simpleName"))
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target, Object newSelection)
				{
					target.addComponent(subselectVakgegevensED);
					target.addComponent(subselectBPVGegevensBO);
					target.addComponent(subselectBekostigingsperiodeBO);
					target.addComponent(subselectNT2ResultaatED);
				}

				@Override
				protected void onBeforeRender()
				{
					// Om te voorkomen dat de huidige optie geselecteerd blijft terwijl
					// deze niet in de nieuwe lijst van opties voorkomt
					setModelObject(null);

					super.onBeforeRender();
				}

			};

		soortGegevensContainer.add(soortGegevensChoice);
		form.add(soortGegevensContainer);
	}

	private void createSubselectBekostiginsperiodeBO()
	{
		subselectBekostigingsperiodeBO =
			new BronBekostigingsperiodeBOSelecterenPanel("subselectBekostigingsperiodeBO",
				new PropertyModel<Verbintenis>(meldingmodel, "gewijzigdeVerbintenisOfPlaatsing"),
				new PropertyModel<Bekostigingsperiode>(meldingmodel, "bekostigingsperiode"))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return soortGegevensChoice.getModelObject() != null
						&& ((Class< ? >) soortGegevensChoice.getModelObject())
							.isAssignableFrom(nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.PeriodegegevensInschrijvingRecord.class);
				}
			};
		subselectBekostigingsperiodeBO.setOutputMarkupPlaceholderTag(true);
		form.add(subselectBekostigingsperiodeBO);
	}

	private void createSubselectNT2ResultaatED()
	{
		subselectNT2ResultaatED =
			new BronNT2ResultaatEDSelecteerPanel("subselectNT2ResultaatED",
				new PropertyModel<Verbintenis>(meldingmodel, "gewijzigdeVerbintenisOfPlaatsing"),
				new PropertyModel<Resultaat>(meldingmodel, "nT2Resultaat"))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return soortGegevensChoice.getModelObject() != null
						&& ((Class< ? >) soortGegevensChoice.getModelObject())
							.isAssignableFrom(nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.NT2Vaardigheden.class);
				}
			};
		subselectNT2ResultaatED.setOutputMarkupPlaceholderTag(true);
		form.add(subselectNT2ResultaatED);
	}

	private void createSubselectBPVGegevensBO()
	{
		subselectBPVGegevensBO =
			new BronBPVGegevensBOSelecterenPanel("subselectBPVGegevensBO",
				new PropertyModel<Verbintenis>(meldingmodel, "gewijzigdeVerbintenisOfPlaatsing"),
				new PropertyModel<BPVInschrijving>(meldingmodel, "bPVInschrijving"))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return soortGegevensChoice.getModelObject() != null
						&& ((Class< ? >) soortGegevensChoice.getModelObject())
							.isAssignableFrom(nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.BpvGegevensRecord.class);
				}
			};
		subselectBPVGegevensBO.setOutputMarkupPlaceholderTag(true);
		form.add(subselectBPVGegevensBO);
	}

	private void createSubselectVakgegevensED()
	{
		subselectVakgegevensED =
			new BronVakgegevensEDSelecteerPanel("subselectVakgegevensED",
				new PropertyModel<Verbintenis>(meldingmodel, "gewijzigdeVerbintenisOfPlaatsing"),
				new PropertyModel<OnderwijsproductAfnameContext>(meldingmodel,
					"onderwijsproductAfnameContext"))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return soortGegevensChoice.getModelObject() != null
						&& ((Class< ? >) soortGegevensChoice.getModelObject())
							.isAssignableFrom(nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.VakgegevensRecord.class);
				}

			};
		subselectVakgegevensED.setOutputMarkupPlaceholderTag(true);
		form.add(subselectVakgegevensED);
	}

	private void setupRecordTypes()
	{
		recordTypesBO
			.add(nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.InschrijvingsgegevensRecord.class);
		recordTypesBO
			.add(nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.PeriodegegevensInschrijvingRecord.class);
		recordTypesBO
			.add(nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.BpvGegevensRecord.class);
		// TODO Dit recordtype implementeren
		// recordTypesBO
		// .add(nl.topicus.onderwijs.bron.bve.batches.pve_9_9.aanlevering.bo.ExamengegevensRecord.class);
		recordTypesBOZonderPeriode
			.add(nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.InschrijvingsgegevensRecord.class);
		recordTypesBOZonderPeriode
			.add(nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.BpvGegevensRecord.class);

		recordTypesED
			.add(nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.InschrijvingsgegevensRecord.class);
		// TODO Dit recordtype implementeren
		// recordTypesED
		// .add(nl.topicus.onderwijs.bron.bve.batches.pve_9_9.aanlevering.ed.ResultaatgegevensRecord.class);
		recordTypesED
			.add(nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.VakgegevensRecord.class);
		recordTypesED
			.add(nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.NT2Vaardigheden.class);

		recordTypesVAVO
			.add(nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.vavo.InschrijvingsgegevensRecord.class);

		recordTypesVO
			.add(nl.topicus.onderwijs.duo.bron.vo.batches.VOInschrijvingMeldingRecordDecentraal.class);
	}

	private void onFormSubmit()
	{
		HandmatigeBronMelding nieuwemelding = meldingmodel.getObject();

		IdObject gewijzigdeVerbintenisOfPlaatsing =
			nieuwemelding.getGewijzigdeVerbintenisOfPlaatsing();

		try
		{
			if (gewijzigdeVerbintenisOfPlaatsing instanceof Verbintenis)
			{
				Verbintenis verbintenis =
					(Verbintenis) nieuwemelding.getGewijzigdeVerbintenisOfPlaatsing();

				maakNieuweBronMelding(verbintenis, nieuwemelding);

			}
			else if (gewijzigdeVerbintenisOfPlaatsing instanceof Plaatsing)
			{
				Plaatsing plaatsing =
					(Plaatsing) nieuwemelding.getGewijzigdeVerbintenisOfPlaatsing();

				maakNieuweBronMelding(plaatsing, nieuwemelding.getSoortMutatie(), nieuwemelding
					.getSoortGegevens());
			}

			setResponsePage(new DeelnemerBronPage(getContextDeelnemer()));
		}
		catch (BronException e)
		{
			error("Kan geen melding voor BRON aanmaken.");
		}
	}

	private void maakNieuweBronMelding(Verbintenis verbintenis, HandmatigeBronMelding nieuwemelding)
			throws BronException
	{
		BronController bronController = new BronController();

		Enum< ? > soortMutatie = nieuwemelding.getSoortMutatie();
		Class< ? extends BronBveAanleverRecord> soortGegevens = nieuwemelding.getSoortGegevens();

		if (verbintenis.isEducatieVerbintenis())
		{
			if (soortGegevens
				.isAssignableFrom(nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.InschrijvingsgegevensRecord.class))
			{
				verbintenis.setHandmatigVersturenNaarBron(soortMutatie);
				bronController.controleerOpWijzigingenOpBronVelden(verbintenis,
					new Object[] {soortMutatie}, new Object[] {null},
					new String[] {"handmatigVersturenNaarBronMutatie"});
				bronController.save();
			}
			else if (soortGegevens
				.isAssignableFrom(nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.ResultaatgegevensRecord.class))
			{

			}
			else if (soortGegevens
				.isAssignableFrom(nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.VakgegevensRecord.class))
			{
				OnderwijsproductAfnameContext context =
					nieuwemelding.getOnderwijsproductAfnameContext();
				context.setHandmatigVersturenNaarBron(soortMutatie);

				bronController.controleerOpWijzigingenOpBronVelden(context,
					new Object[] {soortMutatie}, new Object[] {null},
					new String[] {"handmatigVersturenNaarBronMutatie"});
				bronController.save();
			}
			else if (soortGegevens
				.isAssignableFrom(nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.NT2Vaardigheden.class))
			{
				Resultaat resultaat = nieuwemelding.getNT2Resultaat();
				resultaat.setHandmatigVersturenNaarBron(soortMutatie);

				bronController.controleerOpWijzigingenOpBronVelden(resultaat,
					new Object[] {soortMutatie}, new Object[] {null},
					new String[] {"handmatigVersturenNaarBronMutatie"});
				bronController.save();
			}
		}
		else if (verbintenis.isBOVerbintenis())
		{
			if (soortGegevens
				.isAssignableFrom(nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.InschrijvingsgegevensRecord.class))
			{
				verbintenis.setHandmatigVersturenNaarBron(soortMutatie);
				bronController.controleerOpWijzigingenOpBronVelden(verbintenis,
					new Object[] {soortMutatie}, new Object[] {null},
					new String[] {"handmatigVersturenNaarBronMutatie"});
				bronController.save();
			}
			else if (soortGegevens
				.isAssignableFrom(nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.PeriodegegevensInschrijvingRecord.class))
			{
				Bekostigingsperiode periode = nieuwemelding.getBekostigingsperiode();
				periode.setHandmatigVersturenNaarBron(soortMutatie);
				if (!periode.isSaved())
					periode.save();

				bronController.controleerOpWijzigingenOpBronVelden(periode,
					new Object[] {soortMutatie}, new Object[] {null},
					new String[] {"handmatigVersturenNaarBronMutatie"});
				bronController.save();
			}
			else if (soortGegevens
				.isAssignableFrom(nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.BpvGegevensRecord.class))
			{
				BPVInschrijving bpvInschrijving = nieuwemelding.getBPVInschrijving();
				bpvInschrijving.setHandmatigVersturenNaarBron(soortMutatie);

				bronController.controleerOpWijzigingenOpBronVelden(bpvInschrijving,
					new Object[] {soortMutatie}, new Object[] {null},
					new String[] {"handmatigVersturenNaarBronMutatie"});
				bronController.save();

			}
			else if (soortGegevens
				.isAssignableFrom(nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.ExamengegevensRecord.class))
			{
			}
		}

		BatchDataAccessHelper< ? > helper =
			DataAccessRegistry.getHelper(BatchDataAccessHelper.class);
		helper.batchExecute();

	}

	@SuppressWarnings("unused")
	private void maakNieuweBronMelding(Plaatsing plaatsing, Enum< ? > soortMutatie,
			Class< ? extends BronBveAanleverRecord> soortGegevens) throws BronException
	{
		BronController bronController = new BronController();

		plaatsing.getVerbintenis().setHandmatigVersturenNaarBron(soortMutatie);
		plaatsing.setHandmatigVersturenNaarBron(soortMutatie);

		bronController.controleerOpWijzigingenOpBronVelden(plaatsing, new Object[] {soortMutatie},
			new Object[] {null}, new String[] {"handmatigVersturenNaarBronMutatie"});

		bronController.save();

		BatchDataAccessHelper< ? > helper =
			DataAccessRegistry.getHelper(BatchDataAccessHelper.class);
		helper.batchExecute();
	}

	private void addValidators()
	{
		form.add(new AbstractFormValidator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public FormComponent< ? >[] getDependentFormComponents()
			{
				return new FormComponent[] {};
			}

			@Override
			public void validate(@SuppressWarnings("hiding") Form< ? > form)
			{
				if (geselecteerdeVerbintenisOfPlaatsing.getConvertedInput() == null)
					error(geselecteerdeVerbintenisOfPlaatsing,
						"DeelnemerBronMeldingToevoegenPage.geselecteerdeVerbintenisOfPlaatsing");

				if (soortGegevensChoice.getConvertedInput() == null)
					error(soortGegevensChoice,
						"DeelnemerBronMeldingToevoegenPage.soortGegevensChoice");
				else
				{
					Class< ? > clazz = soortGegevensChoice.getConvertedInput();
					if (clazz
						.isAssignableFrom(nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.VakgegevensRecord.class))
					{
						if (subselectVakgegevensED.getRadioGroupSelecteren().getConvertedInput() == null)
						{
							error(subselectVakgegevensED.getRadioGroupSelecteren(),
								"DeelnemerBronMeldingToevoegenPage.subselectVakgegevensED");
						}
					}
					else if (clazz
						.isAssignableFrom(nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.PeriodegegevensInschrijvingRecord.class))
					{
						if (subselectBekostigingsperiodeBO.getRadioGroupSelecteren()
							.getConvertedInput() == null)
						{
							error(subselectBekostigingsperiodeBO.getRadioGroupSelecteren(),
								"DeelnemerBronMeldingToevoegenPage.subselectBekostigingsperiodeBO");
						}
					}

					else if (clazz
						.isAssignableFrom(nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.BpvGegevensRecord.class))
					{
						if (subselectBPVGegevensBO.getRadioGroupSelecteren().getConvertedInput() == null)
						{
							error(subselectBPVGegevensBO.getRadioGroupSelecteren(),
								"DeelnemerBronMeldingToevoegenPage.subselectBPVGegevensBO");
						}
					}

				}

				if (soortMutatieChoice.getConvertedInput() == null)
					error(soortMutatieChoice,
						"DeelnemerBronMeldingToevoegenPage.soortMutatieChoice");
			}
		});
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form));
		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new DeelnemerBronPage(getContextDeelnemer());
			}

			@Override
			public Class<DeelnemerBronPage> getPageIdentity()
			{
				return DeelnemerBronPage.class;
			}

		}));
		super.fillBottomRow(panel);
	}

	private Form<Void> createForm()
	{
		return new Form<Void>("form")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				onFormSubmit();
			}
		};
	}

	@Override
	public Verbintenis getVerbintenis()
	{
		Object modelObject = geselecteerdeVerbintenisOfPlaatsing.getConvertedInput();
		if (modelObject != null)
		{
			if (modelObject instanceof Verbintenis)
			{
				return (Verbintenis) modelObject;
			}
			else if (modelObject instanceof Plaatsing)
			{
				return ((Plaatsing) modelObject).getVerbintenis();
			}
		}
		return null;
	}

	private class SoortGegevensModel extends LoadableDetachableModel<List<Class< ? >>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<Class< ? >> load()
		{
			return soortGegevensList;
		}
	}

	private class SoortMutatieModel extends LoadableDetachableModel<List< ? extends Enum< ? >>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List< ? extends Enum< ? >> load()
		{
			if (soortMutatieClass != null)
				return Arrays.asList(soortMutatieClass.getEnumConstants());
			else
				return null;
		}
	}

	@SuppressWarnings("unused")
	private class HandmatigeBronMelding implements IDetachable
	{
		private static final long serialVersionUID = 1L;

		private Enum< ? > soortMutatie;

		private Class< ? extends BronBveAanleverRecord> soortGegevens;

		private IModel<IdObject> gewijzigdeVerbintenisOfPlaatsing = ModelFactory.getModel(null);

		private IModel<OnderwijsproductAfnameContext> onderwijsproductAfnameContext =
			ModelFactory.getModel(null);

		private IModel<BPVInschrijving> bPVInschrijving = ModelFactory.getModel(null);

		private IModel<Bekostigingsperiode> bekostigingsperiode = ModelFactory.getModel(null);

		private IModel<Resultaat> nT2Resultaat = ModelFactory.getModel(null);

		public HandmatigeBronMelding()
		{
		}

		public Enum< ? > getSoortMutatie()
		{
			return soortMutatie;
		}

		public void setSoortMutatie(Enum< ? > soortMutatie)
		{
			this.soortMutatie = soortMutatie;
		}

		public Entiteit getGewijzigdeVerbintenisOfPlaatsing()
		{
			return (Entiteit) gewijzigdeVerbintenisOfPlaatsing.getObject();
		}

		public void setGewijzigdeVerbintenisOfPlaatsing(Entiteit gewijzigdeVerbintenisOfPlaatsing)
		{
			this.gewijzigdeVerbintenisOfPlaatsing.setObject(gewijzigdeVerbintenisOfPlaatsing);
		}

		public Class< ? extends BronBveAanleverRecord> getSoortGegevens()
		{
			return soortGegevens;
		}

		public void setSoortGegevens(Class< ? extends BronBveAanleverRecord> soortGegevens)
		{
			this.soortGegevens = soortGegevens;
		}

		public OnderwijsproductAfnameContext getOnderwijsproductAfnameContext()
		{
			return onderwijsproductAfnameContext.getObject();
		}

		public void setOnderwijsproductAfnameContext(
				OnderwijsproductAfnameContext onderwijsproductAfnameContext)
		{
			this.onderwijsproductAfnameContext.setObject(onderwijsproductAfnameContext);
		}

		public void setBPVInschrijving(BPVInschrijving bPVInschrijving)
		{
			this.bPVInschrijving.setObject(bPVInschrijving);
		}

		public BPVInschrijving getBPVInschrijving()
		{
			return bPVInschrijving.getObject();
		}

		public void setBekostigingsperiode(Bekostigingsperiode bekostigingsperiode)
		{
			this.bekostigingsperiode.setObject(bekostigingsperiode);
		}

		public Bekostigingsperiode getBekostigingsperiode()
		{
			return bekostigingsperiode.getObject();
		}

		public void setNT2Resultaat(Resultaat nT2Resultaat)
		{
			this.nT2Resultaat.setObject(nT2Resultaat);
		}

		public Resultaat getNT2Resultaat()
		{
			return nT2Resultaat.getObject();
		}

		@Override
		public void detach()
		{
			ComponentUtil.detachQuietly(gewijzigdeVerbintenisOfPlaatsing);
			ComponentUtil.detachQuietly(onderwijsproductAfnameContext);
			ComponentUtil.detachQuietly(bPVInschrijving);
			ComponentUtil.detachQuietly(bekostigingsperiode);
			ComponentUtil.detachQuietly(nT2Resultaat);
		}
	}
}