package nl.topicus.eduarte.krdparticipatie.web.components.combobox;

import java.util.List;

import nl.topicus.eduarte.entities.participatie.enums.AfspraakTypeCategory;

import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

public class AfspraakTypeCategorieMultipleCheckbox extends
		CheckBoxMultipleChoice<AfspraakTypeCategory>
{
	private static final long serialVersionUID = 1L;

	public AfspraakTypeCategorieMultipleCheckbox(String id, List<AfspraakTypeCategory> choices)
	{
		super(id, choices, new AfspraakTypeCategorieRenderer());
	}

	@Override
	public String getSuffix()
	{
		return " ";
	}

	private static class AfspraakTypeCategorieRenderer implements
			IChoiceRenderer<AfspraakTypeCategory>
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Object getDisplayValue(AfspraakTypeCategory object)
		{
			return (object).toString();
		}

		@Override
		public String getIdValue(AfspraakTypeCategory object, int index)
		{
			return (object).toString();
		}

	}
}
