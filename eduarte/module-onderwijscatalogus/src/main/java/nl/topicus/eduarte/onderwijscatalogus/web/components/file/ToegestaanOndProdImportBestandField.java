package nl.topicus.eduarte.onderwijscatalogus.web.components.file;

import java.io.IOException;

import nl.topicus.cobra.web.components.file.TypedFileUploadField;
import nl.topicus.eduarte.onderwijscatalogus.jobs.ToegestaanOndProdImportFile;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.model.IModel;

public class ToegestaanOndProdImportBestandField extends
		TypedFileUploadField<ToegestaanOndProdImportFile>
{
	private static final long serialVersionUID = 1L;

	public ToegestaanOndProdImportBestandField(String id, IModel<ToegestaanOndProdImportFile> model)
	{
		super(id, model);
	}

	@Override
	protected ToegestaanOndProdImportFile createUpload(FileUpload file) throws IOException
	{
		return new ToegestaanOndProdImportFile(file.getClientFileName(), file.getInputStream());
	}
}
