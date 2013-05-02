package nl.topicus.cobra.web.components.choice;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.model.IModel;

/**
 * RadioChoice component die andere componenten update als zijn waarde veranderd. Is
 * handig voor het kunnen wijzigen van componenten die afhankelijk zijn van de hier
 * geselecteerde waarde. Vervanger voor de voormalige Wicktor componenten.
 * 
 * @author Martijn Dashorst
 */
public class AjaxRadioChoice<T> extends RadioChoice<T>
{
	private static final long serialVersionUID = 1L;

	/**
	 * De lijst van componenten die aangepast moeten worden als dit component wijzigt.
	 */
	private List<Component> components = new ArrayList<Component>();

	private boolean addFormUpdate = true;

	public AjaxRadioChoice(final String id)
	{
		super(id);
		init(true);
	}

	public AjaxRadioChoice(final String id, final List<T> choices)
	{
		super(id, choices);
		init(true);
	}

	public AjaxRadioChoice(final String id, final List<T> data, final IChoiceRenderer<T> renderer)
	{
		super(id, data, renderer);
		init(true);
	}

	public AjaxRadioChoice(final String id, final List<T> data, final IChoiceRenderer<T> renderer,
			boolean addAjaxHandler)
	{
		super(id, data, renderer);
		init(addAjaxHandler);
	}

	public AjaxRadioChoice(final String id, IModel<T> model, final List<T> choices)
	{
		super(id, model, choices);
		init(true);
	}

	public AjaxRadioChoice(final String id, IModel<T> model, final List<T> data,
			final IChoiceRenderer<T> renderer)
	{
		super(id, model, data, renderer);
		init(true);
	}

	public AjaxRadioChoice(final String id, IModel<T> model, final List<T> data,
			final IChoiceRenderer<T> renderer, boolean addAjaxHandler)
	{
		super(id, model, data, renderer);
		init(addAjaxHandler);
	}

	public AjaxRadioChoice(String id, IModel< ? extends List< ? extends T>> choices)
	{
		super(id, choices);
		init(true);
	}

	public AjaxRadioChoice(String id, IModel<T> model, IModel< ? extends List< ? extends T>> choices)
	{
		super(id, model, choices);
		init(true);
	}

	public AjaxRadioChoice(String id, IModel< ? extends List< ? extends T>> choices,
			IChoiceRenderer<T> renderer)
	{
		super(id, choices, renderer);
		init(true);
	}

	public AjaxRadioChoice(String id, IModel< ? extends List< ? extends T>> choices,
			IChoiceRenderer<T> renderer, boolean addAjaxHandler)
	{
		super(id, choices, renderer);
		init(addAjaxHandler);
	}

	public AjaxRadioChoice(String id, IModel<T> model,
			IModel< ? extends List< ? extends T>> choices, IChoiceRenderer<T> renderer)
	{
		super(id, model, choices, renderer);
		init(true);
	}

	public AjaxRadioChoice(String id, IModel<T> model,
			IModel< ? extends List< ? extends T>> choices, IChoiceRenderer<T> renderer,
			boolean addAjaxHandler)
	{
		super(id, model, choices, renderer);
		init(addAjaxHandler);
	}

	public AjaxRadioChoice(String id, IModel< ? extends List< ? extends T>> choices,
			boolean addAjaxHandler)
	{
		super(id, choices);
		init(addAjaxHandler);
	}

	private void init(boolean addAjaxHandler)
	{
		setOutputMarkupId(true);
		if (addAjaxHandler)
		{
			add(newAjaxFormChoiceComponentUpdatingBehavior(getJavaScriptEvent()));
		}
	}

	/**
	 * Voegt een eventuele keuze van een element dat normaal gesproken niet in de lijst
	 * voorkomt, alsnog toe aan de lijst.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List getChoices()
	{
		List list = super.getChoices();
		Object object = getModelObject();
		if (object != null && !list.contains(object))
		{
			try
			{
				list.add(object);
			}
			catch (UnsupportedOperationException e)
			{
				// ignore...
			}
		}

		return list;
	}

	/**
	 * Geeft de naam terug van het JavaScript event waaraan het ajax update event gehangen
	 * wordt.
	 * 
	 * @return "onchange"
	 */
	protected String getJavaScriptEvent()
	{
		return "onchange";
	}

	/**
	 * Factory method voor het maken van de ajax update handler.
	 * 
	 * @param event
	 *            het event waaraan het ajax gebeuren gekoppeld moet worden.
	 * @return het Ajax event.
	 */
	protected AjaxFormChoiceComponentUpdatingBehavior newAjaxFormChoiceComponentUpdatingBehavior(
			String event)
	{
		return new AjaxFormChoiceComponentUpdatingBehavior()
		{
			/** Voor serializatie. */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				for (Component component : components)
				{
					target.addComponent(component);
				}
				AjaxRadioChoice.this.onUpdate(target, getModelObject());
			}

			/**
			 * @see org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior#onError(org.apache.wicket.ajax.AjaxRequestTarget,
			 *      java.lang.RuntimeException)
			 */
			@Override
			protected void onError(AjaxRequestTarget target, RuntimeException e)
			{
				for (Component component : components)
				{
					target.addComponent(component);
				}
				AjaxRadioChoice.this.onError(target, e);
			}

		};
	}

	/**
	 * @see org.apache.wicket.markup.html.form.DropDownChoice#wantOnSelectionChangedNotifications()
	 */
	@Override
	protected boolean wantOnSelectionChangedNotifications()
	{
		return false;
	}

	/**
	 * Voegt de componenten toe aan de Ajax update als een nieuwe waarde wordt
	 * geselecteerd in deze radiochoice.
	 * 
	 * @param components
	 *            de componenten die toegevoegd moeten worden.
	 */
	@SuppressWarnings("hiding")
	public final void connectListForAjaxRefresh(Component... components)
	{
		for (Component component : components)
		{
			component.setOutputMarkupId(true);
			this.components.add(component);
		}
	}

	@Override
	protected final void onSelectionChanged(Object newSelection)
	{
		onUpdate(null, newSelection);
	}

	@SuppressWarnings("unused")
	protected void onUpdate(AjaxRequestTarget target, Object newSelection)
	{
	}

	@SuppressWarnings("unused")
	protected void onError(AjaxRequestTarget target, RuntimeException e)
	{
	}

	protected List<Component> getComponents()
	{
		return components;
	}

	public boolean isAddFormUpdate()
	{
		return addFormUpdate;
	}

	public void setAddFormUpdate(boolean addFormUpdate)
	{
		this.addFormUpdate = addFormUpdate;
	}

	@Override
	protected boolean getStatelessHint()
	{
		return false;
	}

	@Override
	protected void onDisabled(final ComponentTag tag)
	{
	}
}
