package nl.topicus.eduarte.resultaten.web.pages.shared;

import java.util.Arrays;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ExtendedModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.SavableForm;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.EnableModifier;
import nl.topicus.cobra.web.components.form.modifier.VisibilityModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.shortcut.ActionKey;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.Herkansingsscore;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.Scoreschaal;
import nl.topicus.eduarte.resultaten.principals.onderwijs.OnderwijsproductResultaatstructuurWrite;
import nl.topicus.eduarte.resultaten.web.components.factory.ToetsWizardButtonFactory;
import nl.topicus.eduarte.web.components.autoform.EduArteAjaxRefreshModifier;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "Toets bewerken", menu = "Onderwijs > Onderwijsproducten > [onderwijsproduct] > Resultaten > Bewerken > [Toets]")
@InPrincipal(OnderwijsproductResultaatstructuurWrite.class)
public class OnderwijsproductToetsEditPage extends AbstractOnderwijsproductToetsEditPage
{
	private SavableForm<Toets> form;

	private WebMarkupContainer scoreTabelContainer;

	private AutoFieldSet<Toets> scoreLineair;

	public OnderwijsproductToetsEditPage(ExtendedModel<Toets> toetsModel,
			ResultaatstructuurEditPage returnPage)
	{
		super(toetsModel, returnPage);

		form = new SavableForm<Toets>("form", toetsModel);
		add(form);

		form.add(scoreTabelContainer = createScoreTabel());
		form.add(scoreLineair = createScoreLineair());
		form.add(createToetsInputFields());
		form.add(createToetsVerwijzingen());

		createComponents();
	}

	private AutoFieldSet<Toets> createToetsInputFields()
	{
		final AutoFieldSet<Toets> ret =
			new AutoFieldSet<Toets>("toets", getToetsModel(), isVariant() ? "Toetsvariant"
				: "Toets");
		ret.setPropertyNames("soort", "code", "naam", "persoonlijkeToetscode", "referentieCode",
			"schaal", "weging", "automatischeWeging", "verplicht", "samengesteld",
			"samengesteldMetHerkansing", "samengesteldMetVarianten", "verwijsbaar",
			"handmatigInleveren", "overschrijfbaar", "maxAantalNietBehaald", "minAantalIngevuld",
			"maxAantalIngevuld", "minStudiepuntenVoorBehaald", "compenseerbaarVanaf", "rekenregel",
			"formule", "aantalHerkansingen", "scoreBijHerkansing", "scoreschaal", "studiepunten",
			"variantVoorPoging", "alternatiefResultaatMogelijk", "alternatiefCombinerenMetHoofd");
		ret.setSortAccordingToPropertyNames(true);
		ret.setRenderMode(RenderMode.EDIT);

		final EduArteAjaxRefreshModifier scoreSchaalChangeRefresh =
			addModifiersVoorScoreschaal(ret, scoreTabelContainer, scoreLineair);
		addModifiersVoorSoortCodeEnSchaal(ret);
		addModifiersVoorWeging(ret, false).addFields(
			Arrays.asList("samengesteld", "samengesteldMetHerkansing", "samengesteldMetVarianten"));
		addModifiersVoorRekenregelEnFormule(ret);
		addModifiersVoorAantalHerkansingen(ret, scoreSchaalChangeRefresh);
		addModifiersVoorAlternatief(ret);
		addModifiersVoorOverschrijfbaar(ret);
		addModifiersVoorHtmlClassEnHeeftResultaten(ret);
		addModifiersVoorVerwijsbaar(ret);
		addModifiersVoorCompenseerbaarVanaf(ret);

		ret.addFieldModifier(new VisibilityModifier(isVariant(), "variantVoorPoging"));
		ret.addFieldModifier(new VisibilityModifier(!isVariant(), "studiepunten", "soort",
			"verplicht", "samengesteld", "samengesteldMetHerkansing", "samengesteldMetVarianten",
			"maxAantalNietBehaald", "minAantalIngevuld", "maxAantalIngevuld",
			"minStudiepuntenVoorBehaald", "compenseerbaarVanaf", "rekenregel", "formule",
			"aantalHerkansingen", "scoreBijHerkansing", "alternatiefResultaatMogelijk",
			"alternatiefCombinerenMetHoofd"));

		ret.addFieldModifier(new EnableModifier(!getToets().isEindresultaat(), "weging",
			"compenseerbaarVanaf", "verplicht", "automatischeWeging"));
		ret.addFieldModifier(new EnableModifier(getToets().getChildren().isEmpty(), "samengesteld",
			"samengesteldMetVarianten"));
		ret.addFieldModifier(new EnableModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return !getToets().isSamengesteld();
			}
		}, "scoreschaal", "studiepunten"));
		ret.addFieldModifier(new EnableModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return !getToets().isSamengesteldMetHerkansing();
			}
		}, "samengesteldMetVarianten"));
		ret.addFieldModifier(new EnableModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return !getToets().isSamengesteld() || getToets().isSamengesteldMetVarianten();
			}
		}, "aantalHerkansingen"));
		ret.addFieldModifier(new EnableModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return !getToets().isSamengesteld() || getToets().isSamengesteldMetHerkansing()
					|| getToets().isSamengesteldMetVarianten();
			}
		}, "scoreBijHerkansing"));
		ret.addFieldModifier(new EnableModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return getToets().isSamengesteld();
			}
		}, "samengesteldMetHerkansing", "samengesteldMetVarianten", "maxAantalNietBehaald",
			"minAantalIngevuld", "maxAantalIngevuld", "minStudiepuntenVoorBehaald", "rekenregel",
			"formule"));

		ret.addFieldModifier(new EduArteAjaxRefreshModifier("schaal", "compenseerbaarVanaf")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				refreshScoreschaal(true, scoreSchaalChangeRefresh, ret, target);
			}
		});
		ret.addFieldModifier(new ConstructorArgModifier("variantVoorPoging", isVariant()
			? getToets().getParent().getAantalHerkansingen() : 0));
		ret.addFieldModifier(new EduArteAjaxRefreshModifier("variantVoorPoging", "scoreschaal",
			scoreLineair)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				if (getToets().getVariantVoorPoging() > 2)
				{
					refreshScoreschaal(true, scoreSchaalChangeRefresh, ret, target);
				}
			}
		});
		ret
			.addFieldModifier(new EduArteAjaxRefreshModifier("samengesteld",
				"samengesteldMetHerkansing", "samengesteldMetVarianten", "maxAantalNietBehaald",
				"minAantalIngevuld", "maxAantalIngevuld", "minStudiepuntenVoorBehaald",
				"rekenregel", "formule", "aantalHerkansingen", "scoreBijHerkansing", "scoreschaal",
				"overschrijfbaar")
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target)
				{
					getToets().setSamengesteldMetHerkansing(false);
					getToets().setSamengesteldMetVarianten(false);
					refreshScoreschaal(true, scoreSchaalChangeRefresh, ret, target);
				}
			});
		ret.addFieldModifier(new EduArteAjaxRefreshModifier("samengesteldMetHerkansing",
			"samengesteldMetVarianten", "overschrijfbaar", "scoreBijHerkansing")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				getToets().setOverschrijfbaar(false);
			}
		});
		ret.addFieldModifier(new EduArteAjaxRefreshModifier("samengesteldMetVarianten",
			"samengesteldMetHerkansing", "maxAantalNietBehaald", "minAantalIngevuld",
			"maxAantalIngevuld", "minStudiepuntenVoorBehaald", "rekenregel", "formule",
			"alternatiefResultaatMogelijk", "alternatiefCombinerenMetHoofd", "aantalHerkansingen",
			"scoreBijHerkansing", "overschrijfbaar")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				getToets().setOverschrijfbaar(false);
			}
		});
		ret.addFieldModifier(new EnableModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return !getToets().isSamengesteldMetVarianten();
			}
		}, "samengesteldMetHerkansing", "maxAantalNietBehaald", "minAantalIngevuld",
			"maxAantalIngevuld", "minStudiepuntenVoorBehaald", "alternatiefResultaatMogelijk",
			"alternatiefCombinerenMetHoofd"));
		ret.addFieldModifier(new ConstructorArgModifier("referentieCode",
			new PropertyModel<Integer>(getToetsModel(), "referentieVersie")));

		return ret;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				Toets toets = getToets();
				addToetsToStructuur(toets);
				setResponsePage(getReturnPage());
			}
		});

		if (getToets().getParent() != null)
		{

			panel.addButton(new OpslaanButton(panel, form)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onSubmit()
				{
					Toets toets = getToets();
					addToetsToStructuur(toets);

					Toets deelToets = new Toets(getToets().getParent());
					deelToets.setWeging(1);
					deelToets.setScoreBijHerkansing(Herkansingsscore.Hoogste);
					deelToets.setScoreschaal(Scoreschaal.Geen);

					ExtendedModel<Toets> deelToetsModel =
						ModelFactory.getCompoundChangeRecordingModel(deelToets, getToetsModel()
							.getManager());

					setResponsePage(new OnderwijsproductToetsEditPage(deelToetsModel,
						getReturnPage()));
				}

				@Override
				public ActionKey getAction()
				{
					return CobraKeyAction.VOLGENDE;
				}

				@Override
				public String getLabel()
				{
					return "Opslaan en nieuwe toevoegen";
				}

			});
		}

		if (!isVariant())
		{
			EduArteApp.get().getFirstPanelFactory(ToetsWizardButtonFactory.class,
				EduArteContext.get().getOrganisatie()).addWizardButton(panel, form,
				getToetsModel(), getReturnPage());
		}

		super.fillBottomRow(panel);
	}
}
