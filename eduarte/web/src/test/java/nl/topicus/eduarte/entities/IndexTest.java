/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities;

import java.lang.reflect.Field;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.test.AbstractIndexTest;
import nl.topicus.eduarte.noise.entities.NoiseEntity;

import org.springframework.test.context.ContextConfiguration;

/**
 * Test case om te controleren of de indexes wel kloppen.
 * 
 * @author hoeve
 */
@ContextConfiguration(locations = "file:src/test/java/nl/topicus/eduarte/entities/inithibernate.xml")
public class IndexTest<T extends IdObject> extends AbstractIndexTest<T>
{
	@Override
	protected boolean skipClass(Class< ? > checkClass)
	{
		return ViewEntiteit.class.isAssignableFrom(checkClass)
			|| checkClass.isAnnotationPresent(NoiseEntity.class);
	}

	@Override
	protected boolean skipField(String fieldName, Field field)
	{
		return (Entiteit.STR_CREATED_BY + "|" + Entiteit.STR_LAST_MODIFIED_BY).contains(fieldName);
	}
}
