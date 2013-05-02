package nl.topicus.cobra.web.components.link;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.AppendingStringBuffer;

/**
 * Link die om bevestiging vraagt voor de actie wordt uitgevoerd.
 * 
 * @author Martijn Dashorst
 */
public abstract class ConfirmationLink<T> extends Link<T>
{
	private static final long serialVersionUID = 1L;

	private String confirmation;

	public ConfirmationLink(String id, String confirmMessage)
	{
		super(id);
		confirmation = confirmMessage;
	}

	public ConfirmationLink(String id, String confirmMessage, IModel<T> model)
	{
		super(id, model);
		confirmation = confirmMessage;
	}

	public String getConfirmation()
	{
		return confirmation;
	}

	public void setConfirmation(String confirmation)
	{
		this.confirmation = confirmation;
	}

	@Override
	protected String getOnClickScript(final CharSequence url)
	{
		AppendingStringBuffer buffer = new AppendingStringBuffer();
		buffer.append("if(!confirm('");
		buffer.append(confirmation);
		buffer.append("')) return false; return true;");
		return buffer.toString();
	}
}
