package nl.topicus.eduarte.resultaten.web.pages.deelnemer;

import nl.topicus.cobra.app.CobraApplication;
import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.security.actions.Begeleider;
import nl.topicus.eduarte.app.security.actions.Docent;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.resultaten.principals.deelnemer.DeelnemerCollectiefResultatenmatrixWrite;
import nl.topicus.eduarte.resultaten.web.components.resultaat.EditResultatenUIFactory;
import nl.topicus.eduarte.resultaten.web.components.resultaat.GroepResultatenmatrixPanel;
import nl.topicus.eduarte.resultaten.web.components.resultaat.ResultatenEditModel;
import nl.topicus.eduarte.resultaten.web.components.resultaat.ResultatenOpslaanButton;
import nl.topicus.eduarte.web.pages.AbstractDynamicContextPage;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SubpageContext;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurZoekFilter;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;

@PageInfo(title = "Resultatenmatrix bewerken", menu = "Deelnemer > Resultaten > Bewerken")
@InPrincipal(DeelnemerCollectiefResultatenmatrixWrite.class)
@SearchImplementsActions( {Instelling.class, OrganisatieEenheid.class, Begeleider.class,
	Docent.class})
public class DeelnemerCollectiefResultatenmatrixEditPage extends AbstractDynamicContextPage<Void>
		implements IModuleEditPage<Groep>
{
	private DeelnemerCollectiefResultatenmatrixPage returnPage;

	private GroepResultatenmatrixPanel<ResultatenEditModel> resultatenmatrixPanel;

	public DeelnemerCollectiefResultatenmatrixEditPage(
			DeelnemerCollectiefResultatenmatrixPage returnPage)
	{
		super(new SubpageContext(returnPage));
		this.returnPage = returnPage;

		add(resultatenmatrixPanel =
			new GroepResultatenmatrixPanel<ResultatenEditModel>("resultaten",
				new EditResultatenUIFactory<Deelnemer>(), returnPage.getToetsFilter(), returnPage
					.getSelectedDeelnemers()));

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
	public Component createTitle(String id)
	{
		ResultaatstructuurZoekFilter filter =
			returnPage.getToetsFilter().getResultaatstructuurFilter();
		return new Label(id, "Resultatenmatrix - " + filter.getCohort() + " - "
			+ filter.getOnderwijsproduct());
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		returnPage.detach();
	}
}
