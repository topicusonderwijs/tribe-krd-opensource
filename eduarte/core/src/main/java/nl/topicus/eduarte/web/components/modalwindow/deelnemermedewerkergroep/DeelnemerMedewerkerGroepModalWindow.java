package nl.topicus.eduarte.web.components.modalwindow.deelnemermedewerkergroep;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.participatie.DeelnemerMedewerkerGroep;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.zoekfilters.DeelnemerMedewerkerGroepZoekFilter;

import org.apache.wicket.model.IModel;

public class DeelnemerMedewerkerGroepModalWindow extends
		AbstractZoekenModalWindow<DeelnemerMedewerkerGroep>
{
	private static final long serialVersionUID = 1L;

	private DeelnemerMedewerkerGroepZoekFilter filter;

	public DeelnemerMedewerkerGroepModalWindow(String id)
	{
		this(id, null, null);
	}

	public DeelnemerMedewerkerGroepModalWindow(String id, IModel<DeelnemerMedewerkerGroep> model)
	{
		this(id, model, null);
	}

	public DeelnemerMedewerkerGroepModalWindow(String id, IModel<DeelnemerMedewerkerGroep> model,
			DeelnemerMedewerkerGroepZoekFilter filter)
	{
		super(id, model, filter);
		this.filter = filter;
		setTitle("Deelnemer/Groep/Medewerker selecteren");
	}

	@Override
	protected CobraModalWindowBasePanel<DeelnemerMedewerkerGroep> createContents(String id)
	{
		return new DeelnemerMedewerkerGroepSelectiePanel(id, this, filter);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}
}
