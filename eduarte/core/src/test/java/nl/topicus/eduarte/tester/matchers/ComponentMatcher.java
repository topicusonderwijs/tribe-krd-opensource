/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.tester.matchers;

import org.apache.wicket.Component;

public interface ComponentMatcher
{
	public boolean matches(Component component);
}
