package nl.topicus.eduarte.web.components.choice;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.model.IModel;

public class PogingCombobox extends AbstractAjaxDropDownChoice<Integer>
{
	private static final long serialVersionUID = 1L;

	public PogingCombobox(String id, IModel<Integer> model, int aantalHerkansingen)
	{
		super(id, model, createList(aantalHerkansingen), new ChoiceRenderer<Integer>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Object getDisplayValue(Integer object)
			{
				Integer poging = object;
				if (poging == 0)
					return "Toets";
				return "Herkansing " + poging;
			}
		});
	}

	private static List<Integer> createList(int aantalHerkansingen)
	{
		List<Integer> ret = new ArrayList<Integer>();
		for (int count = 0; count <= aantalHerkansingen; count++)
		{
			ret.add(count);
		}
		return ret;
	}
}
