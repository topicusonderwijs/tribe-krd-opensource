/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.EditorClassModifier;
import nl.topicus.cobra.web.components.form.modifier.HtmlClassModifier;
import nl.topicus.cobra.web.components.form.modifier.LabelModifier;
import nl.topicus.cobra.web.components.form.modifier.PseudoFieldModifier;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.dao.helpers.ResultaatZoekFilterInstellingDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.web.components.autoform.EduArteAjaxRefreshModifier;
import nl.topicus.eduarte.web.components.autoform.ModuleBoundFieldModifier;
import nl.topicus.eduarte.web.components.choice.ResultaatstructuurCategorieTypeCombobox;
import nl.topicus.eduarte.web.components.choice.ResultaatstructuurCategorieTypeCombobox.CategorieType;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurZoekFilter;
import nl.topicus.eduarte.zoekfilters.ToetsCodeFilterZoekFilter;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ResultaatZoekFilterPanel extends AutoZoekFilterPanel<ToetsZoekFilter>
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private ResultaatZoekFilterInstellingDataAccessHelper helper;

	private boolean vulGekoppeld;

	private IModel<Groep> groep;

	public ResultaatZoekFilterPanel(String id, ToetsZoekFilter filter, final IPageable pageable,
			boolean toonOnderwijsproduct, boolean toonGekoppeldAanVerbintenis, Groep groep)
	{
		super(id, filter, pageable);

		vulGekoppeld = toonGekoppeldAanVerbintenis;
		this.groep = ModelFactory.getModel(groep);

		List<String> properties = new ArrayList<String>();
		properties.add("peildatum");
		properties.add("toetsCodeFilter");
		if (toonOnderwijsproduct)
		{
			properties.add("resultaatstructuurFilter.onderwijsproduct");
			OnderwijsproductZoekFilter onderwijsproductFilter =
				OnderwijsproductZoekFilter.createDefaultFilter();
			onderwijsproductFilter.setDeelnemers(filter.getResultaatstructuurFilter()
				.getDeelnemers());
			addFieldModifier(new ConstructorArgModifier(
				"resultaatstructuurFilter.onderwijsproduct", onderwijsproductFilter));
			addRefreshOnZoek("resultaatstructuurFilter.onderwijsproduct", pageable);
		}
		properties.add("resultaatstructuurFilter.cohort");
		properties.add("resultaatstructuurFilter.categorieType");
		properties.add("codePath");
		if (toonGekoppeldAanVerbintenis)
		{
			properties.add("resultaatstructuurFilter.alleenGekoppeldAanVerbintenis");
		}
		setPropertyNames(properties);

		ToetsCodeFilterZoekFilter toetsCodeFilterFilter =
			ToetsCodeFilterZoekFilter.createDefaultFilter();
		toetsCodeFilterFilter
			.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		toetsCodeFilterFilter.setMedewerker(EduArteContext.get().getMedewerker());
		addFieldModifier(new ConstructorArgModifier("toetsCodeFilter", toetsCodeFilterFilter));
		addRefreshOnZoek("codePath", pageable);
		addRefreshOnZoek("resultaatstructuurFilter.cohort", pageable);
		addRefreshOnZoek("resultaatstructuurFilter.alleenGekoppeldAanVerbintenis", pageable);
		addRefreshOnZoek("resultaatstructuurFilter.categorieType", pageable);
		addFieldModifier(new PseudoFieldModifier<ResultaatstructuurZoekFilter, CategorieType>(
			ResultaatstructuurZoekFilter.class, "resultaatstructuurFilter.categorieType",
			CategorieType.class, new IModel<CategorieType>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public CategorieType getObject()
				{
					ResultaatstructuurZoekFilter structFilter =
						getZoekfilter().getResultaatstructuurFilter();
					if (structFilter.getCategorie() == null && structFilter.getType() == null)
						return null;
					return new CategorieType(structFilter.getCategorie(), structFilter.getType());
				}

				@Override
				public void setObject(CategorieType object)
				{
					ResultaatstructuurZoekFilter structFilter =
						getZoekfilter().getResultaatstructuurFilter();
					CategorieType categorieType = object;
					structFilter.setCategorie(categorieType == null ? null : categorieType
						.getCategorie());
					structFilter.setType(categorieType == null ? null : categorieType.getType());
				}

				@Override
				public void detach()
				{
				}
			}));
		addFieldModifier(new LabelModifier("resultaatstructuurFilter.categorieType",
			"Resultaatstructuur categorie"));
		addFieldModifier(new EditorClassModifier(ResultaatstructuurCategorieTypeCombobox.class,
			"resultaatstructuurFilter.categorieType"));
		addFieldModifier(new ConstructorArgModifier("resultaatstructuurFilter.categorieType",
			filter.getResultaatstructuurFilter()));
		addFieldModifier(new ModuleBoundFieldModifier(EduArteModuleKey.FORMATIEVE_RESULTATEN,
			"resultaatstructuurFilter.categorieType"));
		addFieldModifier(new HtmlClassModifier("unit_100", "resultaatstructuurFilter.categorieType"));
		addFieldModifier(new ConstructorArgModifier("codePath", filter
			.getResultaatstructuurFilter()));
		addFieldModifier(new ModuleBoundFieldModifier(EduArteModuleKey.FORMATIEVE_RESULTATEN,
			"codePath"));
	}

	private void addRefreshOnZoek(String property, final IPageable pageable)
	{
		addFieldModifier(new EduArteAjaxRefreshModifier(property)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				onZoek(pageable, target);
			}
		});
	}

	@Override
	protected void onZoek(IPageable pageable, AjaxRequestTarget target)
	{
		super.onZoek(pageable, target);
		// refresh hele page, want de resultaateditor kan niet tegen een ajax refresh

		helper.saveZoekFilter(getZoekfilter(), EduArteContext.get().getMedewerker(), groep
			.getObject(), vulGekoppeld);
		helper.batchExecute();

		target.addComponent(getPage());
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		groep.detach();
	}
}
