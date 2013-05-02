package nl.topicus.eduarte.krd.web.pages.beheer.organisatie.extern;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.dao.helpers.ExterneOrganisatieContactPersoonRolDataAccessHelper;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoonRol;
import nl.topicus.eduarte.krd.principals.beheer.ContactpersoonRollen;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.ExterneOrganisatieContactPersoonRolTable;
import nl.topicus.eduarte.web.components.panels.filter.NaamActiefZoekFilterPanel;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.ExterneOrganisatieContactPersoonRolZoekFilter;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

@PageInfo(title = "Contactpersoon rollen", menu = "Beheer > Beheer tabellen >ExterneOrganisatieContactPersoonRol")
@InPrincipal(ContactpersoonRollen.class)
public class ExterneOrganisatieContactPersoonRolZoekenPage extends AbstractBeheerPage<Void>
{
	private static ExterneOrganisatieContactPersoonRolZoekFilter getDefaultFilter()
	{
		ExterneOrganisatieContactPersoonRolZoekFilter ret =
			new ExterneOrganisatieContactPersoonRolZoekFilter();
		ret.setActief(true);
		return ret;
	}

	public ExterneOrganisatieContactPersoonRolZoekenPage()
	{
		super(BeheerMenuItem.ExterneOrganisatieContactPersoonRol);

		ExterneOrganisatieContactPersoonRolZoekFilter filter = getDefaultFilter();
		IDataProvider<ExterneOrganisatieContactPersoonRol> dataprovider =
			GeneralFilteredSortableDataProvider.of(filter,
				ExterneOrganisatieContactPersoonRolDataAccessHelper.class);

		EduArteDataPanel<ExterneOrganisatieContactPersoonRol> datapanel =
			new EduArteDataPanel<ExterneOrganisatieContactPersoonRol>("datapanel", dataprovider,
				new ExterneOrganisatieContactPersoonRolTable());
		datapanel
			.setRowFactory(new CustomDataPanelPageLinkRowFactory<ExterneOrganisatieContactPersoonRol>(
				ExterneOrganisatieContactPersoonRolEditPage.class)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onClick(Item<ExterneOrganisatieContactPersoonRol> item)
				{
					setResponsePage(new ExterneOrganisatieContactPersoonRolEditPage(
						item.getModel(), ExterneOrganisatieContactPersoonRolZoekenPage.this));
				}
			});
		add(datapanel);

		NaamActiefZoekFilterPanel filterPanel =
			new NaamActiefZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new ToevoegenButton(panel, "Nieuwe rol",
			ExterneOrganisatieContactPersoonRolEditPage.class,
			ExterneOrganisatieContactPersoonRolZoekenPage.this));
	}
}
