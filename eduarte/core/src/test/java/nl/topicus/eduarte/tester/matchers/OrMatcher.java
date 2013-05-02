/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.tester.matchers;

import org.apache.wicket.Component;

public class OrMatcher implements ComponentMatcher
{
	private ComponentMatcher left;

	private ComponentMatcher right;

	public OrMatcher(ComponentMatcher left, ComponentMatcher right)
	{
		this.left = left;
		this.right = right;
	}

	@Override
	public boolean matches(Component component)
	{
		return left.matches(component) || right.matches(component);
	}
}
