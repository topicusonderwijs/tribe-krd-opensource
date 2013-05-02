package nl.topicus.eduarte.krd.web.components.choice;

import nl.topicus.cobra.web.components.choice.JaNeeCombobox;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;

public class BronMeldingGeblokkeerdComboBox extends JaNeeCombobox
{
	private static final long serialVersionUID = 1L;

	public BronMeldingGeblokkeerdComboBox(String id)
	{
		super(id, null, new BronMeldingGeblokkeerdRenderer(), false);
	}

	public BronMeldingGeblokkeerdComboBox(String id, IModel<Boolean> model)
	{
		super(id, model, new BronMeldingGeblokkeerdRenderer(), false);
	}

	private static final class BronMeldingGeblokkeerdRenderer implements IChoiceRenderer<Boolean>
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Object getDisplayValue(Boolean object)
		{
			Boolean val = object;
			if (val.booleanValue())
				return "Geblokkeerd";
			return "Niet geblokkeerd";
		}

		@Override
		public String getIdValue(Boolean object, int index)
		{
			Boolean val = object;
			return val.toString();
		}
	}

}
