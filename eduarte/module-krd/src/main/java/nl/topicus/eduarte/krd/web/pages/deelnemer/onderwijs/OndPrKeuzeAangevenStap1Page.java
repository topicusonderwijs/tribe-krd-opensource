package nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs;

import java.util.Arrays;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.dao.hibernate.HibernateSelection;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.selection.AbstractSelectiePanel;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.krd.principals.deelnemer.onderwijsproduct.DeelnemersOnderwijsproductenKeuzeAangeven;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.panels.datapanel.selectie.DeelnemerCohortSelectiePanel;
import nl.topicus.eduarte.web.pages.deelnemer.DeelnemerZoekenPage;
import nl.topicus.eduarte.web.pages.shared.AbstractDeelnemerSelectiePage;
import nl.topicus.eduarte.web.pages.shared.DeelnemerCollectiefPageContext;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

/**
 * Selectie pagina voor Deelnemers.
 * 
 * @author vandekamp
 */
@PageInfo(title = "Individuele keuzes binnen productregels invullen (stap 1 van 3)", menu = "Groep > [groep] > Bewerken > Deelnemer toevoegen")
@InPrincipal(DeelnemersOnderwijsproductenKeuzeAangeven.class)
public class OndPrKeuzeAangevenStap1Page extends AbstractDeelnemerSelectiePage<Verbintenis>
		implements IEditPage
{
	private static final long serialVersionUID = 1L;

	private static final VerbintenisZoekFilter getDefaultFilter()
	{
		VerbintenisZoekFilter filter = new VerbintenisZoekFilter();
		filter.setCohort(Cohort.getHuidigCohort());
		filter.setVerbintenisStatusOngelijkAan(Arrays.asList(VerbintenisStatus.Intake,
			VerbintenisStatus.Beeindigd, VerbintenisStatus.Afgemeld, VerbintenisStatus.Afgewezen));
		filter.setOpleidingEnCohortVerplicht(true);
		filter.addOrderByProperty("deelnemer.deelnemernummer");
		filter.addOrderByProperty("persoon.roepnaam");
		filter.addOrderByProperty("persoon.achternaam");

		return filter;
	}

	public OndPrKeuzeAangevenStap1Page()
	{
		this(getDefaultFilter());
	}

	private OndPrKeuzeAangevenStap1Page(VerbintenisZoekFilter filter)
	{
		super(DeelnemerZoekenPage.class, new DeelnemerCollectiefPageContext(
			"Individuele keuzes binnen productregels invullen",
			DeelnemerCollectiefMenuItem.KeuzesAangeven), filter,
			new HibernateSelection<Verbintenis>(Verbintenis.class),
			new OndPrKeuzeAangevenStap1Target());
	}

	@Override
	protected AbstractSelectiePanel<Verbintenis, Verbintenis, VerbintenisZoekFilter> createSelectiePanel(
			String id, VerbintenisZoekFilter filter, Selection<Verbintenis, Verbintenis> selection)
	{
		return new DeelnemerCohortSelectiePanel<Verbintenis>(id, filter,
			(DatabaseSelection<Verbintenis, Verbintenis>) selection, true);
	}

	@Override
	public int getMaxResults()
	{
		return Integer.MAX_VALUE;
	}
}
