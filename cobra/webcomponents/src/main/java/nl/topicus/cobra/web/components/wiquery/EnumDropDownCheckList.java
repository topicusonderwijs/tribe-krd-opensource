package nl.topicus.cobra.web.components.wiquery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.odlabs.wiquery.ui.commons.WiQueryUIPlugin;

/**
 * @author niesink
 */
@WiQueryUIPlugin
public class EnumDropDownCheckList<T extends Enum<T>> extends DropDownCheckList<T>
{
	public EnumDropDownCheckList(String id, IModel<Collection<T>> model, Class<T> enumClass)
	{
		super(id, model, findChoices(enumClass));
	}

	private static <T extends Enum<T>> List<T> findChoices(Class<T> enumClass)
	{
		return new ArrayList<T>(EnumSet.allOf(enumClass));
	}

	private static final long serialVersionUID = 1L;

}
