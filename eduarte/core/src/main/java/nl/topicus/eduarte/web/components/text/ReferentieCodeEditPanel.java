package nl.topicus.eduarte.web.components.text;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class ReferentieCodeEditPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	public ReferentieCodeEditPanel(String id, IModel<String> codeModel, IModel<Integer> versieModel)
	{
		super(id);
		add(new TextField<String>("code", codeModel));
		add(new TextField<Integer>("versie", versieModel));
	}
}
