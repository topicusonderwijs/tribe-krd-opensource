package nl.topicus.cobra.web.components.choice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IObjectClassAwareModel;

/**
 * @author vandekamp
 * @param <T>
 */
public class EnumRadioChoice<T extends Enum<T>> extends AjaxRadioChoice<T>
{
	private static final long serialVersionUID = 1L;

	private static final class EnumRenderer<TE extends Enum<TE>> implements IChoiceRenderer<TE>
	{
		private static final long serialVersionUID = 1L;

		private final Map<TE, String> names;

		public EnumRenderer(Map<TE, String> names)
		{
			this.names = names;
		}

		public String getDisplayValue(TE object)
		{
			if (names != null)
			{
				String name = names.get(object);
				if (name != null)
					return name;
			}
			return object.toString();
		}

		public String getIdValue(TE object, int index)
		{
			return Integer.toString(object.ordinal());
		}
	}

	public EnumRadioChoice(String id, IObjectClassAwareModel<T> model)
	{
		super(id, model, findChoices(model), new EnumRenderer<T>(null), false);
	}

	private static <T extends Enum<T>> List<T> findChoices(IObjectClassAwareModel<T> model)
	{
		Class<T> clazz = model.getObjectClass();
		if (clazz == null)
			throw new IllegalArgumentException("Model cannot determine object class");

		return new ArrayList<T>(EnumSet.allOf(clazz));
	}

	public EnumRadioChoice(String id, T... choices)
	{
		super(id, Arrays.asList(choices), new EnumRenderer<T>(null), false);
	}

	public EnumRadioChoice(String id, boolean addAjaxHandler, T... choices)
	{
		super(id, Arrays.asList(choices), new EnumRenderer<T>(null), addAjaxHandler);
	}

	public EnumRadioChoice(String id, IModel<T> model, T... choices)
	{
		super(id, model, Arrays.asList(choices), new EnumRenderer<T>(null), false);
	}

	public EnumRadioChoice(String id, IModel<T> model, IModel<List<T>> choices)
	{
		super(id, model, choices, new EnumRenderer<T>(null), false);
	}

	public EnumRadioChoice(String id, IModel<T> model, List<T> choices)
	{
		super(id, model, choices, new EnumRenderer<T>(null), false);
	}

	public EnumRadioChoice(String id, IModel<T> model, boolean addAjaxHandler, T... choices)
	{
		super(id, model, Arrays.asList(choices), new EnumRenderer<T>(null), addAjaxHandler);
	}

	public EnumRadioChoice(String id, IModel<T> model, Map<T, String> names, T... choices)
	{
		super(id, model, Arrays.asList(choices), new EnumRenderer<T>(names), false);
	}
}
