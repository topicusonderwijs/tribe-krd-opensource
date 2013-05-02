/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.tester.matchers;

import org.apache.wicket.Component;

public class ComponentTypeMatcher implements ComponentMatcher
{
	private Class< ? extends Component> componentType;

	public ComponentTypeMatcher(Class< ? extends Component> componentType)
	{
		this.componentType = componentType;
	}

	@Override
	public boolean matches(Component component)
	{
		return componentType.isAssignableFrom(component.getClass());
	}
}
