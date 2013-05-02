/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.beheer.rapportage;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractPageBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.core.principals.rapportage.DocumentTemplateRead;
import nl.topicus.eduarte.dao.helpers.DocumentTemplateDataAccessHelper;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateCategorie;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.DocumentTemplateTable;
import nl.topicus.eduarte.web.components.panels.filter.DocumentTemplateZoekFilterPanel;
import nl.topicus.eduarte.web.pages.AbstractDynamicContextPage;
import nl.topicus.eduarte.web.pages.PageContext;
import nl.topicus.eduarte.web.pages.shared.BeheerPageContext;
import nl.topicus.eduarte.zoekfilters.DocumentTemplateZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.data.IDataProvider;

/**
 * Pagina voor het beheren van document templates. Document templates zijn RTF-bestanden
 * (of wellicht in de toekomst nog andere formaten) die door de instelling in het systeem
 * kunnen worden gezet. Deze templates kunnen vervolgens bij een deelnemer worden
 * opgevraagd, waarbij properties van de huidige deelnemer worden ingevuld.
 * 
 * @author hoeve
 */
@PageInfo(title = "Samenvoegdocumenten", menu = {"Beheer > Rapportage > Samenvoegdocumenten"})
@InPrincipal(DocumentTemplateRead.class)
public class DocumentTemplateZoekenPage extends AbstractDynamicContextPage<Void>
{
	private static final long serialVersionUID = 1L;

	public DocumentTemplateZoekenPage()
	{
		this(new BeheerPageContext("Samenvoegdocumenten", BeheerMenuItem.Samenvoegdocumenten));
	}

	public DocumentTemplateZoekenPage(PageContext context)
	{
		super(context);

		DocumentTemplateZoekFilter filter = getZoekFilter();

		IDataProvider<DocumentTemplate> provider =
			GeneralFilteredSortableDataProvider.of(filter, DocumentTemplateDataAccessHelper.class);
		final EduArteDataPanel<DocumentTemplate> datapanel =
			new EduArteDataPanel<DocumentTemplate>("datapanel", provider, getDataPanelTable());
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<DocumentTemplate>(
			getPageLinkTarget()));
		datapanel.setItemsPerPage(20);
		add(datapanel);
		add(new DocumentTemplateZoekFilterPanel("filter", filter, datapanel));

		createComponents();
	}

	protected DocumentTemplateTable getDataPanelTable()
	{
		return new DocumentTemplateTable();
	}

	protected Class< ? extends Page> getPageLinkTarget()
	{
		return DocumentTemplateKaartPage.class;
	}

	protected DocumentTemplateZoekFilter getZoekFilter()
	{
		DocumentTemplateZoekFilter filter = new DocumentTemplateZoekFilter();
		filter.setCategorieen(DocumentTemplateCategorie.getValues());
		return filter;
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
						return DocumentTemplateEditPage.class;
					}

					@Override
					public Page getPage()
					{
						return new DocumentTemplateEditPage(new DocumentTemplate());
					}
				};
			}
		});
	}
}
