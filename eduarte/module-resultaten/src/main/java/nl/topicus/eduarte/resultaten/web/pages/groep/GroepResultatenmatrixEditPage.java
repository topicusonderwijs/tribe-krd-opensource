package nl.topicus.eduarte.resultaten.web.pages.groep;

import nl.topicus.cobra.app.CobraApplication;
import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.resultaten.principals.groep.GroepResultatenmatrixWrite;
import nl.topicus.eduarte.resultaten.web.components.resultaat.EditResultatenUIFactory;
import nl.topicus.eduarte.resultaten.web.components.resultaat.GroepResultatenmatrixPanel;
import nl.topicus.eduarte.resultaten.web.components.resultaat.ResultatenEditModel;
import nl.topicus.eduarte.resultaten.web.components.resultaat.ResultatenOpslaanButton;
import nl.topicus.eduarte.web.components.menu.GroepMenuItem;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.groep.AbstractGroepPage;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurZoekFilter;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;

@PageInfo(title = "Resultatenmatrix", menu = "Groepen > [Groep] > Resultaten > Bewerken")
@InPrincipal(GroepResultatenmatrixWrite.class)
public class GroepResultatenmatrixEditPage extends AbstractGroepPage implements
		IModuleEditPage<Groep>
{
	private GroepResultatenmatrixPage returnPage;

	private GroepResultatenmatrixPanel<ResultatenEditModel> resultatenmatrixPanel;

	public GroepResultatenmatrixEditPage(Groep groep, GroepResultatenmatrixPage returnPage)
	{
		super(GroepMenuItem.Invoeren, groep);
		this.returnPage = returnPage;

		add(resultatenmatrixPanel =
			new GroepResultatenmatrixPanel<ResultatenEditModel>("resultaten",
				new EditResultatenUIFactory<Deelnemer>(), returnPage.getToetsFilter(), groep));

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
		return new Label(id, getContextGroep() + " - " + filter.getCohort() + " - "
			+ filter.getOnderwijsproduct());
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		returnPage.detach();
	}
}
