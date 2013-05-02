package nl.topicus.cobra.templates.documents.docx;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.templates.TemplateManager;
import nl.topicus.cobra.templates.exceptions.TemplateException;
import nl.topicus.cobra.templates.mocks.MockDeelnemer;
import nl.topicus.cobra.templates.resolvers.BeanPropertyResolver;
import nl.topicus.cobra.templates.resolvers.FieldResolver;

import org.junit.Test;

public class DocxTabNewLineTest
{
	@Test
	public void testTabNewLine() throws TemplateException, IOException
	{
		List<MockDeelnemer> deelnemers = MockDeelnemer.createDeelnemerList(1);

		InputStream inStream = null;
		OutputStream outStream = null;

		try
		{
			inStream = DocxEtikettenTest.class.getResourceAsStream("/tabnewlinetest.docx");

			Word2007Document docx =
				(Word2007Document) TemplateManager.getInstance().createDocumentTemplateByMime(
					Word2007Document.MIME_TYPE, inStream);
			docx.setKeepMergeFields(false); // uiteraard

			outStream = new FileOutputStream("target/tabnewlinetest.docx", false);

			docx.setOutputStream(outStream);

			docx.writeDocumentHeader();

			for (MockDeelnemer deelnemer : deelnemers)
			{
				Map<String, Object> context = new HashMap<String, Object>();
				context.put("deelnemer", deelnemer);

				FieldResolver resolver = new BeanPropertyResolver(context);

				docx.writeSection(resolver);
			}

			docx.writeDocumentFooter();
		}
		finally
		{
			if (inStream != null)
				inStream.close();
			if (outStream != null)
				outStream.close();
		}
	}
}
