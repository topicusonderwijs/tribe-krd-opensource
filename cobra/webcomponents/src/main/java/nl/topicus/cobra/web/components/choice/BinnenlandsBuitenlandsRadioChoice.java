package nl.topicus.cobra.web.components.choice;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;

/**
 * Toont radio choice buttons voor het selecteren van een binnenlands/buitenlands adres.
 * <p>
 * <h3>Voorbeeld</h3>
 * <p>
 * <label><input type="radio" name="binnenlandsbuitenlands" value="binnenlands"
 * checked="checked"> Binnenlands</label> <label><input type="radio"
 * name="binnenlandsbuitenlands" value="buitenlands"> Buitenlands</label>
 * 
 * @author Martijn Dashorst
 */
public class BinnenlandsBuitenlandsRadioChoice extends JaNeeRadioChoice
{
	private static final long serialVersionUID = 1L;

	public BinnenlandsBuitenlandsRadioChoice(String id)
	{
		this(id, null);
	}

	public BinnenlandsBuitenlandsRadioChoice(String id, IModel<Boolean> model)
	{
		super(id, model, valuesModel, new BinnenlandBuitenlandRenderer());
		setPrefix("");
		setSuffix("&nbsp;");
		setOutputMarkupId(true);
	}

	private static final class BinnenlandBuitenlandRenderer implements IChoiceRenderer<Boolean>
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Object getDisplayValue(Boolean object)
		{
			if (object.booleanValue())
				return "Binnenlands";
			return "Buitenlands";
		}

		@Override
		public String getIdValue(Boolean object, int index)
		{
			return object.toString();
		}

	}
}
