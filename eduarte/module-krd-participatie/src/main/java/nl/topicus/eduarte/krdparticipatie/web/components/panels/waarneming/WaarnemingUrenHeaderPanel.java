package nl.topicus.eduarte.krdparticipatie.web.components.panels.waarneming;

import nl.topicus.eduarte.entities.participatie.LesdagIndeling;
import nl.topicus.eduarte.entities.participatie.LesuurIndeling;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier.WaarnemingOverzichtInterface;
import nl.topicus.eduarte.participatie.zoekfilters.WaarnemingOverzichtZoekFilter;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;

/**
 * @author vanderkamp
 */
public class WaarnemingUrenHeaderPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	private WaarnemingOverzichtZoekFilter filter;

	private boolean toevoegen = false;

	public WaarnemingUrenHeaderPanel(String id, final WaarnemingOverzichtInterface page)
	{
		super(id);
		filter = page.getFilterModel().getObject();
		RepeatingView uren = new RepeatingView("uren");
		if (page.getLesweekIndelingModel().getObject() != null)
		{
			for (LesdagIndeling lesdag : (page.getLesweekIndelingModel().getObject())
				.getLesdagIndelingenOrderedByDay())
			{
				int dagVanWeek = lesdag.getDagNummer();
				for (LesuurIndeling lestijd : lesdag.getLesuurIndeling())
				{
					int lesuur = lestijd.getLesuur();
					if (lesuur >= page.getVanLesuur(dagVanWeek)
						&& lesuur <= page.getTotLesuur(dagVanWeek))
					{
						uren.add(new Label("" + dagVanWeek + "-" + lesuur, "" + lesuur));

						Component c = uren.get("" + dagVanWeek + "-" + lesuur);
						if (lesuur == page.getTotLesuur(dagVanWeek))
						{
							c.add(new AttributeAppender("class",
								new Model<String>("tdRightBorder"), " "));
						}
						if (isToegen())
							c
								.add(new AttributeAppender("class", new Model<String>("tdYellow"),
									" "));
					}
				}
			}
		}
		add(uren);
		add(new WebMarkupContainer("totalenContainer")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return filter.isToonTotalenKolommen();
			}
		}.setRenderBodyOnly(true));
	}

	private boolean isToegen()
	{
		toevoegen = !toevoegen;
		return toevoegen;
	}
}
