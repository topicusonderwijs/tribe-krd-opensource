package nl.topicus.eduarte.web.components.panels.datapanel.selectie;

import nl.topicus.cobra.commons.interfaces.ZoekFilter;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.dao.helpers.ZoekFilterDataAccessHelper;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.panels.datapanel.selection.AbstractDatabaseSelectiePanel;
import nl.topicus.cobra.zoekfilters.DetachableZoekFilter;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;

import org.apache.wicket.markup.repeater.data.IDataProvider;

public abstract class EduArteDatabaseSelectiePanel<R, S extends IdObject, D extends ZoekFilter<S> & DetachableZoekFilter<S>>
		extends AbstractDatabaseSelectiePanel<R, S, D>
{
	private static final long serialVersionUID = 1L;

	public EduArteDatabaseSelectiePanel(String id, D filter,
			Class< ? extends ZoekFilterDataAccessHelper<S, D>> dahClass,
			DatabaseSelection<R, S> selection)
	{
		super(id, filter, dahClass, selection);
	}

	@Override
	protected CustomDataPanel<S> createDataPanel(String id, IDataProvider<S> provider,
			CustomDataPanelContentDescription<S> contents)
	{
		return new EduArteDataPanel<S>(id, provider, contents);
	}

	@Override
	public int getItemsPerPage()
	{
		return 20;
	}
}
