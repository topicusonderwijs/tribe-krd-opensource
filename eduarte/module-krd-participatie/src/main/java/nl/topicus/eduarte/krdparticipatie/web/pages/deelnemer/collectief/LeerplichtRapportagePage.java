package nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.collectief;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.CollectionDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.krdparticipatie.principals.deelnemer.DeelnemerLeerplichtRapportage;
import nl.topicus.eduarte.krdparticipatie.web.components.menu.enums.ParticipatieDeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.krdparticipatie.web.components.tables.LeerplichtRapportageTable;
import nl.topicus.eduarte.rapportage.leerplicht.LeerplichtRapportage;
import nl.topicus.eduarte.rapportage.leerplicht.LeerplichtRapportageModel;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.zoekfilters.LeerplichtRapportageZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;

/**
 * @author vandekamp
 */
@PageInfo(title = "Zoeken naar deelnemers met teveel ongeoorloofd verzuim", menu = "Deelnemer > Aanwezigheid > Verzuimrapportage")
@InPrincipal(DeelnemerLeerplichtRapportage.class)
public class LeerplichtRapportagePage extends SecurePage
{
	private static final long serialVersionUID = 1L;

	private LeerplichtRapportageZoekFilterPanel filterPanel;

	private static final LeerplichtRapportageZoekFilter getDefaultFilter()
	{
		LeerplichtRapportageZoekFilter filter = new LeerplichtRapportageZoekFilter();
		filter.setPeildatum(TimeUtil.getInstance().currentDate());
		VerbintenisZoekFilter inschrijvingFilter = new VerbintenisZoekFilter();
		inschrijvingFilter.setOrganisatieEenheid(EduArteContext.get()
			.getDefaultOrganisatieEenheid());
		filter.setDeelnemerFilter(inschrijvingFilter);

		return filter;
	}

	public LeerplichtRapportagePage()
	{
		this(getDefaultFilter());
	}

	public LeerplichtRapportagePage(LeerplichtRapportageZoekFilter filter)
	{
		super(CoreMainMenuItem.Deelnemer);
		filter.getDeelnemerFilter().setAuthorizationContext(
			new OrganisatieEenheidLocatieAuthorizationContext(this));
		CollectionDataProvider<LeerplichtRapportage> provider =
			new CollectionDataProvider<LeerplichtRapportage>(new LeerplichtRapportageModel(filter));
		EduArteDataPanel<LeerplichtRapportage> datapanel =
			new EduArteDataPanel<LeerplichtRapportage>("datapanel", provider,
				new LeerplichtRapportageTable());
		datapanel.setItemsPerPage(20);
		add(datapanel);
		filterPanel = new LeerplichtRapportageZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);

		createComponents();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id,
			ParticipatieDeelnemerCollectiefMenuItem.VerzuimRapportage);
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, "Leerplichtigen die teveel afwezig zijn").setOutputMarkupId(true);
	}
}
