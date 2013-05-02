package nl.topicus.cobra.web.components.modal;

import nl.topicus.cobra.test.AllowedMethods;
import nl.topicus.cobra.web.components.panels.TypedPanel;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.CloseButtonCallback;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public abstract class CobraModalWindow<T> extends TypedPanel<T> implements WindowClosedCallback
{
	@AllowedMethods( {"public org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow(java.lang.String)"})
	private class InternalModalWindow extends ModalWindow
	{
		private static final long serialVersionUID = 1L;

		private InternalModalWindow(String id)
		{
			super(id);
		}

		@Override
		public int getInitialHeight()
		{
			return initialHeight;
		}

		@Override
		public int getInitialWidth()
		{
			return initialWidth;
		}

		@Override
		public IModel<String> getTitle()
		{
			return new Model<String>(title);
		}

		@Override
		public boolean isResizable()
		{
			return resizable;
		}

		@Override
		public String getCookieName()
		{
			return cookieName;
		}
	}

	private static final long serialVersionUID = 1L;

	private ModalWindow modalWindow;

	private WindowClosedCallback chainWindowClosedCallback;

	private CloseButtonCallback tmpCloseButtonCallback;

	private int initialWidth = 600;

	private int initialHeight = 400;

	private boolean resizable = true;

	private String cookieName;

	private String title;

	public CobraModalWindow(String id)
	{
		super(id);
	}

	public CobraModalWindow(String id, IModel<T> model)
	{
		super(id, model);
	}

	abstract protected CobraModalWindowBasePanel<T> createContents(String id);

	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();
		if (!hasBeenRendered())
			findContainer().addModalWindow(this);
	}

	private ModalWindowContainer findContainer()
	{
		ModalWindowContainer ret = getPage().getMetaData(new ModalWindowContainerLocator());
		if (ret == null)
			throw new WicketRuntimeException(
				"Cannot find a ModalWindowContainer on the page, make sure it is added to the page");
		return ret;
	}

	public ModalWindow createModalWindow(String windowId)
	{
		modalWindow = new InternalModalWindow(windowId);
		modalWindow.setWindowClosedCallback(this);
		if (tmpCloseButtonCallback != null)
			modalWindow.setCloseButtonCallback(tmpCloseButtonCallback);
		return modalWindow;
	}

	public void show(AjaxRequestTarget target)
	{
		modalWindow.setContent(createContents(modalWindow.getContentId()));
		modalWindow.show(target);
		target.appendJavascript(getContents().onShowStatement().render().toString());
	}

	public void close(AjaxRequestTarget target)
	{
		modalWindow.close(target);
	}

	public void setCloseButtonCallback(CloseButtonCallback callback)
	{
		if (modalWindow == null)
			tmpCloseButtonCallback = callback;
		else
			modalWindow.setCloseButtonCallback(callback);
	}

	public void setWindowClosedCallback(WindowClosedCallback callback)
	{
		chainWindowClosedCallback = callback;
	}

	public void setInitialHeight(int initialHeight)
	{
		this.initialHeight = initialHeight;
	}

	public void setInitialWidth(int initialWidth)
	{
		this.initialWidth = initialWidth;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public void setResizable(boolean resizable)
	{
		this.resizable = resizable;
	}

	public void setCookieName(String cookieName)
	{
		this.cookieName = cookieName;
	}

	protected ModalWindow getModalWindow()
	{
		return modalWindow;
	}

	@SuppressWarnings("unchecked")
	public CobraModalWindowBasePanel<T> getContents()
	{
		return (CobraModalWindowBasePanel<T>) modalWindow.get(modalWindow.getContentId());
	}

	public boolean isShown()
	{
		return modalWindow != null && modalWindow.isShown();
	}

	@Override
	public void onClose(AjaxRequestTarget target)
	{
		if (chainWindowClosedCallback != null)
			chainWindowClosedCallback.onClose(target);
		modalWindow.setContent(new WebMarkupContainer(modalWindow.getContentId()));
	}
}
