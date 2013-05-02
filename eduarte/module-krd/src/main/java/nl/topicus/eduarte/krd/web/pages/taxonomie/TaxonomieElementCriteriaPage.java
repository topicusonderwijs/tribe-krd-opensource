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
import nl.topicus.eduarte.dao.helpers.CriteriumDataAccessHelper;
import nl.topicus.eduarte.entities.criteriumbank.Criterium;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;
import nl.topicus.eduarte.krd.web.components.panels.filter.CriteriumZoekFilterPanel;
import nl.topicus.eduarte.web.components.menu.TaxonomieElementMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.CriteriumTable;
import nl.topicus.eduarte.web.pages.onderwijs.taxonomie.AbstractTaxonomieElementPage;
import nl.topicus.eduarte.zoekfilters.CriteriumZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * Pagina met de criteria van een verbintenisgebied.
 * 
 * @author loite
 */
@PageInfo(title = "Criteria", menu = {"Onderwijs > Taxonomie > [taxonomie-element] > Criteria"})
@InPrincipal(TaxonomieElementRead.class)
@RechtenSoorten( {RechtenSoort.INSTELLING, RechtenSoort.BEHEER})
public class TaxonomieElementCriteriaPage extends AbstractTaxonomieElementPage
{
	private static final long serialVersionUID = 1L;

	private final CriteriumZoekFilterPanel filterPanel;

	private static final CriteriumZoekFilter getDefaultFilter(Verbintenisgebied verbintenisgebied,
			IModel<Cohort> cohortModel)
	{
		CriteriumZoekFilter filter = new CriteriumZoekFilter(verbintenisgebied, cohortModel);
		return filter;
	}

	public TaxonomieElementCriteriaPage(TaxonomieElement verbintenisgebied)
	{
		this(verbintenisgebied, getDefaultFilter((Verbintenisgebied) verbintenisgebied,
			EduArteSession.get().getSelectedCohortModel()));
	}

	public TaxonomieElementCriteriaPage(TaxonomieElement verbintenisgebied,
			CriteriumZoekFilter filter)
	{
		super(TaxonomieElementMenuItem.Criteria, ModelFactory.getCompoundModel(verbintenisgebied));
		GeneralDataProvider<Criterium, CriteriumZoekFilter> provider =
			GeneralDataProvider.of(filter,  CriteriumDataAccessHelper.class);
		CustomDataPanel<Criterium> datapanel =
			new EduArteDataPanel<Criterium>("datapanel", provider, new CriteriumTable());
		datapanel.setItemsPerPage(Integer.MAX_VALUE);
		add(datapanel);
		filterPanel = new CriteriumZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);
		createComponents();
	}

}
