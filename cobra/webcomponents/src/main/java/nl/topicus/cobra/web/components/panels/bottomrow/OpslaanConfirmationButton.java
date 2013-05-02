package nl.topicus.cobra.web.components.panels.bottomrow;

import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.util.string.AppendingStringBuffer;

/**
 * @author vandekamp
 */
public class OpslaanConfirmationButton extends AbstractBottomRowButton
{
	private static final long serialVersionUID = 1L;

	private Form< ? > form;

	public OpslaanConfirmationButton(BottomRowPanel bottomRow, Form< ? > form)
	{
		super(bottomRow, "Opslaan", CobraKeyAction.OPSLAAN, ButtonAlignment.RIGHT);
		this.form = form;
	}

	public OpslaanConfirmationButton(BottomRowPanel bottomRow, Form< ? > form, String label)
	{
		super(bottomRow, label, CobraKeyAction.OPSLAAN, ButtonAlignment.RIGHT);
		this.form = form;
	}

	@Override
	protected WebMarkupContainer getLink(String linkId)
	{
		if (isFormSubmittingButton())
		{
			return new SubmitLink(linkId, form)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onSubmit()
				{
					OpslaanConfirmationButton.this.onSubmit();
				}

				@Override
				protected void onComponentTag(ComponentTag tag)
				{
					super.onComponentTag(tag);
					if (useConfirmation())
					{
						String triggerScript = super.getTriggerJavaScript();
						AppendingStringBuffer buffer = new AppendingStringBuffer();
						buffer.append("if(confirm('");
						buffer.append(getConfirmation());
						buffer.append("'))");
						if (triggerScript == null)
							buffer.append(" return false; return true;");
						else
							buffer.append("{" + triggerScript + "}");
						tag.put("onclick", buffer.toString());
					}
				}
			};
		}
		return new Link<Void>(linkId)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				OpslaanConfirmationButton.this.onSubmit();
			}
		};
	}

	protected boolean useConfirmation()
	{
		return true;
	}

	protected String getConfirmation()
	{
		return "Weet u zeker dat u wilt opslaan?";
	}

	public Form< ? > getForm()
	{
		return form;
	}

	protected void onSubmit()
	{
	}

	protected boolean isFormSubmittingButton()
	{
		return true;
	}
}
