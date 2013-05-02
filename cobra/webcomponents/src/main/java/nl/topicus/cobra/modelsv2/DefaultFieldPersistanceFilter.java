/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.modelsv2;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Version;

import nl.topicus.cobra.entities.FieldPersistance;
import nl.topicus.cobra.entities.FieldPersistenceMode;

/**
 * Default persistance filter voor {@link ExtendedModel}. Dit filter pakt alle standaard
 * velden die ook door hibernate gepersist worden.
 * 
 * @author loite, marrink, papegaaij
 */
public class DefaultFieldPersistanceFilter implements FieldPersistanceFilter
{
	private static final long serialVersionUID = 1L;

	/** de instance van het standaard field persistance filter. */
	public static final FieldPersistanceFilter INSTANCE = new DefaultFieldPersistanceFilter();

	/**
	 * Persist alle non static, non final, non transient, non javax.persistence.Transient
	 * non javax.persistence.Version,non javax.persistence.Id velden. Ook wordt er gekeken
	 * naar de {@link FieldPersistance} annotation.
	 * 
	 * @see nl.topicus.cobra.modelsv2.FieldPersistanceFilter#shouldPersist(java.lang.reflect.Field,
	 *      java.lang.Class)
	 */
	@Override
	public boolean shouldPersist(Field field, Class< ? > objectClass)
	{
		int modifiers = field.getModifiers();
		if (Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers)
			|| Modifier.isTransient(modifiers) || field.isAnnotationPresent(Version.class)
			|| field.isAnnotationPresent(Transient.class) || field.isAnnotationPresent(Id.class))
			return false;
		if (field.isAnnotationPresent(FieldPersistance.class)
			&& field.getAnnotation(FieldPersistance.class).value()
				.equals(FieldPersistenceMode.SKIP))
			return false;
		return true;
	}

}
