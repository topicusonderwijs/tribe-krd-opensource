package nl.topicus.eduarte.web.components.choice.multiple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class UitgebreidZoekMultipleChoice<T> extends FormComponentPanel<Collection<T>>
{
	private static final long serialVersionUID = 1L;

	private boolean active = false;

	private ListMultipleChoice<T> multipleChoice;

	public UitgebreidZoekMultipleChoice(String id, IModel<Collection<T>> model,
			IModel< ? extends List< ? extends T>> choices, IChoiceRenderer< ? super T> renderer)
	{
		super(id, model);
		setOutputMarkupId(true);
		add(new AjaxCheckBox("active", new PropertyModel<Boolean>(this, "active"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				target.addComponent(UitgebreidZoekMultipleChoice.this);
			}
		});
		multipleChoice = new ListMultipleChoice<T>("multipleChoice", model, choices, renderer)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public List< ? extends T> getChoices()
			{
				if (!active)
					return new ArrayList<T>();
				return super.getChoices();
			}

			@Override
			public boolean isVisible()
			{
				return isActive();
			}
		};
		multipleChoice.setOutputMarkupId(true);
		add(multipleChoice);
		if (model != null && model.getObject() != null)
		{
			active = !((Collection< ? >) model.getObject()).isEmpty();
		}
	}

	protected void setChoices(IModel< ? extends List< ? extends T>> choices)
	{
		multipleChoice.setChoices(choices);
	}

	@Override
	protected void convertInput()
	{
		setConvertedInput(multipleChoice.getConvertedInput());
	}

	public boolean isActive()
	{
		return active;
	}
}