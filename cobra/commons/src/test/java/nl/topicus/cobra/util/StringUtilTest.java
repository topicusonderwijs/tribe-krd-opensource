package nl.topicus.cobra.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringUtilTest
{
	@Test
	public void getNumberPart1()
	{
		String test = "t.o. 12 a";
		assertEquals(new Integer(12), StringUtil.getFirstNumberSequence(test));
	}

	@Test
	public void getNumberPart2()
	{
		String test = "";
		assertNull(StringUtil.getFirstNumberSequence(test));
	}

	@Test
	public void getNumberPart3()
	{
		String test = "abcdefg";
		assertNull(StringUtil.getFirstNumberSequence(test));
	}

	@Test
	public void getNumberPart4()
	{
		String test = "12345678";
		assertEquals(new Integer(12345678), StringUtil.getFirstNumberSequence(test));
	}

	@Test
	public void getNumberPart5()
	{
		String test = "1234-a";
		assertEquals(new Integer(1234), StringUtil.getFirstNumberSequence(test));
	}

	@Test
	public void getNumberPart6()
	{
		String test = "123a";
		assertEquals(new Integer(123), StringUtil.getFirstNumberSequence(test));
	}

	@Test
	public void getNumberPart7()
	{
		String test = "a123";
		assertEquals(new Integer(123), StringUtil.getFirstNumberSequence(test));
	}

	@Test
	public void getNumberPart8()
	{
		String test = "a-123";
		assertEquals(new Integer(123), StringUtil.getFirstNumberSequence(test));
	}

	@Test
	public void truncate1()
	{
		String org = "test";
		String replace = "..";
		int max = 500;
		assertEquals(org, StringUtil.truncate(org, max, replace));
	}

	@Test
	public void truncate2()
	{
		String org = "test";
		int max = 3;
		assertEquals("tes", StringUtil.truncate(org, max, null));
	}

	@Test
	public void truncate3()
	{
		String org = "test";
		String replace = "..";
		int max = 3;
		assertEquals("t..", StringUtil.truncate(org, max, replace));
	}

	@Test
	public void truncate4()
	{
		String replace = "..";
		int max = 500;
		assertNull(StringUtil.truncate(null, max, replace));
	}

	@Test
	public void truncate5()
	{
		String org = "test";
		String replace = "..";
		int max = 0;
		assertEquals(replace, StringUtil.truncate(org, max, replace));
	}

	@Test
	public void truncate6()
	{
		String org = "test";
		int max = 0;
		assertEquals(org, StringUtil.truncate(org, max, null));
	}

	@Test
	public void truncate7()
	{
		String org = "test";
		int max = -10;
		assertEquals(org, StringUtil.truncate(org, max, null));
	}

	@Test
	public void samenGesteldeLetter()
	{
		String naam = "IJsbrand";
		String naam1 = "Pietje";

		assertEquals("IJ", StringUtil.containsSamengesteldeLetter(naam));
		assertEquals("P", StringUtil.containsSamengesteldeLetter(naam1));
	}

	@Test
	public void voornamenToVoorletters()
	{
		String voornaam = "jan";
		String voornaam1 = "ijsbrand";

		assertEquals("Jan", StringUtil.firstCharUppercase(voornaam));
		assertEquals("IJsbrand", StringUtil.firstCharUppercase(voornaam1));
		assertEquals("J.", StringUtil.genereerVoorletters(voornaam));
		assertEquals("IJ.", StringUtil.genereerVoorletters(voornaam1));
		assertEquals("J.IJ.", StringUtil.genereerVoorletters(voornaam + " " + voornaam1));
		assertEquals("J.", StringUtil.genereerVoorletters(voornaam + "-" + voornaam1));
	}

	@Test
	public void voorvoegselsEersteLetterHoofdletter()
	{
		assertEquals("'T", StringUtil.firstLetterUppercase("'t"));
		assertEquals("T", StringUtil.firstLetterUppercase("t"));
		assertEquals("Ten", StringUtil.firstLetterUppercase("ten"));
		assertEquals("10 Links", StringUtil.firstLetterUppercase("10 links"));
		assertEquals("******", StringUtil.firstLetterUppercase("******"));
	}

	@Test
	public void puntSeparated()
	{
		assertEquals("A.", StringUtil.puntSeperated("a"));
		assertEquals("A.B.C.", StringUtil.puntSeperated("abc"));
		assertEquals("", StringUtil.puntSeperated(""));
		assertEquals("IJ.K.", StringUtil.puntSeperated("IJK"));
		assertEquals("K.IJ.", StringUtil.puntSeperated("kij"));
		assertEquals("K.I.J.", StringUtil.puntSeperated("ki.j"));
		assertEquals("A.F.T.H.", StringUtil.puntSeperated("A.F.Th"));
	}

	@Test
	public void countOccurances()
	{
		assertEquals(0, StringUtil.countOccurances(null, '.'));
		assertEquals(0, StringUtil.countOccurances("", '.'));
		assertEquals(0, StringUtil.countOccurances(" ", '.'));
		assertEquals(0, StringUtil.countOccurances("abcdefgh", '.'));
		assertEquals(1, StringUtil.countOccurances(".", '.'));
		assertEquals(1, StringUtil.countOccurances("a.b", '.'));
		assertEquals(1, StringUtil.countOccurances("a.", '.'));
		assertEquals(2, StringUtil.countOccurances("a..", '.'));
		assertEquals(2, StringUtil.countOccurances("..a", '.'));
		assertEquals(2, StringUtil.countOccurances("a.b.", '.'));
	}

	@Test
	public void toStringTest()
	{
		assertNull("_", StringUtil.toString(null, "-", (String) null));
		assertEquals("_", StringUtil.toString(null, "-", "_"));
		assertEquals("_", StringUtil.toString(JavaUtil.asList(), "-", "_"));
		assertEquals("1", StringUtil.toString(JavaUtil.asList(1), "-", "_"));
		assertEquals("1-2", StringUtil.toString(JavaUtil.asList(1, 2), "-", "_"));
		assertEquals("1-2-null", StringUtil.toString(JavaUtil.asList(1, 2, null), "-", "_"));
		assertEquals("12", StringUtil.toString(JavaUtil.asList(1, 2), null, "_"));
		assertEquals("null", StringUtil.toString(JavaUtil.asList((Object) null), null, "_"));
		assertEquals("nullnullnull",
			StringUtil.toString(JavaUtil.asList(null, null, null), null, "_"));
	}

	@Test
	public void convertCamelCase()
	{
		String[] camels =
			new String[] {"EduArteDeelnemerMutatieLogVerwerker",
				"EenEduArteDeelnemerMutatieLogVerwerker", "RedSpiderDeelnemerWijzigingenVerwerker",
				"TheQuickLittleBrownFox", "HTTP", "HTTPRequest", "STATIC_CONSTANT", null};
		String[] converted =
			new String[] {"EduArte deelnemer mutatie log verwerker",
				"Een EduArte deelnemer mutatie log verwerker",
				"RedSpider deelnemer wijzigingen verwerker", "The quick little brown fox", "HTTP",
				"HTTPRequest", "STATIC CONSTANT", null};
		int i = 0;
		for (String camel : camels)
		{
			assertEquals(converted[i], StringUtil.convertCamelCase(camel));
			i++;
		}
	}

	@Test
	public void checkMatchesRegExp()
	{
		assertTrue(StringUtil.checkMatchesRegExp("test", "abc", ".*"));
		assertTrue(StringUtil.checkMatchesRegExp("test", "abc", "\\w*"));
		assertTrue(StringUtil.checkMatchesRegExp("test", "abc", "\\w*"));
		assertFalse(StringUtil.checkMatchesRegExp("test", "a$bc", "\\w*"));
		assertTrue(StringUtil.checkMatchesRegExp("brincode", "00GT", "[0-9]{2}?[a-zA-Z]{2}?"));
		assertTrue(StringUtil.checkMatchesRegExp("brincode", "00gt", "[0-9]{2}?[a-zA-Z]{2}?"));
		assertFalse(StringUtil.checkMatchesRegExp("brincode", "000g/", "[0-9]{2}?[a-zA-Z]{2}?"));
	}

	@Test
	public void emptyOrStringValue()
	{
		assertEquals("", StringUtil.emptyOrStringValue(null));
		assertEquals("1", StringUtil.emptyOrStringValue(1));
	}

	@Test
	public void nullOrStringValue()
	{
		assertNull(StringUtil.nullOrStringValue(null));
		assertEquals("1", StringUtil.nullOrStringValue(1));
	}

	@Test
	public void checkIsIpAdres()
	{
		assertTrue(StringUtil.isIpAdres("1.1.1.1"));
		assertTrue(StringUtil.isIpAdres("192.168.1.1"));
		assertTrue(StringUtil.isIpAdres("255.255.255.255"));

		assertFalse(StringUtil.isIpAdres("192.168.1.x"));
		assertFalse(StringUtil.isIpAdres("192.168.1."));
		assertFalse(StringUtil.isIpAdres("192.168.1.-1"));
		assertFalse(StringUtil.isIpAdres("322.168.1.1"));
		assertFalse(StringUtil.isIpAdres("192.256.1.1"));
		assertFalse(StringUtil.isIpAdres("192.168.999.1"));
		assertFalse(StringUtil.isIpAdres("192.168.1.876"));
	}
}