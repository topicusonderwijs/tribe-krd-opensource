package nl.topicus.cobra.templates.documents.rtf;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nl.topicus.cobra.templates.FieldInfo;
import nl.topicus.cobra.templates.documents.DocumentTemplate;
import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.cobra.templates.exceptions.TemplateCreationException;
import nl.topicus.cobra.templates.exceptions.TemplateException;
import nl.topicus.cobra.templates.exceptions.TemplateParseException;
import nl.topicus.cobra.templates.monitors.DocumentTemplateProgressMonitor;
import nl.topicus.cobra.templates.parsers.rtf.RTFParserDelegateImpl;
import nl.topicus.cobra.templates.resolvers.FieldResolver;

import com.etranslate.tm.processing.rtf.ParseException;
import com.etranslate.tm.processing.rtf.RTFParser;
import com.etranslate.tm.processing.rtf.RTFParserDelegate;

public class RtfDocument extends AbstractRtfGroup implements DocumentTemplate
{
	/**
	 * Het output MIME type
	 */
	public static String MIME_TYPE = "text/rtf";

	private RtfSection section = null;

	private RtfSection currentSection = null;

	private Iterator<RtfField> fieldIterator = null;

	private boolean isFirstSection = true;

	private BufferedWriter outWriter;

	private OutputStream outStream;

	private DocumentTemplateProgressMonitor monitor;

	/**
	 * default = true, in sommige gevallen wilt de gebruiker misschien de mergefield info
	 * niet meer hebben.
	 */
	private boolean keepMergeFields = KEEP_MERGE_FIELDS_DEFAULT;

	public static boolean KEEP_MERGE_FIELDS_DEFAULT = true;

	@Override
	public String getContentType()
	{
		return MIME_TYPE;
	}

	@Override
	public DocumentTemplateType getType()
	{
		return DocumentTemplateType.RTF;
	}

	@Override
	public List<FieldInfo> getFieldInfo(FieldResolver resolver)
	{
		List<RtfField> fields = getFields();
		List<FieldInfo> result = new ArrayList<FieldInfo>(fields.size());

		// Alle header/footer fields
		for (RtfField field : fields)
		{
			FieldInfo info = field.getFieldInfo(resolver);
			if (info != null)
				result.add(info);
		}

		if (section != null)
		{
			// Alle repeatable section fields
			for (RtfField field : section.getFields())
			{
				FieldInfo info = field.getFieldInfo(resolver);
				if (info != null)
					result.add(info);
			}
		}

		return result;
	}

	public void setSection(RtfSection section)
	{
		this.section = section;
	}

	private BufferedWriter getOutputWriter()
	{
		if (outWriter == null)
			throw new IllegalStateException(
				"Er is geen outputstream beschikbaar, gebruik setOutputStream.");

		return outWriter;
	}

	@Override
	public void setOutputStream(OutputStream outStream)
	{
		outWriter = new BufferedWriter(new OutputStreamWriter(outStream));
		this.outStream = outStream;
	}

	@Override
	public void mergeDocumentHeader(FieldResolver resolver)
	{
		// geen actie nodig.
	}

	@Override
	public void mergeDocumentFooter(FieldResolver resolver)
	{
		// geen actie nodig.
	}

	@Override
	public void writeSection(FieldResolver resolver) throws TemplateException
	{
		try
		{
			if (section != null)
			{
				if (currentSection == null)
				{
					currentSection = section.clone();
					section.setParent(this);
					currentSection.setParent(this);

					if (isFirstSection)
					{
						isFirstSection = false;
					}
					else
					{
						currentSection.getElements().add(0, new RtfDelimiter(" "));
						currentSection.getElements().add(0, new RtfControlWord("\\page"));
					}

					currentSection.expandPropertyLists(resolver);
				}

				if (fieldIterator == null)
				{
					fieldIterator = currentSection.getFields().iterator();
				}

				// stukje code om etiketten te ondersteunen
				boolean aborted = false;
				while (fieldIterator.hasNext() && !aborted)
				{
					RtfField field = fieldIterator.next();
					String type = field.getInstruction().getType();
					if (type.equals("NEXT"))
					{
						aborted = true;
						field.clear();
					}
					else
						field.merge(resolver);
				}

				if (!aborted)
				{
					currentSection.write(getOutputWriter());
					getOutputWriter().flush();
					currentSection = null;
					fieldIterator = null;
				}

				if (monitor != null)
					monitor.afterWriteSection();
			}
		}
		catch (CloneNotSupportedException e)
		{
			throw new TemplateCreationException("Error creating new section.", e);
		}
		catch (IOException e)
		{
			throw new TemplateCreationException("Error creating new section.", e);
		}
	}

	public static RtfDocument createDocument(InputStream inStream) throws TemplateException
	{
		RTFParserDelegate delegate = new RTFParserDelegateImpl();
		RTFParser parser = new RTFParser(inStream);

		parser.setDelegate(delegate);

		try
		{
			parser.parse();
		}
		catch (ParseException e)
		{
			throw new TemplateParseException("Error parsing rtf template.", e);
		}

		return ((RTFParserDelegateImpl) delegate).getDocument();
	}

	public void writeDocumentHeader() throws TemplateException
	{
		try
		{
			getOutputWriter().append("{");
			super.write(getOutputWriter());
			getOutputWriter().flush();
		}
		catch (IOException e)
		{
			throw new TemplateCreationException("Cannot write document", e);
		}
	}

	public void writeDocumentFooter() throws TemplateException
	{
		try
		{
			if (fieldIterator != null)
			{
				while (fieldIterator.hasNext())
					fieldIterator.next().clear();
			}
			if (currentSection != null)
			{
				currentSection.write(outWriter);
			}
			outWriter.append("}");
			outWriter.flush();
		}
		catch (IOException e)
		{
			throw new TemplateCreationException("Cannot write document", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see nl.topicus.cobra.templates.documents.rtf.AbstractRtfGroup#clone()
	 */
	@Override
	public AbstractRtfGroup clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException(
			"Cloning of the entire RTF document structure is not supported.");
	}

	@Override
	public String toString()
	{
		return "{" + super.toString() + section.toString() + "}";
	}

	@Override
	public void writePageFooter()
	{
		// does nothing in rtfs
	}

	@Override
	public void writePageHeader()
	{
		// does nothing in rtfs
	}

	@Override
	public void setProgressMonitor(DocumentTemplateProgressMonitor monitor)
	{
		this.monitor = monitor;
	}

	@Override
	public OutputStream getOutputStream()
	{
		return outStream;
	}

	@Override
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
		return true;
	}
}
