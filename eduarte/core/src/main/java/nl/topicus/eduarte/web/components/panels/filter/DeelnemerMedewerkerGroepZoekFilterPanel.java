/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.ArrayList;
import java.util.Arrays;

import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.eduarte.entities.participatie.enums.DeelnemerMedewerkerGroepEnum;
import nl.topicus.eduarte.zoekfilters.DeelnemerMedewerkerGroepZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * @author loite
 */
public class DeelnemerMedewerkerGroepZoekFilterPanel extends
		AutoZoekFilterPanel<DeelnemerMedewerkerGroepZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public DeelnemerMedewerkerGroepZoekFilterPanel(String id,
			DeelnemerMedewerkerGroepZoekFilter filter, IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("omschrijving", "type"));
		ArrayList<DeelnemerMedewerkerGroepEnum> types =
			new ArrayList<DeelnemerMedewerkerGroepEnum>();
		types.addAll(Arrays.asList(DeelnemerMedewerkerGroepEnum.values()));
		types.remove(filter.getExcludeType());
		addFieldModifier(new ConstructorArgModifier("type", types));
	}
}