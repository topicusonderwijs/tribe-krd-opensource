/*
 * Copyright (C) 2001 eTranslate, Inc. All Rights Reserved
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Contact: <eric@etranslate.com>
 */

package com.etranslate.tm.processing.rtf;

import java.io.OutputStream;
import java.util.List;

/**
 * Implemented by classes that receive RTFParser messages. Created: Tue Jul 3 10:29:05
 * 2001
 * 
 * @author Eric Friedman
 * @version $Id: RTFParserDelegate.java,v 1.2 2001/07/10 03:07:53 eric Exp $
 */
public interface RTFParserDelegate
{
	/** constants representing RTF contexts in which text events may occur */
	public static final int IN_DOCUMENT = 0;

	public static final int IN_FONTTBL = 1;

	public static final int IN_FILETBL = 2;

	public static final int IN_COLORTBL = 3;

	public static final int IN_STYLESHEET = 4;

	public static final int IN_LISTTABLE = 5;

	public static final int IN_STYLE = 6;

	public static final int IN_REVTBL = 7;

	public static final int IN_INFO = 8;

	public static final int IN_PNTEXT = 9;

	public static final String NO_STYLE = new String();

	public OutputStream getNextOutputStream(int context);

	public void delimiter(String delimiter, int context);

	/**
	 * Receive a block of text from the RTF document. The text is in the named style and
	 * occurs in <code>context</code.
	 * 
	 * <p>
	 * Style is guaranteed to have object identity with one of the styles in the list
	 * provided by the styleList message, if that has been called.
	 * </p>
	 * 
	 * @param text
	 *            a <code>String</code> value
	 * @param style
	 *            a <code>String</code> value
	 * @param context
	 *            an <code>int</code> value
	 */
	public void text(String text, String style, int context);

	/**
	 * Receive a control symbol in a particular context.
	 * 
	 * @param controlSymbol
	 *            a <code>String</code> value
	 * @param context
	 *            an <code>int</code> value
	 */
	public void controlSymbol(String controlSymbol, int context);

	/**
	 * Receive a control word in a particular context. The value, if not provided, will be
	 * <code>0</code> as per the RTF spec.
	 * 
	 * @param controlWord
	 *            a <code>String</code> value
	 * @param value
	 *            an <code>int</code> value
	 * @param context
	 *            an <code>int</code> value
	 */
	public void controlWord(String controlWord, int value, int context);

	/**
	 * Receive notification about the opening of an RTF group with the specified depth.
	 * The depth value is that of the group just opened.
	 * 
	 * @param depth
	 *            an <code>int</code> value
	 */
	public void openGroup(int depth);

	/**
	 * Receive notification about the closing of an RTF group with the specified depth.
	 * The depth value is that of the group just closed.
	 * 
	 * @param depth
	 *            an <code>int</code> value
	 */
	public void closeGroup(int depth);

	/**
	 * Receive notification about the list of style names defined for the document
	 * 
	 * @param styles
	 *            a <code>List</code> of <code>String</code> objects.
	 */
	@SuppressWarnings("unchecked")
	public void styleList(List styles);

	/**
	 * The document parsing has begun.
	 */
	public void startDocument();

	/**
	 * Parsing is complete.
	 */
	public void endDocument();

}
