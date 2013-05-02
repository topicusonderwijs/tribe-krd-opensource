package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.orgehdwijzigen;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.cobra.web.components.link.ConfirmationLink;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerVerbintenissenWrite;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.verbintenis.VerbintenisCollectiefEditOverzichtPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.verbintenis.AbstractVerbintenisSelectiePage;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectieTarget;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.markup.html.link.Link;

/**
 * @author idserda
 */
@PageInfo(title = "Organisatie-eenheid en locatie colllectief wijzigen stap 2", menu = {"Deelnemer > Verbintenissen"})
@InPrincipal(DeelnemerVerbintenissenWrite.class)
public class OrganisatieEenheidLocatieCollectiefWijzigenSelectiePage extends
		AbstractVerbintenisSelectiePage implements IEditPage
{
	private static final long serialVersionUID = 1L;

	public OrganisatieEenheidLocatieCollectiefWijzigenSelectiePage(
			final OrganisatieEenheidLocatieCollectiefWijzigenModel model, SecurePage returnPage)
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
			final OrganisatieEenheidLocatieCollectiefWijzigenModel model)
	{
		return new AbstractSelectieTarget<Verbintenis, Verbintenis>(
			OrganisatieEenheidLocatieCollectiefWijzigenOverzichtPage.class, "Wijzigen")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Link<Void> createLink(String linkId,
					final ISelectionComponent<Verbintenis, Verbintenis> base)
			{
				return new ConfirmationLink<Void>(linkId,
					"Weet u zeker dat u de geselecteerde verbintenissen wilt wijzigen?")
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick()
					{
						OrganisatieEenheidLocatieCollectiefWijzigenOverzichtPage overzichtPage =
							new OrganisatieEenheidLocatieCollectiefWijzigenOverzichtPage();
						overzichtPage
							.startJob(new OrganisatieEenheidLocatieCollectiefWijzigenDataMap<Verbintenis>(
								base, model));
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