package nl.topicus.cobra.templates.parsers.rtf;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Calendar;

import nl.topicus.cobra.templates.documents.rtf.RtfDocument;
import nl.topicus.cobra.templates.exceptions.TemplateException;
import nl.topicus.cobra.templates.resolvers.DummyFieldResolver;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etranslate.tm.processing.rtf.ParseException;
import com.etranslate.tm.processing.rtf.RTFParser;
import com.etranslate.tm.processing.rtf.RTFParserDelegate;

public class RTFParserTest
{
	private static Logger log = LoggerFactory.getLogger(RTFParserTest.class);

	private Timer timer;

	@Before
	public void beforeTest()
	{
		timer = new Timer();
	}

	@After
	public void afterTest()
	{
		timer = null;
	}

	@Test
	public void parseJDK16_11_Licence() throws FileNotFoundException, IOException, ParseException,
			TemplateException
	{
		testRtfDocument("/rtf-examples/JDK16_11_LICENSE.rtf");
	}

	@Test
	public void parseWindowsLiveMessengerLicence() throws FileNotFoundException, IOException,
			ParseException, TemplateException
	{
		testRtfDocument("/rtf-examples/WindowsLiveMessengerLicense.rtf");
	}

	@Test
	public void parseBusinessObjectsLicenceEN() throws FileNotFoundException, IOException,
			ParseException, TemplateException
	{
		testRtfDocument("/rtf-examples/BusinessObjectsLicense_en.rtf");
	}

	@Test
	public void parseBusinessObjectsLicenceDE() throws FileNotFoundException, IOException,
			ParseException, TemplateException
	{
		testRtfDocument("/rtf-examples/BusinessObjectsLicense_de.rtf");
	}

	@Test
	public void parseBusinessObjectsLicenceZH() throws FileNotFoundException, IOException,
			ParseException, TemplateException
	{
		testRtfDocument("/rtf-examples/BusinessObjectsLicense_zh_CN.rtf");
	}

	@Test
	public void parseCarManufacturersWord2003Template() throws FileNotFoundException, IOException,
			ParseException, TemplateException
	{
		testRtfDocument("/carmanufacturers_Word2003.rtf");
	}

	@Test
	public void parseCarManufacturersWord2007Template() throws FileNotFoundException, IOException,
			ParseException, TemplateException
	{
		testRtfDocument("/carmanufacturers_Word2007.rtf");
	}

	private void testRtfDocument(String filename) throws FileNotFoundException, IOException,
			ParseException, TemplateException
	{
		log.info("Testing rtf document '" + filename + "'...");

		RtfDocument doc = parseRtfDocument(filename);

		Assert.assertNotNull(doc);

		String docAsString = writeRtfDocumentToString(doc);

		Assert.assertNotNull(docAsString);
		Assert.assertTrue(docAsString.length() > 0);

		verifyRtfDocument(RTFParserTest.class.getResourceAsStream(filename), docAsString);
	}

	private RtfDocument parseRtfDocument(String filename) throws ParseException
	{
		RTFParserDelegate delegate = new RTFParserDelegateImpl();

		RTFParser parser = new RTFParser(RTFParserTest.class.getResourceAsStream(filename));
		parser.setDelegate(delegate);

		timer.startTimer();

		parser.parse();

		timer.stopTimer();
		timer.log("Parsing");

		return ((RTFParserDelegateImpl) delegate).getDocument();
	}

	private String writeRtfDocumentToString(RtfDocument doc) throws TemplateException
	{
		ByteArrayOutputStream os = null;

		try
		{
			os = new ByteArrayOutputStream();
			doc.setOutputStream(os);

			timer.startTimer();

			doc.writeDocumentHeader();
			doc.writeSection(new DummyFieldResolver());
			doc.writeDocumentFooter();

			String text = os.toString();

			timer.stopTimer();
			timer.log("Writing");

			return text;
		}
		finally
		{
			try
			{
				if (os != null)
					os.close();
			}
			catch (Exception e)
			{
				// ignore
			}
		}
	}

	private void verifyRtfDocument(InputStream original, String doc) throws IOException
	{
		timer.startTimer();

		BufferedReader r1 = new BufferedReader(new InputStreamReader(original));
		BufferedReader r2 = new BufferedReader(new StringReader(doc));

		int line = 0;
		boolean end = false;
		while (!end)
		{
			line++;
			String s1 = r1.readLine();
			String s2 = r2.readLine();
			Assert.assertEquals("Mismatch on line " + line, s1, s2);
			end = s1 == null;
		}

		timer.stopTimer();
		timer.log("Verifying");
	}

	private class Timer
	{
		private Long startTime, stopTime;

		public void startTimer()
		{
			startTime = getTimeInMillis();
		}

		public void stopTimer()
		{
			stopTime = getTimeInMillis();
		}

		private Long getTimeInMillis()
		{
			return Calendar.getInstance().getTimeInMillis();
		}

		public void log(String action)
		{
			log.info(action + " took " + String.valueOf(stopTime - startTime) + "ms.");
		}
	}
}
