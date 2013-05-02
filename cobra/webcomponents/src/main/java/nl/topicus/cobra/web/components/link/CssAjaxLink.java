/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.link;

import nl.topicus.cobra.web.security.TargetBasedSecurityCheck;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.actions.WaspAction;
import org.apache.wicket.security.checks.ISecurityCheck;
import org.apache.wicket.security.components.ISecureComponent;
import org.apache.wicket.security.components.SecureComponentHelper;

/**
 * AjaxLink die afhankelijk van enabled of disabled status een andere css class krijgt
 * toegewezen. Indien er een securitycheck op deze link gezet wordt is het handig om te
 * zorgen dat deze link altijd zichtbaar is. Je kunt hiervoor
 * {@link #addDefaultSecurityCheck(Class)} gebruiken of zelf 1 toevoegen.
 * 
 * @author marrink
 */
public abstract class CssAjaxLink<T> extends AjaxLink<T> implements ISecureComponent
{

	private static final long serialVersionUID = 1L;

	public CssAjaxLink(String id, String cssEnabled, String cssDisabled)
	{
		this(id, null, cssEnabled, cssDisabled);
	}

	public CssAjaxLink(String id, IModel<T> object, final String cssEnabled,
			final String cssDisabled)
	{
		super(id, object);
		add(new AttributeAppender("class", new AbstractReadOnlyModel<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				return isEnabled() ? cssEnabled : cssDisabled;
			}
		}, " "));
	}

	/**
	 * Voegt een standaard {@link TargetBasedSecurityCheck} toe met alternatieve rendering
	 * check.
	 * 
	 * @param clickTarget
	 */
	public final void addDefaultSecurityCheck(Class< ? extends Page> clickTarget)
	{
		TargetBasedSecurityCheck check = new TargetBasedSecurityCheck(this, clickTarget);
		check.setUseAlternativeRenderCheck(true);
		setSecurityCheck(check);
	}

	@Override
	public ISecurityCheck getSecurityCheck()
	{
		return SecureComponentHelper.getSecurityCheck(this);
	}

	@Override
	public boolean isActionAuthorized(String waspAction)
	{
		return SecureComponentHelper.isActionAuthorized(this, waspAction);
	}

	@Override
	public boolean isActionAuthorized(WaspAction action)
	{
		return SecureComponentHelper.isActionAuthorized(this, action);
	}

	@Override
	public boolean isAuthenticated()
	{
		return SecureComponentHelper.isAuthenticated(this);
	}

	@Override
	public void setSecurityCheck(ISecurityCheck check)
	{
		SecureComponentHelper.setSecurityCheck(this, check);

	}
}
