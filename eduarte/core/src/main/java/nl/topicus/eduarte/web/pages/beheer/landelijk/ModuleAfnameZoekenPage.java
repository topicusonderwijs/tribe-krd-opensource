package nl.topicus.eduarte.web.pages.beheer.landelijk;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.security.RechtenSoorten;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.core.principals.Always;
import nl.topicus.eduarte.dao.helpers.ModuleAfnameDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.ModuleAfname;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.ModuleAfnameTable;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.ModuleAfnameZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;

@InPrincipal(Always.class)
@PageInfo(title = "Module-afnames", menu = "Beheer > Modules")
@RechtenSoorten(RechtenSoort.BEHEER)
public class ModuleAfnameZoekenPage extends AbstractBeheerPage<Void>
{
	public ModuleAfnameZoekenPage()
	{
		super(BeheerMenuItem.Modules);

		EduArteDataPanel<ModuleAfname> datapanel =
			new EduArteDataPanel<ModuleAfname>("datapanel", GeneralFilteredSortableDataProvider.of(
				new ModuleAfnameZoekFilter(), ModuleAfnameDataAccessHelper.class),
				new ModuleAfnameTable());
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<ModuleAfname>(
			ModuleAfnameEditPage.class));
		add(datapanel);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new ToevoegenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return ModuleAfnameEditPage.class;
			}

			@Override
			public Page getPage()
			{
				return new ModuleAfnameEditPage(new ModuleAfname());
			}
		}));
	}
}
