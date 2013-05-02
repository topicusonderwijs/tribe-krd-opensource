package nl.topicus.eduarte.web.components.choice;

import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.MeeteenheidKoppelType;

import org.apache.wicket.model.IModel;

/**
 * 
 * 
 * @author vanharen
 */
public class MeeteenheidKoppelTypeCombobox extends EnumCombobox<MeeteenheidKoppelType>
{
	private static final long serialVersionUID = 1L;

	public MeeteenheidKoppelTypeCombobox(String id, IModel<MeeteenheidKoppelType> model)
	{
		this(id, model, MeeteenheidKoppelType.values());
	}

	public MeeteenheidKoppelTypeCombobox(String id, IModel<MeeteenheidKoppelType> model,
			MeeteenheidKoppelType[] meeteenheidKoppelTypes)
	{
		super(id, model, true, meeteenheidKoppelTypes);
	}
}
