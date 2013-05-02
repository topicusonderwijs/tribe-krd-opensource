package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.bronmutatie;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.cobra.web.components.link.ConfirmationLink;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.core.principals.App;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.verbintenis.VerbintenisCollectiefEditOverzichtPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.verbintenis.AbstractVerbintenisSelectiePage;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectieTarget;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.markup.html.link.Link;

/**
 * @author loite
 */
@PageInfo(title = "Collectief BRON-mutaties aanmaken stap 2", menu = {"Deelnemer > Collectief > BRON-mutaties"})
@InPrincipal(App.class)
public class CollectiefBronmutatieSelectiePage extends AbstractVerbintenisSelectiePage implements
		IEditPage
{
	private static final long serialVersionUID = 1L;

	public CollectiefBronmutatieSelectiePage(CollectiefBronmutatieModel model, SecurePage returnPage)
	{
		super(returnPage, getDefaultFilter(), getSelectieTarget(model));
		setDefaultModel(model);
	}

	private static VerbintenisZoekFilter getDefaultFilter()
	{
		VerbintenisZoekFilter filter = new VerbintenisZoekFilter();
		return filter;
	}

	private static AbstractSelectieTarget<Verbintenis, Verbintenis> getSelectieTarget(
			final CollectiefBronmutatieModel model)
	{
		return new AbstractSelectieTarget<Verbintenis, Verbintenis>(
			CollectiefBronmutatieOverzichtPage.class, "BRON-mutaties aanmaken")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Link<Void> createLink(String linkId,
					final ISelectionComponent<Verbintenis, Verbintenis> base)
			{
				return new ConfirmationLink<Void>(linkId,
					"Weet u zeker dat u voor de geselecteerde verbintenissen BRON-mutaties wilt aanmaken?")
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick()
					{
						CollectiefBronmutatieOverzichtPage overzichtPage =
							new CollectiefBronmutatieOverzichtPage();
						overzichtPage.startJob(new CollectiefBronmutatieDataMap<Verbintenis>(base,
							model));
						setResponsePage(overzichtPage);
					}
				};
			}
		};
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		panel.addButton(new AnnulerenButton(panel, VerbintenisCollectiefEditOverzichtPage.class));
	}

	@Override
	public boolean supportsBookmarks()
	{
		return false;
	}
}