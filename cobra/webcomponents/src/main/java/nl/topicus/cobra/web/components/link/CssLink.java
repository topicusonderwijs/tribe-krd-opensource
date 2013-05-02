/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.link;

import nl.topicus.cobra.web.security.TargetBasedSecurityCheck;

import org.apache.wicket.Page;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.actions.WaspAction;
import org.apache.wicket.security.checks.ISecurityCheck;
import org.apache.wicket.security.components.ISecureComponent;
import org.apache.wicket.security.components.SecureComponentHelper;

/**
 * Link die afhankelijk van enabled of disabled status een andere css class krijgt
 * toegewezen. Indien er een securitycheck op deze link gezet wordt is het handig om te
 * zorgen dat deze link altijd zichtbaar is. Je kunt hiervoor
 * {@link #addDefaultSecurityCheck(Class)} gebruiken of zelf 1 toevoegen.
 * 
 * @author marrink
 */
public abstract class CssLink<T> extends Link<T> implements ISecureComponent
{
	private static final long serialVersionUID = 1L;

	private final String cssEnabled;

	private final String cssDisabled;

	public CssLink(String id, String cssEnabled, String cssDisabled)
	{
		super(id);
		this.cssEnabled = cssEnabled;
		this.cssDisabled = cssDisabled;
	}

	public CssLink(String id, IModel<T> object, String cssEnabled, String cssDisabled)
	{
		super(id, object);
		this.cssEnabled = cssEnabled;
		this.cssDisabled = cssDisabled;
	}

	@Override
	protected void onComponentTag(ComponentTag tag)
	{
		tag.put("class", cssEnabled);
		super.onComponentTag(tag);
	}

	@Override
	protected void disableLink(ComponentTag tag)
	{
		tag.put("class", cssDisabled);
		super.disableLink(tag);
	}

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
