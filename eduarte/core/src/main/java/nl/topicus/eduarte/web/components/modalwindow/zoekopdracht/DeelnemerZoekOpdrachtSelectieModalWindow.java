package nl.topicus.eduarte.web.components.modalwindow.zoekopdracht;

import java.util.List;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.rapportage.DeelnemerZoekOpdracht;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Modal window voor het selecteren van een deelnemerzoekopdracht
 * 
 * @author niesink
 */
public class DeelnemerZoekOpdrachtSelectieModalWindow extends
		CobraModalWindow<DeelnemerZoekOpdracht>
{
	private static final long serialVersionUID = 1L;

	private IModel<List<DeelnemerZoekOpdracht>> deelnemerZoekOpdrachtListModel;

	public DeelnemerZoekOpdrachtSelectieModalWindow(String id,
			IModel<List<DeelnemerZoekOpdracht>> deelnemerZoekOpdrachtListModel)
	{
		super(id, new Model<DeelnemerZoekOpdracht>());
		this.deelnemerZoekOpdrachtListModel = deelnemerZoekOpdrachtListModel;
		setInitialWidth(600);
		setInitialHeight(400);
		setTitle("Selecteer een opgeslagen zoekopdracht");
	}

	@Override
	protected CobraModalWindowBasePanel<DeelnemerZoekOpdracht> createContents(String id)
	{
		return new DeelnemerZoekOpdrachtSelectiePanel(id, this, deelnemerZoekOpdrachtListModel);
	}

	@Override
	protected void detachModel()
	{
		ComponentUtil.detachQuietly(deelnemerZoekOpdrachtListModel);
		super.detachModel();
	}
}
