package nl.topicus.eduarte.web.components.modalwindow.deelnemer;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * @author Henzen
 */
public class DeelnemerSelectieModalWindow extends AbstractZoekenModalWindow<Verbintenis>
{
	private static final long serialVersionUID = 1L;

	private VerbintenisZoekFilter filter;

	public DeelnemerSelectieModalWindow(String id)
	{
		this(id, null, null);
	}

	public DeelnemerSelectieModalWindow(String id, IModel<Verbintenis> model)
	{
		this(id, model, null);
	}

	public DeelnemerSelectieModalWindow(String id, IModel<Verbintenis> model,
			VerbintenisZoekFilter filter)
	{
		super(id, model, filter);
		this.filter = filter;
		setTitle("Deelnemer selecteren");
	}

	@Override
	protected CobraModalWindowBasePanel<Verbintenis> createContents(String id)
	{
		return new DeelnemerSelectiePanel(id, this, filter);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}
}
