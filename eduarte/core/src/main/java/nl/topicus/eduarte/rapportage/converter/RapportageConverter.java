package nl.topicus.eduarte.rapportage.converter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.cobra.templates.exceptions.TemplateException;
import nl.topicus.cobra.templates.exceptions.TemplateFormattingException;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.app.EduArteApp;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.quartz.JobExecutionException;

public class RapportageConverter
{

	/**
	 * Indien nodig converteren we het document tot een ander type.
	 * 
	 * @param filename
	 *            de naam die het meegegeven document representeerd.
	 */
	public void convertDocument(nl.topicus.cobra.templates.documents.DocumentTemplate document,
			DocumentTemplateType startType, DocumentTemplateType eindType, String filename,
			boolean returnAsZipFile, EduArteApp eduArteApp) throws TemplateException
	{
		if (eindType.equals(startType))
			return;

		if (StringUtil.isEmpty(eduArteApp.getOOServletURL()))
			throw new TemplateFormattingException(
				"Fout tijdens het converteren van het document formaat.",
				new JobExecutionException("Kon de locatie van Open Office niet vinden."));

		try
		{
			PostMethod filePost = new PostMethod(eduArteApp.getOOServletURL());
			filePost.getParams().setBooleanParameter(HttpMethodParams.USE_EXPECT_CONTINUE, false);

			Part[] parts =
				{
					new StringPart("format", eindType.getFileExtension()),
					new StringPart("zip", String.valueOf(returnAsZipFile)),
					new FilePart("file", new ByteArrayPartSource(filename,
						((ByteArrayOutputStream) document.getOutputStream()).toByteArray()))};

			filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));

			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

			int status = client.executeMethod(filePost);

			if (status == HttpStatus.SC_OK)
			{
				byte[] response = filePost.getResponseBody();
				document.setOutputStream(new ByteArrayOutputStream());
				document.getOutputStream().write(response);
			}
			else
				throw new TemplateFormattingException("Verkeerde reponse van Open Office, status: "
					+ String.valueOf(status));
		}
		catch (IOException e)
		{
			throw new TemplateFormattingException(
				"Fout tijdens het converteren van het document formaat.",
				new JobExecutionException("Kon geen verbinding maken met Open Office.", e));
		}
	}

}
