/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.providers.TaxonomieProvider;
import nl.topicus.eduarte.web.components.autoform.EduArteAjaxRefreshModifier;
import nl.topicus.eduarte.zoekfilters.TaxonomieElementZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * @author loite
 */
public class TaxonomieElementZoekFilterPanel extends
		AutoZoekFilterPanel<TaxonomieElementZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public TaxonomieElementZoekFilterPanel(String id, final TaxonomieElementZoekFilter filter,
			IPageable pageable, boolean toonTaxonomieCombo)
	{
		super(id, filter, pageable);
		if (toonTaxonomieCombo)
			setPropertyNames(Arrays.asList("peildatum", "taxonomie", "taxonomieElementType",
				"taxonomiecode", "afkorting", "naam", "externeCode"));
		else
			setPropertyNames(Arrays.asList("peildatum", "taxonomieElementType", "taxonomiecode",
				"afkorting", "naam", "externeCode"));

		addFieldModifier(new ConstructorArgModifier("taxonomieElementType", new TaxonomieProvider()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Taxonomie getTaxonomie()
			{
				return filter.getTaxonomie();
			}
		}, false));

		addFieldModifier(new EduArteAjaxRefreshModifier("taxonomie", "taxonomieElementType"));
	}
}
