package nl.topicus.eduarte.web.components.file;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import nl.topicus.cobra.web.components.file.TypedFileUploadField;
import nl.topicus.eduarte.app.util.importeren.accounts.AccountsImporterenFile;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.model.IModel;

public class AccountImportBestandField extends TypedFileUploadField<AccountsImporterenFile>
{
	private static final long serialVersionUID = 1L;

	public AccountImportBestandField(String id, IModel<AccountsImporterenFile> model)
	{
		super(id, model);
	}

	@Override
	protected AccountsImporterenFile createUpload(FileUpload file) throws Exception
	{
		return new AccountsImporterenFile(new BufferedReader(new InputStreamReader(file
			.getInputStream(), "Cp1252")), file.getClientFileName());
	}
}
