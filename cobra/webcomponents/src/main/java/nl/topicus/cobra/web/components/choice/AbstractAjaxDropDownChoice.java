package nl.topicus.cobra.web.components.choice;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.web.components.wiquery.resources.ResourceRefUtil;

import org.apache.wicket.Component;
import org.apache.wicket.RequestContext;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.AbstractSingleSelectChoice;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.IOnChangeListener;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.portlet.PortletRequestContext;
import org.apache.wicket.security.swarm.models.SwarmModel;
import org.odlabs.wiquery.core.commons.CoreJavaScriptHeaderContributor;
import org.odlabs.wiquery.core.commons.IWiQueryPlugin;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.options.Options;
import org.odlabs.wiquery.ui.commons.WiQueryUIPlugin;

/**
 * DropDownChoice component die andere componenten update als zijn waarde verandert. Is
 * handig voor het kunnen wijzigen van componenten die afhankelijk zijn van de hier
 * geselecteerde waarde. Vervanger voor de voormalige Wicktor componenten.
 * 
 * @author Martijn Dashorst
 */
@WiQueryUIPlugin
public abstract class AbstractAjaxDropDownChoice<T> extends DropDownChoice<T> implements
		IWiQueryPlugin
{
	private static final long serialVersionUID = 1L;

	/** De lijst van componenten die aangepast moeten worden als dit component wijzigt. */
	private List<Component> components = new ArrayList<Component>();

	private boolean addFormUpdate = true;

	private boolean addAjax;

	/**
	 * Selecteert de eerste optie in de lijst indien de lijst uit max 1 element bestaat.
	 */
	private boolean autoSelectOnlyOption = true;

	private boolean forceAutoSelectOnlyOption = false;

	private boolean reselectOnlyOptionOnNextRender = true;

	private boolean submitOnEnter = true;

	private boolean autoResizeDropdown = false;

	private boolean addSelectedItemToChoicesWhenNotInList = true;

	public AbstractAjaxDropDownChoice(final String id)
	{
		super(id);
		init(true);
	}

	public AbstractAjaxDropDownChoice(final String id, final List< ? extends T> choices)
	{
		super(id, choices);
		init(true);
	}

	public AbstractAjaxDropDownChoice(final String id, final List< ? extends T> data,
			final IChoiceRenderer< ? super T> renderer)
	{
		super(id, data, renderer);
		init(true);
	}

	public AbstractAjaxDropDownChoice(final String id, final List< ? extends T> data,
			final IChoiceRenderer< ? super T> renderer, boolean addAjaxHandler)
	{
		super(id, data, renderer);
		init(addAjaxHandler);
	}

	public AbstractAjaxDropDownChoice(final String id, final List< ? extends T> data,
			final IChoiceRenderer< ? super T> renderer, boolean addAjaxHandler,
			boolean submitOnEnter)
	{
		super(id, data, renderer);
		init(addAjaxHandler);
		this.submitOnEnter = submitOnEnter;
	}

	public AbstractAjaxDropDownChoice(final String id, IModel<T> model,
			final List< ? extends T> choices)
	{
		super(id, model, choices);
		init(true);
	}

	public AbstractAjaxDropDownChoice(final String id, IModel<T> model,
			final List< ? extends T> data, final IChoiceRenderer< ? super T> renderer)
	{
		super(id, model, data, renderer);
		init(true);
	}

	public AbstractAjaxDropDownChoice(final String id, IModel<T> model,
			final List< ? extends T> data, final IChoiceRenderer< ? super T> renderer,
			boolean addAjaxHandler)
	{
		super(id, model, data, renderer);
		init(addAjaxHandler);
	}

	public AbstractAjaxDropDownChoice(String id, IModel< ? extends List< ? extends T>> choices)
	{
		super(id, choices);
		init(true);
	}

	public AbstractAjaxDropDownChoice(String id, IModel<T> model,
			IModel< ? extends List< ? extends T>> choices)
	{
		super(id, model, choices);
		init(true);
	}

	public AbstractAjaxDropDownChoice(String id, IModel< ? extends List< ? extends T>> choices,
			IChoiceRenderer< ? super T> renderer)
	{
		super(id, choices, renderer);
		init(true);
	}

	public AbstractAjaxDropDownChoice(String id, IModel< ? extends List< ? extends T>> choices,
			IChoiceRenderer< ? super T> renderer, boolean addAjaxHandler)
	{
		super(id, choices, renderer);
		init(addAjaxHandler);
	}

	public AbstractAjaxDropDownChoice(String id, IModel<T> model,
			IModel< ? extends List< ? extends T>> choices, IChoiceRenderer< ? super T> renderer)
	{
		super(id, model, choices, renderer);
		init(true);
	}

	public AbstractAjaxDropDownChoice(String id, IModel<T> model,
			IModel< ? extends List< ? extends T>> choices, IChoiceRenderer< ? super T> renderer,
			boolean addAjaxHandler)
	{
		super(id, model, choices, renderer);
		init(addAjaxHandler);
	}

	public AbstractAjaxDropDownChoice(String id, IModel< ? extends List< ? extends T>> choices,
			boolean addAjaxHandler)
	{
		super(id, choices);
		init(addAjaxHandler);
	}

	private void initSecureChoicesModel()
	{
		try
		{
			Field choicesField =
				AbstractSingleSelectChoice.class.getSuperclass().getDeclaredField("choices");
			choicesField.setAccessible(true);
			Object choicesModel = choicesField.get(this);
			if (choicesModel instanceof SwarmModel< ? >)
				((SwarmModel< ? >) choicesModel).getSecurityId(this);
		}
		catch (SecurityException e)
		{
			throw new RuntimeException(e);
		}
		catch (NoSuchFieldException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalArgumentException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void setAddAjax(boolean addAjax)
	{
		this.addAjax = addAjax;
	}

	private void init(boolean addAjaxHandler)
	{
		addAjax = addAjaxHandler;
		setOutputMarkupId(true);
		add(new HeaderContributor(new CoreJavaScriptHeaderContributor()));
	}

	public AbstractAjaxDropDownChoice<T> setAddSelectedItemToChoicesWhenNotInList(
			boolean addSelectedItemToChoicesWhenNotInList)
	{
		this.addSelectedItemToChoicesWhenNotInList = addSelectedItemToChoicesWhenNotInList;
		return this;
	}

	/**
	 * Voegt een eventuele keuze van een element dat normaal gesproken niet in de lijst
	 * voorkomt, alsnog toe aan de lijst.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> getChoices()
	{
		initSecureChoicesModel();
		List<T> list = (List<T>) super.getChoices();
		if (addSelectedItemToChoicesWhenNotInList)
		{
			T object = getModelObject();
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
		}
		return list;

	}

	protected AjaxEventBehavior newAjaxFormComponentUpdatingBehavior()
	{
		return new AjaxDropDownUpdatingBehaviour()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				for (Component component : components)
				{
					target.addComponent(component);
				}
				AbstractAjaxDropDownChoice.this.onUpdate(target, getModelObject());
			}

			@Override
			protected void onError(AjaxRequestTarget target, RuntimeException e)
			{
				super.onError(target, e);
				for (Component component : components)
				{
					target.addComponent(component);
				}
				AbstractAjaxDropDownChoice.this.onError(target, e);
			}
		};
	}

	@Override
	protected boolean wantOnSelectionChangedNotifications()
	{
		return false;
	}

	/**
	 * Voegt de componenten toe aan de Ajax update als een nieuwe waarde wordt
	 * geselecteerd in deze combobox.
	 * 
	 * @param _components
	 *            de componenten die toegevoegd moeten worden.
	 */
	public final AbstractAjaxDropDownChoice<T> connectListForAjaxRefresh(Component... _components)
	{
		for (Component component : _components)
		{
			component.setOutputMarkupId(true);
			this.components.add(component);
		}
		return this;
	}

	@Override
	protected final void onSelectionChanged(T newSelection)
	{
		onUpdate(null, newSelection);
	}

	@SuppressWarnings("unused")
	protected void onUpdate(AjaxRequestTarget target, T newSelection)
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

	/**
	 * Voegt Javascript toe om op de Enter toets te submitten
	 * 
	 * @see org.apache.wicket.markup.html.form.DropDownChoice#onComponentTag(org.apache.wicket.markup.ComponentTag)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void onComponentTag(ComponentTag tag)
	{
		checkComponentTag(tag, "select");

		// url that points to this components IOnChangeListener method
		CharSequence url = urlFor(IOnChangeListener.INTERFACE);

		if (addFormUpdate)
		{
			Form< ? > form = findParent(Form.class);
			if (form != null)
			{
				// hack to bypass delegation to root form
				Property<Form, ? , ? > defaultSubmitProp =
					ReflectionUtil.findProperty(Form.class, "defaultSubmittingComponent");
				Component defaultButton = (Component) defaultSubmitProp.getValue(form);
				if (defaultButton == null)
					defaultButton = (Component) form.getDefaultButton();
				if (defaultButton != null)
				{
					defaultButton.setOutputMarkupId(true);
					if (submitOnEnter)
					{
						tag.put("onkeypress",
							"if ((window.event &amp;&amp; window.event.keyCode==13)||event.which==13){jQuery('#"
								+ defaultButton.getMarkupId() + "').click();}");
					}
				}
				else
				{
					RequestContext rc = RequestContext.get();
					if (rc.isPortletRequest())
					{
						// restore url back to real wicket path as its going to be
						// interpreted
						// by the form itself
						url = ((PortletRequestContext) rc).getLastEncodedPath();
					}
					if (submitOnEnter)
					{
						tag.put("onkeypress",
							"if ((window.event &amp;&amp; window.event.keyCode==13)||event.which==13){"
								+ form.getJsForInterfaceUrl(url) + "}");
					}
				}
			}
			else
			{
				if (submitOnEnter)
				{
					tag.put("onkeypress",
						"if ((window.event &amp;&amp; window.event.keyCode==13)||event.which==13){window.location.href='"
							+ url + (url.toString().indexOf('?') > -1 ? "&amp;" : "?")
							+ getInputName() + "=' + this.options[this.selectedIndex].value;}");
				}
			}
		}

		super.onComponentTag(tag);
	}

	public boolean isAddFormUpdate()
	{
		return addFormUpdate;
	}

	public AbstractAjaxDropDownChoice<T> setAddFormUpdate(boolean addFormUpdate)
	{
		this.addFormUpdate = addFormUpdate;
		return this;
	}

	@Override
	protected boolean getStatelessHint()
	{
		return false;
	}

	@Override
	protected void onBeforeRender()
	{
		if (addAjax)
		{
			add(newAjaxFormComponentUpdatingBehavior());
		}
		super.onBeforeRender();
	}

	private boolean mustPerformAutoSelect()
	{
		return (isAutoSelectOnlyOption() && isRequired())
			|| (isForceAutoSelectOnlyOption() && reselectOnlyOptionOnNextRender);
	}

	public void reselectOnlyOption()
	{
		reselectOnlyOptionOnNextRender = true;
	}

	public boolean isAutoSelectOnlyOption()
	{
		return autoSelectOnlyOption;
	}

	public AbstractAjaxDropDownChoice<T> setAutoSelectOnlyOption(boolean autoSelectOnlyOption)
	{
		this.autoSelectOnlyOption = autoSelectOnlyOption;
		return this;
	}

	public boolean isForceAutoSelectOnlyOption()
	{
		return forceAutoSelectOnlyOption;
	}

	public AbstractAjaxDropDownChoice<T> setForceAutoSelectOnlyOption(
			boolean forceAutoSelectOnlyOption)
	{
		this.forceAutoSelectOnlyOption = forceAutoSelectOnlyOption;
		return this;
	}

	/**
	 * @return true wanneer men heeft aangegeven deze &lt;select&gt; component om te laten
	 *         toveren naar een &lt;div&gt; met een &lt;ul&gt; hierin.
	 */
	public boolean isAutoResizeDropdown()
	{
		return autoResizeDropdown;
	}

	/**
	 * Gebruik deze setter om aan te geven dat deze &lt;select&gt; component om te laten
	 * toveren naar een &lt;div&gt; met een &lt;ul&gt; hierin. Dit om te voorkomen dat in
	 * IE de pulldown te smal is en men de opties niet kunnen lezen.
	 */
	public AbstractAjaxDropDownChoice<T> setAutoResizeDropdown(boolean autoResizeDropdown)
	{
		this.autoResizeDropdown = autoResizeDropdown;
		return this;
	}

	@Override
	public void contribute(WiQueryResourceManager resourceManager)
	{
		if (isAutoResizeDropdown())
			ResourceRefUtil.addYAIEF(resourceManager);
		resourceManager
			.addJavaScriptResource(AbstractAjaxDropDownChoice.class, "dropdownchoice.js");
	}

	@Override
	public JsStatement statement()
	{
		Options options = new Options();
		options.put("useYaief", isAutoResizeDropdown());
		options.put("autoselect", mustPerformAutoSelect());
		reselectOnlyOptionOnNextRender = false;

		return new JsQuery(this).$().chain("dropDownChoice", options.getJavaScriptOptions());
	}
}
