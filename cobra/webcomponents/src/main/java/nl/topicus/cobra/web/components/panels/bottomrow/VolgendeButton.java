package nl.topicus.cobra.web.components.panels.bottomrow;

import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.Link;

/**
 * Knop voor het doorgaan met de volgende stap in een wizard. Overschrijf
 * {@link #onVolgende()} om deze functionaliteit te implementeren.
 */
public class VolgendeButton extends AbstractBottomRowButton
{
	private static final long serialVersionUID = 1L;

	private Form< ? > form;

	public VolgendeButton(BottomRowPanel bottomRow, Form< ? > form)
	{
		super(bottomRow, "Volgende", CobraKeyAction.VOLGENDE, ButtonAlignment.RIGHT);
		this.form = form;
	}

	@Override
	protected WebMarkupContainer getLink(String linkId)
	{
		if (form != null)
		{
			return createSubmitLink(linkId);
		}
		return createLink(linkId);
	}

	private WebMarkupContainer createSubmitLink(String linkId)
	{
		SubmitLink submitLink = new SubmitLink(linkId, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit()
			{
				onVolgende();
			}
		};
		form.setDefaultButton(submitLink);
		return submitLink;
	}

	private WebMarkupContainer createLink(String linkId)
	{
		return new Link<Void>(linkId)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				onVolgende();
			}
		};
	}

	protected void onVolgende()
	{

	}
}
