package nl.topicus.cobra.templates.documents.docx;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.xpath.XPathExpressionException;

import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.cobra.templates.exceptions.TemplateCreationException;
import nl.topicus.cobra.templates.exceptions.TemplateException;

public class Word2007Template extends Word2007Document
{
	@SuppressWarnings("hiding")
	public static String MIME_TYPE =
		"application/vnd.openxmlformats-officedocument.wordprocessingml.template";

	public Word2007Template() throws XPathExpressionException
	{
		super();
	}

	@Override
	public DocumentTemplateType getType()
	{
		return DocumentTemplateType.DOTX;
	}

	public static Word2007Template createDocument(InputStream inStream) throws TemplateException
	{
		Word2007Template worddoc;

		try
		{
			worddoc = new Word2007Template();
			worddoc.read(inStream);
		}
		catch (IOException e)
		{
			throw new TemplateCreationException("Word template kon niet worden gemaakt.", e);
		}
		catch (XPathExpressionException e)
		{
			throw new TemplateCreationException("Word template kon niet worden gemaakt.", e);
		}

		return worddoc;
	}

}
