/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.io.Serializable;
import java.util.Arrays;

import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateCategorie;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateContext;
import nl.topicus.eduarte.zoekfilters.DocumentTemplateZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * @author loite
 */
public class DocumentTemplateZoekFilterPanel extends
		AutoZoekFilterPanel<DocumentTemplateZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public DocumentTemplateZoekFilterPanel(String id, DocumentTemplateZoekFilter filter,
			IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("omschrijving", "bestandsnaam", "context", "categorie",
			"valide", "actief"));
		addFieldModifier(new ConstructorArgModifier("context",
			(Serializable[]) DocumentTemplateContext.getValues()));
		addFieldModifier(new ConstructorArgModifier("categorie",
			(Serializable[]) DocumentTemplateCategorie.getValues()));

	}
}
