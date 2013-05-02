package nl.topicus.cobra.dataproviders;

import java.util.Collection;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.modelsv2.ExtendedHibernateListModel;
import nl.topicus.cobra.modelsv2.ModelManager;

import org.apache.wicket.model.IModel;

/**
 * @author idserda
 */
public class ExtendedModelDataProvider<T extends IdObject> extends IModelDataProvider<T>
{
	ModelManager manager;

	private static final long serialVersionUID = 1L;

	public ExtendedModelDataProvider(ExtendedHibernateListModel<Collection<T>, T> listModel)
	{
		super(listModel);

		manager = listModel.getManager();
	}

	@Override
	public IModel<T> model(T object)
	{
		return manager.getModel(object, null);
	}
}
