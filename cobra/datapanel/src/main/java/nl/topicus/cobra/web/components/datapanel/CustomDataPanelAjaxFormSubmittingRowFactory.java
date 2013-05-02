package nl.topicus.cobra.web.components.datapanel;

import nl.topicus.cobra.web.behaviors.AppendingAttributeModifier;
import nl.topicus.cobra.web.components.datapanel.items.AjaxClickableItem;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.behavior.IBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * @author hop
 */
public abstract class CustomDataPanelAjaxFormSubmittingRowFactory<T> extends
		CustomDataPanelAjaxClickableRowFactory<T>
{
	private static final long serialVersionUID = 1L;

	private Form< ? > form;

	public CustomDataPanelAjaxFormSubmittingRowFactory(Form< ? > form)
	{
		super();
		this.form = form;
	}

	public CustomDataPanelAjaxFormSubmittingRowFactory(Form< ? > form, IModel<T> model)
	{
		super(model);
		this.form = form;
	}

	@Override
	protected AjaxClickableItem<T> createClickableRow(String id, final CustomDataPanel<T> panel,
			final Item<T> item, final IModel<T> itemModel)
	{
		AjaxClickableItem<T> ret =
			new AjaxFormSubmittingItem(id, item.getIndex(), item.getModel(), itemModel, panel, item);
		if (isSelected(panel, item, itemModel))
			ret.add(new AppendingAttributeModifier("class", "tblSelected"));
		return ret;
	}

	protected abstract void onError(AjaxRequestTarget target);

	private final class AjaxFormSubmittingItem extends AjaxClickableItem<T>
	{
		private final IModel<T> itemModel;

		private final CustomDataPanel<T> panel;

		private final Item<T> item;

		private static final long serialVersionUID = 1L;

		private AjaxFormSubmittingItem(String id, int index, IModel<T> model, IModel<T> itemModel,
				CustomDataPanel<T> panel, Item<T> item)
		{
			super(id, index, model);
			this.itemModel = itemModel;
			this.panel = panel;
			this.item = item;
		}

		@Override
		protected IBehavior newOnClickBehavior()
		{
			return new AjaxFormSubmitBehavior(form, "onclick")
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onError(AjaxRequestTarget target)
				{
					AjaxFormSubmittingItem.this.onError(target);
				}

				@Override
				protected void onSubmit(AjaxRequestTarget target)
				{
					onClick(target);
				}
			};
		}

		@Override
		public void onClick(AjaxRequestTarget target)
		{
			selectedIndex = item.getIndex();
			selectedPage = panel.getCurrentPage();
			if (selectedObject != null)
				selectedObject.setObject(itemModel.getObject());
			CustomDataPanelAjaxFormSubmittingRowFactory.this.onClick(target, item);
		}

		protected void onError(AjaxRequestTarget target)
		{
			CustomDataPanelAjaxFormSubmittingRowFactory.this.onError(target);
		}
	}
}
