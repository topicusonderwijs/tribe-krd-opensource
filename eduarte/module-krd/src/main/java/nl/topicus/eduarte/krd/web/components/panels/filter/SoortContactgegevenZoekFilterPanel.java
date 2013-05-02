/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.web.components.panels.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;

import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.eduarte.entities.adres.StandaardContactgegeven;
import nl.topicus.eduarte.web.components.panels.filter.AutoZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.SoortContactgegevenZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.model.Model;

/**
 * @author hoeve
 */
public class SoortContactgegevenZoekFilterPanel extends
		AutoZoekFilterPanel<SoortContactgegevenZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public SoortContactgegevenZoekFilterPanel(String id, SoortContactgegevenZoekFilter filter,
			IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("naam", "code", "actief", "standaardContactgegeven",
			"typeContactgegeven"));
		addFieldModifier(new ConstructorArgModifier("standaardContactgegeven",
			new Model<ArrayList<StandaardContactgegeven>>(new ArrayList<StandaardContactgegeven>(
				EnumSet.allOf(StandaardContactgegeven.class)))));
	}
}
