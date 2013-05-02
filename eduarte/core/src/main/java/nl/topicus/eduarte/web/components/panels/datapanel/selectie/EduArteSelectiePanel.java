package nl.topicus.eduarte.web.components.panels.datapanel.selectie;

import nl.topicus.cobra.commons.interfaces.ZoekFilter;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.selection.AbstractSelectiePanel;
import nl.topicus.cobra.zoekfilters.DetachableZoekFilter;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;

import org.apache.wicket.markup.repeater.data.IDataProvider;

public abstract class EduArteSelectiePanel<R, S extends IdObject, D extends ZoekFilter<S> & DetachableZoekFilter<S>>
		extends AbstractSelectiePanel<R, S, D>
{
	private static final long serialVersionUID = 1L;

	public EduArteSelectiePanel(String id, D filter, Selection<R, S> selection)
	{
		super(id, filter, selection);
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
