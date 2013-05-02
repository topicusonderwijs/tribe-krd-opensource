package nl.topicus.eduarte.web.components.panels;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;

public class ContBoxAfdrukLinkPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	public ContBoxAfdrukLinkPanel(String id, final String naam)
	{
		super(id);
		add(new AjaxLink<Void>("afdrukLink")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				target.appendJavascript("printdiv('" + naam + "');");
			}

		});
	}
}
