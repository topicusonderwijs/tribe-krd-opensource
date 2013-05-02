package nl.topicus.eduarte.web.components.modalwindow.groep;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.zoekfilters.GroepZoekFilter;

import org.apache.wicket.model.IModel;

public class GroepSelectieModalWindow extends AbstractZoekenModalWindow<Groep>
{
	private static final long serialVersionUID = 1L;

	private GroepZoekFilter filter;

	public GroepSelectieModalWindow(String id, IModel<Groep> model, GroepZoekFilter filter)
	{
		super(id, model, filter);
		this.filter = filter;
		setTitle("Groep selecteren");
	}

	@Override
	protected CobraModalWindowBasePanel<Groep> createContents(String id)
	{
		return new GroepSelectiePanel(id, this, filter);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}
}
