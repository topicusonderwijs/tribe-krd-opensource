package nl.topicus.eduarte.web.components.quicksearch.voorvoegsel;

import nl.topicus.cobra.web.components.quicksearch.AbstractBaseSearchEditor;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.entities.landelijk.Voorvoegsel;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.web.components.modalwindow.voorvoegsel.VoorvoegselSelectieModalWindow;
import nl.topicus.eduarte.zoekfilters.VoorvoegselZoekFilter;

import org.apache.wicket.model.IModel;

public class VoorvoegselSearchEditor extends AbstractBaseSearchEditor<Voorvoegsel, String>
{
	private static final long serialVersionUID = 1L;

	private static final VoorvoegselZoekFilter getDefaultFilter()
	{
		VoorvoegselZoekFilter filter = new VoorvoegselZoekFilter();
		filter.addOrderByProperty("naam");
		return filter;
	}

	private VoorvoegselZoekFilter filter;

	public VoorvoegselSearchEditor(String id, IModel<String> model)
	{
		this(id, model, getDefaultFilter());
	}

	public VoorvoegselSearchEditor(String id, IModel<String> model, VoorvoegselZoekFilter filter)
	{
		super(id, model);
		this.filter = filter;
	}

	@Override
	public AbstractZoekenModalWindow<Voorvoegsel> createModelWindow(String id, IModel<String> model)
	{
		return new VoorvoegselSelectieModalWindow(id, new WrappedModel(model), filter);
	}

	@Override
	public QuickSearchField<Voorvoegsel> createSearchField(String id, IModel<String> model)
	{
		return new VoorvoegselQuickSearchField(id, new WrappedModel(model),
			new ZoekFilterCopyManager().copyObject(filter));
	}

	@Override
	protected String convertTToS(Voorvoegsel search)
	{
		return search == null ? null : search.toString();
	}
}
