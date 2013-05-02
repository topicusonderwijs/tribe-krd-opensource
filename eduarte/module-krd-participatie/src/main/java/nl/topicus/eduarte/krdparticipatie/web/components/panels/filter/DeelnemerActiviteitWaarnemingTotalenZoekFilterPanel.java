package nl.topicus.eduarte.krdparticipatie.web.components.panels.filter;

import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.eduarte.participatie.zoekfilters.DeelnemerActiviteitTotalenZoekFilter;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

/**
 * @author vandekamp
 */
public class DeelnemerActiviteitWaarnemingTotalenZoekFilterPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	public DeelnemerActiviteitWaarnemingTotalenZoekFilterPanel(String id,
			DeelnemerActiviteitTotalenZoekFilter filter)
	{
		super(id);
		Form<DeelnemerActiviteitTotalenZoekFilter> form =
			new Form<DeelnemerActiviteitTotalenZoekFilter>("form",
				new CompoundPropertyModel<DeelnemerActiviteitTotalenZoekFilter>(filter));
		add(form);
		form.add(new DatumField("beginDatum"));
		form.add(new DatumField("eindDatum"));
		form.add(new JaNeeCombobox("activiteitenTonen").setNullValid(true));
	}
}
