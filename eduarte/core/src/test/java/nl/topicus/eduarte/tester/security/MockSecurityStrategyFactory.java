/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.tester.security;

import org.apache.wicket.security.strategies.WaspAuthorizationStrategy;
import org.apache.wicket.security.swarm.strategies.SwarmStrategyFactory;

public class MockSecurityStrategyFactory extends SwarmStrategyFactory
{
	private MockSecurityStrategy strategy;

	public MockSecurityStrategyFactory(Object hiveQueen, MockSecurityStrategy strategy)
	{
		super(hiveQueen);

		this.strategy = strategy;
	}

	@Override
	public void destroy()
	{
		strategy = null;
	}

	@Override
	public WaspAuthorizationStrategy newStrategy()
	{
		return strategy;
	}
}
