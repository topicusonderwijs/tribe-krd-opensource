package nl.topicus.eduarte.web.components.resultaat;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.EditorClassModifier;
import nl.topicus.cobra.web.components.form.modifier.LabelModifier;
import nl.topicus.cobra.web.components.form.modifier.PseudoFieldModifier;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.dao.helpers.ResultaatZoekFilterInstellingDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur.Type;
import nl.topicus.eduarte.web.components.autoform.ModuleBoundFieldModifier;
import nl.topicus.eduarte.web.components.choice.ResultaatstructuurCategorieTypeCombobox;
import nl.topicus.eduarte.web.components.choice.ResultaatstructuurCategorieTypeCombobox.CategorieType;
import nl.topicus.eduarte.web.components.panels.templates.RapportageConfiguratiePanel;
import nl.topicus.eduarte.web.components.panels.templates.RapportageConfiguratieRegistratie;
import nl.topicus.eduarte.web.pages.shared.RapportageConfiguratiePage;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurZoekFilter;
import nl.topicus.eduarte.zoekfilters.ToetsCodeFilterZoekFilter;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

@RapportageConfiguratieRegistratie(naam = "resultaat", factoryType = ResultaatRapportageConfiguratieFactory.class, configuratieType = RapportageResultaten.class)
public class ResultaatRapportageConfiguratiePanel extends Panel implements
		RapportageConfiguratiePanel<Verbintenis>
{
	private static final long serialVersionUID = 1L;

	private ResultaatRapportageConfiguratieFactory config;

	private ToetsZoekFilter filter;

	public ResultaatRapportageConfiguratiePanel(String id,
			RapportageConfiguratiePage<Verbintenis, Verbintenis, VerbintenisZoekFilter> page)
	{
		super(id);

		filter = new ToetsZoekFilter();
		filter.setResultaatstructuurFilter(new ResultaatstructuurZoekFilter());
		config = new ResultaatRapportageConfiguratieFactory(filter);

		List<Deelnemer> deelnemers = new ArrayList<Deelnemer>();
		for (Verbintenis curVerbintenis : page.getSelection().getSelectedElements())
			deelnemers.add(curVerbintenis.getDeelnemer());
		filter.getResultaatstructuurFilter().setDeelnemers(deelnemers);
		filter.getResultaatstructuurFilter().setType(Type.SUMMATIEF);
		filter.getResultaatstructuurFilter().setAuthorizationContext(
			new OrganisatieEenheidLocatieAuthorizationContext(this));
		DataAccessRegistry.getHelper(ResultaatZoekFilterInstellingDataAccessHelper.class)
			.vulZoekFilter(filter, EduArteContext.get().getMedewerker(), null, true);

		ToetsCodeFilterZoekFilter toetsCodeFilterFilter =
			ToetsCodeFilterZoekFilter.createDefaultFilter();
		toetsCodeFilterFilter
			.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		toetsCodeFilterFilter.setMedewerker(EduArteContext.get().getMedewerker());

		AutoFieldSet<ResultaatRapportageConfiguratieFactory> inputfields =
			new AutoFieldSet<ResultaatRapportageConfiguratieFactory>("inputfields",
				new Model<ResultaatRapportageConfiguratieFactory>(config), "Instellingen");
		inputfields.setPropertyNames("cohort", "categorieType", "alleenGekoppeldAanVerbintenis",
			"toetsCodeFilter");
		inputfields.setRenderMode(RenderMode.EDIT);
		inputfields.setSortAccordingToPropertyNames(true);
		inputfields
			.addFieldModifier(new PseudoFieldModifier<ResultaatRapportageConfiguratieFactory, CategorieType>(
				ResultaatRapportageConfiguratieFactory.class, "categorieType", CategorieType.class,
				new IModel<CategorieType>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public CategorieType getObject()
					{
						if (config.getCategorie() == null && config.getType() == null)
							return null;
						return new CategorieType(config.getCategorie(), config.getType());
					}

					@Override
					public void setObject(CategorieType categorieType)
					{
						config.setCategorie(categorieType == null ? null : categorieType
							.getCategorie());
						config.setType(categorieType == null ? null : categorieType.getType());
					}

					@Override
					public void detach()
					{
					}
				}));
		inputfields.addFieldModifier(new LabelModifier("categorieType", "Categorie"));
		inputfields.addFieldModifier(new EditorClassModifier(
			ResultaatstructuurCategorieTypeCombobox.class, "categorieType"));
		inputfields.addFieldModifier(new ConstructorArgModifier("categorieType", filter
			.getResultaatstructuurFilter()));
		inputfields.addFieldModifier(new ModuleBoundFieldModifier(
			EduArteModuleKey.FORMATIEVE_RESULTATEN, "categorieType"));
		inputfields.addFieldModifier(new ConstructorArgModifier("toetsCodeFilter",
			toetsCodeFilterFilter));
		add(inputfields);
	}

	@Override
	public ResultaatRapportageConfiguratieFactory getConfiguratie()
	{
		return config.prepareForJob();
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		config.detach();
		filter.detach();
	}
}
