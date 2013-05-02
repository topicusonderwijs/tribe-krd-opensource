package nl.topicus.eduarte.web.components.choice;

import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.entities.inschrijving.VrijstellingType;

import org.apache.wicket.model.IObjectClassAwareModel;

public class VrijstellingTypeCombobox extends EnumCombobox<VrijstellingType>
{
	private static final long serialVersionUID = 1L;

	public VrijstellingTypeCombobox(String id, IObjectClassAwareModel<VrijstellingType> model)
	{
		super(id, model, EduArteApp.get().isHogerOnderwijs() ? VrijstellingType.valuesHO()
			: VrijstellingType.valuesBVE());
	}

	public VrijstellingTypeCombobox(String id)
	{
		this(id, null);
	}
}
