package nl.topicus.cobra.web.components.wiquery.auto;

import java.util.List;

import nl.topicus.cobra.util.StringUtil;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.ValidationError;
import org.odlabs.wiquery.core.commons.IWiQueryPlugin;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.options.Options;
import org.odlabs.wiquery.ui.autocomplete.AutocompleteJavascriptResourceReference;
import org.odlabs.wiquery.ui.commons.WiQueryUIPlugin;
import org.odlabs.wiquery.ui.position.PositionJavascriptResourceReference;
import org.odlabs.wiquery.ui.widget.WidgetJavascriptResourceReference;

@WiQueryUIPlugin
public class QuickSearchField<T> extends FormComponent<T> implements IWiQueryPlugin,
		IAutoCompleteBase<T>
{
	private static final long serialVersionUID = 1L;

	private JQueryAutoCompleteBehavior<T> behavior;

	private AddHiddenFieldBehavior hiddenField;

	private IModel<List<T>> choices;

	private IAutoCompleteChoiceRenderer< ? super T> renderer;

	private Options options = new Options();

	private String hiddenValueOverride;

	public QuickSearchField(String id, IModel<T> model, List<T> choices,
			IAutoCompleteChoiceRenderer< ? super T> renderer)
	{
		this(id, model, new ListModel<T>(choices), renderer);
	}

	public QuickSearchField(String id, IModel<T> model, IModel<List<T>> choices,
			IAutoCompleteChoiceRenderer< ? super T> renderer)
	{
		super(id, model);
		this.renderer = renderer;
		this.choices = choices;

		setMaxResults(30);
		add(behavior = new JQueryAutoCompleteBehavior<T>(this));
		add(hiddenField = new AddHiddenFieldBehavior());
		if (getWidth() != null)
			getOptions().putLiteral("width", getWidth() + "px");
	}

	/**
	 * 
	 * @return De breedte van het uitklappanel in pixels. Geeft standaard null terug, wat
	 *         betekent dat de breedte gelijk is aan het veld zelf.
	 */
	public Integer getWidth()
	{
		return null;
	}

	public Options getOptions()
	{
		return options;
	}

	public int getMaxResults()
	{
		return options.getInt("max");
	}

	public void setMaxResults(int maxResults)
	{
		options.put("max", maxResults);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getChoices(String query)
	{
		List<T> ret;
		if (choices instanceof IFilteredModel)
			ret = (List<T>) ((IFilteredModel) choices).getObject(query, getMaxResults());
		else
			ret = choices.getObject();
		if (ret == null)
		{
			throw new NullPointerException(
				"List of choices is null - Was the supplied 'Choices' model empty?");
		}
		return ret;
	}

	@Override
	public boolean isInputNullable()
	{
		return false;
	}

	@Override
	public boolean checkRequired()
	{
		if (isRequired())
		{
			convertInput();
			return getConvertedInput() != null;
		}
		return true;
	}

	@Override
	protected void convertInput()
	{
		convertInput(getRawInput(), hiddenValueOverride == null ? getRequest().getParameter(
			hiddenField.getPreviousFieldName()) : hiddenValueOverride);
	}

	@SuppressWarnings("unchecked")
	private void convertInput(String rawInput, String hiddenInput)
	{
		try
		{
			if (StringUtil.isEmpty(rawInput))
			{
				setConvertedInput(null);
			}
			else if (StringUtil.isEmpty(hiddenInput))
			{
				if (!convertInputFromRawInput(rawInput))
					reportNoSelectionError();
			}
			else
			{
				T obj = (T) renderer.getObject(hiddenInput);
				if (!getRawInput().equals(renderer.getFieldValue(obj)))
				{
					if (!convertInputFromRawInput(rawInput))
						reportInvalidError();
				}
				else
					setConvertedInput((T) renderer.getObject(hiddenInput));
			}
		}
		catch (IllegalArgumentException e)
		{
			reportInvalidError();
		}
	}

	public boolean convertInputFromRawInput(String rawInput)
	{
		List<T> checkChoices = getChoices(rawInput);
		if (checkChoices.size() == 1)
		{
			setConvertedInput(checkChoices.get(0));
			return true;
		}
		for (T curChoice : checkChoices)
		{
			if (rawInput.equals(renderer.getFieldValue(curChoice)))
			{
				setConvertedInput(curChoice);
				return true;
			}
		}
		T converted = convertInvalidInput(rawInput, checkChoices);
		if (converted != null)
		{
			setConvertedInput(converted);
			return true;
		}
		return false;
	}

	/**
	 * Overschrijf deze methode in een subclass om input te matchen volgens extra criteria
	 * (zoals code - naam matchen op code of naam)
	 * 
	 * @param input
	 * @param possibleResults
	 * @return null als er geen match is
	 */
	protected T convertInvalidInput(String input, List<T> possibleResults)
	{
		return null;
	}

	private void reportNoSelectionError()
	{
		ValidationError error = new ValidationError();
		error.addMessageKey("noselect");
		error((IValidationError) error);
	}

	private void reportInvalidError()
	{
		ValidationError error = new ValidationError();
		error.addMessageKey("invalid");
		error.setVariable("value", getRawInput());
		error((IValidationError) error);
	}

	@Override
	public IAutoCompleteChoiceRenderer< ? super T> getRenderer()
	{
		return renderer;
	}

	@SuppressWarnings("unchecked")
	private FormComponentPanel<T> getFormComponentPanelParent()
	{
		if (getParent() instanceof FormComponentPanel< ? >)
			return (FormComponentPanel<T>) getParent();
		return null;
	}

	@Override
	public IModel<String> getLabel()
	{
		FormComponentPanel<T> parent = getFormComponentPanelParent();
		return parent == null ? super.getLabel() : parent.getLabel();
	}

	@Override
	public boolean isRequired()
	{
		FormComponentPanel<T> parent = getFormComponentPanelParent();
		return parent == null ? super.isRequired() : parent.isRequired();
	}

	@Override
	public boolean isEnabled()
	{
		FormComponentPanel<T> parent = getFormComponentPanelParent();
		return parent == null ? super.isEnabled() : parent.isEnabled();
	}

	@Override
	protected void onComponentTag(ComponentTag tag)
	{
		checkComponentTag(tag, "input");
		if (tag.getAttributes().containsKey("type"))
			checkComponentTagAttribute(tag, "type", "text");
		String value = StringUtil.isEmpty(getRawInput()) ? getValue() : getRawInput();
		if (StringUtil.isNotEmpty(value))
			tag.put("value", value);
		super.onComponentTag(tag);
	}

	@Override
	protected String getModelValue()
	{
		T value = getModelObject();
		if (value != null)
			return getRenderer().getFieldValue(value);
		else
			return null;
	}

	@Override
	public void contribute(WiQueryResourceManager resourceManager)
	{
		resourceManager.addJavaScriptResource(WidgetJavascriptResourceReference.get());
		resourceManager.addJavaScriptResource(PositionJavascriptResourceReference.get());
		resourceManager.addJavaScriptResource(AutocompleteJavascriptResourceReference.get());
		resourceManager.addJavaScriptResource(QuickSearchField.class, "quicksearch.js");
	}

	@Override
	public JsStatement statement()
	{
		options.putLiteral("hiddenField", hiddenField.getValueFieldName());
		options.putLiteral("source", behavior.getCallbackUrl().toString());
		return new JsQuery(this).$().chain("quickSearch", options.getJavaScriptOptions());
	}

	public void setHiddenValueOverride(String hiddenVal)
	{
		this.hiddenValueOverride = hiddenVal;
	}

}
