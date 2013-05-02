package nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.CollectionDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.GroupProperty;
import nl.topicus.eduarte.app.EduArteSession;
import nl.topicus.eduarte.core.principals.onderwijs.OpleidingInzien;
import nl.topicus.eduarte.dao.helpers.ProductregelDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ToetsDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.krd.web.components.panels.filter.ProductregelZoekFilterPanel;
import nl.topicus.eduarte.providers.OpleidingProvider;
import nl.topicus.eduarte.web.components.menu.OpleidingMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.ToetsTable;
import nl.topicus.eduarte.web.pages.onderwijs.opleiding.AbstractOpleidingPage;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.ProductregelZoekFilter;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurZoekFilter;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Pagina die de resultaatstructuren van alle onderwijsproducten die aan deze opleiding
 * zijn gekoppeld toont.
 * 
 * @author loite
 */
@PageInfo(title = "Resultaatstructuren bij opleiding", menu = {"Onderwijs > [opleiding] > Resultaatstructuren"})
@InPrincipal(OpleidingInzien.class)
public class OpleidingResultaatstructurenPage extends AbstractOpleidingPage
{
	private static final long serialVersionUID = 1L;

	private final ProductregelZoekFilterPanel filterPanel;

	private final ToetsenModel toetsenModel;

	private static final ProductregelZoekFilter getDefaultFilter(Opleiding opleiding,
			IModel<Cohort> cohortModel)
	{
		ProductregelZoekFilter filter = new ProductregelZoekFilter(opleiding, cohortModel);
		return filter;
	}

	private final class ToetsenModel extends LoadableDetachableModel<List<Toets>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<Toets> load()
		{
			List<Toets> res = new ArrayList<Toets>();
			List<Productregel> productregels =
				DataAccessRegistry.getHelper(ProductregelDataAccessHelper.class).list(
					filterPanel.getZoekfilter());

			List<Onderwijsproduct> uniqueOnderwijsproducten = new ArrayList<Onderwijsproduct>();

			for (Productregel productregel : productregels)
			{
				for (Onderwijsproduct product : productregel
					.getOnderwijsproducten(getContextOpleiding()))
				{
					if (!uniqueOnderwijsproducten.contains(product))
					{
						uniqueOnderwijsproducten.add(product);

						ResultaatstructuurZoekFilter resultaatstructuurFilter =
							new ResultaatstructuurZoekFilter(product);
						resultaatstructuurFilter
							.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
								OpleidingResultaatstructurenPage.this));
						resultaatstructuurFilter.setCohort(productregel.getCohort());
						ToetsZoekFilter ret = new ToetsZoekFilter(resultaatstructuurFilter);
						ret.addOrderByProperty("boom");
						List<Toets> toetsen =
							DataAccessRegistry.getHelper(ToetsDataAccessHelper.class).list(ret);
						res.addAll(toetsen);
					}
				}
			}
			return res;
		}

	}

	public OpleidingResultaatstructurenPage(OpleidingProvider provider)
	{
		this(provider.getOpleiding());
	}

	public OpleidingResultaatstructurenPage(Opleiding opleiding)
	{
		this(opleiding, getDefaultFilter(opleiding, EduArteSession.get().getSelectedCohortModel()));
	}

	public OpleidingResultaatstructurenPage(Opleiding opleiding, ProductregelZoekFilter filter)
	{
		super(OpleidingMenuItem.Resultaatstructuren, opleiding);

		toetsenModel = new ToetsenModel();
		CollectionDataProvider<Toets> provider = new CollectionDataProvider<Toets>(toetsenModel);

		ToetsTable table =
			ToetsTable
				.getToetstableVoorMeerdereOnderwijsproducten(new AbstractReadOnlyModel<Integer>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public Integer getObject()
					{
						List<Toets> toetsen = toetsenModel.getObject();
						if (toetsen.isEmpty())
							return 1;
						int maxDepth = 1;
						for (Toets toets : toetsen)
						{
							int depth = toets.getResultaatstructuur().getDepth();
							if (depth > maxDepth)
								maxDepth = depth;
						}
						return maxDepth;
					}
				});
		table.setDefaultGroupProperty(new GroupProperty<Toets>(
			"resultaatstructuur.onderwijsproduct.codeAndTitle", "Onderwijsproduct",
			"resultaatstructuur.onderwijsproduct"));
		CustomDataPanel<Toets> datapanel =
			new EduArteDataPanel<Toets>("datapanel", provider, table);
		datapanel.setItemsPerPage(Integer.MAX_VALUE);
		add(datapanel);

		filterPanel = new ProductregelZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);
		createComponents();
	}

}
