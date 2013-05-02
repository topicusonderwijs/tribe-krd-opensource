package nl.topicus.eduarte.web.components.modalwindow.stagemarktbedrijfsgegevens;

import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.web.components.factory.StagemarktBedrijfsgegevens;
import nl.topicus.eduarte.web.components.factory.StagemarktBedrijfsgegevensZoekFilter;

import org.apache.wicket.model.IModel;

public class StagemarktBedrijfsgegevensModalWindow extends
		CobraModalWindow<StagemarktBedrijfsgegevens>
{
	private static final long serialVersionUID = 1L;

	private StagemarktBedrijfsgegevensZoekFilter filter;

	public StagemarktBedrijfsgegevensModalWindow(String id,
			IModel<StagemarktBedrijfsgegevens> model, StagemarktBedrijfsgegevensZoekFilter filter)
	{
		this(id, model);
		this.filter = filter;
	}

	public StagemarktBedrijfsgegevensModalWindow(String id, IModel<StagemarktBedrijfsgegevens> model)
	{
		super(id, model);
		setTitle("Bedrijfsgegevens selecteren");
	}

	@Override
	protected CobraModalWindowBasePanel<StagemarktBedrijfsgegevens> createContents(String id)
	{
		return new StagemarktBedrijfsgegevensPanel(id, this, filter);
	}
}
