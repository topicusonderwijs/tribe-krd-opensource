package nl.topicus.cobra.web.components.datapanel.settings;

import nl.topicus.cobra.web.components.datapanel.ColumnModel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;

public class SelecteerKolommenModalWindow<T> extends CobraModalWindow<Void>
{
	private static final long serialVersionUID = 1L;

	private CustomDataPanelContentDescription<T> content;

	private ColumnModel<T> settingsModel;

	public SelecteerKolommenModalWindow(String id, CustomDataPanelContentDescription<T> content,
			final ColumnModel<T> settingsModel)
	{
		super(id);
		this.content = content;
		this.settingsModel = settingsModel;
		setResizable(false);
		setInitialWidth(875);
		setInitialHeight(520);
		setTitle("Selecteer kolommen");
	}

	@Override
	protected CobraModalWindowBasePanel<Void> createContents(String id)
	{
		return new SelecteerKolommenPanel<T>(id, this, content, settingsModel);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		content.detach();
	}
}
