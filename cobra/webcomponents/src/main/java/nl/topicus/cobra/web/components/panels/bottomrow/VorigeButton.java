package nl.topicus.cobra.web.components.panels.bottomrow;

import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.Link;

/**
 * Knop om terug te gaan naar de vorige stap in een wizard. Bewaart de invoer van de
 * gebruiker, en valideert deze ook! Overschrijf {@link #onVorige()} om gedrag te
 * implementeren voor het terug gaan naar de vorige stap.
 */
public class VorigeButton extends AbstractBottomRowButton
{
	private static final long serialVersionUID = 1L;

	private Form< ? > form;

	public VorigeButton(BottomRowPanel bottomRow, Form< ? > form)
	{
		super(bottomRow, "Vorige", CobraKeyAction.TERUG, ButtonAlignment.RIGHT);
		this.form = form;
	}

	public VorigeButton(BottomRowPanel bottomRow)
	{
		super(bottomRow, "Vorige", CobraKeyAction.TERUG, ButtonAlignment.RIGHT);
	}

	@Override
	protected WebMarkupContainer getLink(String linkId)
	{
		if (form != null)
		{
			return new SubmitLink(linkId, form)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onSubmit()
				{
					onVorige();
				}
			};
		}
		else
		{
			return new Link<Void>(linkId)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick()
				{
					onVorige();
				}
			};
		}
	}

	protected void onVorige()
	{
	}
}
