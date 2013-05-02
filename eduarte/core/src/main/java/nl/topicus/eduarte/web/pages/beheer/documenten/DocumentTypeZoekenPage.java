package nl.topicus.eduarte.web.pages.beheer.documenten;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.core.principals.beheer.systeem.DocumentPrincipal;
import nl.topicus.eduarte.dao.helpers.DocumentTypeDataAccessHelper;
import nl.topicus.eduarte.entities.bijlage.DocumentType;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.CodeNaamActiefTable;
import nl.topicus.eduarte.web.components.panels.filter.CodeNaamActiefZoekFilterPanel;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.DocumentTypeZoekFilter;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

@PageInfo(title = "Documenttypes", menu = "Beheer > Beheer tabellen > Documenttypes")
@InPrincipal(DocumentPrincipal.class)
public class DocumentTypeZoekenPage extends AbstractBeheerPage<Void>
{
	private static DocumentTypeZoekFilter getDefaultFilter()
	{
		DocumentTypeZoekFilter ret = new DocumentTypeZoekFilter();
		ret.setActief(Boolean.TRUE);
		return ret;
	}

	public DocumentTypeZoekenPage()
	{
		super(BeheerMenuItem.DocumentTypes);
		DocumentTypeZoekFilter filter = getDefaultFilter();
		IDataProvider<DocumentType> dataprovider =
			GeneralFilteredSortableDataProvider.of(filter, DocumentTypeDataAccessHelper.class);

		EduArteDataPanel<DocumentType> datapanel =
			new EduArteDataPanel<DocumentType>("datapanel", dataprovider,
				new CodeNaamActiefTable<DocumentType>("Documenttypes")
					.addColumn(new CustomPropertyColumn<DocumentType>("Categorie", "Categorie",
						"categorie.naam", "categorie.naam")));
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<DocumentType>(
			DocumentTypeEditPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<DocumentType> item)
			{
				setResponsePage(new DocumentTypeEditPage(item.getModel(),
					DocumentTypeZoekenPage.this));
			}
		});
		add(datapanel);

		CodeNaamActiefZoekFilterPanel filterPanel =
			new CodeNaamActiefZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new ToevoegenButton(panel, "Nieuw documenttype",
			DocumentTypeEditPage.class, DocumentTypeZoekenPage.this));
	}
}
