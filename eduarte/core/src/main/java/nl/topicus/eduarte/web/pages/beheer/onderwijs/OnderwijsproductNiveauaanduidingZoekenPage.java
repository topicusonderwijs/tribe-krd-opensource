package nl.topicus.eduarte.web.pages.beheer.onderwijs;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.core.principals.onderwijs.CurriculumInzien;
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductNiveauaanduiding;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.CodeNaamActiefTable;
import nl.topicus.eduarte.web.components.panels.filter.CodeNaamActiefZoekFilterPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.CodeNaamActiefZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.data.IDataProvider;

@PageInfo(title = "Niveauaanduiding onderwijsproduct", menu = "Beheer > Onderwijs > Niveauaanduiding")
@InPrincipal(CurriculumInzien.class)
public class OnderwijsproductNiveauaanduidingZoekenPage extends AbstractBeheerPage<Void>
{
	private static CodeNaamActiefZoekFilter<OnderwijsproductNiveauaanduiding> getDefaultFilter()
	{
		CodeNaamActiefZoekFilter<OnderwijsproductNiveauaanduiding> ret =
			new CodeNaamActiefZoekFilter<OnderwijsproductNiveauaanduiding>(
				OnderwijsproductNiveauaanduiding.class);
		ret.setActief(true);
		return ret;
	}

	public OnderwijsproductNiveauaanduidingZoekenPage()
	{
		super(BeheerMenuItem.OnderwijsproductNiveau);
		CodeNaamActiefZoekFilter<OnderwijsproductNiveauaanduiding> filter = getDefaultFilter();
		@SuppressWarnings("unchecked")
		IDataProvider<OnderwijsproductNiveauaanduiding> dataprovider =
			new GeneralFilteredSortableDataProvider(filter,
				CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class);

		CodeNaamActiefTable<OnderwijsproductNiveauaanduiding> table =
			new CodeNaamActiefTable<OnderwijsproductNiveauaanduiding>("Niveauaanduiding");
		EduArteDataPanel<OnderwijsproductNiveauaanduiding> datapanel =
			new EduArteDataPanel<OnderwijsproductNiveauaanduiding>("datapanel", dataprovider, table);
		datapanel
			.setRowFactory(new CustomDataPanelPageLinkRowFactory<OnderwijsproductNiveauaanduiding>(
				OnderwijsproductNiveauaanduidingEditPage.class));
		add(datapanel);
		CodeNaamActiefZoekFilterPanel filterPanel =
			new CodeNaamActiefZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new ToevoegenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				OnderwijsproductNiveauaanduiding niveau = new OnderwijsproductNiveauaanduiding();
				return new OnderwijsproductNiveauaanduidingEditPage(niveau,
					OnderwijsproductNiveauaanduidingZoekenPage.this);
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return OnderwijsproductNiveauaanduidingEditPage.class;
			}
		}));
	}
}
