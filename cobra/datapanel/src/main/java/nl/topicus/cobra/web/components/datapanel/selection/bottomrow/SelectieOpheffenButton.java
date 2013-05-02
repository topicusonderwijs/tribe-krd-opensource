package nl.topicus.cobra.web.components.datapanel.selection.bottomrow;

import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;

public class SelectieOpheffenButton<R, S> extends AbstractSelecterenButton<R, S>
{
	private static final long serialVersionUID = 1L;

	public SelectieOpheffenButton(BottomRowPanel bottomRow, ISelectionComponent<R, S> selectionPanel)
	{
		super(bottomRow, selectionPanel, "Selectie opheffen");
	}

	@Override
	protected void performSelectionAction(CustomDataPanel<S> datapanel, Selection<R, S> selection)
	{
		selection.clear();
	}
}
