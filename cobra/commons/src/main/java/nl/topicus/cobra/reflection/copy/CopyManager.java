package nl.topicus.cobra.reflection.copy;

import java.lang.reflect.Field;

public interface CopyManager
{
	public boolean getMustCopyInstanceOf(Class< ? > clazz);

	public boolean getMustCopyField(Field field);

	public <T> T copyObject(T object);
}
