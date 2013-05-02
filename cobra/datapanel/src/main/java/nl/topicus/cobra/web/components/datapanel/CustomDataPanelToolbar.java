package nl.topicus.cobra.web.components.datapanel;

import java.util.List;

import nl.topicus.cobra.web.components.panels.TypedPanel;

import org.apache.wicket.model.IModel;

public class CustomDataPanelToolbar<T> extends TypedPanel<List<CustomColumn<T>>>
{
	public static final String TOOLBAR_ID = "toolbar";

	private static final long serialVersionUID = 1L;

	public CustomDataPanelToolbar()
	{
		this(null);
	}

	public CustomDataPanelToolbar(IModel<List<CustomColumn<T>>> model)
	{
		super(TOOLBAR_ID, model);
	}
}
