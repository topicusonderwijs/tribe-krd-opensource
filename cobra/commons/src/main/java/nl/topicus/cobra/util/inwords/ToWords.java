/*
 * @(#)ToWords.java
 *
 * Summary: Interface that all number-to-word translators must implement to be interchangeable.
 *
 * Copyright: (c) 1999-2009 Roedy Green, Canadian Mind Products, http://mindprod.com
 *
 * Licence: This software may be copied and used freely for any purpose but military.
 *          http://mindprod.com/contact/nonmil.html
 *
 * Requires: JDK 1.5+
 *
 * Created with: IntelliJ IDEA IDE.
 *
 * Version History:
 *  1.0 1999-01-12 - initial version
 */
package nl.topicus.cobra.util.inwords;

/**
 * Interface that all number-to-word translators must implement to be interchangeable.
 * 
 * @author Roedy Green, Canadian Mind Products
 * @version 1.0 1999-01-12 - initial version
 * @since 1999-01-12
 */
public interface ToWords
{
	// -------------------------- PUBLIC INSTANCE METHODS --------------------------

	/**
	 * translate number to words. e.g. -12345 to "minus twelve thousand three hundred
	 * forty-five" usually implement a static version inWords that toWords calls. public
	 * static String inWords (long num);
	 * <p/>
	 * instance method, for use in callbacks, class.forName("xxx").getInstance().inWords
	 * etc.
	 * 
	 * @param num
	 *            the number to convert to words.
	 * 
	 * @return the number in spelled out in words.
	 */
	public String toWords(long num);
}// end InWords
