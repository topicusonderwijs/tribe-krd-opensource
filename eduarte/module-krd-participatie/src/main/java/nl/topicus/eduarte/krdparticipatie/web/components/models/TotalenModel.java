package nl.topicus.eduarte.krdparticipatie.web.components.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.eduarte.dao.participatie.helpers.WaarnemingDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.AbsentieReden;
import nl.topicus.eduarte.entities.participatie.Waarneming;
import nl.topicus.eduarte.entities.participatie.enums.WaarnemingSoort;
import nl.topicus.eduarte.krdparticipatie.web.components.panels.waarneming.WaarnemingTotaalOverzicht;
import nl.topicus.eduarte.participatie.zoekfilters.WaarnemingOverzichtZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.WaarnemingZoekFilter;

import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author vanderkamp
 */
public final class TotalenModel extends LoadableDetachableModel<List<WaarnemingTotaalOverzicht>>
{
	private static final long serialVersionUID = 1L;

	private WaarnemingOverzichtZoekFilter filter;

	private List<WaarnemingTotaalOverzicht> res;

	public TotalenModel(WaarnemingOverzichtZoekFilter filter)
	{
		this.filter = filter;
	}

	@Override
	protected List<WaarnemingTotaalOverzicht> load()
	{
		res = new ArrayList<WaarnemingTotaalOverzicht>();
		WaarnemingZoekFilter waarnemingZoekFilter;
		if (filter.getGroep() != null)
		{
			waarnemingZoekFilter = new WaarnemingZoekFilter();
			waarnemingZoekFilter.setGroep(filter.getGroep());
			waarnemingZoekFilter.setBeginDatumTijd(filter.getBeginWeek(filter.getBeginDatum()));
			waarnemingZoekFilter.setEindDatumTijd(filter.getEindWeek(filter.getBeginDatum()));
		}
		else
		{
			waarnemingZoekFilter = new WaarnemingZoekFilter();
			waarnemingZoekFilter.setDeelnemer(filter.getDeelnemer());
			waarnemingZoekFilter.setBeginDatumTijd(filter.getBeginWeek(filter.getBeginDatum()));
			waarnemingZoekFilter.setEindDatumTijd(filter.getEindWeek(filter.getEindDatum()));
		}
		waarnemingZoekFilter.setContract(filter.getContract());
		List<Waarneming> aanwezig = new ArrayList<Waarneming>();
		// List<Waarneming> afwezig = new ArrayList<Waarneming>();
		// List<Waarneming> afwezigZonderMelding = new ArrayList<Waarneming>();
		List<Waarneming> geoorloofd = new ArrayList<Waarneming>();
		List<Waarneming> ongeoorloofd = new ArrayList<Waarneming>();
		Map<AbsentieReden, List<Waarneming>> waarnemingMetReden =
			new HashMap<AbsentieReden, List<Waarneming>>();

		WaarnemingDataAccessHelper helper =
			DataAccessRegistry.getHelper(WaarnemingDataAccessHelper.class);
		List<Waarneming> waarnemingenList =
			helper.getWaarnemingenOverlapEnGelijk(waarnemingZoekFilter);
		for (Waarneming waarneming : waarnemingenList)
		{
			if (waarneming.getWaarnemingSoort().equals(WaarnemingSoort.Aanwezig))
				aanwezig.add(waarneming);
			if (waarneming.getWaarnemingSoort().equals(WaarnemingSoort.Afwezig))
			{
				// afwezig.add(waarneming);
				if (waarneming.getAbsentieMelding() == null)
				{
					// afwezigZonderMelding.add(waarneming);
					ongeoorloofd.add(waarneming);
				}
				else
				{
					if (waarneming.getAbsentieMelding().getAbsentieReden().isGeoorloofd())
						geoorloofd.add(waarneming);
					else
					{
						ongeoorloofd.add(waarneming);
					}
					if (waarnemingMetReden.containsKey(waarneming.getAbsentieMelding()
						.getAbsentieReden()))
						waarnemingMetReden.get(waarneming.getAbsentieMelding().getAbsentieReden())
							.add(waarneming);
					else
					{
						List<Waarneming> list = new ArrayList<Waarneming>();
						list.add(waarneming);
						waarnemingMetReden.put(waarneming.getAbsentieMelding().getAbsentieReden(),
							list);
					}
				}
			}
		}
		res.add(new WaarnemingTotaalOverzicht("Aanwezig (P)", ModelFactory.getListModel(aanwezig),
			"Green"));
		res.add(new WaarnemingTotaalOverzicht("Geoorloofd afwezig (G)", ModelFactory
			.getListModel(geoorloofd), "Orange"));
		res.add(new WaarnemingTotaalOverzicht("Ongeoorloofd afwezig (O)", ModelFactory
			.getListModel(ongeoorloofd), "Red"));
		for (AbsentieReden reden : waarnemingMetReden.keySet())
		{
			res.add(new WaarnemingTotaalOverzicht(reden.getAfkortingOmschrijving(), ModelFactory
				.getListModel(waarnemingMetReden.get(reden)), ""));
		}
		return res;

	}

	@Override
	protected void onDetach()
	{
		if (res != null)
		{
			for (WaarnemingTotaalOverzicht waarnemingTotaalOverzicht : res)
			{
				ComponentUtil.detachQuietly(waarnemingTotaalOverzicht);
			}
		}
	}
}
