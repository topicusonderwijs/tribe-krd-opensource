package nl.topicus.cobra.app.converters;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Locale;

import nl.topicus.cobra.converters.BigDecimalConverter;

import org.apache.wicket.util.convert.ConversionException;
import org.junit.Test;

public class BigDecimalConverterTest
{
	Locale defaultLocale = new Locale("nl", "NL");

	@Test
	public void geheelGetalToBigDecimal()
	{
		assertEquals(new BigDecimal("42"), new BigDecimalConverter().convertToObject("42",
			defaultLocale));
	}

	@Test
	public void geheelGetalToString()
	{
		assertEquals("42", new BigDecimalConverter().convertToString(new BigDecimal("42"),
			defaultLocale));
	}

	@Test
	public void kommaGetalMet1DigitToBigDecimal()
	{
		assertEquals(new BigDecimal("3.5"), new BigDecimalConverter().convertToObject("3,5",
			defaultLocale));
	}

	@Test
	public void kommaGetalMet1DigitToString()
	{
		assertEquals("3,5", new BigDecimalConverter().convertToString(new BigDecimal("3.5"),
			defaultLocale));
	}

	@Test
	public void puntGetalMet1DigitToBigDecimal()
	{
		assertEquals(new BigDecimal("3.5"), new BigDecimalConverter().convertToObject("3.5",
			defaultLocale));
	}

	@Test
	public void puntGetalMet1DigitToString()
	{
		assertEquals("3,5", new BigDecimalConverter().convertToString(new BigDecimal("3.5"),
			defaultLocale));
	}

	@Test
	public void kommaGetalMet3DigitsToBigDecimal()
	{
		assertEquals(new BigDecimal("3.555"), new BigDecimalConverter().convertToObject("3,555",
			defaultLocale));
	}

	@Test
	public void kommaGetalMet3DigitsToString()
	{
		assertEquals("3,555", new BigDecimalConverter().convertToString(new BigDecimal("3.555"),
			defaultLocale));
	}

	@Test
	public void puntGetalMet3DigitsToBigDecimal()
	{
		assertEquals(new BigDecimal("3.555"), new BigDecimalConverter().convertToObject("3.555",
			defaultLocale));
	}

	@Test
	public void puntGetalMet3DigitsToString()
	{
		assertEquals("3,555", new BigDecimalConverter().convertToString(new BigDecimal("3.555"),
			defaultLocale));
	}

	@Test
	public void grootGetal1ToBigDecimal()
	{
		assertEquals(new BigDecimal("123456789"), new BigDecimalConverter().convertToObject(
			"123456789", defaultLocale));
	}

	@Test
	public void grootGetal1ToString()
	{
		assertEquals("123.456.789", new BigDecimalConverter().convertToString(new BigDecimal(
			"123456789"), defaultLocale));
	}

	@Test
	public void grootGetal2ToBigDecimal()
	{
		assertEquals(new BigDecimal("123456789"), new BigDecimalConverter().convertToObject(
			"123,456,789", defaultLocale));
	}

	@Test
	public void grootGetal3ToBigDecimal()
	{
		assertEquals(new BigDecimal("123456789"), new BigDecimalConverter().convertToObject(
			"123.456.789", defaultLocale));
	}

	@Test
	public void grootGetalMetKommaToBigDecimal()
	{
		assertEquals(new BigDecimal("123456789.0"), new BigDecimalConverter().convertToObject(
			"123,456,789.0", defaultLocale));
	}

	@Test
	public void grootGetalMetKommaToString()
	{
		assertEquals("123.456.789,0", new BigDecimalConverter().convertToString(new BigDecimal(
			"123456789.0"), defaultLocale));
	}

	@Test
	public void grootGetalMetPuntToBigDecimal()
	{
		assertEquals(new BigDecimal("123456789.0"), new BigDecimalConverter().convertToObject(
			"123.456.789,0", defaultLocale));
	}

	@Test
	public void getalMet2DigitsToBigDecimal()
	{
		assertEquals(new BigDecimal("17.00"), new BigDecimalConverter(2).convertToObject("17",
			defaultLocale));
	}

	@Test
	public void getalMet2DigitsToString()
	{
		assertEquals("17,00", new BigDecimalConverter(2).convertToString(new BigDecimal("17"),
			defaultLocale));
	}

	@Test
	public void getalMetVeelDigitsToBigDecimal()
	{
		assertEquals(new BigDecimal("17.123456789"), new BigDecimalConverter().convertToObject(
			"17,123456789", defaultLocale));
	}

	@Test
	public void getalMetVeelDigitsToString()
	{
		assertEquals("17,123456789", new BigDecimalConverter().convertToString(new BigDecimal(
			"17.123456789"), defaultLocale));
	}

	@Test
	public void getalMet3DigitsToBigDecimalMet2Digits()
	{
		assertEquals(new BigDecimal("17.50"), new BigDecimalConverter(2).convertToObject("17,499",
			defaultLocale));
	}

	@Test
	public void getalMet3DigitsToStringMet2Digits()
	{
		assertEquals("17,50", new BigDecimalConverter(2).convertToString(new BigDecimal("17.499"),
			defaultLocale));
	}

	@Test
	public void getalMetVeelDigitsToBigDecimalMet2Digits()
	{
		assertEquals(new BigDecimal("17.12"), new BigDecimalConverter(2).convertToObject(
			"17,123456789", defaultLocale));
	}

	@Test
	public void getalMetVeelDigitsToStringMet2Digits()
	{
		assertEquals("17,12", new BigDecimalConverter(2).convertToString(new BigDecimal(
			"17.123456789"), defaultLocale));
	}

	@Test
	public void getalMet1DigitToBigDecimalMet2Digits()
	{
		assertEquals(new BigDecimal("17.50"), new BigDecimalConverter(2).convertToObject("17,5",
			defaultLocale));
	}

	@Test
	public void getalMet1DigitToStringMet2Digits()
	{
		assertEquals("17,50", new BigDecimalConverter(2).convertToString(new BigDecimal("17.5"),
			defaultLocale));
	}

	@Test(expected = ConversionException.class)
	public void geenNummer()
	{
		new BigDecimalConverter().convertToObject("aap", defaultLocale);
	}

	@Test
	public void puntenOpDeVerkeerdePlek()
	{
		assertEquals(new BigDecimal("10000"), new BigDecimalConverter().convertToObject("1.00.00",
			defaultLocale));
	}

	@Test
	public void kommasOpDeVerkeerdePlek()
	{
		assertEquals(new BigDecimal("10000"), new BigDecimalConverter().convertToObject("1,00,00",
			defaultLocale));
	}

	@Test
	public void puntenOpDeVerkeerdePlekEnEenKomma()
	{
		assertEquals(new BigDecimal("10000.42"), new BigDecimalConverter().convertToObject(
			"1.00.00,42", defaultLocale));
	}

	@Test
	public void kommasOpDeVerkeerdePlekEnEenPunt()
	{
		assertEquals(new BigDecimal("10000.42"), new BigDecimalConverter().convertToObject(
			"1,00,00.42", defaultLocale));
	}

	@Test(expected = ConversionException.class)
	public void puntenEnKommasVerkeerdom()
	{
		assertEquals(new BigDecimal("1003000.421"), new BigDecimalConverter().convertToObject(
			"1.003,000,421", defaultLocale));
	}

	@Test(expected = ConversionException.class)
	public void kommasEnPuntenVerkeerdom()
	{
		assertEquals(new BigDecimal("1003000.421"), new BigDecimalConverter().convertToObject(
			"1,003.000.421", defaultLocale));
	}

	/**
	 * A bigdecimal tale... (Hobbit anyone?)
	 */
	@Test
	public void thereAndBackAgainAndThereBigDecimal()
	{
		BigDecimalConverter converter = new BigDecimalConverter();

		assertEquals(new BigDecimal("3500"), converter.convertToObject(converter.convertToString(
			converter.convertToObject("3500", defaultLocale), defaultLocale), defaultLocale));
	}

	@Test
	public void thereAndBackAgainAndThereString()
	{
		BigDecimalConverter converter = new BigDecimalConverter();

		assertEquals("3500", converter.convertToString(converter.convertToObject(converter
			.convertToString(new BigDecimal("3500"), defaultLocale), defaultLocale), defaultLocale));
	}

	@Test
	public void insertDotHeleCijfer()
	{
		assertEquals(new BigDecimal("7.5"), new BigDecimalConverter(1, true).convertToObject("75",
			defaultLocale));
	}

	@Test
	public void insertDot2HeleCijfer2Decimalen()
	{
		assertEquals(new BigDecimal("0.75"), new BigDecimalConverter(2, true).convertToObject("75",
			defaultLocale));
	}

	@Test
	public void insertDotCijfer1Decimaal()
	{
		assertEquals(new BigDecimal("7.50"), new BigDecimalConverter(2, true).convertToObject(
			"7,5", defaultLocale));
	}
}
