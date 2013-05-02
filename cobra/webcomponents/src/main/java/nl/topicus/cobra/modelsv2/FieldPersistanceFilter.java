/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.modelsv2;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Filter to decide if fields should be persisited by the {@link ExtendedModel}.
 * Opmerking. Gebruik liever geen anonymous inner classes omdat die anders mee
 * geserialiseerd worden. Beter is om static nested classes te gebruiken.
 * 
 * @author marrink
 */
public interface FieldPersistanceFilter extends Serializable
{
	/**
	 * Callback to decide which fields should be persisted. By default static or final
	 * fields should be ignored.
	 * 
	 * @param field
	 * @param objectClass
	 * @return true if the field should be persisted, false otherwise
	 */
	public boolean shouldPersist(Field field, Class< ? > objectClass);
}
