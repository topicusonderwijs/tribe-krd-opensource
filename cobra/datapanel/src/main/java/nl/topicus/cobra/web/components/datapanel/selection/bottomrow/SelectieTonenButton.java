package nl.topicus.cobra.web.components.datapanel.selection.bottomrow;

import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.cobra.web.components.datapanel.selection.SwitchSelectionDataProvider;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;

public class SelectieTonenButton<R, S> extends AbstractSelecterenButton<R, S>
{
	private static final long serialVersionUID = 1L;

	private boolean selectieTonen;

	public SelectieTonenButton(BottomRowPanel bottomRow, ISelectionComponent<R, S> selectionPanel)
	{
		super(bottomRow, selectionPanel, "Selectie tonen");
	}

	@Override
	protected void performSelectionAction(CustomDataPanel<S> datapanel, Selection<R, S> selection)
	{
		SwitchSelectionDataProvider< ? > provider =
			(SwitchSelectionDataProvider< ? >) datapanel.getDataProvider();
		selectieTonen = !selectieTonen;
		provider.setUseSelection(selectieTonen);
	}

	@Override
	public boolean isVisible()
	{
		if (getSelectionPanel().getDataPanel() != null)
		{
			return super.isVisible()
				&& getSelectionPanel().getDataPanel().getDataProvider() instanceof SwitchSelectionDataProvider< ? >;
		}
		else
		{
			return super.isVisible();
		}
	}

	@Override
	public String getLabel()
	{
		return selectieTonen ? "Alles tonen" : "Selectie tonen";
	}
}
