package nl.topicus.cobra.web.components.datapanel.selection.bottomrow;

import java.util.Iterator;

import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;

public class HelePaginaSelecterenButton<R, S> extends AbstractSelecterenButton<R, S>
{
	private static final long serialVersionUID = 1L;

	public HelePaginaSelecterenButton(BottomRowPanel bottomRow,
			ISelectionComponent<R, S> selectionPanel)
	{
		super(bottomRow, selectionPanel, "Hele pagina selecteren");
	}

	@Override
	protected void performSelectionAction(CustomDataPanel<S> datapanel, Selection<R, S> selection)
	{
		int rows = datapanel.getItemsPerPage();
		int page = datapanel.getCurrentPage();

		Iterator< ? extends S> it = datapanel.getDataProvider().iterator(page * rows, rows);
		while (it.hasNext())
		{
			selection.add(it.next());
		}
	}
}
