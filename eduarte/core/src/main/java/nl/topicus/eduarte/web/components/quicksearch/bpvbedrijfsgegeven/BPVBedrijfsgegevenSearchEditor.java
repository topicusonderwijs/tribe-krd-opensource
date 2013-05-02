package nl.topicus.eduarte.web.components.quicksearch.bpvbedrijfsgegeven;

import nl.topicus.cobra.web.components.quicksearch.AbstractSearchEditor;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.entities.bpv.BPVBedrijfsgegeven;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.web.components.modalwindow.externeorganisatie.BPVBedrijfsgegevenSelectieModalWindow;
import nl.topicus.eduarte.zoekfilters.BPVBedrijfsgegevenZoekFilter;

import org.apache.wicket.model.IModel;

public class BPVBedrijfsgegevenSearchEditor extends AbstractSearchEditor<BPVBedrijfsgegeven>
{

	private static final long serialVersionUID = 1L;

	private static BPVBedrijfsgegevenZoekFilter getDefaultFilter()
	{
		BPVBedrijfsgegevenZoekFilter filter = new BPVBedrijfsgegevenZoekFilter();
		filter.addOrderByProperty("externeOrganisatie.naam");
		return filter;
	}

	private BPVBedrijfsgegevenZoekFilter filter;

	public BPVBedrijfsgegevenSearchEditor(String id, IModel<BPVBedrijfsgegeven> model)
	{
		this(id, model, getDefaultFilter());
	}

	public BPVBedrijfsgegevenSearchEditor(String id, IModel<BPVBedrijfsgegeven> model,
			BPVBedrijfsgegevenZoekFilter filter)
	{
		super(id, model);
		this.filter = filter;
	}

	@Override
	public AbstractZoekenModalWindow<BPVBedrijfsgegeven> createModelWindow(String id,
			IModel<BPVBedrijfsgegeven> model)
	{
		return new BPVBedrijfsgegevenSelectieModalWindow(id, model, filter);
	}

	@Override
	public QuickSearchField<BPVBedrijfsgegeven> createSearchField(String id,
			IModel<BPVBedrijfsgegeven> model)
	{
		return new BPVBedrijfsgegevenQuickSearchField(id, model, new ZoekFilterCopyManager()
			.copyObject(filter));
	}

	@Override
	public void detachModels()
	{
		filter.detach();
	}
}
