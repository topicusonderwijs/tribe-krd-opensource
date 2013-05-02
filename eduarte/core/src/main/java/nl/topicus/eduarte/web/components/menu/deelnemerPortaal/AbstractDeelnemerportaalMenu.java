/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.menu.deelnemerPortaal;

import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IModel;

/**
 * @author ambrosius, papegaaij
 */
public abstract class AbstractDeelnemerportaalMenu extends AbstractMenuBar
{

	private static final long serialVersionUID = 1L;

	private IModel<Verbintenis> verbintenisModel;

	public AbstractDeelnemerportaalMenu(String id, MenuItemKey selectedTab,
			IModel<Verbintenis> verbintenisModel)
	{
		super(id, selectedTab);
		this.verbintenisModel = verbintenisModel;
	}

	/**
	 * @param pageClass
	 * @return Een nieuwe DeelnemerPageLink
	 */
	public IPageLink createPageLink(Class< ? extends SecurePage> pageClass)
	{
		return new DeelnemerportaalPageLink(pageClass);
	}

	private final class DeelnemerportaalPageLink implements IPageLink
	{
		private static final long serialVersionUID = 1L;

		private final Class< ? extends SecurePage> pageClass;

		public DeelnemerportaalPageLink(Class< ? extends SecurePage> pageClass)
		{
			this.pageClass = pageClass;
		}

		@Override
		public Page getPage()
		{
			return ReflectionUtil.invokeConstructor(pageClass, getVerbintenis());
		}

		@Override
		public Class< ? extends Page> getPageIdentity()
		{
			return pageClass;
		}
	}

	@Override
	public void detachModels()
	{
		super.detachModels();
		verbintenisModel.detach();
	}

	public IModel<Verbintenis> getVerbintenisModel()
	{
		return verbintenisModel;
	}

	/**
	 * @return De verbintenis die gekoppeld is aan het menu
	 */
	public Verbintenis getVerbintenis()
	{
		return verbintenisModel.getObject();
	}

}
