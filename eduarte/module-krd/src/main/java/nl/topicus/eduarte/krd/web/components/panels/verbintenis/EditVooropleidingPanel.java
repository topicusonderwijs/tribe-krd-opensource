package nl.topicus.eduarte.krd.web.components.panels.verbintenis;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.cobra.web.components.quicksearch.ISelectListener;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.validators.BegindatumVoorEinddatumValidator;
import nl.topicus.cobra.web.validators.DatumGroterOfGelijkDatumValidator;
import nl.topicus.cobra.web.validators.DatumVerschilValidator;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.dao.helpers.SoortVooropleidingDataAccessHelper;
import nl.topicus.eduarte.entities.hogeronderwijs.VooropleidingVakResultaat;
import nl.topicus.eduarte.entities.hogeronderwijs.VooropleidingVerificatieStatus;
import nl.topicus.eduarte.entities.inschrijving.Schooladvies;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding;
import nl.topicus.eduarte.entities.inschrijving.Vooropleiding;
import nl.topicus.eduarte.entities.inschrijving.VooropleidingVak;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding.SoortOnderwijs;
import nl.topicus.eduarte.entities.inschrijving.Vooropleiding.SoortVooropleidingOrganisatie;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatieAdres;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.krd.web.components.panels.VrijVeldEntiteitEditPanel;
import nl.topicus.eduarte.web.components.choice.SchooladviesComboBox;
import nl.topicus.eduarte.web.components.modalwindow.verbintenis.VerificatieModalWindow;
import nl.topicus.eduarte.web.components.panels.datapanel.table.VooropleidingVakResultaatTable;
import nl.topicus.eduarte.web.components.panels.verbintenis.DeelnemerVooropleidingVakResultaatEditPanel;
import nl.topicus.eduarte.web.components.quicksearch.externeorganisatie.ExterneOrganisatieSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.land.LandSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.soortvooropleiding.SoortVooropleidingSearchEditor;
import nl.topicus.eduarte.zoekfilters.ExterneOrganisatieZoekFilter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * 
 * @author idserda
 * @author hop
 * 
 */
public class EditVooropleidingPanel extends TypedPanel<Vooropleiding>
{
	private static final long serialVersionUID = 1L;

	private WebMarkupContainer externeOrganisatieRow;

	private WebMarkupContainer landRow;

	private WebMarkupContainer soortVooropleidingRow;

	private WebMarkupContainer citoscoreRow;

	private WebMarkupContainer schooladviesRow;

	private WebMarkupContainer soortOrganisatieRow;

	private WebMarkupContainer naamRow;

	private WebMarkupContainer plaatsRow;

	private ExterneOrganisatieSearchEditor externeOrganisatieVeld;

	private TextField<String> naamVeld;

	private TextField<String> plaatsVeld;

	private DatumField begindatumField;

	private DatumField einddatumField;

	private TextField<Integer> aantalJarenOnderwijsField;

	private AjaxCheckBox aantalJarenZelfInvullenCheckbox;

	private Label categorieVooropleiding;

	private SoortVooropleidingOrganisatie soortOrganisatie;

	private boolean isDigitaleAanmelding;

	private boolean toonSchool;

	private ModelManager manager;

	private VerificatieModalWindow<Vooropleiding> verificatieModalWindow;

	private WebMarkupContainer verificatie;

	public EditVooropleidingPanel(String id, IModel<Vooropleiding> model,
			boolean isDigitaleAanmelding, ModelManager manager)
	{
		this(id, model, isDigitaleAanmelding, true, true, manager);
	}

	public EditVooropleidingPanel(String id, IModel<Vooropleiding> model,
			boolean isDigitaleAanmelding, boolean toonVooropleidingData, boolean toonSchool,
			ModelManager manager)
	{
		super(id, model);
		this.manager = manager;
		this.isDigitaleAanmelding = isDigitaleAanmelding;
		this.toonSchool = toonSchool;
		soortOrganisatie = getSoortVooropleidingOrganisatie();
		add(createExterneOrganisatieRow());
		add(createLandRow());
		add(createNaamVeld());
		add(createPlaatsVeld());
		add(createSoortOrganisatieRadioChoice());
		add(createCitoscoreRow());
		add(createSchooladviesRow());
		add(createSoortVooropleidingRow());

		categorieVooropleiding =
			new Label("categorieVooropleiding", new PropertyModel<SoortOnderwijs>(getModel(),
				"soortOnderwijs"));
		categorieVooropleiding.setOutputMarkupId(true);
		add(categorieVooropleiding);

		add(new JaNeeCombobox("diplomaBehaald", new PropertyModel<Boolean>(getModel(),
			"diplomaBehaald"), true)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target, Boolean newSelection)
			{
				target.addComponent(categorieVooropleiding);
			}
		}.setRequired(isDigitaleAanmelding));

		WebMarkupContainer begindatumRow = new WebMarkupContainer("begindatumRow");
		begindatumRow.setVisible(toonVooropleidingData);
		begindatumRow.setOutputMarkupPlaceholderTag(true);
		begindatumField =
			new DatumField("begindatum", new PropertyModel<Date>(getModel(), "begindatum"));
		begindatumField.add(new AantalJarenOnderwijsAjaxUpdater());
		begindatumRow.add(begindatumField);
		add(begindatumRow);

		WebMarkupContainer einddatumRow = new WebMarkupContainer("einddatumRow");
		einddatumRow.setVisible(toonVooropleidingData);
		einddatumRow.setOutputMarkupPlaceholderTag(true);
		einddatumField =
			new DatumField("einddatum", new PropertyModel<Date>(getModel(), "einddatum"));
		if (!isDigitaleAanmelding)
			einddatumField.add(new AantalJarenOnderwijsAjaxUpdater());
		einddatumField.setRequired(isDigitaleAanmelding);
		einddatumField.setVisible(toonVooropleidingData);
		einddatumRow.add(einddatumField);
		add(einddatumRow);

		WebMarkupContainer jarenOnderwijsRow = new WebMarkupContainer("jarenOnderwijsRow");
		jarenOnderwijsRow.setVisible(toonVooropleidingData);
		jarenOnderwijsRow.setOutputMarkupPlaceholderTag(true);
		aantalJarenOnderwijsField =
			new TextField<Integer>("aantalJarenOnderwijs", new PropertyModel<Integer>(getModel(),
				"aantalJarenOnderwijs"));
		aantalJarenOnderwijsField.add(new AantalJarenOnderwijsAjaxUpdater());
		aantalJarenOnderwijsField.setOutputMarkupId(true);
		aantalJarenOnderwijsField.setVisible(toonVooropleidingData);
		if (getVooropleiding() != null)
		{
			aantalJarenOnderwijsField.setEnabled(getVooropleiding().isAantalJarenZelfInvullen());
		}
		jarenOnderwijsRow.add(aantalJarenOnderwijsField);
		add(jarenOnderwijsRow);

		WebMarkupContainer jarenZelfInvullenRow = new WebMarkupContainer("jarenZelfInvullenRow");
		jarenOnderwijsRow.setVisible(toonVooropleidingData);
		jarenOnderwijsRow.setOutputMarkupPlaceholderTag(true);
		aantalJarenZelfInvullenCheckbox =
			new AjaxCheckBox("aantalJarenZelfInvullen", new PropertyModel<Boolean>(getModel(),
				"aantalJarenZelfInvullen"))
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target)
				{
					boolean zelfInvullen = getVooropleiding().isAantalJarenZelfInvullen();
					aantalJarenOnderwijsField.setEnabled(zelfInvullen);
					if (!zelfInvullen)
					{
						Integer aantalJarenOnderwijs =
							getVooropleiding().berekenAantalJarenOnderwijs();
						if (aantalJarenOnderwijs != null)
						{
							getVooropleiding().setAantalJarenOnderwijs(aantalJarenOnderwijs);
						}
					}
					target.addComponent(aantalJarenOnderwijsField);
				}
			};
		aantalJarenZelfInvullenCheckbox.setOutputMarkupId(true);
		aantalJarenZelfInvullenCheckbox.setVisible(toonVooropleidingData);
		jarenZelfInvullenRow.add(aantalJarenZelfInvullenCheckbox);
		add(jarenZelfInvullenRow);

		VrijVeldEntiteitEditPanel<Vooropleiding> VVEEPanel =
			new VrijVeldEntiteitEditPanel<Vooropleiding>("vrijVelden", getModel());
		VVEEPanel.getVrijVeldZoekFilter().setDossierScherm(true);
		VVEEPanel.getVrijVeldZoekFilter().setCategorie(VrijVeldCategorie.VOOROPLEIDING);
		add(VVEEPanel);

		PropertyModel<List<VooropleidingVakResultaat>> pmodel =
			new PropertyModel<List<VooropleidingVakResultaat>>(getModel(),
				"vooropleidingVakResultaten");

		DeelnemerVooropleidingVakResultaatEditPanel panel =
			new DeelnemerVooropleidingVakResultaatEditPanel("vakresultaateditpanel", pmodel,
				manager, new VooropleidingVakResultaatTable())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public VooropleidingVakResultaat createNewT()
				{
					VooropleidingVakResultaat ret =
						new VooropleidingVakResultaat(getVooropleiding());
					ret.setVak(new VooropleidingVak());
					return ret;
				}

				@Override
				public boolean isVisible()
				{
					return isHogerOnderwijs();
				}

			};
		add(panel);
		add(createVooropleidingVerificatiePanel());
		createVerificatieModalWindow();
		add(verificatieModalWindow);

	}

	public IModel<Vooropleiding> getVooropleidingModel()
	{
		return getModel();
	}

	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();
		if (!hasBeenRendered())
		{
			Form< ? > form = findParent(Form.class);
			form.add(new BegindatumVoorEinddatumValidator(begindatumField, einddatumField));
			Date geboorteDatum = getVooropleiding().getDeelnemer().getPersoon().getGeboortedatum();
			if (geboorteDatum != null)
			{
				form.add(new DatumGroterOfGelijkDatumValidator("Begindatum", begindatumField,
					geboorteDatum));
				form.add(new DatumGroterOfGelijkDatumValidator("Einddatum", einddatumField,
					geboorteDatum));
				aantalJarenOnderwijsField.add(new DatumVerschilValidator(geboorteDatum));
			}

		}
	}

	private WebMarkupContainer createExterneOrganisatieRow()
	{
		externeOrganisatieRow = new WebMarkupContainer("externeOrganisatieRow")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return EditVooropleidingPanel.this.isVisible()
					&& SoortVooropleidingOrganisatie.ExterneOrganisatie.equals(soortOrganisatie)
					&& toonSchool;
			}
		};
		externeOrganisatieRow.setOutputMarkupPlaceholderTag(true);

		ExterneOrganisatieZoekFilter filter = new ExterneOrganisatieZoekFilter();
		filter.setBijVooropleiding(true);

		externeOrganisatieVeld =
			new ExterneOrganisatieSearchEditor("externeOrganisatie",
				new PropertyModel<ExterneOrganisatie>(getModel(), "externeOrganisatie"), filter);

		externeOrganisatieVeld.addListener(new ISelectListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onUpdate(AjaxRequestTarget target)
			{
				onUpdateExterneOrganisatieCode(target, externeOrganisatieVeld.getModelObject());
			}
		});
		externeOrganisatieVeld.setOutputMarkupPlaceholderTag(true);
		externeOrganisatieVeld.setRequired(true);
		externeOrganisatieVeld.setLabel(new Model<String>("Onderwijsinstelling"));
		externeOrganisatieRow.add(externeOrganisatieVeld);
		return externeOrganisatieRow;
	}

	private WebMarkupContainer createLandRow()
	{
		landRow = new WebMarkupContainer("landRow")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return EditVooropleidingPanel.this.isVisible()
					&& SoortVooropleidingOrganisatie.Buitenland.equals(soortOrganisatie)
					&& toonSchool;
			}

		};
		LandSearchEditor land =
			new LandSearchEditor("land", new PropertyModel<Land>(getModel(), "land"));
		land.setLabel(new Model<String>("land"));
		landRow.add(land);
		landRow.setOutputMarkupPlaceholderTag(true);
		return landRow;
	}

	private WebMarkupContainer createNaamVeld()
	{
		naamRow = new WebMarkupContainer("naamRow")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return EditVooropleidingPanel.this.isVisible() && toonSchool;
			}
		};
		naamVeld = new TextField<String>("naam", new PropertyModel<String>(getModel(), "naam"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled()
			{
				return super.isEnabled()
					&& !SoortVooropleidingOrganisatie.ExterneOrganisatie.equals(soortOrganisatie);
			}
		};
		naamVeld.setOutputMarkupId(true);
		naamRow.add(naamVeld);
		return naamRow;
	}

	private WebMarkupContainer createPlaatsVeld()
	{
		plaatsRow = new WebMarkupContainer("plaatsRow")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return EditVooropleidingPanel.this.isVisible() && toonSchool;
			}
		};
		plaatsVeld =
			new TextField<String>("plaats", new PropertyModel<String>(getModel(), "plaats"))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isEnabled()
				{
					return super.isEnabled()
						&& !SoortVooropleidingOrganisatie.ExterneOrganisatie
							.equals(soortOrganisatie);

				}
			};
		plaatsVeld.setOutputMarkupId(true);
		plaatsRow.add(plaatsVeld);
		return plaatsRow;
	}

	private WebMarkupContainer createSoortOrganisatieRadioChoice()
	{
		soortOrganisatieRow = new WebMarkupContainer("soortOrganisatieRow")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return EditVooropleidingPanel.this.isVisible() && toonSchool;
			}
		};

		RadioChoice<SoortVooropleidingOrganisatie> rt =
			new RadioChoice<SoortVooropleidingOrganisatie>("soortOrganisatie",
				new PropertyModel<SoortVooropleidingOrganisatie>(getModel(), "soortOrganisatie"),
				Arrays.asList(SoortVooropleidingOrganisatie.values()));
		if (isDigitaleAanmelding)
		{
			rt =
				new RadioChoice<SoortVooropleidingOrganisatie>("soortOrganisatie", Arrays
					.asList(SoortVooropleidingOrganisatie.values()),
					new IChoiceRenderer<SoortVooropleidingOrganisatie>()
					{

						private static final long serialVersionUID = 1L;

						@Override
						public String getIdValue(SoortVooropleidingOrganisatie object, int index)
						{
							return Integer.toString(index);
						}

						@Override
						public Object getDisplayValue(SoortVooropleidingOrganisatie object)
						{
							if (object != null)
							{
								SoortVooropleidingOrganisatie svo = object;
								if (SoortVooropleidingOrganisatie.ExterneOrganisatie == svo)
									return "Binnenland";
								return svo.toString();
							}
							return "";
						}
					});
		}
		rt.add(createSoortOrganisatieUpdatingBehaviour());
		soortOrganisatieRow.add(rt);
		return soortOrganisatieRow;
	}

	private WebMarkupContainer createCitoscoreRow()
	{
		citoscoreRow = new WebMarkupContainer("citoscoreRow")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return EditVooropleidingPanel.this.isVisible()
					&& isBasisOfVoortgezetOnderwijsSoort() && !isDigitaleAanmelding;
			}

		};
		TextField<Integer> citoscore =
			new TextField<Integer>("citoscore", new PropertyModel<Integer>(getModel(), "citoscore"));
		citoscoreRow.setOutputMarkupPlaceholderTag(true);
		citoscoreRow.add(citoscore);
		return citoscoreRow;
	}

	private WebMarkupContainer createSchooladviesRow()
	{
		schooladviesRow = new WebMarkupContainer("schooladviesRow")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return EditVooropleidingPanel.this.isVisible()
					&& isBasisOfVoortgezetOnderwijsSoort() && !isDigitaleAanmelding;
			}

		};
		schooladviesRow.setOutputMarkupPlaceholderTag(true);
		schooladviesRow.add(new SchooladviesComboBox("schooladvies",
			new PropertyModel<Schooladvies>(getModel(), "schooladvies")));
		return schooladviesRow;
	}

	private WebMarkupContainer createSoortVooropleidingRow()
	{
		soortVooropleidingRow = new WebMarkupContainer("soortVooropleidingRow");
		soortVooropleidingRow.setOutputMarkupId(true);
		SoortVooropleidingSearchEditor editor =
			new SoortVooropleidingSearchEditor("soortVooropleiding",
				new PropertyModel<SoortVooropleiding>(getModel(), "soortVooropleiding"));
		editor.addListener(new ISelectListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onUpdate(AjaxRequestTarget target)
			{
				target.addComponent(citoscoreRow);
				target.addComponent(schooladviesRow);
				target.addComponent(categorieVooropleiding);
			}
		});
		editor.setRequired(true);
		editor.setLabel(new Model<String>("Soort vooropleiding"));
		soortVooropleidingRow.add(editor);
		return soortVooropleidingRow;
	}

	private AjaxFormChoiceComponentUpdatingBehavior createSoortOrganisatieUpdatingBehaviour()
	{
		return new AjaxFormChoiceComponentUpdatingBehavior()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				soortOrganisatie =
					(SoortVooropleidingOrganisatie) ((RadioChoice< ? >) getComponent())
						.getModelObject();
				if (SoortVooropleidingOrganisatie.ExterneOrganisatie.equals(soortOrganisatie))
				{
					setPlaats(null);
					setOrganisatie(null);
					onUpdateSoortOrganisatie(target, Land.getNederland());
				}
				else if (SoortVooropleidingOrganisatie.Buitenland.equals(soortOrganisatie))
				{
					setExterneOrganisatie(null);
					onUpdateSoortOrganisatie(target, null);
				}
				else if (SoortVooropleidingOrganisatie.Overig.equals(soortOrganisatie))
				{
					setExterneOrganisatie(null);
					onUpdateSoortOrganisatie(target, Land.getOnbekend());
				}
			}
		};
	}

	private void onUpdateSoortOrganisatie(AjaxRequestTarget target, Land land)
	{
		setLand(land);

		target.addComponent(soortVooropleidingRow);
		target.addComponent(externeOrganisatieRow);
		target.addComponent(landRow);
		target.addComponent(naamVeld);
		target.addComponent(plaatsVeld);
	}

	private boolean isBasisOfVoortgezetOnderwijsSoort()
	{
		SoortVooropleiding soort = getVooropleiding().getSoortVooropleiding();
		return soort != null
			&& (soort.getSoortOnderwijsMetDiploma().isBasisOfVoortgezetOnderwijs() || soort
				.getSoortOnderwijsZonderDiploma().isBasisOfVoortgezetOnderwijs());
	}

	private void onUpdateExterneOrganisatieCode(AjaxRequestTarget target,
			ExterneOrganisatie externeOrganisatie)
	{
		if (externeOrganisatie != null)
		{
			setExterneOrganisatie(externeOrganisatie);
			setOrganisatie(externeOrganisatie.getNaam());
			setPlaats(getBrinPlaats(externeOrganisatie));

			// Als het een BRIN externe organisatie en een basisschool is, selecteer dan
			// automatisch de bijbehorende soort vooropleiding
			if (externeOrganisatie instanceof Brin)
			{
				Brin brin = (Brin) externeOrganisatie;

				if (brin.getOnderwijssector() != null
					&& brin.getOnderwijssector().isBasisonderwijs())
				{
					SoortVooropleidingDataAccessHelper helper =
						DataAccessRegistry.getHelper(SoortVooropleidingDataAccessHelper.class);
					SoortVooropleiding soort = helper.get(SoortOnderwijs.Basisonderwijs);
					if (soort != null)
					{
						getVooropleiding().setSoortVooropleiding(soort);
						target.addComponent(soortVooropleidingRow);
						target.addComponent(citoscoreRow);
						target.addComponent(schooladviesRow);
					}
				}
			}
		}
		else
		{
			setOrganisatie(null);
			setPlaats(null);
		}

		target.addComponent(externeOrganisatieVeld);
		target.addComponent(naamVeld);
		target.addComponent(plaatsVeld);

	}

	private String getBrinPlaats(ExterneOrganisatie externeOrganisatie)
	{
		ExterneOrganisatieAdres eersteAdresOpPeilDatum = externeOrganisatie.getFysiekAdres();

		if (eersteAdresOpPeilDatum != null)
		{
			return eersteAdresOpPeilDatum.getAdres().getPlaats();
		}
		return null;
	}

	private Vooropleiding getVooropleiding()
	{
		return getModel().getObject();
	}

	private SoortVooropleidingOrganisatie getSoortVooropleidingOrganisatie()
	{
		if (getVooropleiding() != null)
		{
			return getVooropleiding().getSoortOrganisatie();
		}
		else
			return null;
	}

	private void setLand(Land land)
	{
		getVooropleiding().setLand(land);
	}

	private void setExterneOrganisatie(ExterneOrganisatie externeOrganisatie)
	{
		getVooropleiding().setExterneOrganisatie(externeOrganisatie);
	}

	private void setPlaats(String plaats)
	{
		getVooropleiding().setPlaats(plaats);
	}

	private void setOrganisatie(String organisatie)
	{
		getVooropleiding().setNaam(organisatie);
	}

	private class AantalJarenOnderwijsAjaxUpdater extends AjaxFormComponentUpdatingBehavior
	{
		private static final long serialVersionUID = 1L;

		private AantalJarenOnderwijsAjaxUpdater()
		{
			super("onblur");
		}

		@Override
		protected void onUpdate(AjaxRequestTarget target)
		{
			Vooropleiding vooropleiding = getVooropleiding();
			if (!vooropleiding.isAantalJarenZelfInvullen())
			{
				Integer aantalJarenOnderwijs = vooropleiding.berekenAantalJarenOnderwijs();
				if (aantalJarenOnderwijs != null)
				{
					vooropleiding.setAantalJarenOnderwijs(aantalJarenOnderwijs);
					target.addComponent(aantalJarenOnderwijsField);
				}
			}
		}
	}

	private void createVerificatieModalWindow()
	{
		this.verificatieModalWindow =
			new VerificatieModalWindow<Vooropleiding>("verificatieModalWindow", "Verificatie",
				createVooropleidingVerificatieAutoFieldSet(getVerificatieStatussen()), Arrays
					.asList(verificatie));
	}

	private WebMarkupContainer createVooropleidingVerificatiePanel()
	{
		verificatie = new WebMarkupContainer("verificatie");
		verificatie.add(createVooropleidingVerificatieAutoFieldSet(null));
		verificatie.add(createVerificatieUitvoerenLink());
		verificatie.add(createVerificatieAfmeldenLink());
		verificatie.setOutputMarkupId(true);
		verificatie.setVisible(isHogerOnderwijs());
		return verificatie;
	}

	private AutoFieldSet<Vooropleiding> createVooropleidingVerificatieAutoFieldSet(
			List<VooropleidingVerificatieStatus> enumAvailableStatusList)
	{
		AutoFieldSet<Vooropleiding> verificatieAutoFieldSet =
			new AutoFieldSet<Vooropleiding>("verificatieFieldset", getModel(), "Verificatie");
		verificatieAutoFieldSet.setPropertyNames("verificatieStatus", "verificatieDatum",
			"verificatieDoor", "verificatieDoorInstelling", "verificatieDoorMedewerker");

		if (enumAvailableStatusList != null)
		{
			verificatieAutoFieldSet.addFieldModifier(new ConstructorArgModifier(
				"verificatieStatus", ModelFactory.getModel(enumAvailableStatusList)));
		}

		verificatieAutoFieldSet.setRenderMode(RenderMode.DISPLAY);
		return verificatieAutoFieldSet;
	}

	private AjaxLink<Object> createVerificatieAfmeldenLink()
	{
		AjaxLink<Object> gbaVerificatieUitvoerenLink =
			new AjaxLink<Object>("vooropleidingVerificatieAfmelden")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target)
				{
					getVooropleiding()
						.setVerificatieStatus(VooropleidingVerificatieStatus.Afmelden);
					target.addComponent(verificatie);
				}

				@Override
				public boolean isVisible()
				{
					if (getVooropleiding() == null
						|| getVooropleiding().getVerificatieStatus() == null || manager == null)
						return false;
					return getVooropleiding().getVerificatieStatus().kanAfgemeldWorden()
						&& isHogerOnderwijs();
				}
			};
		gbaVerificatieUitvoerenLink.add(new Label("verificatieAfmelden", "Verificatie afmelden"));
		return gbaVerificatieUitvoerenLink;
	}

	private AjaxLink<Object> createVerificatieUitvoerenLink()
	{
		AjaxLink<Object> gbaVerificatieUitvoerenLink =
			new AjaxLink<Object>("vooropleidingVerificatieUitvoeren")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target)
				{
					verificatieModalWindow.show(target);
				}

				@Override
				public boolean isVisible()
				{
					if (manager == null)
						return false;
					if (getVooropleiding() == null
						|| getVooropleiding().getVerificatieStatus() == null)
						return true;
					return !getVooropleiding().getVerificatieStatus().isOK() && isHogerOnderwijs();
				}
			};
		gbaVerificatieUitvoerenLink.add(new Label("verificatieUitvoeren", "Verificatie uitvoeren"));
		return gbaVerificatieUitvoerenLink;
	}

	private List<VooropleidingVerificatieStatus> getVerificatieStatussen()
	{
		return Arrays.asList(VooropleidingVerificatieStatus.DecentrlGeverifrdInst,
			VooropleidingVerificatieStatus.NietGeverifrdIBInst,
			VooropleidingVerificatieStatus.Afmelden);
	}

	protected Boolean isHogerOnderwijs()
	{
		return EduArteApp.get().isModuleActive(EduArteModuleKey.HOGER_ONDERWIJS);
	}

	@Override
	protected void onDetach()
	{
		getModel().detach();
		super.onDetach();
	}

}
