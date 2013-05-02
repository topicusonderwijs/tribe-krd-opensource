package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.eduarte.entities.vrijevelden.VrijVeldKeuzeOptie;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitPropertyRenderer;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

/**
 * Combobox voor VrijVeldKeuzeOptie.
 * 
 * @author hoeve
 */
public class VrijVeldKeuzeOptieCombobox extends DropDownChoice<VrijVeldKeuzeOptie>
{
	private static final long serialVersionUID = 1L;

	public VrijVeldKeuzeOptieCombobox(String id, IModel<VrijVeldKeuzeOptie> model,
			IModel< ? extends List< ? extends VrijVeldKeuzeOptie>> choices)
	{
		super(id, model, choices, new EntiteitPropertyRenderer("naam"));
	}
}
