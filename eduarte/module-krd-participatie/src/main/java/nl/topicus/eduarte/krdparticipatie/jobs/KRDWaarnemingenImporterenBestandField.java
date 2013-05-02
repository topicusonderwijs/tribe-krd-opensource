package nl.topicus.eduarte.krdparticipatie.jobs;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import nl.topicus.cobra.web.components.file.TypedFileUploadField;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.model.IModel;

public class KRDWaarnemingenImporterenBestandField extends
		TypedFileUploadField<KRDWaarnemingenImportFile>
{
	private static final long serialVersionUID = 1L;

	public KRDWaarnemingenImporterenBestandField(String id, IModel<KRDWaarnemingenImportFile> model)
	{
		super(id, model);
	}

	@Override
	protected KRDWaarnemingenImportFile createUpload(FileUpload file) throws Exception
	{
		String bestandsnaam = file.getClientFileName();
		if (!bestandsnaam.endsWith(".csv"))
		{
			error("Alleen CSV-bestanden worden ondersteund.");
			return null;
		}
		return new KRDWaarnemingenImportFile(bestandsnaam, new BufferedReader(
			new InputStreamReader(file.getInputStream())));
	}
}
