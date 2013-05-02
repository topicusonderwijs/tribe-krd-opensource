package nl.topicus.eduarte.web.components.panels;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.web.components.panels.TypedPanel;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

public abstract class AbstractDropdownToevoegenBewerkenPanel<T extends Enum<T>, E> extends
		TypedPanel<List<E>>
{
	private class SingleGekoppeldeTemplateModel implements IModel<E>
	{
		private static final long serialVersionUID = 1L;

		private final int index;

		private ListModelDataProvider<E> dataprovider;

		private SingleGekoppeldeTemplateModel(int index, ListModelDataProvider<E> dataprovider)
		{
			this.index = index;
			this.dataprovider = dataprovider;
		}

		@Override
		public E getObject()
		{
			return dataprovider.getList().get(index);
		}

		@Override
		@SuppressWarnings("unchecked")
		public void setObject(Object object)
		{
			if (object == null)
				getEntiteiten().remove(index);
			else
				getEntiteiten().add(index, (E) object);
		}

		@Override
		public void detach()
		{
		}

		@Override
		public int hashCode()
		{
			return index;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (obj instanceof AbstractDropdownToevoegenBewerkenPanel< ? , ? >.SingleGekoppeldeTemplateModel)
			{
				SingleGekoppeldeTemplateModel other = (SingleGekoppeldeTemplateModel) obj;
				return other.index == index;
			}
			return false;
		}
	}

	private static final long serialVersionUID = 1L;

	public AbstractDropdownToevoegenBewerkenPanel(String id, final IModel<List<E>> model)
	{
		super(id, model);

		DataView<E> entiteiten =
			new DataView<E>("entiteiten", new ListModelDataProvider<E>(
				new AbstractReadOnlyModel<List<E>>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public List<E> getObject()
					{
						List<E> templates = new ArrayList<E>(getEntiteiten());
						templates.add(null);
						return templates;
					}
				})
			{
				private static final long serialVersionUID = 1L;

				@Override
				public IModel<E> model(Object baseObject)
				{
					int index = getList().indexOf(baseObject);
					return new SingleGekoppeldeTemplateModel(index, this);
				}
			})
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(Item<E> item)
				{
					AbstractDropdownEditPanel<T, E> editPanel =
						createDropdownEditPanel("editPanel", item.getModel());
					editPanel.addUpdateComponent(AbstractDropdownToevoegenBewerkenPanel.this);
					item.add(editPanel);
				}
			};
		add(entiteiten);
		entiteiten.setItemReuseStrategy(ReuseIfModelsEqualStrategy.getInstance());
		setOutputMarkupId(true);
	}

	protected abstract AbstractDropdownEditPanel<T, E> createDropdownEditPanel(String id,
			IModel<E> model);

	private List<E> getEntiteiten()
	{
		return getModelObject();
	}
}
