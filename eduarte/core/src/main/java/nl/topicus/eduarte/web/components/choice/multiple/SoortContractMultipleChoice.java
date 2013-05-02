package nl.topicus.eduarte.web.components.choice.multiple;

import java.util.Collection;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.SoortContractDataAccessHelper;
import nl.topicus.eduarte.entities.contract.SoortContract;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitPropertyRenderer;
import nl.topicus.eduarte.zoekfilters.SoortContractZoekFilter;

import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Combobox voor soorten contract.
 * 
 * @author loite
 */
public class SoortContractMultipleChoice extends ListMultipleChoice<SoortContract>
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

	public SoortContractMultipleChoice(String id)
	{
		this(id, null);
	}

	public SoortContractMultipleChoice(String id, IModel<Collection<SoortContract>> model)
	{
		super(id, model, new ListModel(), new EntiteitPropertyRenderer("naam"));
	}
}
