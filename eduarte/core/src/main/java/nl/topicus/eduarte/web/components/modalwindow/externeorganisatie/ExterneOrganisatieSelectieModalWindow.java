package nl.topicus.eduarte.web.components.modalwindow.externeorganisatie;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.zoekfilters.ExterneOrganisatieZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * Modal window voor het selecteren van een ExterneOrganisatie.
 * 
 * @author hoeve
 */
public class ExterneOrganisatieSelectieModalWindow extends
		AbstractZoekenModalWindow<ExterneOrganisatie>
{
	private static final long serialVersionUID = 1L;

	private ExterneOrganisatieZoekFilter filter;

	public ExterneOrganisatieSelectieModalWindow(String id)
	{
		this(id, null, null);
	}

	public ExterneOrganisatieSelectieModalWindow(String id, IModel<ExterneOrganisatie> model)
	{
		this(id, model, null);
	}

	public ExterneOrganisatieSelectieModalWindow(String id, IModel<ExterneOrganisatie> model,
			ExterneOrganisatieZoekFilter filter)
	{
		super(id, model, filter);
		this.filter = filter;
		filter.addOrderByProperty("naam");
		setTitle("Externe organisatie selecteren");
	}

	@Override
	protected CobraModalWindowBasePanel<ExterneOrganisatie> createContents(String id)
	{
		return new ExterneOrganisatieSelectiePanel(id, this, filter);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}
}
