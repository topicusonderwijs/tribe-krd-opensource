package nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.hibernate.HibernateSelection;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductStatus;
import nl.topicus.eduarte.krd.principals.deelnemer.onderwijsproduct.DeelnemerOnderwijsproductenWrite;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.web.pages.shared.AbstractOnderwijsproductSelectiePage;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectieTarget;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductZoekFilter;

import org.apache.wicket.markup.html.link.Link;

/**
 * @author vandekamp
 */
@PageInfo(title = "Onderwijsproducten toevoegen stap 1", menu = {"Deelnemer > [deelnemer] > Onderwijs -> Afgenomen onderwijsproducten -> Toevoegen"})
@InPrincipal(DeelnemerOnderwijsproductenWrite.class)
@SearchImplementsActions( {Instelling.class, OrganisatieEenheid.class})
public class OnderwijsproductenToevoegenPage1 extends AbstractOnderwijsproductSelectiePage
		implements IModuleEditPage<Verbintenis>
{
	private static final long serialVersionUID = 1L;

	private static OnderwijsproductZoekFilter getDefaultFilter(Verbintenis inschrijving)
	{
		OnderwijsproductZoekFilter filter = new OnderwijsproductZoekFilter();
		filter.setOrganisatieEenheid(inschrijving.getOrganisatieEenheid());
		filter.setLocatie(inschrijving.getLocatie());
		filter.setCohort(inschrijving.getCohort());
		filter.setStatus(OnderwijsproductStatus.Beschikbaar);

		if (VerbintenisStatus.Intake.equals(inschrijving.getStatus()))
			filter.setBijIntake(true);

		return filter;
	}

	public OnderwijsproductenToevoegenPage1(Verbintenis inschrijving,
			AbstractDeelnemerPage returnPage)
	{
		this(returnPage, getDefaultFilter(inschrijving), new HibernateSelection<Onderwijsproduct>(
			Onderwijsproduct.class));
	}

	public OnderwijsproductenToevoegenPage1(final AbstractDeelnemerPage returnPage,
			final OnderwijsproductZoekFilter filter,
			final HibernateSelection<Onderwijsproduct> selection)
	{
		super(returnPage, filter, selection,
			new AbstractSelectieTarget<Onderwijsproduct, Onderwijsproduct>(
				OnderwijsproductenToevoegenPage2.class, "Volgende")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Link<Void> createLink(String linkId,
						final ISelectionComponent<Onderwijsproduct, Onderwijsproduct> base)
				{
					return new Link<Void>(linkId)
					{
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick()
						{
							setResponsePage(new OnderwijsproductenToevoegenPage2(returnPage
								.getContextVerbintenis(),
								(OnderwijsproductenToevoegenPage1) getPage(), base
									.getSelectedElements()));
						}
					};
				}
			});
	}

	@Override
	public int getMaxResults()
	{
		return Integer.MAX_VALUE;
	}
}
