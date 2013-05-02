package nl.topicus.eduarte.krdparticipatie.web.components.panels.datapanels.columns;

import java.util.List;

import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.participatie.LesweekIndeling;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

public class LesweekindelingDropDownPanel extends TypedPanel<LesweekIndeling>
{
	private static final long serialVersionUID = 1L;

	public LesweekindelingDropDownPanel(String id, IModel<LesweekIndeling> model,
			IModel<List<LesweekIndeling>> choises)
	{
		super(id, model);

		DropDownChoice<LesweekIndeling> dropdown =
			new DropDownChoice<LesweekIndeling>("dropdown", model, choises);
		add(dropdown);
	}
}
