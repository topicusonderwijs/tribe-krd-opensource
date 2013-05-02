package nl.topicus.eduarte.onderwijscatalogus.web.components.file;

import java.io.IOException;

import nl.topicus.cobra.web.components.file.TypedFileUploadField;
import nl.topicus.eduarte.onderwijscatalogus.jobs.OnderwijsproductImportFile;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.model.IModel;

public class OnderwijsproductImportBestandField extends
		TypedFileUploadField<OnderwijsproductImportFile>
{
	private static final long serialVersionUID = 1L;

	public OnderwijsproductImportBestandField(String id, IModel<OnderwijsproductImportFile> model)
	{
		super(id, model);
	}

	@Override
	protected OnderwijsproductImportFile createUpload(FileUpload file) throws IOException
	{
		return new OnderwijsproductImportFile(file.getClientFileName(), file.getInputStream());
	}
}
