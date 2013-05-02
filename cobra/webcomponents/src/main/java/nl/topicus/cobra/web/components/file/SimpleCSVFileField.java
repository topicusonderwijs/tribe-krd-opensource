package nl.topicus.cobra.web.components.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.model.IModel;

public class SimpleCSVFileField extends TypedFileUploadField<SimpleCSVFile>
{
	private static final long serialVersionUID = 1L;

	public SimpleCSVFileField(String id, IModel<SimpleCSVFile> model)
	{
		super(id, model);
	}

	@Override
	protected SimpleCSVFile createUpload(FileUpload file) throws IOException
	{
		String bestandsnaam = file.getClientFileName();
		if (!bestandsnaam.endsWith(".csv"))
		{
			error("Alleen CSV-bestanden worden ondersteund.");
			return null;
		}
		return new SimpleCSVFile(bestandsnaam, new BufferedReader(new InputStreamReader(
			file.getInputStream())));
	}
}
