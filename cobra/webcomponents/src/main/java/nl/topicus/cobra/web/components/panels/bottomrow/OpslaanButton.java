package nl.topicus.cobra.web.components.panels.bottomrow;

import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.util.string.AppendingStringBuffer;

/**
 * @author papegaaij
 * @author loite
 */
public class OpslaanButton extends AbstractBottomRowButton
{
	private static final long serialVersionUID = 1L;

	private Form< ? > form;

	public OpslaanButton(BottomRowPanel bottomRow, Form< ? > form)
	{
		super(bottomRow, "Opslaan", CobraKeyAction.OPSLAAN, ButtonAlignment.RIGHT);
		this.form = form;
	}

	public OpslaanButton(BottomRowPanel bottomRow, Form< ? > form, String label)
	{
		super(bottomRow, label, CobraKeyAction.OPSLAAN, ButtonAlignment.RIGHT);
		this.form = form;
	}

	@Override
	protected WebMarkupContainer getLink(String linkId)
	{
		if (isFormSubmittingButton() && getConfirmationString() != null)
		{
			return new SubmitLink(linkId, getForm())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onSubmit()
				{
					OpslaanButton.this.onSubmit();
				}

				@Override
				protected void onComponentTag(ComponentTag tag)
				{
					super.onComponentTag(tag);

					AppendingStringBuffer buffer = new AppendingStringBuffer();
					buffer.append("if(!confirm('");
					buffer.append(getConfirmationString());
					buffer.append("')) return false; ");
					buffer.append(tag.getString("onclick").toString());

					tag.put("onclick", buffer.toString());
				}
			};
		}
		if (isFormSubmittingButton() && getConfirmationString() == null)
		{
			return new SubmitLink(linkId, getForm())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onSubmit()
				{
					OpslaanButton.this.onSubmit();
				}

			};
		}
		else if (getConfirmationString() != null)
		{
			return new Link<Void>(linkId)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick()
				{
					OpslaanButton.this.onSubmit();
				}

				@Override
				protected CharSequence getOnClickScript(CharSequence url)
				{
					AppendingStringBuffer buffer = new AppendingStringBuffer();
					buffer.append("if(!confirm('");
					buffer.append(getConfirmationString());
					buffer.append("')) return false; return true;");
					return buffer.toString();
				}
			};
		}

		return new Link<Void>(linkId)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				OpslaanButton.this.onSubmit();
			}
		};

	}

	protected String getConfirmationString()
	{
		return null;
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
