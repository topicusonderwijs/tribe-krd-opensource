package nl.topicus.eduarte.web.components.modalwindow.externeorganisatie.contactpersoon;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoon;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.zoekfilters.ExterneOrganisatieContactPersoonZoekFilter;

import org.apache.wicket.model.IModel;

public class ExterneOrganisatieContactPersoonSelectieModalWindow extends
		AbstractZoekenModalWindow<ExterneOrganisatieContactPersoon>
{
	private static final long serialVersionUID = 1L;

	private ExterneOrganisatieContactPersoonZoekFilter filter;

	public ExterneOrganisatieContactPersoonSelectieModalWindow(String id,
			ExterneOrganisatieContactPersoonZoekFilter filter)
	{
		this(id, null, filter);
	}

	public ExterneOrganisatieContactPersoonSelectieModalWindow(String id,
			IModel<ExterneOrganisatieContactPersoon> model,
			ExterneOrganisatieContactPersoonZoekFilter filter)
	{
		super(id, model, filter);

		if (filter == null)
			throw new IllegalArgumentException("filter is null");

		this.filter = filter;
		setTitle("Contactpersoon selecteren");
	}

	@Override
	protected CobraModalWindowBasePanel<ExterneOrganisatieContactPersoon> createContents(String id)
	{
		return new ExterneOrganisatieContactPersoonSelectiePanel(id, this, filter);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}
}
