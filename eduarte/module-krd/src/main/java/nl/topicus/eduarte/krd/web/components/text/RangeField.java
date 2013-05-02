package nl.topicus.eduarte.krd.web.components.text;

import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.krd.entities.mutatielog.Range;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class RangeField extends TypedPanel<Range>
{
	private static final long serialVersionUID = 1L;

	public RangeField(String id, IModel<Range> object)
	{
		super(id, object);

		TextField<Integer> from =
			new TextField<Integer>("from", new PropertyModel<Integer>(getModel(), "from"),
				Integer.class);
		TextField<Integer> to =
			new TextField<Integer>("to", new PropertyModel<Integer>(getModel(), "to"),
				Integer.class);

		add(from);
		add(to);
	}
}
