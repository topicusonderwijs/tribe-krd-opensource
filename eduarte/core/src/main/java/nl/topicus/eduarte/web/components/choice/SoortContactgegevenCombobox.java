package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.SoortContactgegevenDataAccessHelper;
import nl.topicus.eduarte.entities.adres.SoortContactgegeven;
import nl.topicus.eduarte.web.components.choice.renderer.SoortContactgegevenRenderer;
import nl.topicus.eduarte.zoekfilters.SoortContactgegevenZoekFilter;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Combobox voor locaties.
 * 
 * @author loite
 */
public class SoortContactgegevenCombobox extends DropDownChoice<SoortContactgegeven>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<SoortContactgegeven>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<SoortContactgegeven> load()
		{
			SoortContactgegevenDataAccessHelper soortHelper =
				DataAccessRegistry.getHelper(SoortContactgegevenDataAccessHelper.class);
			SoortContactgegevenZoekFilter filter = new SoortContactgegevenZoekFilter();
			filter.setActief(true);

			return soortHelper.list(filter);
		}
	}

	public SoortContactgegevenCombobox(String id)
	{
		this(id, null);
	}

	public SoortContactgegevenCombobox(String id, IModel<SoortContactgegeven> model)
	{
		super(id, model, new ListModel(), new SoortContactgegevenRenderer());
	}

	public SoortContactgegevenCombobox(String id, IModel<SoortContactgegeven> model,
			IModel< ? extends List< ? extends SoortContactgegeven>> choices)
	{
		super(id, model, choices, new SoortContactgegevenRenderer());
	}
}
