package nl.topicus.eduarte.entities.signalering.events;

import nl.topicus.cobra.entities.IdObject;

public interface IObjectGekoppeldEvent<T extends IdObject>
{
	public T getObject();
}
