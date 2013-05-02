package nl.topicus.eduarte.web.components.choice.participatie.olc;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.cobra.web.components.choice.render.ToStringRenderer;
import nl.topicus.eduarte.dao.participatie.helpers.OlcLocatieDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.olc.OlcLocatie;
import nl.topicus.eduarte.participatie.zoekfilters.OlcLocatieZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class OlcLocatieCombobox extends AbstractAjaxDropDownChoice<OlcLocatie>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<OlcLocatie>>
	{
		private static final long serialVersionUID = 1L;

		private OlcLocatieZoekFilter filter;

		public ListModel(OlcLocatieZoekFilter filter)
		{
			this.filter = filter;
		}

		@Override
		protected List<OlcLocatie> load()
		{
			return DataAccessRegistry.getHelper(OlcLocatieDataAccessHelper.class).list(filter);
		}

		@Override
		public void detach()
		{
			super.detach();
			filter.detach();
		}
	}

	private OlcLocatieZoekFilter createFilterVoorActief()
	{
		OlcLocatieZoekFilter filter = new OlcLocatieZoekFilter();
		filter.setActief(true);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		return filter;
	}

	public OlcLocatieCombobox(String id, IModel<OlcLocatie> model)
	{
		super(id, model, (IModel<List<OlcLocatie>>) null, new ToStringRenderer(), false);
		setChoices(new ListModel(createFilterVoorActief()));
	}
}
