/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages;

/**
 * De verschillende pagina style.
 * 
 * @author marrink
 */
public enum PageStyle
{
	/**
	 * Default pagina met side-bar.
	 */
	Default("contBox"),
	/**
	 * Pagina over volledige scherm om te zoeken.
	 */
	Zoeken("contSearchBox");

	private String cssStyle;

	private PageStyle()
	{

	}

	private PageStyle(String cssStyle)
	{
		this.cssStyle = cssStyle;

	}

	/**
	 * De corresponderende css style for dit type pagina.
	 * 
	 * @return css class
	 */
	public String getCssStyle()
	{
		return cssStyle;
	}

	protected void setCssStyle(String cssStyle)
	{
		this.cssStyle = cssStyle;
	}

}
