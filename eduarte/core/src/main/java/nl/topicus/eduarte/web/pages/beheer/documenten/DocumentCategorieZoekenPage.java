package nl.topicus.eduarte.web.pages.beheer.documenten;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.core.principals.beheer.systeem.DocumentPrincipal;
import nl.topicus.eduarte.dao.helpers.DocumentCategorieDataAccessHelper;
import nl.topicus.eduarte.entities.bijlage.DocumentCategorie;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateRecht;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.CodeNaamActiefTable;
import nl.topicus.eduarte.web.components.panels.filter.CodeNaamActiefZoekFilterPanel;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.DocumentCategorieZoekFilter;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

@PageInfo(title = "Documentcategorieën", menu = "Beheer > Beheer tabellen > Documentcategorieën")
@InPrincipal(DocumentPrincipal.class)
public class DocumentCategorieZoekenPage extends AbstractBeheerPage<Void>
{
	private static DocumentCategorieZoekFilter getDefaultFilter()
	{
		DocumentCategorieZoekFilter ret = new DocumentCategorieZoekFilter();
		ret.setActief(Boolean.TRUE);
		return ret;
	}

	public DocumentCategorieZoekenPage()
	{
		super(BeheerMenuItem.DocumentCategorien);

		DocumentCategorieZoekFilter filter = getDefaultFilter();
		IDataProvider<DocumentCategorie> dataprovider =
			GeneralFilteredSortableDataProvider.of(filter, DocumentCategorieDataAccessHelper.class);

		EduArteDataPanel<DocumentCategorie> datapanel =
			new EduArteDataPanel<DocumentCategorie>("datapanel", dataprovider,
				new CodeNaamActiefTable<DocumentCategorie>("Documentcategorie"));
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<DocumentCategorie>(
			DocumentCategorieEditPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<DocumentCategorie> item)
			{
				setResponsePage(new DocumentCategorieEditPage(ModelFactory
					.getCompoundChangeRecordingModel(item.getModel().getObject(),
						new DefaultModelManager(DocumentCategorie.class,
							DocumentTemplateRecht.class)), DocumentCategorieZoekenPage.this));
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
		panel.addButton(new ToevoegenButton(panel, "Nieuwe categorie",
			DocumentCategorieEditPage.class, DocumentCategorieZoekenPage.this));
	}
}
