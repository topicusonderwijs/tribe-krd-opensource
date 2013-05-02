package nl.topicus.cobra.web.components.datapanel.selection.bottomrow;

import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractAjaxLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;

import org.apache.wicket.ajax.AjaxRequestTarget;

public abstract class AbstractSelecterenButton<R, S> extends AbstractAjaxLinkButton
{
	private static final long serialVersionUID = 1L;

	private ISelectionComponent<R, S> selectionPanel;

	public AbstractSelecterenButton(BottomRowPanel bottomRow,
			ISelectionComponent<R, S> selectionPanel, String label)
	{
		super(bottomRow, label, CobraKeyAction.GEEN, ButtonAlignment.LEFT);
		this.selectionPanel = selectionPanel;
	}

	public ISelectionComponent<R, S> getSelectionPanel()
	{
		return selectionPanel;
	}

	@Override
	protected void onClick(AjaxRequestTarget target)
	{
		performSelectionAction(selectionPanel.getDataPanel(), selectionPanel.getSelection());
		target.addComponent(selectionPanel.getDataPanel());
		selectionPanel.onSelectionChanged(target);
	}

	protected abstract void performSelectionAction(CustomDataPanel<S> datapanel,
			Selection<R, S> selection);

	public static <R, S> void addSelectieButtons(BottomRowPanel bottomRow,
			ISelectionComponent<R, S> selectionPanel)
	{
		bottomRow.addButton(new AllesSelecterenButton<R, S>(bottomRow, selectionPanel));
		bottomRow.addButton(new HelePaginaSelecterenButton<R, S>(bottomRow, selectionPanel));
		bottomRow.addButton(new SelectieOpheffenButton<R, S>(bottomRow, selectionPanel));
		bottomRow.addButton(new SelectieTonenButton<R, S>(bottomRow, selectionPanel));
	}
}
