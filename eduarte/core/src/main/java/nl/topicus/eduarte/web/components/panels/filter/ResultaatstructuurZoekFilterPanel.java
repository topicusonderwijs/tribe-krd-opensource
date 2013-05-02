/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.EditorClassModifier;
import nl.topicus.cobra.web.components.form.modifier.HtmlClassModifier;
import nl.topicus.cobra.web.components.form.modifier.LabelModifier;
import nl.topicus.cobra.web.components.form.modifier.PseudoFieldModifier;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.web.components.autoform.EduArteAjaxRefreshModifier;
import nl.topicus.eduarte.web.components.autoform.ModuleBoundFieldModifier;
import nl.topicus.eduarte.web.components.choice.ResultaatstructuurCategorieTypeCombobox;
import nl.topicus.eduarte.web.components.choice.ResultaatstructuurCategorieTypeCombobox.CategorieType;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductZoekFilter;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.model.IModel;

public class ResultaatstructuurZoekFilterPanel extends
		AutoZoekFilterPanel<ResultaatstructuurZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public ResultaatstructuurZoekFilterPanel(String id, ResultaatstructuurZoekFilter filter,
			IPageable pageable, boolean toonCategorieType)
	{
		super(id, filter, pageable);

		List<String> properties = new ArrayList<String>();
		properties.add("onderwijsproduct");
		properties.add("cohort");
		if (toonCategorieType)
			properties.add("categorieType");
		setPropertyNames(properties);

		OnderwijsproductZoekFilter onderwijsproductFilter =
			OnderwijsproductZoekFilter.createDefaultFilter();
		onderwijsproductFilter.setDeelnemers(filter.getDeelnemers());
		addFieldModifier(new ConstructorArgModifier("onderwijsproduct", onderwijsproductFilter));

		addFieldModifier(new PseudoFieldModifier<ResultaatstructuurZoekFilter, CategorieType>(
			ResultaatstructuurZoekFilter.class, "categorieType", CategorieType.class,
			new IModel<CategorieType>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public CategorieType getObject()
				{
					ResultaatstructuurZoekFilter structFilter = getZoekfilter();
					if (structFilter.getCategorie() == null && structFilter.getType() == null)
						return null;
					return new CategorieType(structFilter.getCategorie(), structFilter.getType());
				}

				@Override
				public void setObject(CategorieType object)
				{
					ResultaatstructuurZoekFilter structFilter = getZoekfilter();
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
		addFieldModifier(new LabelModifier("categorieType", "Resultaatstructuur categorie"));
		addFieldModifier(new EditorClassModifier(ResultaatstructuurCategorieTypeCombobox.class,
			"categorieType"));
		addFieldModifier(new ConstructorArgModifier("categorieType", filter));
		addFieldModifier(new ModuleBoundFieldModifier(EduArteModuleKey.FORMATIEVE_RESULTATEN,
			"categorieType"));
		addFieldModifier(new HtmlClassModifier("unit_100", "categorieType"));

		addFieldModifier(new EduArteAjaxRefreshModifier("onderwijsproduct", "categorieType"));
		addFieldModifier(new EduArteAjaxRefreshModifier("cohort", "categorieType"));
	}
}
