package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.dao.helpers.MeeteenheidDataAccessHelper;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Meeteenheid;
import nl.topicus.eduarte.web.components.choice.renderer.MeeteenheidRenderer;
import nl.topicus.eduarte.zoekfilters.MeeteenheidZoekFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author vanharen
 */
public class MeeteenheidCombobox extends AbstractAjaxDropDownChoice<Meeteenheid>
{
	private static final long serialVersionUID = 1L;

	public MeeteenheidCombobox(String id)
	{
		this(id, null);
	}

	public MeeteenheidCombobox(String id, IModel<Meeteenheid> model)
	{
		super(id, model, new MeeteenheidListModel(), new MeeteenheidRenderer());
		setNullValid(true);
	}

	private static final class MeeteenheidListModel extends
			LoadableDetachableModel<List<Meeteenheid>>
	{
		private static final long serialVersionUID = 1L;

		private MeeteenheidZoekFilter filter;

		@Override
		protected List<Meeteenheid> load()
		{
			filter = new MeeteenheidZoekFilter();
			filter.addOrderByProperty("naam");
			return DataAccessRegistry.getHelper(MeeteenheidDataAccessHelper.class).list(filter);
		}

		@Override
		public void detach()
		{
			super.detach();
			if (filter != null)
				filter.detach();
		}
	}
}
