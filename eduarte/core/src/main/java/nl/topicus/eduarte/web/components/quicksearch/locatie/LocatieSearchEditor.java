package nl.topicus.eduarte.web.components.quicksearch.locatie;

import nl.topicus.cobra.web.components.quicksearch.AbstractSearchEditor;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.web.components.modalwindow.locatie.LocatieSelectieModalWindow;
import nl.topicus.eduarte.zoekfilters.LocatieZoekFilter;

import org.apache.wicket.model.IModel;

public class LocatieSearchEditor extends AbstractSearchEditor<Locatie>
{
	private static final long serialVersionUID = 1L;

	private LocatieZoekFilter filter;

	public LocatieSearchEditor(String id, IModel<Locatie> model)
	{
		this(id, model, new LocatieZoekFilter());
	}

	public LocatieSearchEditor(String id, IModel<Locatie> model, LocatieZoekFilter filter)
	{
		super(id, model);
		this.filter = filter;
	}

	@Override
	public AbstractZoekenModalWindow<Locatie> createModelWindow(String id, IModel<Locatie> model)
	{
		return new LocatieSelectieModalWindow(id, model, filter);
	}

	@Override
	public QuickSearchField<Locatie> createSearchField(String id, IModel<Locatie> model)
	{
		return new LocatieQuickSearchField(id, model, new ZoekFilterCopyManager()
			.copyObject(filter));
	}
}
