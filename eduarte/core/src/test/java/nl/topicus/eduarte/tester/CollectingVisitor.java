/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.tester;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.eduarte.tester.matchers.ComponentMatcher;

import org.apache.wicket.Component;

public class CollectingVisitor<X extends Component> implements Component.IVisitor<Component>
{
	private ComponentMatcher matcher;

	private boolean findFirst = false;

	List<X> matchedComponents = new ArrayList<X>();

	public CollectingVisitor(ComponentMatcher matcher)
	{
		this.matcher = matcher;
	}

	public CollectingVisitor(ComponentMatcher matcher, boolean findFirst)
	{
		this.matcher = matcher;
		this.findFirst = findFirst;
	}

	public List<X> getMatchedComponents()
	{
		return matchedComponents;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object component(Component component)
	{
		if (matcher.matches(component))
		{
			matchedComponents.add((X) component);
			return findFirst ? Component.IVisitor.STOP_TRAVERSAL
				: Component.IVisitor.CONTINUE_TRAVERSAL;
		}
		return Component.IVisitor.CONTINUE_TRAVERSAL;
	}
}
