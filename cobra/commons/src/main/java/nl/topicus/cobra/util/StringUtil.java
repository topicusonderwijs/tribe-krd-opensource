/*
 * Copyright (c) 2005-2009, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author marrink
 */
public final class StringUtil
{
	/**
	 * Converteerd objecten naar strings. Dit in het geval dat je iets anders wilt dan de
	 * toString().
	 * 
	 * @author marrink
	 * @param <T>
	 */
	public static interface StringConverter<T>
	{
		/**
		 * Converteerd object naar String waarde. Wordt aangeroepen bij elk item in de
		 * lijst.
		 * 
		 * @param object
		 *            het object of null
		 * @param listIndex
		 *            index van dit object uit een lijst, -1 indien dit object niet uit
		 *            een lijst komt
		 * @return String of null indien niet geconverteerd kan worden
		 */
		public String toString(T object, int listIndex);

		/**
		 * De teken separator voor elk item uit de lijst. Wordt aangeroepen na elk element
		 * in de lijst zolang er nog een volgend element is
		 * 
		 * @param listIndex
		 *            index van dit item uit de lijst, als er geen lijst is wordt deze
		 *            methode niet aangeroepen
		 * @return String of null indien een separator niet nodig is.
		 */
		public String getSeparator(int listIndex);
	}

	/**
	 * Lettercombinaties die in het Nederlandse alfabet gezien worden als 1 letter.
	 */
	public static final String[] SAMENGESTELDE_LETTERS = {"IJ"};

	private static final char[] ORACLE_WILDCARDS = {'%', '_', '['};

	private static final String EMAIL_PATTERN =
		"^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z]{2,}){1}$)";

	private static final String IPADRES_PATTERN =
		"^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";

	/**
	 * Default stringconverter die de toString value van het object gebruikt. Kan overweg
	 * met null.
	 */
	private static final class DefaultStringConverter<T extends Object> implements
			StringConverter<T>
	{
		private final String separator;

		/**
		 * Constructor
		 */
		public DefaultStringConverter()
		{
			this(", ");
		}

		/**
		 * Constructor
		 * 
		 * @param separator
		 */
		public DefaultStringConverter(String separator)
		{
			super();
			this.separator = separator;
		}

		@Override
		public String toString(Object object, int listIndex)
		{
			return String.valueOf(object);
		}

		@Override
		public String getSeparator(int listIndex)
		{
			return separator;
		}
	}

	private static String SUB = "";

	private static String[] HIGH_ASCII_XLATE =
		new String[] {" ", "!", "c", SUB, SUB, "Y", "|", SUB, "\"", "(c)", "a", "<<", "-", "-",
			"(R)", "-", " ", "+/-", "2", "3", "'", "u", "P", ".", ",", "1", "o", ">>", " 1/4",
			" 1/2", " 3/4", "?", "A", "A", "A", "A", "A", "A", "AE", "C", "E", "E", "E", "E", "I",
			"I", "I", "I", "D", "N", "O", "O", "O", "O", "O", "x", "O", "U", "U", "U", "U", "Y",
			"Th", "ss", "a", "a", "a", "a", "a", "a", "ae", "c", "e", "e", "e", "e", "i", "i", "i",
			"i", "d", "n", "o", "o", "o", "o", "o", ":", "o", "u", "u", "u", "u", "y", "th", "y"};

	private StringUtil()
	{
		super();
	}

	/**
	 * Controleert of de waarde van de parameter voldoet aan het formaat van de reguliere
	 * expressie.
	 * 
	 * @param parameter
	 *            de naam van de parameter
	 * @param value
	 *            de waarde van de parameter
	 * @param regexp
	 *            de reguliere expressie waaraan voldaan moet worden
	 * @return <code>true</code> als de value voldoet aan de regexpr.
	 */
	public static boolean checkMatchesRegExp(String parameter, String value, String regexp)
	{
		Asserts.assertNotNull(parameter, value);
		Asserts.assertNotNull("regexp", regexp);

		Pattern pattern = Pattern.compile(regexp);
		String trimmedValue = value.trim();
		Matcher matcher = pattern.matcher(trimmedValue.subSequence(0, trimmedValue.length()));
		boolean matches = matcher.matches();
		return matches;
	}

	/**
	 * Vervang high-ascii symbolen door symbolen uit vertaal tabel.
	 * 
	 * @param tekst
	 *            invoer tekst
	 * @return tekst zonder high-ascii symbolen
	 * @deprecated zie ICU4J Transliterator, Transliterator.getInstance("NFD; [:Nonspacing
	 *             Mark:] Remove; NFC")
	 */
	@Deprecated()
	public static String stripHighAscii(String tekst)
	{
		if (tekst == null)
			return null;
		StringBuffer result = new StringBuffer();
		for (char c : tekst.toCharArray())
		{
			if (c > 0x9F)
			{
				int index = c - 0xA0;
				if (index < HIGH_ASCII_XLATE.length)
				{
					result.append(HIGH_ASCII_XLATE[c - 0xA0]);
				}
				else
				{
					result.append(c);
				}
			}
			else
			{
				result.append(c);
			}
		}
		return result.toString();
	}

	/**
	 * Geeft de eerste nummer sequentie terug in de tekst, dus 'abc1234def' geeft 1234
	 * terug. Negatieve getallen worden niet als zodaing herkent dus a-123 wordt 123.
	 * 
	 * @param tekst
	 *            de tekst die geparst moet worden.
	 * @return de integer waarde van de nummer sequentie.
	 */
	public static Integer getFirstNumberSequence(String tekst)
	{
		if (tekst == null)
			return Integer.valueOf(0);

		int start = Integer.MAX_VALUE;
		int end = Integer.MIN_VALUE;

		// hou vast of je in een getallen sequentie zit, initieel niet
		boolean inNumberSequence = false;
		for (int i = 0; i < tekst.length(); i++)
		{
			if (tekst.charAt(i) >= '0' && tekst.charAt(i) <= '9')
			{
				start = Math.min(i, start);
				end = Math.max(i, end);
				// we zitten nu in een getallen sequentie
				inNumberSequence = true;
			}
			else
			{
				// het is geen getal, dus mogelijk zijn we nu uit de
				// getallen sequentie gelopen
				if (inNumberSequence)
				{
					// einde van de eerste sequentie
					break;
				}
			}
		}
		// als de start positie hoger is dan de lengte van de tekst, zat er geen
		// getal in de tekst
		if (start > tekst.length())
		{
			return null;
		}
		return Integer.valueOf(tekst.substring(start, end + 1));
	}

	/**
	 * @param string
	 * @return het laatste teken van de gegeven string.
	 */
	public static String getLastCharacter(String string)
	{
		if (string == null)
		{
			return null;
		}
		if ("".equals(string))
		{
			return "";
		}
		return string.substring(string.length() - 1);
	}

	/**
	 * Geeft true als de gegeven string numeriek is, en anders false.
	 * 
	 * @param string
	 * @return true als de string numeriek is, en anders false
	 */
	public static boolean isNumeric(String string)
	{
		if (string == null || isEmpty(string))
			return false;
		for (int i = 0; i < string.length(); i++)
		{
			if (string.charAt(i) < '0' || string.charAt(i) > '9')
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Geeft true als de gegeven string een email adres is, en anders false.
	 * 
	 * @param string
	 * @return true als de string een email adres is, en anders false
	 */
	public static boolean isEmail(String string)
	{
		Pattern pattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
		return !isEmpty(string) && pattern.matcher(string).matches();
	}

	public static boolean isIpAdres(String string)
	{
		Pattern pattern = Pattern.compile(IPADRES_PATTERN, Pattern.CASE_INSENSITIVE);
		return !isEmpty(string) && pattern.matcher(string).matches();
	}

	/**
	 * Geeft true als de gegeven string numeriek is, eventueel inclusief een komma of
	 * punt.
	 * 
	 * @param string
	 *            de string die gecontroleerd moet worden.
	 * @return true als de string numeriek is, en anders false
	 */
	public static boolean isDecimal(String string)
	{
		if (string == null)
			return false;
		for (int i = 0; i < string.length(); i++)
		{
			if (!(string.charAt(i) == ',' || string.charAt(i) == '.' || (i == 0 && string.charAt(i) == '-')))
			{
				if (string.charAt(i) < '0' || string.charAt(i) > '9')
				{
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Returns a string in the form of Abcdefg. The first char of the string is made
	 * uppercase all other characters are made lowercase.
	 * 
	 * @param name
	 * @return a formatted string.
	 */
	public static String firstCharUppercase(String name)
	{
		if (StringUtil.isEmpty(name))
			return " ";

		if (name.length() > 1)
		{
			String samenGesteld = containsSamengesteldeLetter(name);
			if (StringUtil.isNotEmpty(samenGesteld))
			{
				return new StringBuffer(name.length()).append(samenGesteld).append(
					name.substring(samenGesteld.length()).toLowerCase()).toString();
			}
			return new StringBuffer(name.length()).append(
				String.valueOf(name.charAt(0)).toUpperCase()).append(
				name.substring(1).toLowerCase()).toString();
		}

		return name.toUpperCase();
	}

	/**
	 * Returns a string with the first letter of the string made uppercase and all other
	 * characters lowercase.
	 * 
	 * @param name
	 * @return a formatted string.
	 */
	public static String firstLetterUppercase(String name)
	{
		if (StringUtil.isEmpty(name))
			return " ";

		Integer firstLetterPos = null;

		for (int i = 0; i < name.length() && firstLetterPos == null; i++)
		{
			if (Character.isLetter(name.charAt(i)))
				firstLetterPos = i;
		}

		if (firstLetterPos != null)
			return new StringBuffer(name.length()).append(name.substring(0, firstLetterPos))
				.append(firstCharUppercase(name.substring(firstLetterPos))).toString();
		else
			return name;
	}

	/**
	 * Returns a string in the form of AbcDEfgH. The first char of the string is made
	 * uppercase all other characters are untouched.
	 * 
	 * @param name
	 * @return a formatted string.
	 */
	public static String onlyFirstCharUppercase(String name)
	{
		if (StringUtil.isEmpty(name))
			return " ";

		if (name.length() > 1)
		{
			String samenGesteld = containsSamengesteldeLetter(name);
			if (StringUtil.isNotEmpty(samenGesteld))
			{
				return new StringBuffer(name.length()).append(samenGesteld).append(
					name.substring(samenGesteld.length())).toString();
			}
			return new StringBuffer(name.length()).append(
				String.valueOf(name.charAt(0)).toUpperCase()).append(name.substring(1)).toString();
		}

		return name.toUpperCase();
	}

	/**
	 * @param value
	 * @return de samengestelde letter of anders de eerste letter van de string.
	 */
	public static String containsSamengesteldeLetter(String value)
	{
		if (isEmpty(value))
			return " ";
		String naam = value.toUpperCase();

		for (String letter : SAMENGESTELDE_LETTERS)
		{
			if (naam.startsWith(letter))
			{
				return letter;
			}
		}

		return String.valueOf(value.charAt(0)).toUpperCase();
	}

	/**
	 * Returns a string in the form 'Abc Def Ghi'. the first char of each word in the
	 * string is made uppercase, all other characters are made lowercase.
	 * 
	 * @param sentence
	 * @return a string in the form 'Abc Def Ghi'. the first char of each word in the
	 *         string is made uppercase, all other characters are made lowercase.
	 */
	public static String firstCharUppercaseOfEachWord(String sentence)
	{
		if (StringUtil.isEmpty(sentence))
			return " ";

		StringTokenizer tokenizer = new StringTokenizer(sentence.trim(), " \t\n\r\f-", true);
		StringBuffer buffer = new StringBuffer();
		while (tokenizer.hasMoreTokens())
		{
			buffer.append(firstCharUppercase(tokenizer.nextToken()));
		}
		return buffer.toString();
	}

	public static String firstCharUppercaseOnlyIfOneWordAndStartWithLowercase(String sentence)
	{
		if (StringUtil.isEmpty(sentence))
			return " ";

		StringTokenizer tokenizer = new StringTokenizer(sentence.trim(), " \t\n\r\f-", true);

		boolean moreThanOneWord = tokenizer.countTokens() > 1;
		boolean startWithLowercase =
			sentence.substring(0, 1).equals(sentence.substring(0, 1).toLowerCase());

		if (moreThanOneWord || !startWithLowercase)
			return sentence;
		else
			return firstCharUppercase(sentence);
	}

	/**
	 * Returns a string in the form 'A.D.G.'. ADG, adg, etc wordt gescheiden door een
	 * punt. N.a.v. Mantis 40442 is besloten Th.G ook aan te passen naar T.H.G.
	 * 
	 * @param sentence
	 * @return Returns a string in the form 'A.D.G.'. ADG, adg, etc wordt gescheiden door
	 *         een punt. N.a.v. Mantis 40442 is besloten Th.G ook aan te passen naar
	 *         T.H.G.
	 */
	public static String puntSeperated(String sentence)
	{
		List<String> adapted = new ArrayList<String>();

		for (int i = 0; i < sentence.length(); i++)
		{
			char c = sentence.charAt(i);
			if (c != '.')
			{
				boolean done = false;
				for (String samengesteld : SAMENGESTELDE_LETTERS)
				{
					if (sentence.substring(i).toUpperCase().startsWith(samengesteld.toUpperCase()))
					{
						adapted.add(samengesteld);
						i += samengesteld.length() - 1;
						done = true;
					}
				}
				if (!done && c != ' ')
					adapted.add(Character.toString(c).toUpperCase());

			}
			// n.a.v. Mantis 40442 wordt niet langer bij de eerste voorkomende punt de
			// originele string geretourneerd.
		}

		// volgens mij is deze escapeString niet langer noodzakelijk.
		// String puntSeperated =
		// escapeString(maakPuntSeparatedString(adapted));
		String puntSeperated = maakPuntSeparatedString(adapted);
		if (!StringUtil.isEmpty(puntSeperated))
			puntSeperated = puntSeperated + ".";

		return puntSeperated;
	}

	/**
	 * Controleerd of een String null is of uit whitespace bestaat.
	 * 
	 * @param string
	 * @return true als de string null is of als hij volledig uit whitespace bestaat,
	 *         anders false.
	 */
	public static boolean isEmpty(String string)
	{
		return string == null || string.trim().length() == 0;
	}

	public static boolean areAllEmpty(String... strings)
	{
		for (String string : strings)
		{
			if (isNotEmpty(string))
				return false;
		}
		return true;
	}

	public static boolean areAllNotEmpty(String... strings)
	{
		for (String string : strings)
		{
			if (isEmpty(string))
				return false;
		}
		return true;
	}

	/**
	 * Controleerd of een String null is of uit whitespace bestaat.
	 * 
	 * @param object
	 * @return true als het object null is of als hij volledig uit whitespace bestaat,
	 *         anders false.
	 */
	public static boolean isEmpty(Object object)
	{
		return object == null || isEmpty(object.toString());
	}

	/**
	 * Inverse van isEmpty.
	 * 
	 * @param string
	 * @return false als de string null is of als de string volledig uit whitespace
	 *         bestaat, anders true.
	 * @see #isEmpty(String)
	 */
	public static boolean isNotEmpty(String string)
	{
		return string != null && string.trim().length() > 0;
	}

	/**
	 * Inverse van isEmpty.
	 * 
	 * @param string
	 * @return false als de string null is of als de string volledig uit whitespace
	 *         bestaat, anders true.
	 * @see #isEmpty(String)
	 */
	public static boolean isNotEmpty(Object string)
	{
		return string != null && string.toString().trim().length() > 0;
	}

	/**
	 * Makes a best efford to truncate a string into the desired length. If the string
	 * gets truncated an optional string can be used to replace the part that gets cut
	 * off, the returned string (including replacement) will not be longer then maxLength.
	 * It is best to use short replacement strings. If the replacement string is too long
	 * or the maxLength too short to produce a non empty string (e.g. maxLength=3 and
	 * replacement = "..." would result normally in "") then either the replacement or if
	 * that is null (happens only if maxLength=0) the original string are returned
	 * unchanged.
	 * 
	 * @param source
	 *            the text to truncate
	 * @param maxLength
	 *            maximum length of the returned string including any cutoff relacements
	 * @param cutoffReplacement
	 *            a string that will be used to replace the cut off part (usually ...) or
	 *            null
	 * @return A string with a maximum length of maxLength, unless
	 *         maxLength-cutoffReplacement.length()&lt;=0 then the cutoffReplacement will
	 *         be returned unless that is null then the original string is returned.
	 */
	public static String truncate(String source, int maxLength, String cutoffReplacement)
	{
		if (source == null || source.length() <= maxLength)
			return source;
		int cutoffLength = maxLength;
		if (cutoffReplacement != null)
			cutoffLength -= cutoffReplacement.length();
		if (cutoffLength <= 0)
		{
			if (cutoffReplacement != null)
				return cutoffReplacement;
			return source;
		}
		if (cutoffReplacement != null)
			return source.substring(0, cutoffLength) + cutoffReplacement;
		return source.substring(0, cutoffLength);
	}

	/**
	 * Escapes the given string into an html string where all 'special' characters are
	 * escaped.
	 * 
	 * @param value
	 *            The string to be escaped
	 * @return The escaped string
	 */
	public static String escapeString(String value)
	{
		String result = null;
		try
		{
			result = URLEncoder.encode(value, "UTF-8", false);
		}
		catch (UnsupportedEncodingException ex)
		{
			throw new RuntimeException("UTF-8 not supported", ex);
		}
		return result;
	}

	public static String escapeForJavascriptString(String value)
	{
		StringBuilder sb = new StringBuilder();
		for (char curChar : value.toCharArray())
		{
			switch (curChar)
			{
				case '"':
					sb.append("\\\"");
					break;
				case '\\':
					sb.append("\\\\");
					break;
				case '\n':
					sb.append("\\n");
					break;
				case '\r':
					sb.append("\\r");
					break;
				case '\t':
					sb.append("\\t");
					break;
				case '\f':
					sb.append("\\f");
					break;
				case '\b':
					sb.append("\\b");
					break;
				default:
					sb.append(curChar);
					break;
			}
		}
		return sb.toString();
	}

	/**
	 * Geeft de text terug als deze gevuld is, of anders de lege string.
	 * 
	 * @param text
	 *            de text
	 * @return "" of de text.
	 */
	public static String valueOrEmptyIfNull(Object text)
	{
		if (text == null)
		{
			return "";
		}
		return text.toString();
	}

	/**
	 * Geeft de text terug als deze gevuld is, of anders het gegeven alternatief.
	 * 
	 * @param text
	 *            de text
	 * @param alternative
	 *            Het alternatief wanneer null
	 * @return het alternatief of de text.
	 */
	public static String valueOrAlternativeIfNull(Object text, String alternative)
	{
		if (text == null)
		{
			return alternative;
		}
		return text.toString();
	}

	/**
	 * Geeft de text terug als deze gevuld is, of anders het gegeven alternatief.
	 * 
	 * @param text
	 *            de text
	 * @param alternative
	 *            Het alternatief wanneer leeg
	 * @return het alternatief of de text.
	 */
	public static String valueOrAlternativeIfEmpty(Object text, String alternative)
	{
		if (isEmpty(text))
		{
			return alternative;
		}
		return text.toString();
	}

	/**
	 * Repeats the given string the specified amount of times. If the src String is null
	 * or the number of times to repeat is below 1 an empty string is returned.
	 * 
	 * @param src
	 * @param repeat
	 * @return a String.
	 */
	public static String repeatString(String src, int repeat)
	{
		if (src == null || repeat < 1)
			return "";
		StringBuffer buffer = new StringBuffer(src.length() * repeat);
		for (int i = 0; i < repeat; i++)
			buffer.append(src);
		return buffer.toString();
	}

	/**
	 * Maakt een teken-separated string van de gegeven lijst door toString() aan te roepen
	 * voor elk object in de lijst.
	 * 
	 * @param list
	 * @param teken
	 * @return een teken-separated string van de gegeven lijst door toString() aan te
	 *         roepen voor elk object in de lijst.
	 */
	public static String maakTekenSeparatedString(Collection< ? extends Object> list, String teken)
	{
		return toString(list, teken, "");
	}

	/**
	 * Maakt een comma-separated string van de gegeven lijst door toString() aan te roepen
	 * voor elk object in de lijst. Indien de lijst leeg of null is dan wordt de lege
	 * string gereturned.
	 * 
	 * @param list
	 * @return een comma separated string van de items in de lijst of een lege string.
	 */
	public static String maakCommaSeparatedString(Collection< ? extends Object> list)
	{
		return toString(list, ", ", "");
	}

	/**
	 * Maakt een comma-separated string van de gegeven lijst door toString() aan te roepen
	 * voor elk object in de lijst.
	 * 
	 * @param list
	 * @param defaultByEmptyList
	 *            default string voor als de lijst leeg of null is.
	 * @return een comma gescheiden lijst of de default string.
	 */
	public static String maakCommaSeparatedString(Collection< ? extends Object> list,
			String defaultByEmptyList)
	{
		return toString(list, ", ", defaultByEmptyList);
	}

	/**
	 * Maakt een teken gescheiden lijst van alle items uit de lijst. Gebruikt de default
	 * stringconverter
	 * 
	 * @param list
	 * @param token
	 * @param defaultString
	 *            optionele string die gereturned wordt als de lijst null of leeg is.
	 * @return de string
	 */
	public static <T> String toString(Collection<T> list, String token, String defaultString)
	{
		return toString(list, defaultString, new DefaultStringConverter<T>(token));
	}

	/**
	 * Maakt een teken gescheiden lijst van alle items uit de lijst.
	 * 
	 * @param <T>
	 * @param list
	 * @param defaultString
	 *            optionele string die gereturned wordt als de lijst null of leeg is.
	 * @param converter
	 * @return de string
	 */
	public static <T> String toString(Collection<T> list, String defaultString,
			StringConverter< ? super T> converter)
	{
		if (list == null || list.isEmpty())
			return defaultString;
		StringConverter< ? super T> myConverter = converter;
		if (myConverter == null)
			myConverter = new DefaultStringConverter<T>();
		StringBuilder builder = new StringBuilder(list.size() * 20); // guess
		int index = 0;
		boolean first = true;
		for (T obj : list)
		{
			if (!first)
			{
				String token = myConverter.getSeparator(index);
				if (token != null)
					builder.append(token);
			}
			String strValue = myConverter.toString(obj, index);
			if (strValue != null)
			{
				builder.append(strValue);
				first = false;
			}
			index++;
		}
		return builder.toString();
	}

	/**
	 * Maakt een punt-separated string van de gegeven lijst door toString() aan te roepen
	 * voor elk object in de lijst. Er komt geen ruimte tussen de verschillende lijst
	 * objecten.
	 * 
	 * @param list
	 * @return de elementen van de lijst gescheiden door punten
	 */
	public static String maakPuntSeparatedString(Collection< ? extends Object> list)
	{
		return toString(list, ".", "");
	}

	/**
	 * Verwijdert alle cijfers in de gegeven input string.
	 * 
	 * @param input
	 * @return de inputstring zonder cijfers
	 */
	public static String verwijderCijfers(String input)
	{
		String ret = input;
		for (int counter = 0; counter < 10; counter++)
		{
			ret = ret.replace(String.valueOf(counter), "");
		}
		return ret;
	}

	/**
	 * Voegt voorloopnullen toe aan de gegeven string zodat het resultaat de gegeven
	 * lengte krijgt.
	 * 
	 * @param str
	 * @param length
	 * @return de inputstring inclusief eventuele voorloopnullen
	 */
	public static String voegVoorloopnullenToe(String str, int length)
	{
		if (str == null || length <= str.length())
		{
			return str;
		}
		String voorloopNullen = StringUtil.repeatString("0", length - str.length());
		return voorloopNullen.concat(str);
	}

	public static String voegVoorloopnullenToe(int i, int length)
	{
		return voegVoorloopnullenToe(Integer.toString(i), length);
	}

	/**
	 * Voegt spaties toe aan het einde of kapt de string af, om exact op de aangegeven
	 * lengte uit te komen.
	 */
	public static String kapAfOfVoegSpatiesToe(String str, int length)
	{
		if (str == null)
			return spaties(length);

		if (str.length() > length)
			return str.substring(0, length);

		return str + spaties(length - str.length());
	}

	public static String kapAfOfVoegNullenToe(Integer i, int length)
	{
		String str = "";
		if (i != null)
			str = Integer.toString(i);
		if (str.length() < length)
			return voegVoorloopnullenToe(str, length);
		return str.substring(str.length() - length, str.length());
	}

	public static String kapAfOfVoegNullenToe(String str, int length)
	{
		if (str == null)
			str = "";
		if (str.length() < length)
			return voegVoorloopnullenToe(str, length);
		return str.substring(str.length() - length, str.length());
	}

	/**
	 * Verwijder voorloopnullen van de gegeven string.
	 */
	public static String verwijderVoorloopnullen(String str)
	{
		if (str == null)
		{
			return str;
		}
		int i = 0;
		while (i < str.length() && str.charAt(i) == '0')
			i++;
		return str.substring(i);
	}

	/**
	 * Telt het aantal keer dat een character voorkomt in een string
	 * 
	 * @param org
	 * @param character
	 * @return het aantal keer dat de gegeven karakter voorkomt in de gegeven string
	 */
	public static int countOccurances(String org, char character)
	{
		if (org == null)
			return 0;
		int count = 0;
		int index = 0;
		while (index >= 0)
		{
			index = org.indexOf(character, index);
			if (index >= 0)
			{
				count++;
				index++;
			}
		}
		return count;
	}

	/**
	 * @param strings
	 * @param separator
	 * @return de gegeven strings aan elkaar
	 */
	public static String join(Collection<String> strings, String separator)
	{
		String komma = "";
		StringBuilder sb = new StringBuilder();
		for (String string : strings)
		{
			sb.append(komma);
			komma = separator;
			sb.append(string);
		}
		return sb.toString();
	}

	/**
	 * @param string1
	 * @param string2
	 * @return true als string2 gelijk is aan het begin van string1 waarbij de
	 *         vergelijking hoofdletter ongevoelig is. ("V6vec12", "V6V") geeft bijv.
	 *         true, ("V6gec12", "V6v") geeft false.
	 */
	public static boolean startsWithIgnoreCase(String string1, String string2)
	{
		if (string1 == null || string2 == null)
			return false;
		int len = string2.length();
		if (string1.length() < len)
			return false;
		String test = string1.substring(0, len);

		return test.equalsIgnoreCase(string2);
	}

	/**
	 * Returns the classname of a class, this is excluding the packagename.
	 * 
	 * @param class1
	 * @return the classname.
	 * @deprecated gebruik class.getSimpleName()
	 */
	@Deprecated
	public static String getClassName(Class< ? > class1)
	{
		return class1.getSimpleName();
	}

	/**
	 * Returns the first word of the given input string.
	 * 
	 * @param input
	 *            The input string to search.
	 * @return The first word of the input string.
	 */
	public static String getFirstWord(String input)
	{
		StringTokenizer tok = new StringTokenizer(input);
		return tok.nextToken();
	}

	/**
	 * Geeft aan of de gegeven string een bekende Oracle wildcard bevat, zoals
	 * bijvoorbeeld %.
	 * 
	 * @param string
	 *            de string die gechecked moet worden
	 * @return true als de string een wildcard bevat, en anders false.
	 */
	public static boolean containsOracleWildcard(String string)
	{
		if (string != null)
		{
			for (int i = 0; i < ORACLE_WILDCARDS.length; i++)
			{
				if (string.indexOf(ORACLE_WILDCARDS[i]) > -1)
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Converteert een string met wildcards (* of ?) naar een regular expression,
	 * inclusief het escapen van regexp-characters.
	 * 
	 * @param wildcard
	 *            de string met wildcards
	 * @return De regexp
	 */
	public static String wildcardToRegex(String wildcard)
	{
		StringBuffer s = new StringBuffer(wildcard.length());
		s.append('^');
		for (int i = 0, is = wildcard.length(); i < is; i++)
		{
			char c = wildcard.charAt(i);
			switch (c)
			{
				case '*':
					s.append(".*");
					break;
				case '?':
					s.append(".");
					break;
				// escape special regexp-characters
				case '(':
				case ')':
				case '[':
				case ']':
				case '$':
				case '^':
				case '.':
				case '{':
				case '}':
				case '|':
				case '\\':
					s.append("\\");
					s.append(c);
					break;
				default:
					s.append(c);
					break;
			}
		}
		s.append('$');
		return (s.toString());
	}

	/**
	 * Converts Strings following the CammelCase standard to normal strings. e.g.
	 * TheQuickLittleBrownFox is converted to The quick little brown fox. i.e. every
	 * capital is replaced by a space and the lowercase of that capital, except for the
	 * first char and sequences of capitals like HTTP. Also _ is replaced by a space.
	 * 
	 * @param camelCase
	 * @return converted string or null if the input was null
	 */
	public static String convertCamelCase(String camelCase)
	{
		if (camelCase == null)
			return null;
		boolean containsEduArte = camelCase.contains("EduArte");
		boolean containsRedSpider = camelCase.contains("RedSpider");

		StringBuilder builder = new StringBuilder(camelCase.length() + 10);
		builder.append(camelCase.charAt(0));
		char c;
		for (int i = 1; i < camelCase.length(); i++)
		{
			c = camelCase.charAt(i);
			if (c == '_')
				builder.append(' ');
			else if (Character.isUpperCase(c) && i + 1 < camelCase.length()
				&& !Character.isUpperCase(camelCase.charAt(i + 1))
				&& !Character.isUpperCase(camelCase.charAt(i - 1)))
			{
				builder.append(' ');
				builder.append(Character.toLowerCase(c));
			}
			else
				builder.append(c);
		}
		if (containsEduArte)
		{
			replaceAll(builder, "edu arte", "EduArte");
			replaceAll(builder, "Edu arte", "EduArte");
		}
		if (containsRedSpider)
		{
			replaceAll(builder, "red spider", "RedSpider");
			replaceAll(builder, "Red spider", "RedSpider");
		}
		return builder.toString();
	}

	private static void replaceAll(StringBuilder builder, String tekst, String replaceWith)
	{
		int pos = builder.indexOf(tekst);
		while (pos != -1)
		{
			builder.replace(pos, pos + tekst.length(), replaceWith);
			pos = builder.indexOf(tekst, pos + replaceWith.length());
		}
	}

	public static String convertCamelCaseFirstCharUpperCase(String camelCase)
	{
		return convertCamelCase(firstCharUppercase(camelCase));
	}

	/**
	 * Verwijdert alle html-tags van de gegeven string.
	 * 
	 * @param html
	 *            De html-inputstring die verlost moet worden van tags.
	 * @return De tekst zonder tags.
	 */
	public static String stripHtmlTags(String html)
	{
		return html.replaceAll("\\<.*?>", "");
	}

	/**
	 * @param str1
	 * @param str2
	 * @return true indien beide strings null zijn, of als ze equal zijn, en anders false.
	 */
	public static boolean equalOrBothNull(String str1, String str2)
	{
		if (str1 == null && str2 == null)
			return true;
		if (str1 == null)
			return false;
		return str1.equals(str2);
	}

	/**
	 * Controleert of twee strings gelijk zijn, waarbij null en lege string ook als gelijk
	 * beschouwd wordt.
	 * 
	 * @param a
	 * @param b
	 * @return true als zowel String a als String b leeg zijn (isEmpty(a) && isEmpty(b)).
	 *         Anders wordt a.equals(b) teruggegeven.
	 */
	public static boolean equalOrBothEmpty(String a, String b)
	{
		if (isEmpty(a) && isEmpty(b))
			return true;
		return (a == null) ? false : a.equals(b);
	}

	/**
	 * @param voornamen
	 * @return voorletters op basis van de gegeven voornamen.
	 */
	public static String genereerVoorletters(String voornamen)
	{
		if (StringUtil.isEmpty(voornamen))
			return null;
		String[] namen = voornamen.split(" ", -1);
		StringBuilder res = new StringBuilder();
		for (String naam : namen)
		{
			if (StringUtil.isNotEmpty(naam))
			{
				boolean isSamengesteld = false;
				for (String samengesteld : SAMENGESTELDE_LETTERS)
				{
					if (naam.toUpperCase().startsWith(samengesteld.toUpperCase()))
					{
						res.append(samengesteld).append(".");
						isSamengesteld = true;
						break;
					}
				}
				if (!isSamengesteld)
				{
					res.append(naam.substring(0, 1).toUpperCase()).append(".");
				}
			}
		}
		return res.toString();
	}

	/**
	 * Returns a hex encoded representation of the byte array.
	 */
	public static String hexEncode(byte[] aInput)
	{
		StringBuffer result = new StringBuffer();
		char[] digits =
			{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
		for (int idx = 0; idx < aInput.length; ++idx)
		{
			byte b = aInput[idx];
			result.append(digits[(b & 0xf0) >> 4]);
			result.append(digits[b & 0x0f]);
		}
		return result.toString();
	}

	/**
	 * @param list
	 * @param prefix
	 * @return true indien de lijst met strings minimaal 1 string bevat die met de gegeven
	 *         prefix begint.
	 */
	public static boolean containsStringStartingWith(List<String> list, String prefix)
	{
		for (String s : list)
		{
			if (s.startsWith(prefix))
				return true;
		}
		return false;
	}

	/**
	 * Geeft <code>null</code> terug als de value <code>null</code> is, of anders de
	 * String waarde. Handig wanneer je geen <tt>"null"</tt> wilt terugkrijgen.
	 */
	public static String nullOrStringValue(Object value)
	{
		if (value == null)
			return null;

		return String.valueOf(value);
	}

	/**
	 * Geeft <tt>""</tt> terug wanneer de value <tt>null</tt> is, of anders de String
	 * waarde. Zorgt ervoor dat je dus geen <tt>"null"</tt> String waarde terugkrijgt.
	 */
	public static String emptyOrStringValue(Object value)
	{
		if (value == null)
			return "";

		return String.valueOf(value);
	}

	/**
	 * Zet de eerste letter om naar een kleine letter en laat de rest van de String
	 * ongemoeid.
	 */
	public static String firstCharLowercase(String name)
	{
		return new StringBuffer(name.length()).append(String.valueOf(name.charAt(0)).toLowerCase())
			.append(name.substring(1)).toString();
	}

	public static String spaties(int aantal)
	{
		return repeatString(" ", aantal);
	}

	/**
	 * Wijzigt de gegeven string in een geldige bestandsnaam. Tekens zoals '*' worden
	 * vervangen voor een spatie.
	 */
	public static String createValidFileName(String filename)
	{
		if (filename == null)
			return null;
		return filename.replace("/", " ").replace("\\", " ").replace("*", " ").replace("?", " ")
			.replace(":", " ").replace("|", " ").replace("\"", " ").replace("<", " ").replace(">",
				" ");
	}
}
