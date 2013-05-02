package nl.topicus.cobra.web.components.datapanel.selection;

import java.util.List;

import nl.topicus.cobra.commons.interfaces.ZoekFilter;
import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;

import org.apache.wicket.ajax.AjaxRequestTarget;

public interface ISelectionComponent<R, S>
{
	public Selection<R, S> getSelection();

	public ZoekFilter<S> getFilter();

	public List<S> getSelectedSearchElements();

	public List<R> getSelectedElements();

	public CustomDataPanel<S> getDataPanel();

	public void onSelectionChanged(AjaxRequestTarget target);
}
