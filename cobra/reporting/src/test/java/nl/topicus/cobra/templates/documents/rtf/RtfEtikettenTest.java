package nl.topicus.cobra.templates.documents.rtf;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.templates.TemplateManager;
import nl.topicus.cobra.templates.exceptions.InvalidTemplateException;
import nl.topicus.cobra.templates.exceptions.TemplateException;
import nl.topicus.cobra.templates.mocks.MockDeelnemer;
import nl.topicus.cobra.templates.resolvers.BeanPropertyResolver;
import nl.topicus.cobra.templates.resolvers.FieldResolver;
import nl.topicus.cobra.templates.validators.BeanPropertyRtfValidator;

import org.junit.Test;

public class RtfEtikettenTest
{
	@Test
	public void testEtiketten() throws FileNotFoundException, IOException, TemplateException
	{
		List<MockDeelnemer> deelnemers = MockDeelnemer.createDeelnemerList(50);

		InputStream inStream = null;
		OutputStream outStream = null;
		OutputStreamWriter writer = null;

		try
		{
			inStream = RtfDocumentTest.class.getResourceAsStream("/etiketten.rtf");

			RtfDocument rtf =
				(RtfDocument) TemplateManager.getInstance().createDocumentTemplateByMime(
					RtfDocument.MIME_TYPE, inStream);
			rtf.setKeepMergeFields(false); // uiteraard

			List<String> errors = validateTemplateAndReturnErrors(rtf);
			boolean isValid = errors == null;
			if (isValid)
			{
				outStream = new FileOutputStream("target/etiketten.rtf", false);

				rtf.setOutputStream(outStream);

				rtf.writeDocumentHeader();

				for (MockDeelnemer deelnemer : deelnemers)
				{
					Map<String, Object> context = new HashMap<String, Object>();
					context.put("deelnemer", deelnemer);

					FieldResolver resolver = new BeanPropertyResolver(context);

					rtf.writeSection(resolver);
				}

				rtf.writeDocumentFooter();
			}
			else
			{
				outStream = new FileOutputStream("target/errors_etiketten.txt", false);
				writer = new OutputStreamWriter(outStream);

				StringBuffer buffer = new StringBuffer();
				buffer
					.append("De template is niet valide. Controleer of alle velden bestaan. \r\n");
				if (errors != null)
				{
					for (String error : errors)
					{
						buffer.append(error + "\r\n");
					}
				}
				writer.write(buffer.toString());

				throw new InvalidTemplateException("De template is niet valide.");
			}
		}
		finally
		{
			if (inStream != null)
				inStream.close();
			if (writer != null)
				writer.close();
			if (outStream != null)
				outStream.close();
		}

	}

	private List<String> validateTemplateAndReturnErrors(RtfDocument document)
	{
		BeanPropertyResolver resolver = new BeanPropertyResolver(getDefaultContext());
		BeanPropertyRtfValidator validator = new BeanPropertyRtfValidator(document);
		return validator.validateAndReturnErrors(resolver);
	}

	private Object getDefaultContext()
	{
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("deelnemer", new MockDeelnemer());

		return context;
	}

}
