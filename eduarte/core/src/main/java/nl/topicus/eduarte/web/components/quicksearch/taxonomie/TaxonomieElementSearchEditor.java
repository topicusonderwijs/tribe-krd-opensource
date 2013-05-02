package nl.topicus.eduarte.web.components.quicksearch.taxonomie;

import nl.topicus.cobra.web.components.quicksearch.AbstractSearchEditor;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.web.components.modalwindow.taxonomie.TaxonomieElementSelectieModalWindow;
import nl.topicus.eduarte.zoekfilters.TaxonomieElementZoekFilter;

import org.apache.wicket.model.IModel;

public class TaxonomieElementSearchEditor extends AbstractSearchEditor<TaxonomieElement>
{
	private static final long serialVersionUID = 1L;

	private TaxonomieElementZoekFilter filter;

	public TaxonomieElementSearchEditor(String id, TaxonomieElementZoekFilter filter)
	{
		this(id, null, filter);
	}

	public TaxonomieElementSearchEditor(String id, IModel<TaxonomieElement> model,
			TaxonomieElementZoekFilter filter)
	{
		super(id, model);
		this.filter = filter;
	}

	@Override
	public AbstractZoekenModalWindow<TaxonomieElement> createModelWindow(String id,
			IModel<TaxonomieElement> model)
	{
		return new TaxonomieElementSelectieModalWindow(id, model, filter);
	}

	@Override
	public QuickSearchField<TaxonomieElement> createSearchField(String id,
			IModel<TaxonomieElement> model)
	{
		return new TaxonomieElementQuickSearchField(id, model, new ZoekFilterCopyManager()
			.copyObject(filter));
	}
}
