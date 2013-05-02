package nl.topicus.eduarte.krdparticipatie.web.pages.groep;

import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.eduarte.participatie.zoekfilters.DeelnemerActiviteitTotalenZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.TotalenPerGroepZoekFilter;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

/**
 * @author vandekamp
 */
public class TotalenPerGroepZoekFilterPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	public TotalenPerGroepZoekFilterPanel(String id, TotalenPerGroepZoekFilter filter)
	{
		super(id);
		Form<DeelnemerActiviteitTotalenZoekFilter> form =
			new Form<DeelnemerActiviteitTotalenZoekFilter>("form",
				new CompoundPropertyModel<DeelnemerActiviteitTotalenZoekFilter>(filter))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onSubmit()
				{
					// niks te doen
				}
			};
		add(form);
		form.add(new DatumField("beginDatum"));
		form.add(new DatumField("eindDatum"));
	}
}
