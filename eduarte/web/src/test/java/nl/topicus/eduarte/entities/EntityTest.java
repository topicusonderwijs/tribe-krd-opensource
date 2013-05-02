/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities;

import nl.topicus.cobra.test.AbstractEntityTest;

import org.springframework.test.context.ContextConfiguration;

/**
 * Test case om te controleren of de hibernate configuratie wel klopt. B.V. de annotaties.
 * 
 * @author marrink
 */
@ContextConfiguration(locations = "file:src/test/java/nl/topicus/eduarte/entities/inithibernate.xml")
public class EntityTest extends AbstractEntityTest
{
}
