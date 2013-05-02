package nl.topicus.eduarte.web.components.modalwindow.medewerker;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.zoekfilters.MedewerkerZoekFilter;

import org.apache.wicket.model.IModel;

public class MedewerkerSelectieModalWindow extends AbstractZoekenModalWindow<Medewerker>
{
	private static final long serialVersionUID = 1L;

	private MedewerkerZoekFilter filter;

	private boolean lockHeeftAccount;

	public MedewerkerSelectieModalWindow(String id)
	{
		this(id, null, null, false);
	}

	public MedewerkerSelectieModalWindow(String id, IModel<Medewerker> model)
	{
		this(id, model, null, false);
	}

	public MedewerkerSelectieModalWindow(String id, IModel<Medewerker> model,
			MedewerkerZoekFilter filter, boolean lockHeeftAccount)
	{
		super(id, model, filter);
		this.filter = filter;
		filter.addOrderByProperty("persoon.achternaam");
		filter.addOrderByProperty("persoon.roepnaam");
		this.lockHeeftAccount = lockHeeftAccount;
		setTitle("Medewerker selecteren");
	}

	@Override
	protected CobraModalWindowBasePanel<Medewerker> createContents(String id)
	{
		return new MedewerkerSelectiePanel(id, this, filter, lockHeeftAccount);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}
}
