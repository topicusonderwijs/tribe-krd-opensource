package nl.topicus.eduarte.web.components.quicksearch.plaats;

import nl.topicus.cobra.web.components.quicksearch.AbstractBaseSearchEditor;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.landelijk.Plaats;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.web.components.modalwindow.plaats.PlaatsSelectieModalWindow;
import nl.topicus.eduarte.zoekfilters.PlaatsZoekFilter;

import org.apache.wicket.model.IModel;

public class PlaatsSearchEditor extends AbstractBaseSearchEditor<Plaats, String>
{
	private static final long serialVersionUID = 1L;

	private IModel<Land> landModel;

	private static final PlaatsZoekFilter getDefaultFilter()
	{
		PlaatsZoekFilter filter = new PlaatsZoekFilter();
		filter.addOrderByProperty("sorteerNaam");
		return filter;
	}

	private PlaatsZoekFilter filter;

	public PlaatsSearchEditor(String id, IModel<String> model)
	{
		this(id, model, null, getDefaultFilter());
	}

	public PlaatsSearchEditor(String id, IModel<String> model, IModel<Land> landModel)
	{
		this(id, model, landModel, getDefaultFilter());
	}

	public PlaatsSearchEditor(String id, IModel<String> model, PlaatsZoekFilter filter)
	{
		this(id, model, null, filter);
	}

	public PlaatsSearchEditor(String id, IModel<String> model, IModel<Land> landModel,
			PlaatsZoekFilter filter)
	{
		super(id, model);
		this.filter = filter;
		this.landModel = landModel;
	}

	@Override
	public AbstractZoekenModalWindow<Plaats> createModelWindow(String id, IModel<String> model)
	{
		return new PlaatsSelectieModalWindow(id, new WrappedModel(model, landModel), filter);
	}

	@Override
	public QuickSearchField<Plaats> createSearchField(String id, IModel<String> model)
	{
		PlaatsQuickSearchField searchField =
			new PlaatsQuickSearchField(id, new WrappedModel(model, landModel), landModel,
				new ZoekFilterCopyManager().copyObject(filter));

		// als landmodel gezet, dan geen zoekresultaten cachen ==> land kan gewijzigd zijn
		if (landModel != null)
			searchField.getOptions().put("cacheLength", 0);

		return searchField;
	}

	private boolean isNederland()
	{
		if (landModel == null)
			return true;

		Land land = landModel.getObject();
		return land != null && land.isNederland();
	}

	@Override
	protected boolean uitgebreidZoekenIsVisible()
	{
		return super.uitgebreidZoekenIsVisible() && isNederland();
	}

	@Override
	protected String convertTToS(Plaats search)
	{
		return search == null ? null : search.toString();
	}
}
