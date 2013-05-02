/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.groep;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelRowFactory;
import nl.topicus.eduarte.core.principals.groep.GroepDocumenten;
import nl.topicus.eduarte.entities.bijlage.BijlageEntiteit;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.groep.GroepBijlage;
import nl.topicus.eduarte.providers.GroepProvider;
import nl.topicus.eduarte.web.components.menu.GroepMenuItem;
import nl.topicus.eduarte.web.components.panels.DocumentenPanel;

import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.PropertyModel;

/**
 * @author hoeve
 */
@PageInfo(title = "Groep documenten", menu = "Groep > [groep] -> Documenten")
@InPrincipal(GroepDocumenten.class)
public class GroepDocumentenPage extends AbstractGroepPage
{
	private static final long serialVersionUID = 1L;

	/**
	 * Bookmarkable constructor.
	 * 
	 * @see AbstractGroepPage#getGroepFromPageParameters(PageParameters)
	 */
	public GroepDocumentenPage(PageParameters parameters)
	{
		this(AbstractGroepPage.getGroepFromPageParameters(parameters));
	}

	public GroepDocumentenPage(GroepProvider provider)
	{
		this(provider.getGroep());
	}

	/**
	 * Constructor
	 * 
	 * @param groep
	 */
	public GroepDocumentenPage(Groep groep)
	{
		super(GroepMenuItem.Documenten, groep);

		add(new DocumentenPanel<GroepBijlage, Groep>("datapanel", getContextGroepModel(), false)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected CustomDataPanelRowFactory<BijlageEntiteit> createRowFactory()
			{
				return new CustomDataPanelRowFactory<BijlageEntiteit>();
			}

		});

		createComponents();
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, new PropertyModel<String>(getDefaultModel(), "naam"));
	}
}
