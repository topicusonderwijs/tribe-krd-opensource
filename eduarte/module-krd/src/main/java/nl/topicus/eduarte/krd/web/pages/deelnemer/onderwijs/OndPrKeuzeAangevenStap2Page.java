package nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.hibernate.HibernateSelection;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.krd.principals.deelnemer.onderwijsproduct.DeelnemersOnderwijsproductenKeuzeAangeven;
import nl.topicus.eduarte.krd.web.pages.shared.AbstractProductregelSelectiePage;
import nl.topicus.eduarte.zoekfilters.ProductregelZoekFilter;

/**
 * @author vandekamp
 */
@PageInfo(title = "Individuele keuzes binnen productregels invullen (stap 2 van 3)", menu = {"Deelnemer > Onderwijsproducten > Keuzes aangeven > [deelnemers] > Verder"})
@InPrincipal(DeelnemersOnderwijsproductenKeuzeAangeven.class)
public class OndPrKeuzeAangevenStap2Page extends AbstractProductregelSelectiePage implements
		IEditPage
{
	private static final long serialVersionUID = 1L;

	public OndPrKeuzeAangevenStap2Page(List<Verbintenis> selectedVerbintenissen,
			OndPrKeuzeAangevenStap1Page returnPage)
	{
		this(getDefaultFilter(selectedVerbintenissen.get(0)), returnPage);
	}

	private static ProductregelZoekFilter getDefaultFilter(Verbintenis eersteVerbintenis)
	{
		return new ProductregelZoekFilter(eersteVerbintenis.getOpleiding(), eersteVerbintenis
			.getCohort());
	}

	private OndPrKeuzeAangevenStap2Page(ProductregelZoekFilter filter,
			OndPrKeuzeAangevenStap1Page returnPage)
	{
		super(returnPage, filter, new HibernateSelection<Productregel>(Productregel.class),
			new OndPrKeuzeAangevenStap2Target(returnPage));
	}

	@Override
	public int getMaxResults()
	{
		return Integer.MAX_VALUE;
	}
}
