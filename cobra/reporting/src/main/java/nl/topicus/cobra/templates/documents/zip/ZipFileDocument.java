package nl.topicus.cobra.templates.documents.zip;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.templates.FieldInfo;
import nl.topicus.cobra.templates.documents.DocumentTemplate;
import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.cobra.templates.exceptions.TemplateCreationException;
import nl.topicus.cobra.templates.exceptions.TemplateException;
import nl.topicus.cobra.templates.monitors.DocumentTemplateProgressMonitor;
import nl.topicus.cobra.templates.resolvers.FieldResolver;

/**
 * Mock document welke een Zip file voorstelt met TemplateDocumenten in zich.
 * 
 * @author hoeve
 */
public class ZipFileDocument implements DocumentTemplate
{
	private OutputStream outStream;

	public static String MIME_TYPE = "application/zip";

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
	public List<FieldInfo> getFieldInfo(FieldResolver resolver)
	{
		return new ArrayList<FieldInfo>();
	}

	@Override
	public OutputStream getOutputStream()
	{
		return outStream;
	}

	@Override
	public DocumentTemplateType getType()
	{
		return DocumentTemplateType.ZIP;
	}

	@Override
	public void mergeDocumentFooter(FieldResolver resolver)
	{
	}

	@Override
	public void mergeDocumentHeader(FieldResolver resolver)
	{
	}

	@Override
	public void setOutputStream(OutputStream outStream)
	{
		this.outStream = outStream;
	}

	@Override
	public void setProgressMonitor(DocumentTemplateProgressMonitor monitor)
	{

	}

	@Override
	public void writeDocumentFooter()
	{

	}

	@Override
	public void writeDocumentHeader()
	{

	}

	@Override
	public void writePageFooter()
	{

	}

	@Override
	public void writePageHeader()
	{

	}

	@Override
	public void writeSection(FieldResolver resolver)
	{

	}

	public static ZipFileDocument createDocument(@SuppressWarnings("unused") InputStream inStream)
			throws TemplateException
	{
		throw new TemplateCreationException("ZipFileDocument does not support this function.");
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
