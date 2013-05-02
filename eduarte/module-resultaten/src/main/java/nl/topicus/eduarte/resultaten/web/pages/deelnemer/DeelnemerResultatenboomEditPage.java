package nl.topicus.eduarte.resultaten.web.pages.deelnemer;

import nl.topicus.cobra.app.CobraApplication;
import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.security.RequiredSecurityCheck;
import nl.topicus.eduarte.app.security.checks.NietOverledenSecurityCheck;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.resultaten.principals.deelnemer.DeelnemerResultatenboomWrite;
import nl.topicus.eduarte.resultaten.web.components.resultaat.EditResultatenUIFactory;
import nl.topicus.eduarte.resultaten.web.components.resultaat.ResultatenEditModel;
import nl.topicus.eduarte.resultaten.web.components.resultaat.ResultatenOpslaanButton;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.resultaat.ResultatenboomPanel;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.web.pages.deelnemer.resultaten.DeelnemerResultatenboomPage;

@PageInfo(title = "Resultatenboom bewerken", menu = "Deelnemer > [Deelnemer] > Resultaten > Resultatenboom > Bewerken")
@InPrincipal(DeelnemerResultatenboomWrite.class)
@RequiredSecurityCheck(NietOverledenSecurityCheck.class)
public class DeelnemerResultatenboomEditPage extends AbstractDeelnemerPage implements
		IModuleEditPage<Deelnemer>
{
	private DeelnemerResultatenboomPage returnPage;

	private ResultatenboomPanel<ResultatenEditModel> resultatenboomPanel;

	public DeelnemerResultatenboomEditPage(Deelnemer deelnemer,
			DeelnemerResultatenboomPage returnPage)
	{
		super(DeelnemerMenuItem.Resultatenboom, deelnemer, returnPage.getContextVerbintenis());

		this.returnPage = returnPage;

		add(resultatenboomPanel =
			new ResultatenboomPanel<ResultatenEditModel>("resultaten",
				new EditResultatenUIFactory<Toets>(), returnPage.getToetsFilter(), deelnemer,
				returnPage.getToetsFilter().getResultaatstructuurFilter().getCohort()));
		resultatenboomPanel.checkResultatenOnOpen(returnPage);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new ResultatenOpslaanButton(panel, resultatenboomPanel, returnPage, true));
		panel.addButton(new ResultatenOpslaanButton(panel, resultatenboomPanel, returnPage, false)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && CobraApplication.get().isDevelopment();
			}
		}.setAction(CobraKeyAction.GEEN).setLabel("Doe alsof"));
		panel.addButton(new AnnulerenButton(panel, returnPage));
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		returnPage.detach();
	}
}
