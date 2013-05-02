package nl.topicus.eduarte.web.components.modalwindow;

import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IObjectClassAwareModel;

public class EnumSelectModalWindow<T extends Enum<T>> extends CobraModalWindow<T>
{
	public EnumSelectModalWindow(String id, IObjectClassAwareModel<T> model)
	{
		super(id, model);
	}

	private static final long serialVersionUID = 1L;

	@Override
	protected CobraModalWindowBasePanel<T> createContents(String id)
	{
		return new EnumSelectModalWindowPanel<T>(id, this)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target, Item<T> item)
			{
				EnumSelectModalWindow.this.onClick(target, item);
			}
		};
	}

	@SuppressWarnings("unused")
	protected void onClick(AjaxRequestTarget target, Item<T> item)
	{
	}
}
