package nl.topicus.eduarte.web.components.modalwindow.taxonomie;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.zoekfilters.TaxonomieElementZoekFilter;

import org.apache.wicket.model.IModel;

public class TaxonomieElementSelectieModalWindow extends
		AbstractZoekenModalWindow<TaxonomieElement>
{
	private static final long serialVersionUID = 1L;

	private TaxonomieElementZoekFilter filter;

	public TaxonomieElementSelectieModalWindow(String id, TaxonomieElementZoekFilter filter)
	{
		this(id, null, filter);
	}

	public TaxonomieElementSelectieModalWindow(String id, IModel<TaxonomieElement> model,
			TaxonomieElementZoekFilter filter)
	{
		super(id, model, filter);
		this.filter = filter;
		setTitle("Verbintenisgebied selecteren");
	}

	@Override
	protected CobraModalWindowBasePanel<TaxonomieElement> createContents(String id)
	{
		return new TaxonomieElementSelectiePanel(id, this, filter);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}
}
