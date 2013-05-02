package nl.topicus.eduarte.web.components.panels.verbintenis;

import java.util.List;

import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.LabelModifier;
import nl.topicus.cobra.web.components.form.modifier.VisibilityModifier;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.hogeronderwijs.Hoofdfase;
import nl.topicus.eduarte.entities.hogeronderwijs.OpleidingsVorm;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.VerbintenisContract;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.inschrijving.Vervolgonderwijs.SoortVervolgonderwijs;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.entities.vrijevelden.VerbintenisVrijVeld;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.VrijVeldEntiteitPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.VerbintenisContractTable;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class DeelnemerVerbintenisPanel extends TypedPanel<Verbintenis>
{
	private static final long serialVersionUID = 1L;

	public DeelnemerVerbintenisPanel(String id, IModel<Verbintenis> model)
	{
		super(id, model);

		add(new Label("caption", new AbstractReadOnlyModel<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				if (isIntake())
					return "Intake";
				return "Verbintenis";
			}
		}

		));
		AutoFieldSet<Verbintenis> detailsLinks =
			new AutoFieldSet<Verbintenis>("detailsLinks", getVerbintenisModel());
		detailsLinks.setOutputMarkupId(true);
		detailsLinks.setPropertyNames("status", "bronStatus", "bronDatum", "opleiding",
			"externeCode", "indicatieGehandicapt", "opleiding.naam", "opleiding.code",
			"opleiding.leerweg", "opleiding.niveau", "intensiteit", "contacturenPerWeek",
			"organisatieEenheid", "locatie", "team", "relevanteVooropleiding.omschrijving",
			"relevanteVooropleiding.soortOnderwijs", "toelichting", "redenInburgering", "brinnaam",
			"profielInburgering", "staatsExamenType", "leerprofiel", "deelcursus",
			"soortPraktijkexamen", "beginNiveauSchriftelijkeVaardigheden",
			"eindNiveauSchriftelijkeVaardigheden");
		detailsLinks.setSortAccordingToPropertyNames(true);
		detailsLinks.setRenderMode(RenderMode.DISPLAY);
		detailsLinks.addFieldModifier(new LabelModifier("brinnaam", "Kenniscentrum"));
		detailsLinks.addFieldModifier(new LabelModifier("relevanteVooropleiding.omschrijving",
			"Relevante vooropleiding"));
		detailsLinks.addFieldModifier(new LabelModifier("relevanteVooropleiding.soortOnderwijs",
			"Categorie vooropleiding"));
		detailsLinks.addFieldModifier(new VisibilityModifier(new IsInburgeringModel(),
			"redenInburgering", "profielInburgering", "deelcursus", "soortPraktijkexamen",
			"beginNiveauSchriftelijkeVaardigheden", "eindNiveauSchriftelijkeVaardigheden"));
		detailsLinks.addFieldModifier(new VisibilityModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return getOpleiding() != null
					&& (getOpleiding().getVerbintenisgebied().getTaxonomiecode().startsWith("5.SE"));
			}

		}, "staatsExamenType"));
		detailsLinks.addFieldModifier(new VisibilityModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return getOpleiding() != null && getOpleiding().isKiesKenniscentrum();
			}

		}, "brinnaam"));

		detailsLinks.addFieldModifier(new VisibilityModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return getVerbintenis() != null
					&& (getVerbintenis().isInburgeringVerbintenis() || getVerbintenis()
						.isEducatieVerbintenis());
			}
		}, "leerprofiel"));

		detailsLinks.addFieldModifier(new VisibilityModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return !isIntake() && heeftLwoo();
			}

		}, "lwoo"));
		AbstractReadOnlyModel<Boolean> isBVEVerbintenisModel = new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return isBVEVerbintenis();
			}

		};
		detailsLinks.addFieldModifier(new VisibilityModifier(isBVEVerbintenisModel,
			"indicatieGehandicapt"));

		detailsLinks.addFieldModifier(new VisibilityModifier("contacturenPerWeek",
			new AbstractReadOnlyModel<Boolean>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Boolean getObject()
				{
					Opleiding opleiding = getOpleiding();
					return opleiding != null
						&& (opleiding.getVerbintenisgebied().getTaxonomie().isEducatie()
							|| opleiding.isVavo() || isInburgering() || isStaatsExamen());
				}
			}));
		detailsLinks.addFieldModifier(new LabelModifier("externeCode",
			new AbstractReadOnlyModel<String>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public String getObject()
				{
					return getExterneCodelLabel();
				}

			}));

		IModel<Boolean> isNotIntakeModel = new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return !isIntake();
			}
		};

		detailsLinks.addFieldModifier(new VisibilityModifier(isNotIntakeModel, "opleiding",
			"externeCode", "opleiding.leerweg", "diplomaBehaald"));

		add(detailsLinks);

		WebMarkupContainer creditsContainer = new WebMarkupContainer("creditsContainer")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				OpleidingsVorm vorm = null;
				if (getVerbintenis().getPlaatsingOpPeildatum() != null)
				{
					vorm = getVerbintenis().getPlaatsingOpPeildatum().getOpleidingsVorm();
				}

				return getVerbintenis().isHOVerbintenis()
					&& getVerbintenis().getOpleiding() != null
					&& getVerbintenis().getOpleiding().getHoofdfases(vorm).size() > 0;
			}

		};
		add(creditsContainer);
		OpleidingsVorm vorm = null;
		if (getVerbintenis().getPlaatsingOpPeildatum() != null)
			vorm = getVerbintenis().getPlaatsingOpPeildatum().getOpleidingsVorm();
		RepeatingView creditsPerHoofdfase = new RepeatingView("creditsPerHoofdfase");
		if (getVerbintenis().getOpleiding() != null)
		{
			for (Hoofdfase fase : getVerbintenis().getOpleiding().getHoofdfases(vorm))
			{
				WebMarkupContainer wmc = new WebMarkupContainer(creditsPerHoofdfase.newChildId());
				wmc.add(new Label("hoofdfase", "Credits " + fase.getValue().toLowerCase()));
				wmc.add(new Label("credits", new CreditsPerHoofdfaseModel(getVerbintenisModel(),
					fase)));
				creditsPerHoofdfase.add(wmc);
			}
		}
		creditsContainer.add(creditsPerHoofdfase);

		AutoFieldSet<Verbintenis> detailsRechts =
			new AutoFieldSet<Verbintenis>("detailsRechts", getVerbintenisModel())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return !isIntake();
				}
			};
		detailsRechts.setPropertyNames("begindatum", "datumAanmelden", "datumAkkoord",
			"datumEersteActiviteit", "geplandeEinddatum", "einddatum", "cohort",
			"redenUitschrijving", "vervolgonderwijsSoort", "vervolgonderwijsNaam",
			"diplomaBehaald", "bekostigd", "overeenkomstnummer", "datumOvereenkomstOndertekend",
			"examenDatum");

		detailsRechts.addFieldModifier(new LabelModifier("datumOvereenkomstOndertekend",
			"Overeenkomst ondertekend"));
		detailsRechts.addFieldModifier(new LabelModifier("redenUitschrijving", "Reden beëindigen"));
		detailsRechts.addFieldModifier(new VisibilityModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return getVerbintenis().getDatumOvereenkomstOndertekend() != null;
			}
		}, "datumOvereenkomstOndertekend"));
		detailsRechts.addFieldModifier(new VisibilityModifier(new IsInburgeringContractModel(true),
			"datumAanmelden"));
		detailsRechts.addFieldModifier(new VisibilityModifier(
			new IsInburgeringContractModel(false), "datumAkkoord"));
		detailsRechts.addFieldModifier(new VisibilityModifier(new IsInburgeringModel(),
			"datumEersteActiviteit"));
		detailsRechts.addFieldModifier(new VisibilityModifier(new IsInburgeringModel(),
			"examenDatum"));
		detailsLinks.addFieldModifier(new VisibilityModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return getVerbintenis().getStatus().isAfgesloten();
			}
		}, "redenUitschrijving"));

		detailsRechts.addFieldModifier(new VisibilityModifier(createBeeindigdDisplayModel(false),
			"geplandeEinddatum"));
		detailsRechts.addFieldModifier(new VisibilityModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return getVerbintenis().isBOVerbintenis();
			}
		}, "bekostigd"));

		detailsRechts.addFieldModifier(new VisibilityModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				if (getVerbintenis().getVervolgonderwijs() == null)
					return false;
				SoortVervolgonderwijs soort =
					getVerbintenis().getVervolgonderwijs().getSoortVervolgonderwijs();
				return soort.equals(SoortVervolgonderwijs.BRIN)
					|| soort.equals(SoortVervolgonderwijs.Overig);
			}
		}, "vervolgonderwijsNaam"));
		detailsRechts.addFieldModifier(new LabelModifier("vervolgonderwijsNaam", "Vervolgschool"));
		detailsRechts.addFieldModifier(new VisibilityModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				if (getVerbintenis().getVervolgonderwijs() == null)
					return false;
				SoortVervolgonderwijs soort =
					getVerbintenis().getVervolgonderwijs().getSoortVervolgonderwijs();
				return soort.equals(SoortVervolgonderwijs.Onbekend)
					|| soort.equals(SoortVervolgonderwijs.Intern);
			}
		}, "vervolgonderwijsSoort"));
		detailsRechts.addFieldModifier(new LabelModifier("vervolgonderwijsSoort",
			"Soort vervolgonderwijs"));
		detailsRechts.addFieldModifier(new LabelModifier("einddatum", "Werkelijke Einddatum"));

		detailsRechts.setSortAccordingToPropertyNames(true);
		detailsRechts.setRenderMode(RenderMode.DISPLAY);

		add(detailsRechts);

		add(new VrijVeldEntiteitPanel<VerbintenisVrijVeld, Verbintenis>("vrijveldenPanel",
			getVerbintenisModel()));

		EduArteDataPanel<VerbintenisContract> contractDataPanel =
			new EduArteDataPanel<VerbintenisContract>("contractDataPanel",
				new ListModelDataProvider<VerbintenisContract>(
					new PropertyModel<List<VerbintenisContract>>(getVerbintenisModel(),
						"contracten")), new VerbintenisContractTable());
		add(contractDataPanel);

		add(new VerbintenisBekostigingPanel("bekostigingPanel", getVerbintenisModel()));
	}

	private IModel<Boolean> createBeeindigdDisplayModel(final boolean showWhenEinddatumSet)
	{
		return new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				if (showWhenEinddatumSet)
				{
					return (getVerbintenis().getEinddatum() != null);
				}
				else
				{
					return !(getVerbintenis().getEinddatum() != null);
				}
			}
		};
	}

	private IModel<Verbintenis> getVerbintenisModel()
	{
		return getModel();
	}

	private Verbintenis getVerbintenis()
	{
		return getVerbintenisModel().getObject();
	}

	private Opleiding getOpleiding()
	{
		return getVerbintenis().getOpleiding();
	}

	private boolean isInburgering()
	{
		return getOpleiding() != null && getOpleiding().isInburgering();
	}

	private boolean isStaatsExamen()
	{
		return getOpleiding() != null
			&& (getOpleiding().getVerbintenisgebied().getTaxonomiecode().startsWith("5.SE"));
	}

	private boolean heeftLwoo()
	{
		if (getOpleiding() != null)
		{
			return getOpleiding().heeftLwoo();
		}
		return false;
	}

	private boolean isBVEVerbintenis()
	{
		if (getVerbintenis() != null)
			return getVerbintenis().isBVEVerbintenis();
		return false;
	}

	private String getExterneCodelLabel()
	{
		String code = "Externe code";

		if (getOpleiding() != null)
		{
			Taxonomie taxonomie = getOpleiding().getVerbintenisgebied().getTaxonomie();

			if (taxonomie != null)
			{
				if (taxonomie.isVO())
					code = "ILT-code";
				else if (taxonomie.isBO())
					code = "Crebo-code";
			}
		}

		return code;
	}

	private boolean isIntake()
	{
		return getVerbintenis() != null && getVerbintenis().getStatus() == VerbintenisStatus.Intake;
	}

	private final class IsInburgeringModel extends AbstractReadOnlyModel<Boolean>
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Boolean getObject()
		{
			return getVerbintenis() != null && getVerbintenis().isInburgeringVerbintenis();
		}
	}

	private final class IsInburgeringContractModel extends AbstractReadOnlyModel<Boolean>
	{
		private static final long serialVersionUID = 1L;

		private boolean contract;

		public IsInburgeringContractModel(boolean contract)
		{
			this.contract = contract;
		}

		@Override
		public Boolean getObject()
		{
			return getVerbintenis() != null && getVerbintenis().isInburgeringVerbintenis()
				&& (contract == getVerbintenis().isInburgeringContractVerbintenis());
		}
	}
}