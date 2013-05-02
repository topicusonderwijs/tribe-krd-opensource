/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.web.components.autoform.EduArteAjaxRefreshModifier;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.navigation.paging.IPageable;

public class ToetsZoekFilterPanel extends AutoZoekFilterPanel<ToetsZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public ToetsZoekFilterPanel(String id, ToetsZoekFilter filter, final IPageable pageable,
			Onderwijsproduct onderwijsproduct)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("resultaatstructuurFilter.cohort"));
		addFieldModifier(new ConstructorArgModifier("resultaatstructuurFilter.cohort",
			onderwijsproduct.getBegindatum(), onderwijsproduct.getEinddatum()));
		addFieldModifier(new EduArteAjaxRefreshModifier("resultaatstructuurFilter.cohort")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				onZoek(pageable, target);
			}
		});
	}
}
