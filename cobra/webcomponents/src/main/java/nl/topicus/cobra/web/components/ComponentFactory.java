/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components;

import nl.topicus.cobra.web.components.labels.DatumTijdLabel;
import nl.topicus.cobra.web.components.labels.MaxLengthLabel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * @author loite
 */
public class ComponentFactory
{

	/**
	 * Een nieuw label met het gegeven id. Model wordt door de parent geleverd. Alleen de
	 * body van de label wordt gerenderd (renderBodyOnly = true)
	 * 
	 * @param id
	 *            Het id van de label.
	 * @return Nieuwe datalabel zonder eigen model.
	 */
	public static Label getDataLabel(String id)
	{
		Label res = new Label(id);
		res.setRenderBodyOnly(true);
		return res;
	}

	/**
	 * Een nieuw label met het gegeven id en de gegeven tekst.
	 * 
	 * @param id
	 *            Het id van de label.
	 * @param tekst
	 *            De tekst die getoond moet worden in de label
	 * @return Nieuwe datalabel met de gegeven tekst.
	 */
	public static Label getDataLabel(String id, String tekst)
	{
		Label res = new Label(id, tekst);
		res.setRenderBodyOnly(true);
		return res;
	}

	/**
	 * Een nieuw label met het gegeven id en het gegeven model.
	 * 
	 * @param id
	 *            Het id van de label.
	 * @param model
	 *            Het model van de label
	 * @return Nieuwe datalabel met de gegeven tekst.
	 */
	public static Label getDataLabel(String id, IModel< ? > model)
	{
		Label res = new Label(id, model);
		res.setRenderBodyOnly(true);
		return res;
	}

	/**
	 * Een nieuw label met het gegeven id en de gegeven maximale lengte. Model wordt door
	 * de parent geleverd. De body van het label wordt gerendered, omdat hier ook een
	 * title (tooltip) op gezet wordt als de tekst te lang wordt.
	 * 
	 * @param id
	 * @param maxLength
	 * @return Nieuwe MaxLengthLabel
	 */
	public static MaxLengthLabel getMaxLengthLabel(String id, int maxLength)
	{
		MaxLengthLabel res = new MaxLengthLabel(id, maxLength);
		return res;
	}

	public static DatumTijdLabel getDatumTijdLabel(String id)
	{
		DatumTijdLabel res = new DatumTijdLabel(id);
		res.setRenderBodyOnly(true);
		return res;
	}
}
