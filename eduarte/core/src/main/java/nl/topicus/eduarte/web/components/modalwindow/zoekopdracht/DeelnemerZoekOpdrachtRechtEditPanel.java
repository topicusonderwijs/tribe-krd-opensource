package nl.topicus.eduarte.web.components.modalwindow.zoekopdracht;

import java.util.List;

import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.eduarte.entities.rapportage.DeelnemerZoekOpdrachtRecht;
import nl.topicus.eduarte.web.components.modalwindow.AbstractEduArteToevoegenBewerkenPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.DeelnemerZoekOpdrachtRechtTable;

import org.apache.wicket.model.IModel;

public abstract class DeelnemerZoekOpdrachtRechtEditPanel extends
		AbstractEduArteToevoegenBewerkenPanel<DeelnemerZoekOpdrachtRecht>
{
	private static final long serialVersionUID = 1L;

	public DeelnemerZoekOpdrachtRechtEditPanel(String id,
			IModel<List<DeelnemerZoekOpdrachtRecht>> model, ModelManager manager)
	{
		super(id, model, manager, new DeelnemerZoekOpdrachtRechtTable());
	}

	@Override
	public AbstractToevoegenBewerkenModalWindowPanel<DeelnemerZoekOpdrachtRecht> createModalWindowPanel(
			String id, AbstractToevoegenBewerkenModalWindow<DeelnemerZoekOpdrachtRecht> modalWindow)
	{
		modalWindow.setInitialHeight(250);
		modalWindow.setInitialWidth(450);
		return new DeelnemerZoekOpdrachtRechtModalWindowPanel(id, modalWindow, this);
	}

	@Override
	public String getToevoegenLabel()
	{
		return "Rol koppelen";
	}

	@Override
	public String getModalWindowTitle()
	{
		return "Publiceren voor rol";
	}
}
