package nl.topicus.eduarte.resultaten.web.pages.shared;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ExtendedModel;
import nl.topicus.cobra.modelsv2.ObjectState;
import nl.topicus.cobra.web.behaviors.AjaxFormComponentSaveBehaviour;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.BehaviorModifier;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.EnableModifier;
import nl.topicus.cobra.web.components.form.modifier.HtmlClassModifier;
import nl.topicus.cobra.web.components.form.modifier.ValidateModifier;
import nl.topicus.cobra.web.components.form.modifier.VisibilityModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.pages.FeedbackComponent;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.cobra.web.security.RequiredSecurityCheck;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaal;
import nl.topicus.eduarte.entities.resultaatstructuur.Scoreschaalwaarde;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.ToetsVerwijzing;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaal.Schaaltype;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.Rekenregel;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.Scoreschaal;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.SoortToets;
import nl.topicus.eduarte.resultaten.web.components.modalwindow.ScoreschaalwaardeEditPanel;
import nl.topicus.eduarte.resultaten.web.components.quicksearch.ToetsVerwijzingQuickSearchToevoegenPanel;
import nl.topicus.eduarte.resultaten.web.components.resultaat.ToetsSchaallengteValidator;
import nl.topicus.eduarte.resultaten.web.components.security.ResultaatstructuurSecurityCheck;
import nl.topicus.eduarte.web.components.autoform.EduArteAjaxRefreshModifier;
import nl.topicus.eduarte.web.components.autoform.ModuleBoundFieldModifier;
import nl.topicus.eduarte.web.pages.AbstractDynamicContextPage;
import nl.topicus.eduarte.web.pages.SubpageContext;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.apache.wicket.validation.validator.RangeValidator;

@PageInfo(title = "Toets bewerken", menu = "Onderwijs > Onderwijsproducten > [onderwijsproduct] > Resultaten > Bewerken > [Toets]")
@RequiredSecurityCheck(ResultaatstructuurSecurityCheck.class)
public abstract class AbstractOnderwijsproductToetsEditPage extends
		AbstractDynamicContextPage<Toets> implements IEditPage
{
	private ResultaatstructuurEditPage returnPage;

	private ObjectState originalToets;

	public AbstractOnderwijsproductToetsEditPage(ExtendedModel<Toets> toetsModel,
			ResultaatstructuurEditPage returnPage)
	{
		super(toetsModel, new SubpageContext(returnPage));

		this.returnPage = returnPage;
		originalToets = toetsModel.getState();

		initToets();
	}

	protected void initToets()
	{
		if (isVariant())
		{
			getToets().setSoort(SoortToets.Toets);
			getToets().setVerplicht(false);
			getToets().setSamengesteld(false);
			getToets().setAantalHerkansingen(0);
			getToets().setAlternatiefResultaatMogelijk(false);
		}
	}

	protected int getMinimumAantalHerkansingen()
	{
		int max = 0;
		if (getToets().isSamengesteldMetVarianten())
		{
			for (Toets curDeeltoets : getToets().getChildren())
			{
				max = Math.max(curDeeltoets.getVariantVoorPoging(), max);
			}
		}
		return max;
	}

	protected int getMaximumAantalHerkansingen()
	{
		return getToets().getScoreschaal().equals(Scoreschaal.Lineair) ? 2 : Toets.MAX_HERKANSINGEN;
	}

	protected boolean isVariant()
	{
		return getToets().isVariant();
	}

	protected Toets getToets()
	{
		return getContextModelObject();
	}

	protected ExtendedModel<Toets> getToetsModel()
	{
		return (ExtendedModel<Toets>) getContextModel();
	}

	public ResultaatstructuurEditPage getReturnPage()
	{
		return returnPage;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new AbstractBottomRowButton(panel, "Annuleren", CobraKeyAction.ANNULEREN,
			ButtonAlignment.RIGHT)
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
						getToetsModel().setState(originalToets);
						setResponsePage(returnPage);
					}
				};
			}
		});
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		returnPage.detach();

	}

	protected void addToetsToStructuur(Toets toets)
	{
		if (!toets.getResultaatstructuur().getToetsen().contains(toets))
		{
			toets.getResultaatstructuur().getToetsen().add(toets);
			toets.getParent().getChildren().add(toets);
		}
		if (toets.isSamengesteld() && !toets.isSamengesteldMetVarianten())
			toets.setAantalHerkansingen(0);
		if (!toets.isSamengesteld() && toets.getScoreschaal().equals(Scoreschaal.Geen))
			toets.setOverschrijfbaar(false);
		if (toets.isSamengesteld())
			toets.setStudiepunten(null);
		returnPage.addToetsToUpdate(toets);
	}

	protected void addModifiersVoorSoortCodeEnSchaal(AutoFieldSet<Toets> toetsInput)
	{
		toetsInput.addFieldModifier(new ValidateModifier(new AbstractValidator<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onValidate(IValidatable<String> validatable)
			{
				if (getToets().getParent() != null
					&& getToets().getParent().isCodeUsed(getToets(),
						validatable.getValue().toString()))
				{
					ValidationError error = new ValidationError();
					error.setMessage("De opgegeven code is reeds in gebruik");
					validatable.error(error);
				}
			}
		}, "code"));
		toetsInput.addFieldModifier(new EnableModifier(!getToets().getHeeftResultaten(), "schaal"));
		toetsInput.addFieldModifier(new ConstructorArgModifier("soort", SoortToets
			.mogelijkeWaarden(getToets().getResultaatstructuur().getOnderwijsproduct())));
		toetsInput.addFieldModifier(new BehaviorModifier(new SimpleAttributeModifier("maxlength",
			Integer.toString(10)), "persoonlijkeToetscode"));
	}

	protected EduArteAjaxRefreshModifier addModifiersVoorWeging(AutoFieldSet<Toets> toetsInput,
			final boolean wizard)
	{
		toetsInput.addFieldModifier(new ModuleBoundFieldModifier(
			EduArteModuleKey.FORMATIEVE_RESULTATEN, "automatischeWeging")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean isVisible()
			{
				return super.isVisible() && (!wizard || !getToets().isEindresultaat());
			}
		});
		toetsInput.addFieldModifier(new EnableModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return !getToets().isAutomatischeWeging();
			}
		}, "weging", "samengesteld", "samengesteldMetVarianten"));
		EduArteAjaxRefreshModifier refreshModifier =
			new EduArteAjaxRefreshModifier("automatischeWeging", "weging")
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target)
				{
					getToets().setSamengesteld(true);
					getToets().setSamengesteldMetVarianten(false);
				}
			};
		toetsInput.addFieldModifier(refreshModifier);
		return refreshModifier;
	}

	protected ScoreschaalwaardeEditPanel createScoreTabel()
	{
		ScoreschaalwaardeEditPanel scoreTabel =
			new ScoreschaalwaardeEditPanel("scorewaarden",
				new PropertyModel<List<Scoreschaalwaarde>>(getToetsModel(), "scoreschaalwaarden"),
				getToetsModel().getManager())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Scoreschaalwaarde createNewT()
				{
					return new Scoreschaalwaarde(getToets());
				}

				@Override
				public boolean isVisible()
				{
					return super.isVisible()
						&& Scoreschaal.Tabel.equals(getToets().getScoreschaal());
				}
			};
		scoreTabel.setOutputMarkupPlaceholderTag(true);
		return scoreTabel;
	}

	protected AutoFieldSet<Toets> createScoreLineair()
	{
		AutoFieldSet<Toets> scoreLineair =
			new AutoFieldSet<Toets>("scoreLineair", getToetsModel(), "Lineaire scoreschaal")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return super.isVisible()
						&& Scoreschaal.Lineair.equals(getToets().getScoreschaal());
				}
			};
		scoreLineair.setOutputMarkupPlaceholderTag(true);
		scoreLineair.setPropertyNames("scoreschaalLengteTijdvak1", "scoreschaalNormeringTijdvak1",
			"scoreschaalLengteTijdvak2", "scoreschaalNormeringTijdvak2",
			"scoreschaalLengteTijdvak3", "scoreschaalNormeringTijdvak3");
		scoreLineair.addFieldModifier(new EnableModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				if (isVariant())
					return getToets().getVariantVoorPoging() == 0;
				return getToets().getAantalHerkansingen() >= 0;
			}
		}, "scoreschaalLengteTijdvak1", "scoreschaalNormeringTijdvak1"));
		scoreLineair.addFieldModifier(new EnableModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				if (isVariant())
					return getToets().getVariantVoorPoging() == 1;
				return getToets().getAantalHerkansingen() >= 1;
			}
		}, "scoreschaalLengteTijdvak2", "scoreschaalNormeringTijdvak2"));
		scoreLineair.addFieldModifier(new EnableModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				if (isVariant())
					return getToets().getVariantVoorPoging() == 2;
				return getToets().getAantalHerkansingen() >= 2;
			}
		}, "scoreschaalLengteTijdvak3", "scoreschaalNormeringTijdvak3"));
		scoreLineair.addModifier("scoreschaalNormeringTijdvak1", new RangeValidator<BigDecimal>(
			new BigDecimal("-2.0"), new BigDecimal("4.0")));
		scoreLineair.addModifier("scoreschaalNormeringTijdvak2", new RangeValidator<BigDecimal>(
			new BigDecimal("-2.0"), new BigDecimal("4.0")));
		scoreLineair.addModifier("scoreschaalNormeringTijdvak3", new RangeValidator<BigDecimal>(
			new BigDecimal("-2.0"), new BigDecimal("4.0")));
		if (getToets().isSaved())
		{
			scoreLineair.addModifier("scoreschaalLengteTijdvak1", new ToetsSchaallengteValidator(
				getToetsModel(), 0));
			scoreLineair.addModifier("scoreschaalLengteTijdvak2", new ToetsSchaallengteValidator(
				getToetsModel(), 1));
			scoreLineair.addModifier("scoreschaalLengteTijdvak3", new ToetsSchaallengteValidator(
				getToetsModel(), 2));
		}
		scoreLineair.setRenderMode(RenderMode.EDIT);
		return scoreLineair;
	}

	protected void refreshScoreschaal(boolean clear,
			EduArteAjaxRefreshModifier scoreSchaalChangeRefresh, AutoFieldSet<Toets> fieldset,
			AjaxRequestTarget target)
	{
		if (clear)
			getToets().setScoreschaal(Scoreschaal.Geen);
		if (scoreSchaalChangeRefresh != null)
		{
			scoreSchaalChangeRefresh.refreshComponents(fieldset, target);
			target.addComponent(fieldset.findFieldComponent("scoreschaal"));
		}
	}

	protected void addModifiersVoorAlternatief(final AutoFieldSet<Toets> ret)
	{
		ret.addFieldModifier(new EnableModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return getToets().isAlternatiefResultaatMogelijk();
			}
		}, "alternatiefCombinerenMetHoofd"));
		ret.addFieldModifier(new EduArteAjaxRefreshModifier("alternatiefResultaatMogelijk",
			"alternatiefCombinerenMetHoofd"));
	}

	protected void addModifiersVoorAantalHerkansingen(final AutoFieldSet<Toets> fieldset,
			final EduArteAjaxRefreshModifier scoreSchaalChangeRefresh)
	{
		fieldset.addFieldModifier(new EduArteAjaxRefreshModifier("aantalHerkansingen")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				refreshScoreschaal(false, scoreSchaalChangeRefresh, fieldset, target);
			}

			@Override
			protected void onError(AjaxRequestTarget target)
			{
				FormComponent< ? > herkansingen =
					(FormComponent< ? >) fieldset.findFieldComponent("aantalHerkansingen");
				herkansingen.clearInput();
				FeedbackComponent feedback = fieldset.findParent(FeedbackComponent.class);
				feedback.refreshFeedback(target);
				refreshScoreschaal(false, scoreSchaalChangeRefresh, fieldset, target);
			}
		});

		fieldset.addModifier("aantalHerkansingen", new RangeValidator<Integer>(0, 0)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Integer getMaximum()
			{
				return getMaximumAantalHerkansingen();
			}

			@Override
			public Integer getMinimum()
			{
				return getMinimumAantalHerkansingen();
			}
		});
	}

	protected EduArteAjaxRefreshModifier addModifiersVoorScoreschaal(
			final AutoFieldSet<Toets> fieldset, WebMarkupContainer scoreTabelContainer,
			WebMarkupContainer scoreLineair)
	{
		EduArteAjaxRefreshModifier scoreSchaalChangeRefresh =
			new EduArteAjaxRefreshModifier("scoreschaal", "overschrijfbaar", "aantalHerkansingen",
				scoreTabelContainer, scoreLineair)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target)
				{
					if (Scoreschaal.Lineair.equals(getToets().getScoreschaal()))
						getToets().setAantalHerkansingen(
							Math.min(getToets().getAantalHerkansingen(), 2));
				}
			};
		fieldset.addFieldModifier(scoreSchaalChangeRefresh);
		fieldset.addFieldModifier(new ConstructorArgModifier("scoreschaal",
			new AbstractReadOnlyModel<List<Scoreschaal>>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public List<Scoreschaal> getObject()
				{
					Schaal schaal = getToets().getSchaal();
					if (schaal == null
						|| (isVariant() && (getToets().getVariantVoorPoging() == null || getToets()
							.getVariantVoorPoging() > 2)))
						return Arrays.asList(Scoreschaal.Geen);
					else if (Schaaltype.Cijfer.equals(schaal.getSchaaltype()))
						return Arrays.asList(Scoreschaal.Geen, Scoreschaal.Lineair);
					else
						return Arrays.asList(Scoreschaal.Geen, Scoreschaal.Tabel);
				}
			}));
		return scoreSchaalChangeRefresh;
	}

	protected void addModifiersVoorRekenregelEnFormule(final AutoFieldSet<Toets> fieldset)
	{
		fieldset.addFieldModifier(new EnableModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return getToets().getRekenregel() == Rekenregel.Formule;
			}
		}, "formule"));
		fieldset.addFieldModifier(new BehaviorModifier(new AjaxFormComponentSaveBehaviour(),
			"formule"));
		fieldset.addFieldModifier(new EduArteAjaxRefreshModifier("rekenregel", "formule"));
	}

	protected void addModifiersVoorOverschrijfbaar(final AutoFieldSet<Toets> fieldset)
	{
		fieldset.addFieldModifier(new EnableModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return (getToets().isSamengesteld() && !getToets().isSamengesteldMetHerkansing() && !getToets()
					.isSamengesteldMetVarianten())
					|| !getToets().getScoreschaal().equals(Scoreschaal.Geen);
			}
		}, "overschrijfbaar"));
		fieldset.addFieldModifier(new BehaviorModifier(new AjaxFormComponentSaveBehaviour(),
			"overschrijfbaar"));
	}

	protected void addModifiersVoorHtmlClassEnHeeftResultaten(AutoFieldSet<Toets> fieldset)
	{
		fieldset.addFieldModifier(new HtmlClassModifier("unit_max", "soort", "code", "naam",
			"persoonlijkeToetscode", "schaal", "studiepunten", "weging", "maxAantalNietBehaald",
			"minAantalIngevuld", "maxAantalIngevuld", "minStudiepuntenVoorBehaald",
			"compenseerbaarVanaf", "rekenregel", "formule", "aantalHerkansingen",
			"scoreBijHerkansing", "scoreschaal"));
		fieldset.addFieldModifier(new EnableModifier(!getToets().getHeeftResultaten(), "schaal",
			"variantVoorPoging", "samengesteld", "samengesteldMetVarianten", "overschrijfbaar",
			"aantalHerkansingen", "scoreschaal", "alternatiefResultaatMogelijk"));
		fieldset.addFieldModifier(new EnableModifier(!getToets().getHeeftResultaten()
			|| !getToets().isSamengesteldMetHerkansing(), "samengesteldMetHerkansing"));
	}

	protected ToetsVerwijzingQuickSearchToevoegenPanel createToetsVerwijzingen()
	{
		return new ToetsVerwijzingQuickSearchToevoegenPanel("toetsVerwijzingen",
			new PropertyModel<List<ToetsVerwijzing>>(getToetsModel(), "uitgaandeVerwijzingen"),
			getToetsModel())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible()
					&& EduArteApp.get().isModuleActive(EduArteModuleKey.FORMATIEVE_RESULTATEN);
			}
		};
	}

	protected void addModifiersVoorVerwijsbaar(final AutoFieldSet<Toets> fieldset)
	{
		fieldset.addFieldModifier(new VisibilityModifier(EduArteApp.get().isModuleActive(
			EduArteModuleKey.FORMATIEVE_RESULTATEN), "verwijsbaar", "handmatigInleveren"));
	}

	protected void addModifiersVoorCompenseerbaarVanaf(AutoFieldSet<Toets> fieldset)
	{
		fieldset.addFieldModifier(new ConstructorArgModifier("compenseerbaarVanaf", fieldset
			.getModel()));
	}
}
