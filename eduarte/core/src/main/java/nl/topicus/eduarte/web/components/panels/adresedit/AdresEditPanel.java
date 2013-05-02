package nl.topicus.eduarte.web.components.panels.adresedit;

import java.util.Date;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.converters.PostcodeConverter;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.cobra.web.components.quicksearch.ISelectListener;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.components.text.PostcodeTextField;
import nl.topicus.eduarte.dao.webservices.PostcodeDataAccessHelper.Adres;
import nl.topicus.eduarte.entities.adres.AdresEntiteit;
import nl.topicus.eduarte.entities.adres.AdresseerbaarUtil;
import nl.topicus.eduarte.entities.adres.DuitseDeelstaat;
import nl.topicus.eduarte.entities.adres.filter.OnbeeindigdAdresFilter;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.web.components.quicksearch.land.LandSearchEditor;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.DateValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;

public class AdresEditPanel<T extends AdresEntiteit<T>> extends TypedPanel<T>
{
	private static final long serialVersionUID = 1L;

	public static enum Mode
	{
		INLINE,
		POPUP,
		ADRES_ONLY
	}

	private TextField<String> straat;

	private TextField<String> plaats;

	private TextField<String> postcode;

	private TextField<String> huisnummer;

	private TextField<String> huisnummerToevoeging;

	private LandSearchEditor land;

	private DatumField begindatumField;

	private DatumField einddatumField;

	private Form<Void> panelForm;

	protected Mode mode;

	private WebMarkupContainer deelstaatContainer;

	public AdresEditPanel(String id, IModel<T> adresModel, Mode mode)
	{
		this(id, adresModel, mode, null);
	}

	public AdresEditPanel(String id, IModel<T> adresModel, Mode mode, String header)
	{
		super(id, new CompoundPropertyModel<T>(adresModel));

		this.mode = mode;
		panelForm = new Form<Void>("adresform");
		add(panelForm);
		addFields(header);
		if (Mode.POPUP.equals(mode))
			setDateValidators();
		setStraatPlaatsValidator();

		postcode.add(new AdresEditPostcodeLookupBehavior("onchange", postcode, huisnummer, land,
			huisnummerToevoeging));
		huisnummer.add(new AdresEditPostcodeLookupBehavior("onchange", postcode, huisnummer, land,
			huisnummerToevoeging));
		huisnummerToevoeging.add(new AdresEditPostcodeLookupBehavior("onchange", postcode,
			huisnummer, land, huisnummerToevoeging));
	}

	public Mode getMode()
	{
		return mode;
	}

	private void setStraatPlaatsValidator()
	{
		panelForm.add(new IFormValidator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public FormComponent< ? >[] getDependentFormComponents()
			{
				FormComponent< ? >[] list = new FormComponent[2];
				list[0] = plaats;
				list[1] = straat;

				return list;
			}

			@Override
			public void validate(Form< ? > form)
			{
				String plaatsString = plaats.getConvertedInput();
				String straatString = straat.getConvertedInput();
				if (Land.getNederland().equals(land.getModelObject()))
				{
					AdresEditPostcodeLookupBehavior lookup =
						new AdresEditPostcodeLookupBehavior("onchange", postcode, huisnummer, land,
							huisnummerToevoeging);
					lookup.bind(postcode);
					// geef een dummy ajax target mee om NPE te voorkomen
					lookup.onUpdate(new AjaxRequestTarget(null));

					plaatsString = plaats.getModelObject();
					straatString = straat.getModelObject();
				}
				if (StringUtil.isEmpty(plaatsString))
				{
					plaats.error(plaats.getLabel().getObject().toString() + " mag niet leeg zijn.");
				}
				if (StringUtil.isEmpty(straatString))
				{
					straat.error(straat.getLabel().getObject().toString() + " mag niet leeg zijn.");
				}
			}
		});
	}

	private void addFields(String header)
	{
		WebMarkupContainer headerContainer = new WebMarkupContainer("headerContainer");
		panelForm.add(headerContainer.setVisible(header != null));

		headerContainer.add(new Label("header", header));

		land =
			new LandSearchEditor("adres.land", new PropertyModel<Land>(getDefaultModel(),
				"adres.land"));
		land.addListener(new ISelectListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onUpdate(AjaxRequestTarget target)
			{
				onLandChange(target, land.getModelObject());
			}
		});
		land.setRequired(true);
		land.setLabel(new AdresLabelModel("Land"));
		panelForm.add(land);

		initDeelstaatField();

		initPostcodeField();

		huisnummer = new TextField<String>("adres.huisnummer")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isRequired()
			{
				return isNederland();
			}
		};
		huisnummer.setLabel(new AdresLabelModel("Huisnummer"));
		huisnummer.setOutputMarkupId(true);
		panelForm.add(ComponentUtil.fixLength(huisnummer,
			nl.topicus.eduarte.entities.adres.Adres.class, "huisnummer"));

		huisnummerToevoeging = new TextField<String>("adres.huisnummerToevoeging");
		huisnummerToevoeging.setLabel(new AdresLabelModel("Huisnummertoevoeging"));
		panelForm.add(ComponentUtil.fixLength(huisnummerToevoeging,
			nl.topicus.eduarte.entities.adres.Adres.class, "huisnummerToevoeging"));

		straat = new TextField<String>("adres.straat")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled()
			{
				return !isNederland();
			}

			@Override
			public boolean isRequired()
			{
				return !isNederland();
			}
		};
		straat.setOutputMarkupId(true);
		straat.setLabel(new AdresLabelModel("Straat"));
		straat.setRequired(true);
		panelForm.add(ComponentUtil.fixLength(straat,
			nl.topicus.eduarte.entities.adres.Adres.class, "straat"));

		plaats = new TextField<String>("adres.plaats")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled()
			{
				return !isNederland();
			}

			@Override
			public boolean isRequired()
			{
				return !isNederland();
			}
		};
		plaats.setOutputMarkupId(true);
		plaats.setLabel(new AdresLabelModel("Plaats"));
		plaats.setRequired(true);
		panelForm.add(ComponentUtil.fixLength(plaats,
			nl.topicus.eduarte.entities.adres.Adres.class, "plaats"));

		WebMarkupContainer datumVelden = new WebMarkupContainer("datumVelden")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && !Mode.ADRES_ONLY.equals(mode);
			}
		};
		panelForm.add(datumVelden);

		einddatumField = new DatumField("einddatum");
		datumVelden.add(einddatumField);

		begindatumField = new DatumField("begindatum");
		begindatumField.setRequired(true);
		begindatumField.setLabel(new AdresLabelModel("Begindatum"));
		datumVelden.add(begindatumField);

		panelForm.add(new AjaxCheckBox("adres.geheim")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
			}
		});

		WebMarkupContainer postWoonChecks = new WebMarkupContainer("postWoonChecks")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && Mode.POPUP.equals(mode);
			}
		};
		panelForm.add(postWoonChecks);

		postWoonChecks.add(new Label("fysiekLabel"));
		postWoonChecks.add(new AjaxCheckBox("postadres")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled()
			{
				return super.isEnabled();
			}

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
			}
		});
		postWoonChecks.add(new AjaxCheckBox("fysiekadres")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled()
			{
				return super.isEnabled();
			}

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
			}
		});
		postWoonChecks.add(new AjaxCheckBox("factuuradres")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled()
			{
				return super.isEnabled();
			}

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
			}
		});
	}

	private void initDeelstaatField()
	{
		deelstaatContainer = new WebMarkupContainer("deelstaatContainer")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				T adresEntiteit = AdresEditPanel.this.getModelObject();

				return adresEntiteit != null && adresEntiteit.getAdres().getLand() != null
					&& adresEntiteit.getAdres().getLand().isDuitsland();
			}
		};
		deelstaatContainer.setOutputMarkupPlaceholderTag(true);
		add(deelstaatContainer);
		deelstaatContainer.add(new EnumCombobox<DuitseDeelstaat>("adres.duitseDeelstaat",
			DuitseDeelstaat.values()));
		panelForm.add(deelstaatContainer);
	}

	private void initPostcodeField()
	{
		postcode = new PostcodeTextField("adres.postcode")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isRequired()
			{
				return isNederland();
			}
		};
		postcode.setLabel(new AdresLabelModel("Postcode"));
		postcode.add(new PostcodeValidator());
		postcode.add(new StringValidator()
		{
			private static final long serialVersionUID = 1L;

			public final int getLength()
			{
				if (isNederland())
					return 7;
				else
					return 12;
			}

			@Override
			protected void onValidate(IValidatable<String> validatable)
			{
				if (validatable.getValue().length() > getLength())
				{
					error(validatable);
				}
			}

			@Override
			protected String resourceKey()
			{
				return "StringValidator.maximum";
			}

			@SuppressWarnings("unchecked")
			@Override
			protected Map variablesMap(IValidatable validatable)
			{
				Map map = super.variablesMap(validatable);
				map.put("length", new Integer(((String) validatable.getValue()).length()));
				map.put("maximum", new Integer(getLength()));
				return map;
			}
		});
		postcode.setOutputMarkupId(true);
		panelForm.add(ComponentUtil.fixLength(postcode,
			nl.topicus.eduarte.entities.adres.Adres.class, "postcode"));
	}

	public TextField<String> getPostcode()
	{
		return postcode;
	}

	protected void onLandChange(AjaxRequestTarget target,
			@SuppressWarnings("unused") Land nieuwGekozenLand)
	{
		target.addComponent(straat);
		target.addComponent(plaats);
		target.addComponent(huisnummer);
		target.addComponent(postcode);
		target.addComponent(deelstaatContainer);
	}

	@SuppressWarnings("unused")
	protected void onPostcode(Adres gevonden, AjaxRequestTarget target)
	{
		nl.topicus.eduarte.entities.adres.Adres adres = getModelObject().getAdres();

		adres.setStraat(gevonden.getStraatnaam());
		adres.setHuisnummer(gevonden.getHuisnummer());
		adres.setPostcode(gevonden.getPostcode());
		adres.setPlaats(gevonden.getPlaatsnaam());
		adres.setGemeente(gevonden.getGemeente());
		adres.setProvincie(gevonden.getProvincie());
		adres.setLand(gevonden.getLand());
		adres.setHuisnummerToevoeging(gevonden.getHuisnummerToevoeging());

		panelForm.modelChanged();
	}

	private List<T> getOnbeeindigdeAdressen()
	{
		return AdresseerbaarUtil.getAdressen(getAdres().getEntiteit(), new OnbeeindigdAdresFilter(
			false));
	}

	private T getAdres()
	{
		return getModelObject();
	}

	private Land getLand()
	{
		return land.getModelObject();
	}

	private void setDateValidators()
	{
		if (!getAdres().isSaved())
		{
			// zoek de minimale begindatum, maar alleen als er zowel een post als factuur
			// als fysiek adres is
			Date minimumDatum = null;
			boolean minVoorPost = false;
			boolean minVoorFysiek = false;
			boolean minVoorFactuur = false;
			for (T curAdres : getOnbeeindigdeAdressen())
			{
				if (minimumDatum == null || curAdres.getBegindatum().before(minimumDatum))
					minimumDatum = curAdres.getBegindatum();
				minVoorPost |= curAdres.isPostadres();
				minVoorFysiek |= curAdres.isFysiekadres();
				minVoorFactuur |= curAdres.isFactuuradres();
			}
			if (minimumDatum != null && minVoorPost && minVoorFysiek && minVoorFactuur)
				begindatumField.add(DateValidator.minimum(TimeUtil.getInstance().addDays(
					minimumDatum, 1)));
		}
	}

	private boolean isNederland()
	{
		return Land.getNederland().equals(getLand());
	}

	private final class PostcodeValidator extends PatternValidator
	{
		private static final long serialVersionUID = 1L;

		public PostcodeValidator()
		{
			super(PostcodeConverter.POSTCODE_NL_REGEX);
		}

		@Override
		protected void onValidate(IValidatable<String> validatable)
		{
			if (isNederland())
				super.onValidate(validatable);
		}
	}

	private class AdresLabelModel extends AbstractReadOnlyModel<String>
	{
		private static final long serialVersionUID = 1L;

		private String fieldname;

		private AdresLabelModel(String fieldname)
		{
			this.fieldname = fieldname;
		}

		@Override
		public String getObject()
		{
			T adres = getAdres();
			if (adres.isPostEnFysiekAdres())
				return fieldname;
			else if (adres.isPostadres())
				return fieldname + " van postadres";
			return fieldname + " van " + getAdres().getFysiekLabel().toLowerCase();
		}
	}

	private final class AdresEditPostcodeLookupBehavior extends PostcodeLookupBehavior
	{
		private static final long serialVersionUID = 1L;

		private AdresEditPostcodeLookupBehavior(String event, TextField<String> postcode,
				TextField<String> huisnummer, LandSearchEditor land,
				TextField<String> huisnummerToevoeging)
		{
			super(event, postcode, huisnummer, land, huisnummerToevoeging);

		}

		@Override
		protected void onPostcode(AjaxRequestTarget target, Adres result)
		{
			AdresEditPanel.this.onPostcode(result, target);
			target.addComponent(panelForm);
		}
	}
}
