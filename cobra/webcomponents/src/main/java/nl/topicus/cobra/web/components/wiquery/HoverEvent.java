package nl.topicus.cobra.web.components.wiquery;

import java.io.Serializable;

import org.odlabs.wiquery.core.javascript.ChainableStatement;
import org.odlabs.wiquery.core.javascript.JsScope;

public abstract class HoverEvent implements ChainableStatement, Serializable
{
	private static final long serialVersionUID = 1L;

	@Override
	public String chainLabel()
	{
		return "hover";
	}

	@Override
	public CharSequence[] statementArgs()
	{
		return new String[] {hoverIn().render().toString(), hoverOut().render().toString()};
	}

	public abstract JsScope hoverIn();

	public abstract JsScope hoverOut();
}
