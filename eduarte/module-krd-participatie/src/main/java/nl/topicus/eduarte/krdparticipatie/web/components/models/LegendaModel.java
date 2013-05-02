package nl.topicus.eduarte.krdparticipatie.web.components.models;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;

import org.apache.wicket.model.LoadableDetachableModel;

public class LegendaModel extends LoadableDetachableModel<List<LegendaItem>>
{
	private static final long serialVersionUID = 1L;

	public LegendaModel()
	{
	}

	@Override
	protected List<LegendaItem> load()
	{
		List<LegendaItem> items = new ArrayList<LegendaItem>();
		if (EduArteApp.get().isModuleActive(EduArteModuleKey.PARTICIPATIE))
		{
			items.add(new LegendaItem("Afspraak", "blue-cell"));
			items
				.add(new LegendaItem("Afspraak zonder aanwezigheidsregistratie", "light_blue-cell"));
			items.add(new LegendaItem("Prive afspraak", "purple-cell"));
		}
		items.add(new LegendaItem("Aanwezig", "green-cell"));
		if (EduArteApp.get().isModuleActive(EduArteModuleKey.PARTICIPATIE))
		{
			items.add(new LegendaItem("Aanwezig zonder afspraak", "light_green_cell"));
		}
		items.add(new LegendaItem("Geoorloofd afwezig", "orange-cell"));
		items.add(new LegendaItem("Ongeoorloofd afwezig", "red-cell"));
		items.add(new LegendaItem("Absentiemelding geoorloofd", "yellow-cell"));
		items.add(new LegendaItem("Absentiemelding ongeoorloofd", "red-cell"));
		return items;
	}
}
