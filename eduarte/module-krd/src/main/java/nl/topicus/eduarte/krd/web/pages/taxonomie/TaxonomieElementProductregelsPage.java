package nl.topicus.eduarte.krd.web.pages.taxonomie;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.security.RechtenSoorten;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.eduarte.app.EduArteSession;
import nl.topicus.eduarte.core.principals.onderwijs.taxonomie.TaxonomieElementRead;
import nl.topicus.eduarte.dao.helpers.ProductregelDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;
import nl.topicus.eduarte.krd.web.components.panels.filter.ProductregelZoekFilterPanel;
import nl.topicus.eduarte.web.components.menu.TaxonomieElementMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.ProductregelTable;
import nl.topicus.eduarte.web.pages.onderwijs.taxonomie.AbstractTaxonomieElementPage;
import nl.topicus.eduarte.zoekfilters.ProductregelZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * Pagina met de productregels van een verbintenisgebied.
 * 
 * @author loite
 */
@PageInfo(title = "Productregels", menu = {"Onderwijs > Taxonomie > [taxonomie-element] > Productregels"})
@InPrincipal(TaxonomieElementRead.class)
@RechtenSoorten( {RechtenSoort.INSTELLING, RechtenSoort.BEHEER})
public class TaxonomieElementProductregelsPage extends AbstractTaxonomieElementPage
{
	private static final long serialVersionUID = 1L;

	private final ProductregelZoekFilterPanel filterPanel;

	private static final ProductregelZoekFilter getDefaultFilter(
			Verbintenisgebied verbintenisgebied, IModel<Cohort> cohortModel)
	{
		ProductregelZoekFilter filter = new ProductregelZoekFilter(verbintenisgebied, cohortModel);
		return filter;
	}

	public TaxonomieElementProductregelsPage(TaxonomieElement verbintenisgebied)
	{
		this(verbintenisgebied, getDefaultFilter((Verbintenisgebied) verbintenisgebied,
			EduArteSession.get().getSelectedCohortModel()));
	}

	/**
	 * Constructor
	 * 
	 * @param verbintenisgebied
	 */
	public TaxonomieElementProductregelsPage(TaxonomieElement verbintenisgebied,
			ProductregelZoekFilter filter)
	{
		super(TaxonomieElementMenuItem.Productregels, ModelFactory
			.getCompoundModel(verbintenisgebied));
		GeneralDataProvider<Productregel, ProductregelZoekFilter> provider =
			GeneralDataProvider.of(filter,  ProductregelDataAccessHelper.class);
		CustomDataPanel<Productregel> datapanel =
			new EduArteDataPanel<Productregel>("datapanel", provider, ProductregelTable
				.createVerbintenisgebiedProductregelTable());
		datapanel.setItemsPerPage(Integer.MAX_VALUE);
		add(datapanel);
		filterPanel = new ProductregelZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);
		createComponents();
	}

}
