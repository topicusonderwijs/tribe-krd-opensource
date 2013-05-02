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
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.resultaten.principals.deelnemer.DeelnemerResultatenmatrixWrite;
import nl.topicus.eduarte.resultaten.web.components.resultaat.EditResultatenUIFactory;
import nl.topicus.eduarte.resultaten.web.components.resultaat.ResultatenEditModel;
import nl.topicus.eduarte.resultaten.web.components.resultaat.ResultatenOpslaanButton;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.resultaat.ResultatenmatrixPanel;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.web.pages.deelnemer.resultaten.DeelnemerResultatenmatrixPage;

@PageInfo(title = "Deelnemer resultatenmatrix bewerken", menu = "Deelnemer > [Deelnemer] > Resultaten > Resultatenmatrix > Bewerken")
@InPrincipal(DeelnemerResultatenmatrixWrite.class)
@RequiredSecurityCheck(NietOverledenSecurityCheck.class)
public class DeelnemerResultatenmatrixEditPage extends AbstractDeelnemerPage implements
		IModuleEditPage<Deelnemer>
{
	private DeelnemerResultatenmatrixPage returnPage;

	private ResultatenmatrixPanel<ResultatenEditModel> resultatenmatrixPanel;

	public DeelnemerResultatenmatrixEditPage(Deelnemer deelnemer,
			DeelnemerResultatenmatrixPage returnPage)
	{
		super(DeelnemerMenuItem.Resultatenmatrix, deelnemer, returnPage.getContextVerbintenis());
		this.returnPage = returnPage;

		add(resultatenmatrixPanel =
			new ResultatenmatrixPanel<ResultatenEditModel>("resultaten",
				new EditResultatenUIFactory<Resultaatstructuur>(), returnPage.getToetsFilter(),
				deelnemer, returnPage.getToetsFilter().getResultaatstructuurFilter().getCohort()));

		resultatenmatrixPanel.checkResultatenOnOpen(returnPage);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel
			.addButton(new ResultatenOpslaanButton(panel, resultatenmatrixPanel, returnPage, true));
		panel
			.addButton(new ResultatenOpslaanButton(panel, resultatenmatrixPanel, returnPage, false)
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
