package nl.topicus.eduarte.resultaten.web.pages.onderwijs;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.dao.hibernate.HibernateSelection;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.selection.AbstractSelectiePanel;
import nl.topicus.eduarte.app.EduArteSession;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.resultaten.principals.onderwijs.OnderwijsproductResultaatstructuurKopieren;
import nl.topicus.eduarte.web.components.panels.datapanel.selectie.OnderwijsproductSelectiePanel;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectiePage;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * @author papegaaij
 */
@PageInfo(title = "Resultaatstructuren kopieren stap 1", menu = "Onderwijs > Onderwijsproducten > Kopiëren resultaatstructuren")
@InPrincipal(OnderwijsproductResultaatstructuurKopieren.class)
public class ResultaatstructurenKopierenStap1Page extends
		AbstractSelectiePage<Onderwijsproduct, Onderwijsproduct, OnderwijsproductZoekFilter>
		implements IModuleEditPage<Resultaatstructuur>
{
	private static final long serialVersionUID = 1L;

	private IModel<Resultaatstructuur> bronstructuurModel = new Model<Resultaatstructuur>();

	private final static OnderwijsproductZoekFilter getDefaultFilter(Cohort cohort)
	{
		OnderwijsproductZoekFilter ret = new OnderwijsproductZoekFilter();
		ret.addOrderByProperty("titel");
		ret.setCohort(cohort);
		return ret;
	}

	public ResultaatstructurenKopierenStap1Page(Resultaatstructuur bronStructuur,
			SecurePage returnPage)
	{
		this(returnPage, getDefaultFilter(bronStructuur.getCohort()));
		bronstructuurModel = ModelFactory.getModel(bronStructuur);
	}

	public ResultaatstructurenKopierenStap1Page(ResultaatstructurenKopierenOverzichtPage returnPage)
	{
		this(returnPage,
			getDefaultFilter(EduArteSession.get().getSelectedCohortModel().getObject()));
	}

	public ResultaatstructurenKopierenStap1Page(SecurePage returnPage,
			OnderwijsproductZoekFilter filter)
	{
		super(returnPage, filter, new HibernateSelection<Onderwijsproduct>(Onderwijsproduct.class),
			new ResultaatstructurenKopierenStap2Target(returnPage));
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(bronstructuurModel);
	}

	@Override
	protected AbstractSelectiePanel<Onderwijsproduct, Onderwijsproduct, OnderwijsproductZoekFilter> createSelectiePanel(
			String id, OnderwijsproductZoekFilter filter,
			Selection<Onderwijsproduct, Onderwijsproduct> selection)
	{
		return new OnderwijsproductSelectiePanel(id, filter,
			(DatabaseSelection<Onderwijsproduct, Onderwijsproduct>) selection);
	}

	public Resultaatstructuur getBronStructuur()
	{
		return bronstructuurModel.getObject();
	}

	@Override
	public int getMaxResults()
	{
		return Integer.MAX_VALUE;
	}
}
