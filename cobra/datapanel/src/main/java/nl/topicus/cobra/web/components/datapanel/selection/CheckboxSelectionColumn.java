package nl.topicus.cobra.web.components.datapanel.selection;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.columns.CheckboxColumn;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

public class CheckboxSelectionColumn<R, S> extends CheckboxColumn<S>
{
	private static final long serialVersionUID = 1L;

	private Selection<R, S> selection;

	private List<IUpdateListener<S>> listeners = new ArrayList<IUpdateListener<S>>();

	public CheckboxSelectionColumn(String id, String header, Selection<R, S> selection)
	{
		super(id, header, true, true);
		this.selection = selection;
	}

	@Override
	protected IModel<Boolean> getCheckBoxModel(final IModel<S> rowModel)
	{
		return new IModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				S obj = rowModel.getObject();
				return getSelection().isSelected(obj);
			}

			@Override
			public void setObject(Boolean object)
			{
				boolean value = object;
				S obj = rowModel.getObject();
				if (value)
					getSelection().add(obj);
				else
					getSelection().remove(obj);
			}

			@Override
			public void detach()
			{
			}
		};
	}

	@Override
	protected void onCheckboxSelectionChanged(IModel<Boolean> checkboxModel, IModel<S> rowModel,
			AjaxRequestTarget target)
	{
		for (IUpdateListener<S> curListener : listeners)
		{
			curListener.onUpdate(checkboxModel, rowModel, target);
		}
	}

	public void addUpdateListener(IUpdateListener<S> listener)
	{
		listeners.add(listener);
	}

	private Selection<R, S> getSelection()
	{
		return selection;
	}

	@Override
	public void detach()
	{
		super.detach();
		selection.detach();
	}
}
