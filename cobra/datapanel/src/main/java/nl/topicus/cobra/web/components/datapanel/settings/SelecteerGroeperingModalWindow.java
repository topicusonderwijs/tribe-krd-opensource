package nl.topicus.cobra.web.components.datapanel.settings;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.GroupProperty;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.model.IModel;

/**
 * Modal window waarmee een gebruiker voor een custom data panel de groepering aan kan
 * geven.
 * 
 * @author loite
 */
public class SelecteerGroeperingModalWindow<T> extends CobraModalWindow<GroupProperty<T>>
{
	private static final long serialVersionUID = 1L;

	private CustomDataPanel<T> dataPanel;

	public SelecteerGroeperingModalWindow(String id, final CustomDataPanel<T> dataPanel,
			final IModel<GroupProperty<T>> settingsModel)
	{
		super(id, settingsModel);
		this.dataPanel = dataPanel;
		setWindowClosedCallback(new WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				target.addComponent(dataPanel);
			}
		});
		setInitialWidth(500);
		setInitialHeight(400);
		setTitle("Selecteer groepeeroptie");
	}

	@Override
	protected CobraModalWindowBasePanel<GroupProperty<T>> createContents(String id)
	{
		return new SelecteerGroeperingPanel<T>(id, this, dataPanel, getModel());
	}
}
