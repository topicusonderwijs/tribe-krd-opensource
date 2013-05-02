package nl.topicus.eduarte.web.components.text.autocomplete;

import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.wiquery.auto.AssistingTextField;
import nl.topicus.eduarte.dao.helpers.RolDataAccessHelper;

import org.apache.wicket.model.IModel;

/**
 * Auto complete text veld voor categorieen van rollen. De input zit altijd tussen de
 * resultaten (indien niet leeg, of includeInput is false).
 * 
 * @author papegaaij
 */
public class RolCategorieAutoCompleteTextField extends AssistingTextField
{
	private static final long serialVersionUID = 1L;

	private boolean includeInput;

	public RolCategorieAutoCompleteTextField(String id, boolean includeInput)
	{
		this(id, null, includeInput);
	}

	public RolCategorieAutoCompleteTextField(String id, IModel<String> model, boolean includeInput)
	{
		super(id, model);
		this.includeInput = includeInput;
	}

	@Override
	public List<String> getChoices(String query)
	{
		if (StringUtil.isEmpty(query))
			return Collections.emptyList();
		List<String> ret =
			DataAccessRegistry.getHelper(RolDataAccessHelper.class).getCategorieen(query);
		if (includeInput && !ret.contains(query))
			ret.add(query);
		return ret;
	}

}
