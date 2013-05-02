/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.onderwijs.examenrapportage;

import java.util.Collection;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.modifier.VisibilityModifier;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractPageBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.core.principals.onderwijs.OnderwijsDocumentTemplateRead;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateCategorie;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenuItem;
import nl.topicus.eduarte.web.components.panels.datapanel.table.DocumentTemplateTable;
import nl.topicus.eduarte.web.pages.beheer.rapportage.DocumentTemplateZoekenPage;
import nl.topicus.eduarte.web.pages.shared.OnderwijsPageContext;
import nl.topicus.eduarte.zoekfilters.DocumentTemplateZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.Model;

/**
 * Pagina voor het beheren van document templates. Document templates zijn RTF-bestanden
 * (of wellicht in de toekomst nog andere formaten) die door de instelling in het systeem
 * kunnen worden gezet. Deze templates kunnen vervolgens bij een deelnemer worden
 * opgevraagd, waarbij properties van de huidige deelnemer worden ingevuld.
 * 
 * @author hoeve
 */
@PageInfo(title = "Onderwijs samenvoegdocumenten", menu = {"Onderwijs > Samenvoegdocumenten > samenvoegdocumenten"})
@InPrincipal(OnderwijsDocumentTemplateRead.class)
public class OnderwijsDocumentTemplateZoekenPage extends DocumentTemplateZoekenPage
{
	private static final long serialVersionUID = 1L;

	public OnderwijsDocumentTemplateZoekenPage()
	{
		super(new OnderwijsPageContext("Samenvoegdocumenten",
			OnderwijsCollectiefMenuItem.Samenvoegdocumenten));

		AutoFieldSet< ? > fieldSet = (AutoFieldSet< ? >) get("filter:form:inputfields");
		fieldSet.addFieldModifier(new VisibilityModifier(new Model<Boolean>(false), "context",
			"categorie"));
		Collection<String> propertyNames = fieldSet.getPropertyNames();
		propertyNames.add("examenDocumentType");
		propertyNames.add("taxonomie");

		// createComponents();
	}

	@Override
	protected Class< ? extends Page> getPageLinkTarget()
	{
		return OnderwijsDocumentTemplateKaartPage.class;
	}

	@Override
	protected DocumentTemplateTable getDataPanelTable()
	{
		DocumentTemplateTable table = new DocumentTemplateTable();
		table.getColumns().add(
			3,
			new CustomPropertyColumn<DocumentTemplate>("Onderwijsdocument-type",
				"Onderwijsdocument-type", "examenDocumentType", "examenDocumentType"));
		table.getColumns().add(
			4,
			new CustomPropertyColumn<DocumentTemplate>("Taxonomie", "Taxonomie", "taxonomie",
				"taxonomie"));

		return table;
	}

	@Override
	protected DocumentTemplateZoekFilter getZoekFilter()
	{
		DocumentTemplateZoekFilter filter = new DocumentTemplateZoekFilter();
		filter.setCategorieen(DocumentTemplateCategorie.getExamenValues());

		return filter;
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new OnderwijsCollectiefMenu(id, OnderwijsCollectiefMenuItem.Samenvoegdocumenten);
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new AbstractPageBottomRowButton(panel, "Nieuw samenvoegdocument",
			CobraKeyAction.TOEVOEGEN, ButtonAlignment.RIGHT)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public IPageLink getPageLink()
			{
				return new IPageLink()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public Class< ? extends Page> getPageIdentity()
					{
						return OnderwijsDocumentTemplateEditPage.class;
					}

					@Override
					public Page getPage()
					{
						return new OnderwijsDocumentTemplateEditPage();
					}
				};
			}
		});
	}
}
