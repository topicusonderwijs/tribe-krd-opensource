package nl.topicus.cobra.web.components.choice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import nl.topicus.cobra.web.components.choice.render.EnumRenderer;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IObjectClassAwareModel;
import org.apache.wicket.util.string.PrependingStringBuffer;

/**
 * @author schimmel
 */
public class EnumCheckboxGroup<E extends Enum<E>> extends CheckBoxMultipleChoice<E>
{
	private static final long serialVersionUID = 1L;

	public EnumCheckboxGroup(String id, IObjectClassAwareModel<Collection<E>> model,
			Class<E> enumClass)
	{
		super(id, model, new ArrayList<E>(EnumSet.allOf(enumClass)), new EnumRenderer<E>(null));
	}

	public EnumCheckboxGroup(String id, IObjectClassAwareModel<Collection<E>> model,
			Collection<E> choices)
	{
		super(id, model, new ArrayList<E>(choices), new EnumRenderer<E>(null));
	}

	public EnumCheckboxGroup(String id, IObjectClassAwareModel<Collection<E>> model,
			IModel<List<E>> choices)
	{
		super(id, model, choices, new EnumRenderer<E>(null));
	}

	public EnumCheckboxGroup(String id, IObjectClassAwareModel<Collection<E>> model, E... choices)
	{
		this(id, model, Arrays.asList(choices));
	}

	@Override
	public String getInputName()
	{
		String id = getId();
		final PrependingStringBuffer inputName = new PrependingStringBuffer(id.length());
		Component c = this;
		while (true)
		{
			inputName.prepend(id);
			c = c.getParent();
			if (c == null || (c instanceof Form< ? > && ((Form< ? >) c).isRootForm())
				|| c instanceof Page)
			{
				break;
			}
			inputName.prepend("_");
			id = c.getId();
		}
		if (inputName.equals("submit"))
		{
			inputName.prepend("_");
		}
		return inputName.toString();

	}

}
