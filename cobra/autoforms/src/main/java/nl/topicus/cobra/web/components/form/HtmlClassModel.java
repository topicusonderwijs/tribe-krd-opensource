package nl.topicus.cobra.web.components.form;

import java.util.List;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

public class HtmlClassModel extends AbstractReadOnlyModel<String>
{
	private static final long serialVersionUID = 1L;

	private IModel<List<String>> wrapped;

	public HtmlClassModel(IModel<List<String>> wrapped)
	{
		this.wrapped = wrapped;
	}

	@Override
	public String getObject()
	{
		if (wrapped.getObject() == null)
			return null;
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		for (String curClass : wrapped.getObject())
		{
			if (!first)
				sb.append(' ');
			first = false;
			sb.append(curClass);
		}
		return sb.toString();
	}
}
