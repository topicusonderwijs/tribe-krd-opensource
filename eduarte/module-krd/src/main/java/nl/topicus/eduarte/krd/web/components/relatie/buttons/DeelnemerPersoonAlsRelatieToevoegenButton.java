package nl.topicus.eduarte.krd.web.components.relatie.buttons;

import nl.topicus.cobra.web.components.panels.bottomrow.AbstractPageBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.web.pages.deelnemer.relatie.DeelnemerPersoonAlsRelatieToevoegenPage;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IModel;

public class DeelnemerPersoonAlsRelatieToevoegenButton extends AbstractPageBottomRowButton
{
	private static final long serialVersionUID = 1L;

	public DeelnemerPersoonAlsRelatieToevoegenButton(BottomRowPanel bottomRow,
			final IModel<Deelnemer> deelnemerModel, final IModel<Verbintenis> verbintenisModel)
	{
		super(bottomRow, "Persoon als relatie toevoegen", CobraKeyAction.TOEVOEGEN,
			ButtonAlignment.RIGHT, new IPageLink()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Page getPage()
				{
					return new DeelnemerPersoonAlsRelatieToevoegenPage(deelnemerModel.getObject(),
						verbintenisModel.getObject());
				}

				@Override
				public Class< ? extends SecurePage> getPageIdentity()
				{
					return DeelnemerPersoonAlsRelatieToevoegenPage.class;
				}
			});
	}
}
