package nl.topicus.eduarte.web.components.file;

import java.io.IOException;
import java.io.InputStream;

import nl.topicus.cobra.web.components.file.TypedFileUploadField;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.model.IModel;

public class SSLCertificaatUploadField extends TypedFileUploadField<InputStream>
{

	private static final long serialVersionUID = 1L;

	public SSLCertificaatUploadField(String id, IModel<InputStream> certificaat)
	{
		super(id, certificaat);
	}

	@Override
	protected InputStream createUpload(FileUpload file) throws IOException
	{
		return file.getInputStream();
	}
}
