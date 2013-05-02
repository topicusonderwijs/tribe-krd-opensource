package nl.topicus.eduarte.krdparticipatie.jobs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import nl.topicus.cobra.web.components.file.TypedFileUploadField;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.model.IModel;

public class AbsentiemeldingenImporterenBestandField extends
		TypedFileUploadField<AbsentiemeldingenImportFile>
{
	private static final long serialVersionUID = 1L;

	public AbsentiemeldingenImporterenBestandField(String id,
			IModel<AbsentiemeldingenImportFile> model)
	{
		super(id, model);
	}

	@Override
	protected AbsentiemeldingenImportFile createUpload(FileUpload file) throws IOException
	{
		String bestandsnaam = file.getClientFileName();
		if (!bestandsnaam.endsWith(".csv"))
		{
			error("Alleen CSV-bestanden worden ondersteund.");
			return null;
		}
		return new AbsentiemeldingenImportFile(bestandsnaam, new BufferedReader(
			new InputStreamReader(file.getInputStream())));
	}
}
