/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.templates;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.templates.documents.DocumentTemplate;
import nl.topicus.cobra.templates.documents.csv.CSVDocument;
import nl.topicus.cobra.templates.documents.docx.Word2007Document;
import nl.topicus.cobra.templates.documents.docx.Word2007MacroDocument;
import nl.topicus.cobra.templates.documents.docx.Word2007MacroTemplate;
import nl.topicus.cobra.templates.documents.docx.Word2007Template;
import nl.topicus.cobra.templates.documents.jrxml.JasperReportsTemplate;
import nl.topicus.cobra.templates.documents.rtf.RtfDocument;
import nl.topicus.cobra.templates.documents.xls.ExcelWorkbook;
import nl.topicus.cobra.templates.documents.xlsx.Excel2007Workbook;
import nl.topicus.cobra.templates.exceptions.TemplateCreationException;

/**
 * Registreert de beschikbare document templates. Kan op basis van (input) MIME type of
 * extensie een document template aanmaken.
 * 
 * @author Laurens Hop
 */
public class TemplateManager
{
	private static final TemplateManager instance = new TemplateManager();

	private final Map<String, Class< ? extends DocumentTemplate>> templatesByFileExt;

	private final Map<String, Class< ? extends DocumentTemplate>> templatesByMime;

	/**
	 * Private constructor. Registreert de standaard aanwezige template typen.
	 */
	private TemplateManager()
	{
		templatesByFileExt = new HashMap<String, Class< ? extends DocumentTemplate>>();
		templatesByMime = new HashMap<String, Class< ? extends DocumentTemplate>>();

		registerTemplate(".rtf", RtfDocument.MIME_TYPE, RtfDocument.class);
		registerTemplate(".docx", Word2007Document.MIME_TYPE, Word2007Document.class);
		registerTemplate(".dotx", Word2007Template.MIME_TYPE, Word2007Template.class);
		registerTemplate(".docm", Word2007MacroDocument.MIME_TYPE, Word2007MacroDocument.class);
		registerTemplate(".dotm", Word2007MacroTemplate.MIME_TYPE, Word2007MacroTemplate.class);
		registerTemplate(".jrxml", JasperReportsTemplate.MIME_TYPE, JasperReportsTemplate.class);
		registerTemplate(".xls", ExcelWorkbook.MIME_TYPE, ExcelWorkbook.class);
		registerTemplateByFileExt(".xlt", ExcelWorkbook.class);
		registerTemplate(".xlsx", Excel2007Workbook.MIME_TYPE, Excel2007Workbook.class);
		registerTemplateByFileExt(".xltx", Excel2007Workbook.class);
		registerTemplate(".csv", CSVDocument.MIME_TYPE, CSVDocument.class);
	}

	/**
	 * Retourneert de singleton instance.
	 * 
	 * @return instance
	 */
	public static TemplateManager getInstance()
	{
		return instance;
	}

	/**
	 * Registreert een nieuwe template class op basis van (input) MIME type.
	 * 
	 * @param mime
	 * @param templateClass
	 */
	public void registerTemplateByMime(String mime, Class< ? extends DocumentTemplate> templateClass)
	{
		templatesByMime.put(mime.toLowerCase(), templateClass);
	}

	/**
	 * Registreert een nieuwe template class op basis van (input) file extensie.
	 * 
	 * @param fileExt
	 * @param templateClass
	 */
	public void registerTemplateByFileExt(String fileExt,
			Class< ? extends DocumentTemplate> templateClass)
	{
		templatesByFileExt.put(getExtension(fileExt), templateClass);
	}

	/**
	 * Registreert een nieuwe template class op basis van (input) MIME type en
	 * bestandsextensie.
	 * 
	 * @param fileExt
	 * @param mime
	 * @param templateClass
	 */
	public void registerTemplate(String fileExt, String mime,
			Class< ? extends DocumentTemplate> templateClass)
	{
		registerTemplateByMime(mime.toLowerCase(), templateClass);
		registerTemplateByFileExt(fileExt, templateClass);
	}

	/**
	 * Verwijdert alle registraties van de gegeven template class.
	 * 
	 * @param templateClass
	 */
	public void unregisterTemplate(Class< ? extends DocumentTemplate> templateClass)
	{
		for (Entry<String, Class< ? extends DocumentTemplate>> entry : templatesByFileExt
			.entrySet())
		{
			if (entry.getValue().equals(templateClass))
				templatesByFileExt.remove(entry.getKey());
		}

		for (Entry<String, Class< ? extends DocumentTemplate>> entry : templatesByMime.entrySet())
		{
			if (entry.getValue().equals(templateClass))
				templatesByMime.remove(entry.getKey());
		}
	}

	/**
	 * Geeft een lijst geregistreerde MIME types.
	 * 
	 * @return gesorteerde lijst van MIME types
	 */
	public List<String> getRegisteredMimes()
	{
		List<String> mimes = new ArrayList<String>(templatesByMime.keySet());
		Collections.sort(mimes);
		return mimes;
	}

	/**
	 * Geeft een lijst geregistreerde bestandsextensies.
	 * 
	 * @return gesorteerde lijst van bestandsextensies
	 */
	public List<String> getRegisteredFileExts()
	{
		List<String> fileExts = new ArrayList<String>(templatesByFileExt.keySet());
		Collections.sort(fileExts);
		return fileExts;
	}

	/**
	 * Geeft een lijst geregistreerde MIME types.
	 * 
	 * @return gesorteerde lijst van MIME types
	 */
	public String getRegisteredMimesToString()
	{
		List<String> mimes = new ArrayList<String>(templatesByMime.keySet());
		Collections.sort(mimes);

		StringBuilder toString = new StringBuilder();
		for (String mimetype : mimes)
		{
			toString.append(mimetype);
			toString.append(", ");
		}

		if (toString.length() > 0)
			toString.delete(toString.length() - 2, toString.length() - 1);

		return toString.toString();
	}

	public DocumentTemplate createDocumentTemplateByMime(String mime, InputStream inStream)
			throws TemplateCreationException
	{
		Class< ? extends DocumentTemplate> templateClass = templatesByMime.get(mime.toLowerCase());

		return createDocumentTemplate(templateClass, inStream);
	}

	public DocumentTemplate createDocumentTemplateByFileExt(String fileExt, InputStream inStream)
			throws TemplateCreationException
	{
		String ext = getExtension(fileExt);
		Class< ? extends DocumentTemplate> templateClass = templatesByFileExt.get(ext);

		return createDocumentTemplate(templateClass, inStream);
	}

	private DocumentTemplate createDocumentTemplate(
			Class< ? extends DocumentTemplate> templateClass, InputStream inStream)
			throws TemplateCreationException
	{
		if (templateClass == null)
			throw new TemplateCreationException("Niet ondersteund document type.");

		try
		{
			return (DocumentTemplate) ReflectionUtil.invokeStaticMethod(templateClass,
				"createDocument", inStream);
		}
		catch (Exception e)
		{
			throw new TemplateCreationException("Template kon niet worden gemaakt.", e);
		}
	}

	/**
	 * Zet de extensie om naar universeel formaat: het deel vanaf de laatste '.'; lower
	 * case. Indien geen '.', wordt deze ervoor gezet.
	 * 
	 * @param fileExt
	 * @return
	 */
	private String getExtension(String fileExt)
	{
		String ext = fileExt;
		int i = ext.lastIndexOf('.');
		if (i >= 0)
		{
			ext = ext.substring(i);
		}
		else
			ext = '.' + ext;
		return ext.toLowerCase();
	}

	public boolean isRegisteredFileExtension(String fileName)
	{
		return getRegisteredFileExts().contains(getExtension(fileName));
	}
}
