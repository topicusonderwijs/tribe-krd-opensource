package nl.topicus.eduarte.web.pages.deelnemerportaal.dossier;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.core.principals.deelnemerportaal.DeelnemerportaalGroepen;
import nl.topicus.eduarte.dao.helpers.GroepsdeelnameDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.web.components.menu.deelnemerPortaal.DeelnemerportaalDossierMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.DeelnemerGroepsdeelnameTable;
import nl.topicus.eduarte.web.components.panels.filter.DeelnemerGroepsdeelnameZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.GroepsdeelnameZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.markup.repeater.data.IDataProvider;

/**
 * Pagina alleen voor docenten om snel naar hun eigen groepen te kunnen.
 * 
 * @author bos
 */
@PageInfo(title = "Mijn groepen", menu = "Dossier")
// TODO nick - Deelneemer portaalRecht toevoegen
@InPrincipal(DeelnemerportaalGroepen.class)
public class DeelnemerportaalGroepenPage extends AbstractDeelnemerportaalDossierPage
{
	private static final long serialVersionUID = 1L;

	private DeelnemerGroepsdeelnameZoekFilterPanel filterPanel;

	private static final GroepsdeelnameZoekFilter getDefaultFilter()
	{
		GroepsdeelnameZoekFilter filter = new GroepsdeelnameZoekFilter(getIngelogdeDeelnemer());
		filter.setPeildatum(TimeUtil.getInstance().currentDate());
		return filter;
	}

	public DeelnemerportaalGroepenPage()
	{
		this(getDefaultVerbintenis());
	}

	public DeelnemerportaalGroepenPage(Verbintenis verbintenis)
	{
		this(getDefaultFilter(), verbintenis);
	}

	public DeelnemerportaalGroepenPage(GroepsdeelnameZoekFilter filter, Verbintenis verbintenis)
	{
		super(DeelnemerportaalDossierMenuItem.Groepen, verbintenis);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));

		IDataProvider<Groepsdeelname> provider =
			GeneralFilteredSortableDataProvider.of(filter, GroepsdeelnameDataAccessHelper.class);

		// TODO Paul: Juiste gegevens??
		EduArteDataPanel<Groepsdeelname> datapanel =
			new EduArteDataPanel<Groepsdeelname>("datapanel", provider,
				new DeelnemerGroepsdeelnameTable());
		add(datapanel);

		filterPanel = new DeelnemerGroepsdeelnameZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);

		createComponents();
	}
}
