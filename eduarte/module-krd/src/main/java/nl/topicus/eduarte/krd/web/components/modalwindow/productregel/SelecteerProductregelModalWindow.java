package nl.topicus.eduarte.krd.web.components.modalwindow.productregel;

import java.util.List;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.productregel.Productregel;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Modal window voor het invoeren van een datum
 * 
 * @author vandekamp
 */
public class SelecteerProductregelModalWindow extends CobraModalWindow<Productregel>
{
	private static final long serialVersionUID = 1L;

	private IModel<List<Productregel>> productregelListModel;

	public SelecteerProductregelModalWindow(String id,
			IModel<List<Productregel>> productregelListModel)
	{
		super(id, new Model<Productregel>());
		this.productregelListModel = productregelListModel;
		setInitialWidth(600);
		setInitialHeight(400);
		setTitle("Selecteer een productregel");
	}

	@Override
	protected CobraModalWindowBasePanel<Productregel> createContents(String id)
	{
		return new SelecteerProductregelPanel(id, this, productregelListModel);
	}

	@Override
	protected void detachModel()
	{
		ComponentUtil.detachQuietly(productregelListModel);
		super.detachModel();
	}
}
