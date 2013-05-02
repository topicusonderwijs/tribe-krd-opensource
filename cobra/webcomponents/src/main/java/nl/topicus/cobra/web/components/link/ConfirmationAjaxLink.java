package nl.topicus.cobra.web.components.link;

import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxCallDecorator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.AppendingStringBuffer;

/**
 * Link die om bevestiging vraagt voor de actie wordt uitgevoerd.
 * 
 * @author Martijn Dashorst
 */
public abstract class ConfirmationAjaxLink<T> extends SecureAjaxLink<T>
{
	private static final long serialVersionUID = 1L;

	private String confirmation;

	public ConfirmationAjaxLink(String id, String confirmMessage)
	{
		super(id);
		confirmation = confirmMessage;
	}

	public ConfirmationAjaxLink(String id, String confirmMessage, IModel<T> model)
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
	protected IAjaxCallDecorator getAjaxCallDecorator()
	{
		return new AjaxCallDecorator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public CharSequence decorateScript(CharSequence script)
			{
				AppendingStringBuffer buffer = new AppendingStringBuffer();
				buffer
					.append("if (event.stopPropagation){event.stopPropagation();} else {event.cancelBubble = true;};");
				buffer.append("if(!confirm('");
				buffer.append(confirmation);
				buffer.append("')) return false; ");
				buffer.append(script);
				return buffer;
			}
		};
	}
}
