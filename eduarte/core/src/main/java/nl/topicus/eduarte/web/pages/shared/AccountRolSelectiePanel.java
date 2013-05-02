package nl.topicus.eduarte.web.pages.shared;

import java.util.List;

import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.eduarte.dao.helpers.RolDataAccessHelper;
import nl.topicus.eduarte.entities.security.authentication.AccountRol;
import nl.topicus.eduarte.entities.security.authorization.Rol;
import nl.topicus.eduarte.web.components.panels.datapanel.selectie.EduArteSelectiePanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.RolTable;
import nl.topicus.eduarte.zoekfilters.RolZoekFilter;

import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.data.IDataProvider;

public class AccountRolSelectiePanel extends EduArteSelectiePanel<AccountRol, Rol, RolZoekFilter>
{

	private static final long serialVersionUID = 1L;

	public AccountRolSelectiePanel(String id, RolZoekFilter filter,
			Selection<AccountRol, Rol> selection)
	{
		super(id, filter, selection);
	}

	@Override
	protected CustomDataPanelContentDescription<Rol> createContentDescription()
	{
		return new RolTable();
	}

	@Override
	protected IDataProvider<Rol> createDataProvider(RolZoekFilter filter)
	{
		return GeneralFilteredSortableDataProvider.of(filter, RolDataAccessHelper.class);
	}

	@Override
	protected Panel createZoekFilterPanel(String id, RolZoekFilter filter,
			CustomDataPanel<Rol> customDataPanel)
	{
		return new EmptyPanel(id);
	}

	@Override
	protected String getEntityName()
	{
		return "rollen";
	}

	@Override
	public List<AccountRol> getSelectedElements()
	{
		throw new UnsupportedOperationException();
	}
}
