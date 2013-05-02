package nl.topicus.eduarte.web.components.panels;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.panels.TypedPanel;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;

public abstract class AbstractDropdownEditPanel<T extends Enum<T>, E> extends TypedPanel<E>
{
	private class TypeModel implements IModel<T>
	{
		private static final long serialVersionUID = 1L;

		@Override
		public T getObject()
		{
			return getEntiteit() == null ? null : getType(getEntiteit());
		}

		@Override
		public void setObject(T object)
		{
			if (getEntiteit() == null)
				AbstractDropdownEditPanel.this.setDefaultModelObject(createEntiteit());
			setType(getEntiteit(), object);
		}

		@Override
		public void detach()
		{
		}
	}

	private static final long serialVersionUID = 1L;

	private List<Component> updateComponents = new ArrayList<Component>();

	public AbstractDropdownEditPanel(String id, IModel<E> model, T... values)
	{
		super(id, model);

		setOutputMarkupId(true);

		final EnumCombobox<T> typeBox = new EnumCombobox<T>("type", new TypeModel(), values)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isRequired()
			{
				return getEntiteit() != null;
			}
		};
		typeBox.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				AbstractDropdownEditPanel.this.onUpdate(target);
				target.addComponent(AbstractDropdownEditPanel.this);
			}
		});
		add(typeBox);

		add(new AjaxLink<Void>("delete")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				AbstractDropdownEditPanel.this.setDefaultModelObject(null);
				typeBox.modelChanged();
				onUpdate(target);
				target.addComponent(AbstractDropdownEditPanel.this);
			}

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && getEntiteit() != null;
			}
		});

	}

	public E getEntiteit()
	{
		return getModelObject();
	}

	protected void onUpdate(AjaxRequestTarget target)
	{
		for (Component curUpdateComponent : updateComponents)
			target.addComponent(curUpdateComponent);
	}

	public void addUpdateComponent(Component updateComponent)
	{
		updateComponents.add(updateComponent);
	}

	protected abstract E createEntiteit();

	protected abstract T getType(E entiteit);

	protected abstract void setType(E entiteit, T type);
}
