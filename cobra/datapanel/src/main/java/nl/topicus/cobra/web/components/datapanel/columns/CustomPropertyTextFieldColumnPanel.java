/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.datapanel.columns;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;

import org.apache.wicket.markup.html.panel.Panel;

/**
 * Wrapper panel om een textfield heen welke in een {@link CustomDataPanel} gebruikt kan
 * worden.
 * 
 * @author bos
 */
public class CustomPropertyTextFieldColumnPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 */
	public CustomPropertyTextFieldColumnPanel(String id)
	{
		super(id);
		setRenderBodyOnly(true);
	}
}
