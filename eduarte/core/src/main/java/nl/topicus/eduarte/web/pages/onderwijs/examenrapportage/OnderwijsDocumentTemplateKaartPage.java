/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.onderwijs.examenrapportage;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.BewerkenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.core.principals.onderwijs.OnderwijsDocumentTemplateRead;
import nl.topicus.eduarte.entities.rapportage.OnderwijsDocumentTemplate;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenuItem;
import nl.topicus.eduarte.web.pages.beheer.rapportage.DocumentTemplateKaartPage;
import nl.topicus.eduarte.web.pages.shared.OnderwijsPageContext;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * @author hoeve
 */
@PageInfo(title = "Onderwijs samenvoegdocument", menu = {"Onderwijs > Samenvoegdocumenten > [Onderwijs samenvoegdocumenten]"})
@InPrincipal(OnderwijsDocumentTemplateRead.class)
public class OnderwijsDocumentTemplateKaartPage extends
		DocumentTemplateKaartPage<OnderwijsDocumentTemplate>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor voor bookmarks
	 * 
	 * @param template
	 */
	public OnderwijsDocumentTemplateKaartPage(OnderwijsDocumentTemplate template)
	{
		this(ModelFactory.getModel(template));
	}

	/**
	 * Constructor voor het doorklikken op zoeken pages
	 * 
	 * @param templateModel
	 */
	public OnderwijsDocumentTemplateKaartPage(IModel<OnderwijsDocumentTemplate> templateModel)
	{
		super(templateModel, new OnderwijsPageContext("Samenvoegdocumenten",
			OnderwijsCollectiefMenuItem.Samenvoegdocumenten));
		// createComponents();
	}

	@Override
	public List<String> getPropertyNames()
	{
		List<String> names = new ArrayList<String>(super.getPropertyNames());
		names.add(0, "taxonomie");
		names.add(2, "examenDocumentType");

		return names;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new BewerkenButton<OnderwijsDocumentTemplate>(panel,
			OnderwijsDocumentTemplateEditPage.class, getContextModel()));
		panel.addButton(new TerugButton(panel, OnderwijsDocumentTemplateZoekenPage.class));

	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new OnderwijsCollectiefMenu(id, OnderwijsCollectiefMenuItem.Samenvoegdocumenten);
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, "Samenvoegdocument " + getContextModelObject().getOmschrijving());
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.clear();
		ctorArgTypes.add(OnderwijsDocumentTemplate.class);
		ctorArgValues.clear();
		ctorArgValues.add(getContextModelObject());
	}

}
