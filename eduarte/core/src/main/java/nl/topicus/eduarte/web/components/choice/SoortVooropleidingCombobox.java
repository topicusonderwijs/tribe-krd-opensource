package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.dao.helpers.SoortVooropleidingDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitPropertyRenderer;
import nl.topicus.eduarte.zoekfilters.SoortVooropleidingZoekFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author hop
 */
public class SoortVooropleidingCombobox extends AbstractAjaxDropDownChoice<SoortVooropleiding>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<SoortVooropleiding>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<SoortVooropleiding> load()
		{
			return DataAccessRegistry.getHelper(SoortVooropleidingDataAccessHelper.class).list(
				new SoortVooropleidingZoekFilter());
		}
	}

	public SoortVooropleidingCombobox(String id)
	{
		this(id, null);
	}

	public SoortVooropleidingCombobox(String id, IModel<SoortVooropleiding> model)
	{
		super(id, model, new ListModel(), new EntiteitPropertyRenderer("naam"));
	}
}
