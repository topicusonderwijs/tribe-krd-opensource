package nl.topicus.eduarte.krdparticipatie.web.pages.beheer.lesweek;

import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.participatie.LesdagIndeling;

import org.apache.wicket.model.IModel;

public class LesuurindelinEditModalWindow extends CobraModalWindow<LesdagIndeling>
{
	private static final long serialVersionUID = 1L;

	public LesuurindelinEditModalWindow(String id)
	{
		this(id, null);
	}

	public LesuurindelinEditModalWindow(String id, IModel<LesdagIndeling> model)
	{
		super(id, model);
		setTitle("Lesuren bewerken");
	}

	@Override
	protected CobraModalWindowBasePanel<LesdagIndeling> createContents(String id)
	{
		return new LesuurIndelingEditPanel(id, this);
	}
}
