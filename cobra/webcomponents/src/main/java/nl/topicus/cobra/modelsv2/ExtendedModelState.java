package nl.topicus.cobra.modelsv2;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.model.IModel;

public class ExtendedModelState implements ObjectState
{
	private static final long serialVersionUID = 1L;

	private Map<String, IModel< ? >> fieldStore;

	public ExtendedModelState(Map<String, IModel< ? >> persistedFields)
	{
		fieldStore = new HashMap<String, IModel< ? >>(persistedFields);
	}

	public Map<String, IModel< ? >> getFieldStore()
	{
		return fieldStore;
	}

	@Override
	public void detach()
	{
		for (IModel< ? > curModel : fieldStore.values())
		{
			curModel.detach();
		}
	}

}
