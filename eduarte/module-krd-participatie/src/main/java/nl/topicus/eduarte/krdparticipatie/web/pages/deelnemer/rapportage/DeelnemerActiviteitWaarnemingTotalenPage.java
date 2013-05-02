package nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.rapportage;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.participatie.enums.Schooljaar;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krdparticipatie.principals.deelnemer.DeelnemerTotalenPerOnderwijsproductRapportage;
import nl.topicus.eduarte.krdparticipatie.web.components.menu.enums.ParticipatieDeelnemerMenuItem;
import nl.topicus.eduarte.krdparticipatie.web.components.panels.filter.DeelnemerActiviteitWaarnemingTotalenPanel;
import nl.topicus.eduarte.krdparticipatie.web.components.panels.filter.DeelnemerActiviteitWaarnemingTotalenZoekFilterPanel;
import nl.topicus.eduarte.participatie.zoekfilters.DeelnemerActiviteitTotalenZoekFilter;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * Pagina met de totalen van 1 deelnemer opgesplits per onderwijsproduct
 * 
 * @author vandekamp
 */
@PageInfo(title = "Activiteittotalen", menu = {"Deelnemer > [deelnemer] > Aanwezigheid > Totalen per onderwijsproduct"})
@InPrincipal(DeelnemerTotalenPerOnderwijsproductRapportage.class)
public class DeelnemerActiviteitWaarnemingTotalenPage extends AbstractDeelnemerPage
{
	private static final long serialVersionUID = 1L;

	private DeelnemerActiviteitWaarnemingTotalenZoekFilterPanel filterPanel;

	private static DeelnemerActiviteitTotalenZoekFilter getDefaultFilter()
	{
		DeelnemerActiviteitTotalenZoekFilter filter = new DeelnemerActiviteitTotalenZoekFilter();
		filter.setBeginDatum(Schooljaar.getHuidigSchooljaar().getVanafDatum());
		filter.setEindDatum(TimeUtil.getInstance().currentDate());
		filter.setActiviteitenTonen(true);
		return filter;

	}

	public DeelnemerActiviteitWaarnemingTotalenPage(DeelnemerProvider provider)
	{
		this(provider.getDeelnemer());
	}

	public DeelnemerActiviteitWaarnemingTotalenPage(Deelnemer deelnemer)
	{
		this(deelnemer, deelnemer.getEersteInschrijvingOpPeildatum());
	}

	public DeelnemerActiviteitWaarnemingTotalenPage(Verbintenis verbintenis)
	{
		this(verbintenis.getDeelnemer(), verbintenis);
	}

	public DeelnemerActiviteitWaarnemingTotalenPage(Deelnemer deelnemer, Verbintenis verbintenis)
	{
		this(deelnemer, verbintenis, getDefaultFilter());
	}

	public DeelnemerActiviteitWaarnemingTotalenPage(Deelnemer deelnemer, Verbintenis verbintenis,
			DeelnemerActiviteitTotalenZoekFilter filter)
	{
		super(ParticipatieDeelnemerMenuItem.OnderwijsproductTotalen, deelnemer, verbintenis);
		if (verbintenis != null)
		{
			filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
			filter.setOrganisatieEenheid(verbintenis.getOrganisatieEenheid());
			filterPanel = new DeelnemerActiviteitWaarnemingTotalenZoekFilterPanel("filter", filter);
			add(new DeelnemerActiviteitWaarnemingTotalenPanel("activiteitenPanel", verbintenis,
				filter));
			add(filterPanel);
		}
		else
		{
			add(new WebMarkupContainer("filter"));
			add(new WebMarkupContainer("activiteitenPanel"));
			error("De deelnemer is nog niet ingeschreven, er zijn geen totalen beschikbaar");
		}
		createComponents();
	}
}
