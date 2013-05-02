/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.datapanel.columns;

import nl.topicus.cobra.web.components.datapanel.columns.ButtonColumn;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

/**
 * Kolom met een deleteknop die alleen getoond wordt als een bepaalde module is afgenomen.
 * 
 * @author loite
 */
public abstract class ModuleDependentDeleteColumn<T> extends ButtonColumn<T>
{

	private static final long serialVersionUID = 1L;

	private EduArteModuleKey key;

	public ModuleDependentDeleteColumn(String id, String header, EduArteModuleKey key)
	{
		super(id, header);
		this.key = key;
	}

	@Override
	protected final String getCssDisabled()
	{
		return "deleteItem_grey";
	}

	@Override
	protected final String getCssEnabled()
	{
		return "deleteItem";
	}

	@Override
	public abstract void onClick(WebMarkupContainer item, IModel<T> rowModel);

	@Override
	public boolean isVisible()
	{
		return EduArteApp.get().isModuleActive(key);
	}

}
