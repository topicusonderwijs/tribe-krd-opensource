package nl.topicus.eduarte.krdparticipatie.web.pages.groep;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.entities.participatie.enums.AfspraakTypeCategory;
import nl.topicus.eduarte.krdparticipatie.principals.deelnemer.GroepAanwezigheidRapportage;
import nl.topicus.eduarte.krdparticipatie.web.components.menu.enums.ParticipatieGroepMenuItem;
import nl.topicus.eduarte.krdparticipatie.web.components.panels.rapportage.TotalenPerGroepPanel;
import nl.topicus.eduarte.participatie.zoekfilters.TotalenPerGroepZoekFilter;
import nl.topicus.eduarte.web.pages.groep.AbstractGroepPage;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

/**
 * Pagina voor het totaaloverzicht van waarneming per deelnmer uit de groep
 * 
 * @author vandekamp
 */
@PageInfo(title = "Totalen per groep", menu = {"Groep > [groep] > Participatie > Totalen per groep"})
@InPrincipal(GroepAanwezigheidRapportage.class)
public class TotalenPerGroepPage extends AbstractGroepPage
{
	private static final long serialVersionUID = 1L;

	private static TotalenPerGroepZoekFilter getDefaultFilter(Groep groep)
	{
		TotalenPerGroepZoekFilter filter = new TotalenPerGroepZoekFilter();
		filter.setOrganisatieEenheid(groep.getOrganisatieEenheid());
		filter.setBeginDatum(Schooljaar.huidigSchooljaar().getBegindatum());
		filter.setEindDatum(TimeUtil.getInstance().currentDate());
		List<AfspraakTypeCategory> afspraakTypeCategoryList = new ArrayList<AfspraakTypeCategory>();
		afspraakTypeCategoryList.add(AfspraakTypeCategory.INDIVIDUEEL);
		afspraakTypeCategoryList.add(AfspraakTypeCategory.ROOSTER);
		filter.setAfspraakTypeCategories(afspraakTypeCategoryList);
		return filter;
	}

	public TotalenPerGroepPage(Groep groep)
	{
		this(groep, getDefaultFilter(groep));
	}

	public TotalenPerGroepPage(Groep groep, TotalenPerGroepZoekFilter filter)
	{
		super(ParticipatieGroepMenuItem.Totalen, groep);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		TotalenPerGroepZoekFilterPanel filterPanel =
			new TotalenPerGroepZoekFilterPanel("filter", filter);
		add(new TotalenPerGroepPanel("totalenPerGroepPanel", "Totalen van " + groep.getNaam(),
			groep, filter));
		add(filterPanel);
		createComponents();
	}

}
