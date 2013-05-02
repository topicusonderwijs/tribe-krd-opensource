package nl.topicus.eduarte.krd.web.pages.intake.stap4;

import java.util.*;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.choice.IntegerRangeCombobox;
import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.cobra.web.components.quicksearch.ISelectListener;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.components.text.ReadonlyTextField;
import nl.topicus.cobra.web.components.text.RequiredDatumField;
import nl.topicus.cobra.web.validators.BegindatumVoorEinddatumValidator;
import nl.topicus.cobra.web.validators.DatumGroterOfGelijkDatumValidator;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.contract.ContractOnderdeel;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.inschrijving.IVooropleiding;
import nl.topicus.eduarte.entities.inschrijving.NT2Niveau;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.VerbintenisContract;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding.SoortOnderwijs;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.Leerprofiel;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.ProfielInburgering;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.RedenInburgering;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.SoortPraktijkexamen;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.StaatsExamenType;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.opleiding.Team;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.taxonomie.MBOLeerweg;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.krd.web.components.choice.IntensiteitCombobox;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.LeerjarenModel;
import nl.topicus.eduarte.krd.web.pages.intake.IntakeWizardModel;
import nl.topicus.eduarte.krd.web.pages.intake.IntakeWizardPage;
import nl.topicus.eduarte.krd.web.validators.BronValidator;
import nl.topicus.eduarte.krd.web.validators.BronVerbintenisBegindatumValdidator;
import nl.topicus.eduarte.krd.web.validators.IngangsdatumOpleidingNaBegindatumValidator;
import nl.topicus.eduarte.krd.web.validators.IntensiteitLeerwegKoppelValidator;
import nl.topicus.eduarte.krd.web.validators.VooropleidingVerplichtValidator;
import nl.topicus.eduarte.providers.VerbintenisProvider;
import nl.topicus.eduarte.web.components.choice.CohortCombobox;
import nl.topicus.eduarte.web.components.choice.ContractOnderdeelCombobox;
import nl.topicus.eduarte.web.components.choice.KenniscentrumCombobox;
import nl.topicus.eduarte.web.components.choice.SecureOrganisatieEenheidLocatieFormChoicePanel;
import nl.topicus.eduarte.web.components.choice.SecureOrganisatieEenheidLocatieChoicePanel.OrganisatieEenheidLocatieRequired;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitPropertyRenderer;
import nl.topicus.eduarte.web.components.quicksearch.contract.ContractSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.groep.GroepSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.opleiding.OpleidingSearchEditor;
import nl.topicus.eduarte.zoekfilters.ContractZoekFilter;
import nl.topicus.eduarte.zoekfilters.GroepZoekFilter;
import nl.topicus.eduarte.zoekfilters.OpleidingZoekFilter;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.RangeValidator;

public class VerbintenisPanel extends TypedPanel<Verbintenis> implements VerbintenisProvider
{
	private static final long serialVersionUID = 1L;

	private IntakeWizardPage wizardPage;

	private DatumField begindatumField;

	private DatumField geplandeEinddatumField;

	private CohortCombobox cohort;

	private ReadonlyTextField<MBOLeerweg> leerweg;

	protected Form<Void> form;

	private SecureOrganisatieEenheidLocatieFormChoicePanel<Verbintenis> organisatieEenheidLocatieChoice;

	private AbstractAjaxDropDownChoice<Integer> leerjaar;

	private OpleidingSearchEditor opleidingField;

	private JaNeeCombobox onderwijsproductAfnamesMakenCombobox;

	private AbstractAjaxDropDownChoice<IVooropleiding> relevanteVooropleiding;

	private Label categorieVooropleiding;

	private OpleidingZoekFilter opleidingFilter;

	protected EnumCombobox<VerbintenisStatus> statusCombobox;

	private Label teamLabel;

	public VerbintenisPanel(String id, IntakeWizardPage wizardPage, Form<Void> form)
	{
		super(id);
		this.form = form;
		this.wizardPage = wizardPage;

		createComponents();
	}

	public VerbintenisPanel(String id, IModel<Verbintenis> model, IntakeWizardPage wizardPage,
			Form<Void> form)
	{
		super(id, model);
		this.form = form;
		this.wizardPage = wizardPage;

		createComponents();
	}

	private void createComponents()
	{
		voegOrganisatieEenheidLocatieToe();

		voegOpleidingToe();
		addMaakOnderwijsproductAfnames();
		voegBeginEinddatumToe();
		voegCohortToe();
		voegDatumValidatiesToe();

		leerweg = new ReadonlyTextField<MBOLeerweg>("opleiding.leerweg");
		add(leerweg);

		voegKenniscentrumVeldToe();

		voegIntensiteitToe();
		voegStatusToe();
		voegContractToe();
		voegRelevanteVooropleidingToe();
		voegContacturenPerWeekToe();
		voegLeerprofielVeldToe();
		voegValidatorsToe();

		GroepZoekFilter filter = GroepZoekFilter.createDefaultFilter();
		filter.setPlaatsingsgroep(Boolean.TRUE);
		filter.setOrganisatieEenheidModel(new PropertyModel<OrganisatieEenheid>(this,
			"verbintenis.organisatieEenheid"));
		add(new GroepSearchEditor("plaatsingsgroep", new PropertyModel<Groep>(this,
			"eerstePlaatsing.groep"), filter));

		voegLeerjaarToe();
		voegJarenPraktijkonderwijsToe();

		add(new CheckBox("indicatieGehandicapt", new PropertyModel<Boolean>(getDefaultModel(),
			"indicatieGehandicapt")));
		setOutputMarkupId(true);

		voegInburgeringVeldenToe();
	}

	private void voegInburgeringVeldenToe()
	{
		WebMarkupContainer veldenInburgering = new WebMarkupContainer("veldenInburgering")
		{
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			public boolean isVisible()
			{
				if (isInburgering())
				{
					((EnumCombobox<ProfielInburgering>) get("profielInburgering"))
						.setRequired(!isStaatsexamen());
				}

				return getVerbintenis() != null && getVerbintenis().isInburgeringVerbintenis();
			}
		};

		veldenInburgering.add(new EnumCombobox<RedenInburgering>("redenInburgering",
			RedenInburgering.values()).setRequired(false));
		veldenInburgering.add(new EnumCombobox<ProfielInburgering>("profielInburgering",
			ProfielInburgering.values()).setRequired(true));
		veldenInburgering.add(new EnumCombobox<Leerprofiel>("leerprofiel", Leerprofiel.values())
			.setRequired(true));
		veldenInburgering.add(new JaNeeCombobox("deelcursus").setRequired(true));

		WebMarkupContainer praktijkexamenRow = new WebMarkupContainer("praktijkexamenRow")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				if (getOpleiding() != null && getOpleiding().isInburgering())
				{
					String taxCode = getOpleiding().getVerbintenisgebied().getTaxonomiecode();
					return super.isVisible() && !taxCode.startsWith("5.SE");
				}
				return false;
			}
		};
		praktijkexamenRow.setOutputMarkupId(true);
		praktijkexamenRow.add(new EnumCombobox<SoortPraktijkexamen>("soortPraktijkexamen",
			SoortPraktijkexamen.values()).setRequired(true));
		veldenInburgering.add(praktijkexamenRow);

		veldenInburgering.add(new DatumField("examenDatum"));

		WebMarkupContainer staatsExamenTypeRow = new WebMarkupContainer("staatsExamenTypeRow")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return getVerbintenis() != null && isStaatsexamen();
			}
		};
		staatsExamenTypeRow.setOutputMarkupId(true);
		staatsExamenTypeRow.add(new EnumCombobox<StaatsExamenType>("staatsExamenType",
			StaatsExamenType.values()).setRequired(true));
		veldenInburgering.add(staatsExamenTypeRow);

		veldenInburgering.add(new EnumCombobox<NT2Niveau>("beginNiveauSchriftelijkeVaardigheden",
			NT2Niveau.inburgeringsNiveaus()));
		veldenInburgering.add(new EnumCombobox<NT2Niveau>("eindNiveauSchriftelijkeVaardigheden",
			NT2Niveau.inburgeringsNiveaus()));

		WebMarkupContainer datumAanmeldenRow = new WebMarkupContainer("datumAanmeldenRow")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return getVerbintenis() != null && isInburgeringsContract();
			}
		};
		datumAanmeldenRow.add(new DatumField("datumAanmelden"));
		veldenInburgering.add(datumAanmeldenRow);

		WebMarkupContainer datumAkkoordRow = new WebMarkupContainer("datumAkkoordRow")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return getVerbintenis() != null && !isInburgeringsContract();
			}
		};
		datumAkkoordRow.add(new DatumField("datumAkkoord"));
		veldenInburgering.add(datumAkkoordRow);

		WebMarkupContainer datumEersteActiviteitRow =
			new WebMarkupContainer("datumEersteActiviteitRow")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return getVerbintenis() != null && !isInburgeringsContract();
				}
			};
		datumEersteActiviteitRow.add(new DatumField("datumEersteActiviteit"));
		veldenInburgering.add(datumEersteActiviteitRow);

		add(veldenInburgering);
	}

	public Plaatsing getEerstePlaatsing()
	{
		if (getVerbintenis().getPlaatsingen().isEmpty())
			return null;
		return getVerbintenis().getPlaatsingen().get(0);
	}

	public VerbintenisContract getEersteVerbintenisContract()
	{
		if (getVerbintenis().getContracten().isEmpty())
			return null;
		return getVerbintenis().getContracten().get(0);
	}

	public Contract getEersteContract()
	{
		if (getVerbintenis().getContracten().isEmpty())
			return null;
		return getVerbintenis().getContracten().get(0).getContract();
	}

	public void setEersteContract(Contract contract)
	{
		getVerbintenis().getContracten().clear();
		if (contract != null)
		{
			VerbintenisContract newContract = new VerbintenisContract(getVerbintenis());
			newContract.setContract(contract);
			getVerbintenis().getContracten().add(newContract);
		}
	}

	private boolean isInburgeringsContract()
	{
		Contract contract = getEersteContract();
		return contract != null && contract.isInburgering();
	}

	private void voegLeerjaarToe()
	{
		leerjaar =
			new AbstractAjaxDropDownChoice<Integer>("leerjaar", new PropertyModel<Integer>(this,
				"eerstePlaatsing.leerjaar"), new LeerjarenModel(getVerbintenisModel()))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isRequired()
				{
					if (!verbintenisIsMuteerbaar() && !verbintenisIsAfgesloten())
					{
						Opleiding opleiding = getOpleiding();
						if (opleiding != null)
							return opleiding.getVerbintenisgebied().getTaxonomie().isVO()
								&& !"0090"
									.equals(opleiding.getVerbintenisgebied().getExterneCode());
					}
					return false;
				}
			};
		add(leerjaar);

	}

	private void voegIntensiteitToe()
	{
		WebMarkupContainer intensiteitRow = new WebMarkupContainer("intensiteitRow")
		{
			private IntensiteitCombobox child;

			private static final long serialVersionUID = 1L;
			{
				child = new IntensiteitCombobox("intensiteit", true, VerbintenisPanel.this)
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected boolean wantOnSelectionChangedNotifications()
					{
						return false;
					}

					@Override
					protected void onUpdate(AjaxRequestTarget target, Intensiteit newSelection)
					{
						Intensiteit intensiteit = newSelection;
						if (intensiteit.equals(Intensiteit.Examendeelnemer))
							getVerbintenis().setBekostigd(Bekostigd.Nee);
						else
							getVerbintenis().setBekostigd(Bekostigd.Ja);
					}

					@Override
					public List<Intensiteit> getChoices()
					{
						Verbintenis verbintenis = getVerbintenis();
						List<Intensiteit> choices = new ArrayList<Intensiteit>();
						if (verbintenis.isVOVerbintenis())
							choices.addAll(Arrays.asList(Intensiteit.Voltijd,
								Intensiteit.Examendeelnemer));
						else
						{
							for (Intensiteit intensiteit : Intensiteit.values())
								choices.add(intensiteit);

							Iterator<Intensiteit> it = choices.iterator();
							if (verbintenis.getOpleiding().getLeerweg().equals(MBOLeerweg.BBL))
							{
								while (it.hasNext())
									if (it.next().equals(Intensiteit.Voltijd))
										it.remove();
							}
						}
						return choices;
					}

				};
				child
					.add(new IntensiteitLeerwegKoppelValidator<Intensiteit>(VerbintenisPanel.this));
				add(child);
			}

			@Override
			public boolean isVisible()
			{
				return child.isVisible();
			}
		};
		add(intensiteitRow);
	}

	private void voegJarenPraktijkonderwijsToe()
	{
		WebMarkupContainer jarenPraktijkonderwijsRow =
			new WebMarkupContainer("jarenPraktijkonderwijsRow")
			{

				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					boolean praktijkonderwijs = false;
					Opleiding opleiding = getOpleiding();
					if (opleiding != null)
						praktijkonderwijs =
							opleiding.getVerbintenisgebied().getTaxonomie().isVO()
								&& "0090".equals(opleiding.getVerbintenisgebied().getExterneCode());
					return praktijkonderwijs;
				}
			};
		jarenPraktijkonderwijsRow.add(new IntegerRangeCombobox("jarenPraktijkonderwijs",
			new PropertyModel<Integer>(this, "eerstePlaatsing.jarenPraktijkonderwijs"), 1, 9)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isRequired()
			{
				if (!verbintenisIsMuteerbaar() && !verbintenisIsAfgesloten())
				{
					Opleiding opleiding = getOpleiding();
					if (opleiding != null)
						return opleiding.getVerbintenisgebied().getTaxonomie().isVO()
							&& "0090".equals(opleiding.getVerbintenisgebied().getExterneCode());
				}
				return false;
			}
		});
		add(jarenPraktijkonderwijsRow);
	}

	private void voegContacturenPerWeekToe()
	{
		WebMarkupContainer contacturenRow = new WebMarkupContainer("contacturenRow")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				Opleiding opleiding = getOpleiding();
				return opleiding != null
					&& (opleiding.getVerbintenisgebied().getTaxonomie().isEducatie() || opleiding
						.isVavo());
			}
		};
		contacturenRow.add(new RequiredTextField<Integer>("contacturenPerWeek", Integer.class)
			.add(new RangeValidator<Integer>(0, 40)));
		add(contacturenRow);

	}

	private void voegLeerprofielVeldToe()
	{
		WebMarkupContainer leerprofielRow = new WebMarkupContainer("leerprofielRow")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				Opleiding gekozenOpleiding = getOpleiding();
				if (gekozenOpleiding != null)
				{
					Taxonomie taxonomie = gekozenOpleiding.getVerbintenisgebied().getTaxonomie();
					return taxonomie.isEducatie();
				}
				return false;
			}
		};
		leerprofielRow.add(new EnumCombobox<Leerprofiel>("leerprofiel", Leerprofiel.values())
			.setRequired(true));
		add(leerprofielRow);
	}

	private void voegKenniscentrumVeldToe()
	{
		WebMarkupContainer kenniscentrumRow = new WebMarkupContainer("kenniscentrumRow")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				Opleiding gekozenOpleiding = getOpleiding();
				if (gekozenOpleiding != null)
				{
					return gekozenOpleiding.isKiesKenniscentrum();
				}
				return false;
			}
		};
		kenniscentrumRow.add(new KenniscentrumCombobox("brin").setRequired(true));
		add(kenniscentrumRow);
	}

	private void voegStatusToe()
	{
		statusCombobox =
			new EnumCombobox<VerbintenisStatus>("status", new PropertyModel<VerbintenisStatus>(
				getDefaultModel(), "status"), true, VerbintenisStatus.Voorlopig,
				VerbintenisStatus.Volledig)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target, VerbintenisStatus newSelection)
				{
					boolean verplicht = !verbintenisIsMuteerbaar() && !verbintenisIsAfgesloten();
					organisatieEenheidLocatieChoice.getLocatieCombo().setRequired(verplicht);
					geplandeEinddatumField.setRequired(verbintenisIsBronCommuniceerbaar());
					target.addComponent(VerbintenisPanel.this);

				}
			};
		statusCombobox.setRequired(true);
		add(statusCombobox);

	}

	protected void voegValidatorsToe()
	{
		statusCombobox.add(new BronValidator<VerbintenisStatus>(this, form));
	}

	protected boolean bronValidatorToevoegen()
	{
		return true;
	}

	private boolean verbintenisIsMuteerbaar()
	{
		if (getVerbintenis() != null && getVerbintenis().getStatus() != null)
			return getVerbintenis().getStatus().isMuteerbaar();
		return true;
	}

	private boolean verbintenisIsAfgesloten()
	{
		if (getVerbintenis() != null && getVerbintenis().getStatus() != null)
			return getVerbintenis().getStatus().isAfgesloten();
		return false;
	}

	private boolean verbintenisIsBronCommuniceerbaar()
	{
		return getVerbintenis() != null && getVerbintenis().isBronCommuniceerbaar();
	}

	private void voegRelevanteVooropleidingToe()
	{
		WebMarkupContainer relevanteVooropleidingRow =
			new WebMarkupContainer("relevanteVooropleidingRow");

		relevanteVooropleiding =
			new AbstractAjaxDropDownChoice<IVooropleiding>("relevanteVooropleiding",
				new Vooropleidingen(), new EntiteitPropertyRenderer("omschrijving"))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isRequired()
				{
					Verbintenis verbintenis = getVerbintenis();
					if (verbintenis != null)
					{
						return !verbintenisIsMuteerbaar() && !verbintenisIsAfgesloten()
							&& verbintenis.isBVEVerbintenis();
					}
					return false;
				}

				@Override
				protected void onUpdate(AjaxRequestTarget target, IVooropleiding newSelection)
				{
					target.addComponent(categorieVooropleiding);
				}
			};

		relevanteVooropleiding.setLabel(new Model<String>("Relevante vooropleiding"));

		relevanteVooropleidingRow.add(relevanteVooropleiding);
		relevanteVooropleidingRow.setVisible(isVoegRelevanteVooropleidingToe());
		add(relevanteVooropleidingRow);

		relevanteVooropleiding.add(new VooropleidingVerplichtValidator<IVooropleiding>(this));

		WebMarkupContainer categorieVooropleidingRow =
			new WebMarkupContainer("categorieVooropleidingRow");

		categorieVooropleiding =
			new Label("categorieVooropleiding", new PropertyModel<SoortOnderwijs>(
				getVerbintenisModel(), "relevanteVooropleiding.soortOnderwijs"));
		categorieVooropleiding.setOutputMarkupId(true);

		categorieVooropleidingRow.add(categorieVooropleiding);
		categorieVooropleidingRow.setVisible(isVoegRelevanteVooropleidingToe());
		add(categorieVooropleidingRow);
	}

	private void voegContractToe()
	{
		ContractZoekFilter contractZoekFilter = new ContractZoekFilter();
		contractZoekFilter.setInschrijfdatumModel(new PropertyModel<Date>(getDefaultModel(),
			"begindatum"));
		ContractSearchEditor contract =
			new ContractSearchEditor("contract", getContractModel(), contractZoekFilter);
		contract.addListener(new ISelectListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onUpdate(AjaxRequestTarget target)
			{
				((DatumField) ((WebMarkupContainer) VerbintenisPanel.this
					.get("contractBeginEindDatumRow")).get("contractBegindatum"))
					.setRequired(getEersteContract() != null);
				target.addComponent(VerbintenisPanel.this);
			}
		});
		add(contract);

		WebMarkupContainer contractonderdeelRow = new WebMarkupContainer("contractonderdeelRow");

		contractonderdeelRow.add(new ContractOnderdeelCombobox("contractOnderdeel",
			new PropertyModel<ContractOnderdeel>(this, "eersteVerbintenisContract.onderdeel"),
			new LoadableDetachableModel<List<ContractOnderdeel>>()
			{

				private static final long serialVersionUID = 1L;

				@Override
				protected List<ContractOnderdeel> load()
				{
					if (getEersteContract() != null)
						return getEersteContract().getContractOnderdelen();
					return Collections.emptyList();
				}
			}));
		contractonderdeelRow.setVisible(isVoegRelevanteVooropleidingToe());
		add(contractonderdeelRow);

		WebMarkupContainer contractBeginEindDatumRow =
			new WebMarkupContainer("contractBeginEindDatumRow");
		contractBeginEindDatumRow.add(new DatumField("contractBegindatum", new PropertyModel<Date>(
			this, "eersteVerbintenisContract.begindatum")).setRequired(getVerbintenis() != null
			&& getEersteContract() != null));

		contractBeginEindDatumRow.add(new DatumField("contractEinddatum", new PropertyModel<Date>(
			this, "eersteVerbintenisContract.einddatum")));
		add(contractBeginEindDatumRow);

	}

	protected IModel<Contract> getContractModel()
	{
		return new PropertyModel<Contract>(this, "eersteContract");
	}

	private void voegCohortToe()
	{
		cohort = new CohortCombobox("cohort");
		add(cohort);
	}

	private void voegDatumValidatiesToe()
	{
		form.add(new AbstractFormValidator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public FormComponent< ? >[] getDependentFormComponents()
			{
				return new FormComponent< ? >[] {begindatumField, cohort};
			}

			@Override
			public void validate(Form< ? > formP)
			{
				Date begindatum = begindatumField.getDatum();
				Cohort ch = cohort.getCohort();
				if (begindatum != null && ch != null)
					if (begindatum.before(ch.getBegindatum()))
					{
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("begincohort", TimeUtil.getInstance().formatDate(
							ch.getBegindatum()));
						error(begindatumField, "voorcohort", params);
					}
			}
		});

		form.add(new BegindatumVoorEinddatumValidator(begindatumField, geplandeEinddatumField));

		form.add(new IngangsdatumOpleidingNaBegindatumValidator(opleidingField, begindatumField));

		Date geboorteDatum = getGeboortedatum();
		if (geboorteDatum != null)
		{
			form.add(new DatumGroterOfGelijkDatumValidator("Begindatum", begindatumField,
				geboorteDatum));
		}
	}

	protected Date getGeboortedatum()
	{
		return getWizard().getDeelnemer().getPersoon().getGeboortedatum();
	}

	private void voegBeginEinddatumToe()
	{
		begindatumField = new RequiredDatumField("begindatum")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target, Date newValue)
			{
				Verbintenis verbintenis = getVerbintenis();
				Date geplandeEinddatum = verbintenis.berekenGeplandeEinddatum();
				if (geplandeEinddatum != null)
					verbintenis.setGeplandeEinddatum(geplandeEinddatum);
				verbintenis.setCohort(Cohort.getCohort(newValue));
				getEerstePlaatsing().setBegindatum(newValue);

				target.addComponent(VerbintenisPanel.this);
			}
		};
		begindatumField.add(new BronVerbintenisBegindatumValdidator(getVerbintenisModel()));

		add(begindatumField);

		geplandeEinddatumField = new DatumField("geplandeEinddatum")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target, Date newValue)
			{
				getEerstePlaatsing().setEinddatum(newValue);
			}

			@Override
			public boolean isRequired()
			{
				Verbintenis verbintenis = getVerbintenis();
				return verbintenis != null && verbintenis.isBronCommuniceerbaar();
			}
		};
		add(geplandeEinddatumField);
	}

	private void voegOpleidingToe()
	{
		opleidingFilter = new OpleidingZoekFilter();
		opleidingFilter.setOrganisatieEenheidModel(new PropertyModel<OrganisatieEenheid>(
			getDefaultModel(), "organisatieEenheid"));
		opleidingFilter.setLocatieModel(new PropertyModel<Locatie>(getDefaultModel(), "locatie"));

		opleidingField = new OpleidingSearchEditor("opleiding", null, opleidingFilter)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isRequired()
			{
				// je moet hem invullen als de huidige gekozen status niet muteerbaar
				// is (tenzij hij afgesloten is)
				return !verbintenisIsMuteerbaar() && !verbintenisIsAfgesloten();
			}

			@Override
			protected void onBeforeRender()
			{
				super.onBeforeRender();
				// Zet caching uit zodat de ondertussen aangepaste
				// organisatieeenheid/locatie doorwerkt in het zoekveld
				if (!hasBeenRendered())
					getSearchField().getOptions().put("cacheLength", 0);
			}

		};

		opleidingField.addListener(new ISelectListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onUpdate(AjaxRequestTarget target)
			{
				Verbintenis verbintenis = getVerbintenis();
				Date geplandeEinddatum = verbintenis.berekenGeplandeEinddatum();
				verbintenis.setGeplandeEinddatum(geplandeEinddatum);
				if (verbintenis.getOpleiding() != null)
				{
					if (!verbintenis.getOpleiding().isAangebodenOp(
						verbintenis.getOrganisatieEenheid()))
						verbintenis.setOrganisatieEenheid(null);
					if (!verbintenis.getOpleiding().isAangebodenOp(verbintenis.getLocatie()))
						verbintenis.setLocatie(null);
					organisatieEenheidLocatieChoice.getLocatieCombo().reselectOnlyOption();
					verbintenis.setIntensiteit(verbintenis.getOpleiding().getDefaultIntensiteit());
				}
				target.addComponent(VerbintenisPanel.this);
			}
		});
		opleidingField.setLabel(new Model<String>("Opleiding"));
		add(opleidingField);
	}

	private void addMaakOnderwijsproductAfnames()
	{
		onderwijsproductAfnamesMakenCombobox =
			new JaNeeCombobox("onderwijsproductAfnamesMaken",
				getOnderwijsproductAfnamesAanmakenModel());
		onderwijsproductAfnamesMakenCombobox.setNullValid(false).setRequired(true);
		add(onderwijsproductAfnamesMakenCombobox);
	}

	protected IModel<Boolean> getOnderwijsproductAfnamesAanmakenModel()
	{
		return new PropertyModel<Boolean>(getWizard(), "maakOnderwijsproductAfnames");
	}

	private void voegOrganisatieEenheidLocatieToe()
	{
		organisatieEenheidLocatieChoice =
			new SecureOrganisatieEenheidLocatieFormChoicePanel<Verbintenis>(
				"organisatieEenheidLocatie", getVerbintenisModel(),
				OrganisatieEenheidLocatieRequired.OrganisatieEenheid, new PropertyModel<Opleiding>(
					getVerbintenisModel(), "opleiding"))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onUpdateOrganisatieEenheid(AjaxRequestTarget target,
						OrganisatieEenheid newSelection)
				{
					super.onUpdateOrganisatieEenheid(target, newSelection);
					target.addComponent(teamLabel);
				}

				@Override
				public void onUpdateLocatie(AjaxRequestTarget target, Locatie newSelection)
				{
					target.addComponent(teamLabel);
				}
			};
		organisatieEenheidLocatieChoice.setRenderBodyOnly(true);
		organisatieEenheidLocatieChoice.getOrganisatieEenheidCombo().setNullValid(false);
		organisatieEenheidLocatieChoice.getLocatieCombo().setForceAutoSelectOnlyOption(true);
		add(organisatieEenheidLocatieChoice);

		teamLabel = new Label("team", new LoadableDetachableModel<String>()
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected String load()
			{
				OrganisatieEenheid orgeenh =
					organisatieEenheidLocatieChoice.getOrganisatieEenheidCombo()
						.getOrganisatieEenheid();
				Locatie loc = organisatieEenheidLocatieChoice.getLocatieCombo().getLocatie();
				if (getOpleiding() != null)
				{
					Team team = getOpleiding().selecteerTeam(orgeenh, loc);
					if (team != null)
						return team.toString();
				}
				return "";
			}
		});

		teamLabel.setOutputMarkupId(true);
		add(teamLabel);
	}

	private final class Vooropleidingen extends LoadableDetachableModel<List<IVooropleiding>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<IVooropleiding> load()
		{
			return getVerbintenis().getMogelijkeVooropleidingen();
		}
	}

	public IntakeWizardModel getWizard()
	{
		return wizardPage.getWizard();
	}

	@Override
	public Verbintenis getVerbintenis()
	{
		return getModelObject();
	}

	public IModel<Verbintenis> getVerbintenisModel()
	{
		return getModel();
	}

	private Opleiding getOpleiding()
	{
		if (getVerbintenis() != null)
			return getVerbintenis().getOpleiding();
		return null;
	}

	private boolean isStaatsexamen()
	{
		return getOpleiding() != null
			&& getOpleiding().getVerbintenisgebied().getTaxonomiecode().startsWith("5.SE");
	}

	private boolean isInburgering()
	{
		return getOpleiding() != null && getOpleiding().isInburgering();
	}

	protected boolean isVoegRelevanteVooropleidingToe()
	{
		return true;
	}
}
