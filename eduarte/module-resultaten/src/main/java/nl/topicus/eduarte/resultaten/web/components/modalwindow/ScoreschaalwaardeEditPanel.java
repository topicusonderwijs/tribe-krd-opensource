package nl.topicus.eduarte.resultaten.web.components.modalwindow;

import java.util.List;

import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.resultaatstructuur.Scoreschaalwaarde;
import nl.topicus.eduarte.web.components.modalwindow.AbstractEduArteToevoegenBewerkenPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.ScoreschaalwaardeTable;

import org.apache.wicket.model.IModel;

public abstract class ScoreschaalwaardeEditPanel extends
		AbstractEduArteToevoegenBewerkenPanel<Scoreschaalwaarde>
{
	private static final long serialVersionUID = 1L;

	public ScoreschaalwaardeEditPanel(String id, IModel<List<Scoreschaalwaarde>> model,
			ModelManager manager)
	{
		super(id, model, manager, new ScoreschaalwaardeTable());
	}

	@Override
	public AbstractToevoegenBewerkenModalWindowPanel<Scoreschaalwaarde> createModalWindowPanel(
			String id, AbstractToevoegenBewerkenModalWindow<Scoreschaalwaarde> modalWindow)
	{
		modalWindow.setInitialHeight(250);
		modalWindow.setInitialWidth(450);
		return new ScoreschaalwaardeModalWindowPanel(id, modalWindow, this);
	}

	@Override
	public String getToevoegenLabel()
	{
		return "Schaalwaarde toevoegen";
	}

	@Override
	public String getModalWindowTitle()
	{
		return "Schaalwaarde";
	}
}
