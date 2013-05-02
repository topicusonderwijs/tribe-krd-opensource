package nl.topicus.eduarte.web.components.choice;

import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.eduarte.entities.taxonomie.MBONiveau;

import org.apache.wicket.model.IModel;

public class MBONiveauCombobox extends EnumCombobox<MBONiveau>
{
	private static final long serialVersionUID = 1L;

	public MBONiveauCombobox(String id, IModel<MBONiveau> model)
	{
		super(id, model, MBONiveau.values());
	}

}
