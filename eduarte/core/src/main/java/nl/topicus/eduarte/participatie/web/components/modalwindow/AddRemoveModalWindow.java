package nl.topicus.eduarte.participatie.web.components.modalwindow;

import nl.topicus.cobra.web.components.modal.CobraModalWindow;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;

public abstract class AddRemoveModalWindow extends CobraModalWindow<Void>
{
	private static final long serialVersionUID = 1L;

	private boolean deleteOnClose;

	public AddRemoveModalWindow(String id)
	{
		super(id);

		setWindowClosedCallback(new WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				if (isDeleteOnClose())
				{
					removeProperty(target);
				}
				refreshProperty(target);
			}
		});
	}

	public abstract void refreshProperty(AjaxRequestTarget target);

	public abstract void addProperty(AjaxRequestTarget target);

	public abstract void removeProperty(AjaxRequestTarget target);

	protected void onSubmit()
	{
		setDeleteOnClose(false);
	}

	public void setDeleteOnClose(boolean deleteOnClose)
	{
		this.deleteOnClose = deleteOnClose;
	}

	public boolean isDeleteOnClose()
	{
		return deleteOnClose;
	}
}
