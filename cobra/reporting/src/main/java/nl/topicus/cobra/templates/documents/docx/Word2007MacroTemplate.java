package nl.topicus.cobra.templates.documents.docx;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.xpath.XPathExpressionException;

import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.cobra.templates.exceptions.TemplateCreationException;
import nl.topicus.cobra.templates.exceptions.TemplateException;

public class Word2007MacroTemplate extends Word2007Document
{
	@SuppressWarnings("hiding")
	public static String MIME_TYPE = "application/vnd.ms-word.template.macroEnabled.12";

	public Word2007MacroTemplate() throws XPathExpressionException
	{
		super();
	}

	@Override
	public DocumentTemplateType getType()
	{
		return DocumentTemplateType.DOTM;
	}

	public static Word2007MacroTemplate createDocument(InputStream inStream)
			throws TemplateException
	{
		Word2007MacroTemplate worddoc;

		try
		{
			worddoc = new Word2007MacroTemplate();
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
