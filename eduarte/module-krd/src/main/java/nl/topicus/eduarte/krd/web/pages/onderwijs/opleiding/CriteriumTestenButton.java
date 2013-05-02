package nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding;

import nl.topicus.cobra.web.components.panels.bottomrow.AbstractLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.entities.criteriumbank.Criterium;
import nl.topicus.eduarte.providers.CriteriumProvider;
import nl.topicus.eduarte.util.criteriumbank.CriteriumbankControleTest;
import nl.topicus.eduarte.web.pages.onderwijs.opleiding.AbstractOpleidingPage;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;

/**
 * Button voor het testen van een specifiek criterium. Kan alleen gebruikt worden op een
 * AbstractOpleidingPage die een criterium beheert.
 * 
 * @author loite
 */
public class CriteriumTestenButton extends AbstractLinkButton
{
	private static final long serialVersionUID = 1L;

	private final Form< ? > form;

	public CriteriumTestenButton(BottomRowPanel panel)
	{
		this(panel, null);
	}

	public CriteriumTestenButton(BottomRowPanel panel, Form< ? > form)
	{
		super(panel, "Criterium testen", CobraKeyAction.LINKKNOP1, ButtonAlignment.LEFT);
		this.form = form;
	}

	@Override
	protected void onClick()
	{
		AbstractOpleidingPage page = (AbstractOpleidingPage) getPage();
		Criterium criterium = ((CriteriumProvider) page).getCriterium();
		CriteriumbankControleTest test =
			new CriteriumbankControleTest(page.getContextOpleiding(), criterium.getCohort());
		boolean succes = test.testCriterium(criterium);
		if (!succes)
		{
			error("Er zijn fouten gevonden bij het doorlopen van het criterium");
			String melding = test.getCriteriumbank().getCriteriumMeldingen().get(criterium);
			error(criterium.getVolgnummer() + " - " + criterium.getNaam() + ": " + melding);
		}
		else
		{
			info("Criterium succesvol uitgevoerd zonder fouten");
		}
	}

	@Override
	protected WebMarkupContainer getLink(String linkId)
	{
		if (form == null)
		{
			return super.getLink(linkId);
		}
		return new SubmitLink(linkId, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit()
			{
				onClick();
			}
		};
	}
}
