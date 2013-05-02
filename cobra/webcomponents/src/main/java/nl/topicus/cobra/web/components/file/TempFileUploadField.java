package nl.topicus.cobra.web.components.file;

import java.io.File;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.model.IModel;

/**
 * FileUploadField dat de upload wegschrijft in een temp file. Gebruik dit alleen als je
 * de file direct door wilt sturen naar een job.
 * 
 * @author papegaaij
 */
public class TempFileUploadField extends TypedFileUploadField<File>
{
	private static final long serialVersionUID = 1L;

	public TempFileUploadField(String id, IModel<File> model)
	{
		super(id, model);
	}

	@Override
	protected File createUpload(FileUpload file) throws Exception
	{
		return file.writeToTempFile();
	}

}
