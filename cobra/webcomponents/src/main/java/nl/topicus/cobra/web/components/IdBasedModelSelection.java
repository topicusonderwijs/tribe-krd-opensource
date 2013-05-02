package nl.topicus.cobra.web.components;

import java.io.Serializable;
import java.util.HashSet;

import nl.topicus.cobra.entities.IdObject;

import org.apache.wicket.model.util.CollectionModel;

public class IdBasedModelSelection<S extends IdObject> extends ModelSelection<Serializable, S>
{
	private static final long serialVersionUID = 1L;

	public IdBasedModelSelection()
	{
		super(new CollectionModel<Serializable>(new HashSet<Serializable>()));
	}

	@Override
	protected Serializable convertStoR(S object)
	{
		return object.getIdAsSerializable();
	}
}
