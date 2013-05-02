package nl.topicus.eduarte.web.components.quicksearch.soortvooropleiding;

import nl.topicus.cobra.web.components.quicksearch.AbstractSearchEditor;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.web.components.modalwindow.soortvooropleiding.SoortVooropleidingSelectieModalWindow;
import nl.topicus.eduarte.zoekfilters.SoortVooropleidingZoekFilter;

import org.apache.wicket.model.IModel;

public class SoortVooropleidingSearchEditor extends AbstractSearchEditor<SoortVooropleiding>
{
	private static final long serialVersionUID = 1L;

	private SoortVooropleidingZoekFilter filter;

	public SoortVooropleidingSearchEditor(String id)
	{
		this(id, null, new SoortVooropleidingZoekFilter());
	}

	public SoortVooropleidingSearchEditor(String id, IModel<SoortVooropleiding> model)
	{
		this(id, model, new SoortVooropleidingZoekFilter());
	}

	public SoortVooropleidingSearchEditor(String id, IModel<SoortVooropleiding> model,
			SoortVooropleidingZoekFilter filter)
	{
		super(id, model);
		this.filter = filter;
	}

	@Override
	public AbstractZoekenModalWindow<SoortVooropleiding> createModelWindow(String id,
			IModel<SoortVooropleiding> model)
	{
		return new SoortVooropleidingSelectieModalWindow(id, model, filter);
	}

	@Override
	public QuickSearchField<SoortVooropleiding> createSearchField(String id,
			IModel<SoortVooropleiding> model)
	{
		return new SoortVooropleidingSearchField(id, model, new ZoekFilterCopyManager()
			.copyObject(filter));
	}
}
