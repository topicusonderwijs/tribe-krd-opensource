/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.datapanel;

import org.apache.wicket.model.AbstractReadOnlyModel;

public final class ConcatModel extends AbstractReadOnlyModel<String>
{
	private static final long serialVersionUID = 1L;

	private StringBuffer buffer;

	/**
	 * @param size
	 *            initial size of buffer
	 */
	public ConcatModel(int size)
	{
		super();
		buffer = new StringBuffer(size);
	}

	/**
	 * @see org.apache.wicket.model.IModel#getObject()
	 */
	@Override
	public String getObject()
	{
		return buffer.toString();
	}

	/**
	 * Plakt text achteraan vast.
	 * 
	 * @param txt
	 */
	public void append(String txt)
	{
		buffer.append(txt);
	}

	/**
	 * Verwijderd alle text.
	 */
	public void clear()
	{
		buffer.delete(0, buffer.length());
	}
}