package nl.topicus.cobra.templates.documents.docx;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.xpath.XPathExpressionException;

import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.cobra.templates.exceptions.TemplateCreationException;
import nl.topicus.cobra.templates.exceptions.TemplateException;

public class Word2007MacroDocument extends Word2007Document
{
	@SuppressWarnings("hiding")
	public static String MIME_TYPE = "application/vnd.ms-word.document.macroEnabled.12";

	public Word2007MacroDocument() throws XPathExpressionException
	{
		super();
	}

	@Override
	public DocumentTemplateType getType()
	{
		return DocumentTemplateType.DOCM;
	}

	public static Word2007MacroDocument createDocument(InputStream inStream)
			throws TemplateException
	{
		Word2007MacroDocument worddoc;

		try
		{
			worddoc = new Word2007MacroDocument();
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
