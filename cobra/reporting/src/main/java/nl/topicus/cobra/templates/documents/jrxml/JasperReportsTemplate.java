/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.templates.documents.jrxml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRExportProgressMonitor;
import nl.topicus.cobra.templates.FieldInfo;
import nl.topicus.cobra.templates.documents.DocumentTemplate;
import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.cobra.templates.exceptions.TemplateException;
import nl.topicus.cobra.templates.exceptions.TemplateFormattingException;
import nl.topicus.cobra.templates.exceptions.TemplateParseException;
import nl.topicus.cobra.templates.monitors.DocumentTemplateProgressMonitor;
import nl.topicus.cobra.templates.resolvers.FieldResolver;

/**
 * JasperReports template wrapper voor een .jrxml template. Geeft bij het mergen wrappers
 * mee waarmee de field resolver de parameter map kan leveren en tevens de custom
 * datasource. Output formaat is altijd PDF.
 * 
 * @author Laurens Hop
 */
public class JasperReportsTemplate implements DocumentTemplate
{
	private JasperReport report;

	private JasperPrint print;

	private OutputStream outputStream;

	private JRExportProgressMonitor monitor;

	/**
	 * default = true, in sommige gevallen wilt de gebruiker misschien de mergefield info
	 * niet meer hebben.
	 */
	private boolean keepMergeFields = KEEP_MERGE_FIELDS_DEFAULT;

	public static boolean KEEP_MERGE_FIELDS_DEFAULT = true;

	public JasperReportsTemplate(JasperReport report)
	{
		this.report = report;
	}

	/**
	 * Niet geimplementeerd, omdat JasperReports het vullen van het rapport verzorgt.
	 * 
	 * @return lege lijst
	 * @see nl.topicus.cobra.templates.documents.DocumentTemplate#getFieldInfo(nl.topicus.cobra.templates.resolvers.FieldResolver)
	 */
	@Override
	public List<FieldInfo> getFieldInfo(FieldResolver resolver)
	{
		return Collections.emptyList();
	}

	@Override
	public void mergeDocumentFooter(FieldResolver resolver)
	{

	}

	/**
	 * Retourneert het output MIME type (application/pdf)
	 * 
	 * @see nl.topicus.cobra.templates.documents.DocumentTemplate#getContentType()
	 */
	@Override
	public String getContentType()
	{
		return MIME_TYPE;
	}

	/**
	 * Het output MIME type
	 */
	public static String MIME_TYPE = "application/pdf";

	@Override
	public DocumentTemplateType getType()
	{
		return DocumentTemplateType.JRXML;
	}

	@Override
	public void mergeDocumentHeader(FieldResolver resolver) throws TemplateException
	{
		try
		{
			FieldResolverMap parameters = new FieldResolverMap(resolver);
			if (monitor != null)
				parameters.setMonitor(monitor);

			print =
				JasperFillManager.fillReport(report, parameters, new FieldResolverDataSource(
					resolver, (String) resolver.resolve(DocumentTemplate.CONTEXT_OBJECT_REF_NAME)));
		}
		catch (JRException e)
		{
			throw new TemplateParseException(e.getMessage(), e);
		}
	}

	/**
	 * Laat JasperReports het rapport vullen. Zowel voor de parameters als voor de custom
	 * datasource wordt een wrapper om de field resolver meegegeven.
	 * 
	 * @see nl.topicus.cobra.templates.documents.DocumentTemplate#mergeDocumentFooter(nl.topicus.cobra.templates.resolvers.FieldResolver)
	 */
	@Override
	public void writeDocumentFooter() throws TemplateException
	{
		try
		{
			outputStream.write(JasperExportManager.exportReportToPdf(print));
		}
		catch (IOException e)
		{
			throw new TemplateFormattingException("Kan pdf niet wegschrijven", e);
		}
		catch (JRException e)
		{
			throw new TemplateParseException("Kan template niet analyseren tot pdf", e);
		}
	}

	@Override
	public void writeDocumentHeader()
	{
	}

	@Override
	public void writeSection(FieldResolver resolver)
	{
	}

	public static JasperReportsTemplate createDocument(InputStream inStream) throws JRException
	{
		return new JasperReportsTemplate(JasperCompileManager.compileReport(inStream));
	}

	@Override
	public void setOutputStream(OutputStream outStream)
	{
		outputStream = outStream;
	}

	@Override
	public void writePageFooter()
	{
	}

	@Override
	public void writePageHeader()
	{
	}

	/**
	 * Speciale functie om een monitor aan JR te hangen om zo toch iets van een progress
	 * te krijgen.
	 * 
	 * @see nl.topicus.cobra.templates.documents.DocumentTemplate#setProgressMonitor(nl.topicus.cobra.templates.monitors.DocumentTemplateProgressMonitor)
	 */
	@Override
	public void setProgressMonitor(DocumentTemplateProgressMonitor monitor)
	{
		this.monitor = monitor;
	}

	@Override
	public OutputStream getOutputStream()
	{
		return outputStream;
	}

	/**
	 * @see nl.topicus.cobra.templates.documents.DocumentTemplate#isKeepMergeFields()
	 */
	public boolean isKeepMergeFields()
	{
		return keepMergeFields;
	}

	public void setKeepMergeFields(boolean keepMergeFields)
	{
		this.keepMergeFields = keepMergeFields;
	}

	@Override
	public boolean hasSections()
	{
		return false;
	}
}
