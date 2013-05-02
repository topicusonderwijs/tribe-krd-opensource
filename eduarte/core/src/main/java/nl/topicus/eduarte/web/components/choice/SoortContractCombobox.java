package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.dao.helpers.SoortContractDataAccessHelper;
import nl.topicus.eduarte.entities.contract.SoortContract;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitPropertyRenderer;
import nl.topicus.eduarte.zoekfilters.SoortContractZoekFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Combobox voor functies.
 * 
 * @author loite
 */
public class SoortContractCombobox extends AbstractAjaxDropDownChoice<SoortContract>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<SoortContract>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<SoortContract> load()
		{
			return DataAccessRegistry.getHelper(SoortContractDataAccessHelper.class).list(
				new SoortContractZoekFilter());
		}
	}

	public SoortContractCombobox(String id)
	{
		this(id, null);
	}

	public SoortContractCombobox(String id, IModel<SoortContract> model)
	{
		super(id, model, new ListModel(), new EntiteitPropertyRenderer("naam"));
	}
}
