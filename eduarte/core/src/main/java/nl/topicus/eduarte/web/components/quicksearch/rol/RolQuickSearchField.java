package nl.topicus.eduarte.web.components.quicksearch.rol;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.quicksearch.IdObjectRenderer;
import nl.topicus.cobra.web.components.quicksearch.QuickSearchModel;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.eduarte.dao.helpers.RolDataAccessHelper;
import nl.topicus.eduarte.entities.security.authorization.Rol;
import nl.topicus.eduarte.zoekfilters.RolZoekFilter;

import org.apache.wicket.model.IModel;

public class RolQuickSearchField extends QuickSearchField<Rol>
{
	private static final long serialVersionUID = 1L;

	private RolZoekFilter filter;

	public RolQuickSearchField(String id, IModel<Rol> model, RolZoekFilter filter)
	{
		super(id, model, QuickSearchModel.of(RolDataAccessHelper.class, filter),
			new IdObjectRenderer<Rol>());
		this.filter = filter;
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}

	@Override
	public Integer getWidth()
	{
		return 300;
	}
}
