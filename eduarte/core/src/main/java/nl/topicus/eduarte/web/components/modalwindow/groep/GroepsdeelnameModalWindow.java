package nl.topicus.eduarte.web.components.modalwindow.groep;

import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;

import org.apache.wicket.model.IModel;

public class GroepsdeelnameModalWindow extends CobraModalWindow<Groepsdeelname>
{
	private static final long serialVersionUID = 1L;

	public GroepsdeelnameModalWindow(String id, IModel<Groepsdeelname> model)
	{
		super(id, model);
		setTitle("Groepsdeelname");
		setInitialHeight(200);
		setInitialWidth(500);
		setResizable(false);
	}

	@Override
	protected CobraModalWindowBasePanel<Groepsdeelname> createContents(String id)
	{
		return new GroepsdeelnameModalWindowPanel(id, this);
	}
}
