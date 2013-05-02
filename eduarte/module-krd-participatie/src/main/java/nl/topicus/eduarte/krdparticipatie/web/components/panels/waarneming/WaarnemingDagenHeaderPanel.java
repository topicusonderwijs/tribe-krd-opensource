package nl.topicus.eduarte.krdparticipatie.web.components.panels.waarneming;

import java.util.Date;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.listview.IdObjectListView;
import nl.topicus.eduarte.entities.participatie.LesdagIndeling;
import nl.topicus.eduarte.entities.participatie.LesweekIndeling;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier.WaarnemingOverzichtInterface;
import nl.topicus.eduarte.participatie.zoekfilters.WaarnemingOverzichtZoekFilter;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

/**
 * @author vanderkamp
 */
public class WaarnemingDagenHeaderPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	public WaarnemingDagenHeaderPanel(String id, final WaarnemingOverzichtInterface page,
			final boolean toonDatum)
	{
		super(id);
		setDefaultModel(new CompoundPropertyModel<LesweekIndeling>(page.getLesweekIndelingModel()));
		IdObjectListView<LesdagIndeling> dagen =
			new IdObjectListView<LesdagIndeling>("lesdagIndelingenOrderedByDay")
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem<LesdagIndeling> item)
				{
					LesdagIndeling lesdagIndeling = item.getModelObject();
					int dagVanWeek = lesdagIndeling.getDagNummer();
					String weekDag = TimeUtil.getInstance().getWeekdagNaam(dagVanWeek);
					if (toonDatum)
					{
						WaarnemingOverzichtZoekFilter filter = page.getFilterModel().getObject();
						int week = TimeUtil.getInstance().getWeekOfYear(filter.getBeginDatum());
						int jaar = TimeUtil.getInstance().getYear(filter.getBeginDatum());
						Date datum = TimeUtil.getInstance().getDate(jaar, week, dagVanWeek);
						weekDag += " " + TimeUtil.getInstance().formatDate(datum);
					}

					item.add(new Label("weekDag", new Model<String>(weekDag)));
					int colspan = page.getTotLesuur(dagVanWeek) - page.getVanLesuur(dagVanWeek) + 1;
					item.add(new SimpleAttributeModifier("colspan", "" + colspan));
				}
			};
		add(dagen);
	}
}
