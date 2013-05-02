package nl.topicus.cobra.web.components.wiquery;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.Model;

public class OpenInNewWindowAttributeModifier extends AttributeModifier
{
	private static final long serialVersionUID = 1L;

	public OpenInNewWindowAttributeModifier(String windowName)
	{
		super("onclick", true, new Model<String>("window.open(this.href,'" + windowName
			+ "'); return false;"));
	}

	public OpenInNewWindowAttributeModifier()
	{
		super("onclick", true, new Model<String>("window.open(this.href); return false;"));
	}
}
