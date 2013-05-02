/*
 * @(#)Dutch.java
 *
 * Summary: Display a number in modern Dutch words.
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
 *  1.0 1999-01-15 - based on Norwegian version
 *  1.1 1999-01-16 - half a dozen fine points resolved.
 *  1.2 1999-01-18 - decided on spelling achttien rather than achtien after studying common usage on the Internet.
 *                   9900 now handled as 9 thousand 900 rather than 99 hundred.
 *                   remove space after hundreds.
 *                   to go, possibly change �n to -en.
 *                   minus would also be correct for min, but it would sound stilted.
 *  1.3 1999-01-18 - split off OldDutch and BankerDutch
 *                   convert to modern Dutch. Get rid of trema �n.
 *  1.4 1999-01-21 - make all language specific String literals into constants.
 */
package nl.topicus.cobra.util.inwords;

/**
 * Display a number in modern Dutch words.
 * <p/>
 * e.g. -12345 -&gt; &quot;min twaalfduizend driehonderdvijfenveertig&quot; see also
 * com.mindprod.inwords.OldDutch.java and com.mindprod.inwords.BankerDutch.java.
 * 
 * @author Roedy Green, Canadian Mind Products
 * @version 1.4 1999-01-21 - make all language specific String literals into constants.
 *          pecial thanks go to Michiel Meeuwissen &lt;mihxil@esperanto.nu&gt; ho provided
 *          all the information about Dutch counting sed to write this program. This
 *          program is based on a Pascal rogram he wrote. aurice le Rutte
 *          &lt;MleRutte@Square.NL&gt; also provided information s did Rob Barends
 *          &lt;rbarends@earthlink.net&gt;. Special thanks go to Michiel Meeuwissen
 *          &lt;mihxil@esperanto.nu&gt; who provided all the information about Dutch
 *          counting used to write this program. This program is based on a Pascal program
 *          he wrote. Maurice le Rutte &lt;MleRutte@Square.NL&gt; also provided
 *          information as did Rob Barends &lt;rbarends@earthlink.net&gt;. Why is this
 *          code written in an unusual way? see inwords.use
 * @since 1999-01-15
 */
public class Dutch implements ToWords
{
	// ------------------------------ CONSTANTS ------------------------------

	private static final String AND = "en";

	/**
	 * undisplayed copyright notice
	 * 
	 * @noinspection UnusedDeclaration
	 */
	public static final String EMBEDDED_COPYRIGHT =
		"copyright (c) 1999-2009 Roedy Green, Canadian Mind Products, http://mindprod.com";

	private static final String MINUS = "min";

	private static final String ZERO = "nul";

	// ------------------------------ FIELDS ------------------------------

	private static final String[] groupName = {
	/* We only need up to a trillion, since a long is about 9 * 10 ^ 18 */
	/* After that it goes triliard, quadriljoen, quadriliard */
	/*
	 * American: unit, hundred, thousand, million, billion, trillion, quadrillion,
	 * quintillion
	 */
	"", "honderd", "duizend", "miljoen", "miljard", "biljoen", "biljard", "triljoen"};

	private static final String[] lowName =
		{
		/* numbers 0 .. 20 */
		/* zero is shown as "" since it is never used in combined forms */
		"", "een", "twee", "drie", "vier", "vijf", "zes", "zeven", "acht", "negen", "tien", "elf",
			"twaalf", "dertien", "veertien", "vijftien", "zestien", "zeventien", "achttien"
			/* not achtien */, "negentien"};

	private static final String[] tys = {
	/* 0 10 20 30 .. 90 */
	"", "", "twintig", "dertig", "veertig", "vijftig", "zestig", "zeventig", "tachtig"
	/* not achtig */, "negentig"};

	private static final int[] divisor = {
	/*
	 * how many of this group is needed to form one of the succeeding group.
	 */
	/*
	 * American: unit, hundred, thousand, million, billion, trillion, quadrillion,
	 * quintillion
	 */
	100, 10, 1000, 1000, 1000, 1000, 1000, 1000};

	// -------------------------- PUBLIC INSTANCE METHODS --------------------------

	/**
	 * convert long integer into Dutch words. e.g. // -12345 ->
	 * "min twaalfduizend driehonderdvijfenveertig". Handles negative and positive
	 * integers on range -Long.MAX_VALUE .. Long.MAX_VALUE; It cannot handle
	 * Long.MIN_VALUE;
	 * 
	 * @param num
	 *            number to convert to words
	 * 
	 * @return words
	 */
	@SuppressWarnings("all")
	public String toWords(long num)
	{
		if (num == 0)
			return ZERO;

		boolean negative = (num < 0);
		if (negative)
			num = -num;

		String s = "";
		// Work least significant digit to most, right to left.
		// until high order part is all 0s.
		for (int group = 0; num > 0; group++)
		{
			int remdr = (int) (num % divisor[group]);
			num = num / divisor[group];
			// check for 1100 .. 1999, 2100..2999, ... 5200..5999
			// but not 1000..1099, 2000..2099, ...
			// Special case written as twenty-one hundred.
			// e.g. thousands digit is 1..5 and hundreds digit 1..9
			// Only when no further higher order.
			if (group == 1/* doing hundreds */&& 1 <= num && num <= 5)
			{
				if (remdr > 0)
				{
					remdr += num * 10;
					num = 0;
				}// end if
			}// end if
			String t;
			if (remdr == 0)
				continue;

			else if (remdr == 1)
				// suppress one before hundred & thousand, but not million & up.
				t = (group == 0 || group > 2) ? lowName[1] : "";
			else if (remdr < 20)
				t = lowName[remdr];
			else if (remdr < 100)
			{
				int units = remdr % 10;
				int tens = remdr / 10;
				t = tys[tens];
				if (units > 0)
				{
					/* we write this like eenentwintig, one and twenty, reversed */
					/*
					 * drieenvijftig or treeenvijftig eenenvijftig, no attempt to avoid
					 * eee
					 */
					t = lowName[units] + AND + t;
				}
			}
			else
				t = toWords(remdr);

			// millions on up are separated on left by space
			boolean leftPad = group > 2;
			// hundreds and units written together, rest separated
			boolean rightPad = group != 1;
			s = t + (leftPad ? " " : "") + groupName[group] + (rightPad ? " " : "") + s;
		}
		s = s.trim();
		if (negative)
			s = MINUS + " " + s;

		return s;
	}
}
