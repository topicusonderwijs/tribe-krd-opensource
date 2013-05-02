package nl.topicus.eduarte.krd.web.components.verbintenis.buttons;

import nl.topicus.cobra.web.components.panels.bottomrow.AbstractPageBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.web.pages.intake.stap1.IntakeStap1Personalia;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IModel;

public class HerIntakeButton extends AbstractPageBottomRowButton
{
	private static final long serialVersionUID = 1L;

	public HerIntakeButton(BottomRowPanel bottomRow, final IModel<Deelnemer> deelnemerModel,
			final Page returnPage)
	{
		super(bottomRow, "Her-intake", CobraKeyAction.GEEN, ButtonAlignment.RIGHT, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new IntakeStap1Personalia(deelnemerModel.getObject(), returnPage);
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return IntakeStap1Personalia.class;
			}
		});
	}
}
