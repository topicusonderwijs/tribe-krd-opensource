package nl.topicus.eduarte.web.pages.beheer.organisatie;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.core.principals.groep.GroepsTypePrincipal;
import nl.topicus.eduarte.dao.helpers.GroepstypeDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groepstype;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.CodeNaamActiefTable;
import nl.topicus.eduarte.web.components.panels.filter.CodeNaamActiefZoekFilterPanel;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.GroepstypeZoekFilter;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

@PageInfo(title = "Groepstype", menu = "Beheer > Beheer tabellen > Groepstype")
@InPrincipal(GroepsTypePrincipal.class)
public class GroepstypeZoekenPage extends AbstractBeheerPage<Void>
{
	private static GroepstypeZoekFilter getDefaultFilter()
	{
		GroepstypeZoekFilter ret = new GroepstypeZoekFilter();
		ret.setActief(true);
		return ret;
	}

	public GroepstypeZoekenPage()
	{
		super(BeheerMenuItem.SoortOrganisatie_Eenheden);

		GroepstypeZoekFilter filter = getDefaultFilter();
		IDataProvider<Groepstype> dataprovider =
			GeneralFilteredSortableDataProvider.of(filter, GroepstypeDataAccessHelper.class);

		EduArteDataPanel<Groepstype> datapanel =
			new EduArteDataPanel<Groepstype>("datapanel", dataprovider,
				new CodeNaamActiefTable<Groepstype>("Groepstypen")
					.addColumn(new BooleanPropertyColumn<Groepstype>("Plaatsingsgroep",
						"Plaatsingsgroep", "plaatsingsgroep", "plaatsingsgroep")));
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Groepstype>(
			GroepstypeEditPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<Groepstype> item)
			{
				setResponsePage(new GroepstypeEditPage(item.getModel(), GroepstypeZoekenPage.this));
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
		panel.addButton(new ToevoegenButton(panel, "Nieuw groepstype", GroepstypeEditPage.class,
			GroepstypeZoekenPage.this));
	}
}
