package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.dao.helpers.TypeFinancieringDataAccessHelper;
import nl.topicus.eduarte.entities.contract.TypeFinanciering;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitPropertyRenderer;
import nl.topicus.eduarte.zoekfilters.TypeFinancieringZoekFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Combobox voor functies.
 * 
 * @author loite
 */
public class TypeFinancieringCombobox extends AbstractAjaxDropDownChoice<TypeFinanciering>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<TypeFinanciering>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<TypeFinanciering> load()
		{
			return DataAccessRegistry.getHelper(TypeFinancieringDataAccessHelper.class).list(
				new TypeFinancieringZoekFilter());
		}
	}

	public TypeFinancieringCombobox(String id)
	{
		this(id, null);
	}

	public TypeFinancieringCombobox(String id, IModel<TypeFinanciering> model)
	{
		super(id, model, new ListModel(), new EntiteitPropertyRenderer("naam"));
	}
}
