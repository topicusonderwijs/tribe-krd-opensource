package nl.topicus.eduarte.krd.web.components.panels.datapanel.selectie;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.zoekfilters.DetachableZoekFilter;
import nl.topicus.eduarte.krd.dao.helpers.BronDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.IBronSignaal;
import nl.topicus.eduarte.krd.zoekfilters.BronSignaalZoekFilter;

import org.apache.wicket.model.LoadableDetachableModel;

public class SignalenListModel extends LoadableDetachableModel<List<IBronSignaal>>
{
	private static final long serialVersionUID = 1L;

	private DetachableZoekFilter<IBronSignaal> filter;

	public SignalenListModel(DetachableZoekFilter<IBronSignaal> filter)
	{
		this.filter = filter;
	}

	public static List<IBronSignaal> getSignalen(DetachableZoekFilter<IBronSignaal> filter)
	{
		return DataAccessRegistry.getHelper(BronDataAccessHelper.class).getSignalen(
			(BronSignaalZoekFilter) filter);
	}

	@Override
	protected List<IBronSignaal> load()
	{
		return getSignalen(filter);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}
}
