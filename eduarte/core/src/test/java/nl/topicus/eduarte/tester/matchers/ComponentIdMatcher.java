/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.tester.matchers;

import org.apache.wicket.Component;

public class ComponentIdMatcher implements ComponentMatcher
{
	private final String id;

	public ComponentIdMatcher(String id)
	{
		this.id = id;
	}

	@Override
	public boolean matches(Component component)
	{
		return component.getId().equals(id);
	}
}
