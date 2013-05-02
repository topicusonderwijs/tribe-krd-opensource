package nl.topicus.cobra.modelsv2;

import java.io.Serializable;
import java.util.List;

import nl.topicus.cobra.entities.IdObject;

public interface IObjectAccess extends Serializable
{
	public <T extends IdObject> Class< ? super T> getClass(T object);

	public <T extends IdObject> List<T> get(Class<T> clazz, List<Serializable> ids);

	public <T extends IdObject> List<T> load(Class<T> clazz, List<Serializable> ids);

	public <T extends IdObject> void save(List<T> objects);

	public <T extends IdObject> void delete(List<T> objects);

	public <T extends IdObject> void evict(List<T> objects);

	public Object getActualObject(Object objectIfIdIsNull);
}
