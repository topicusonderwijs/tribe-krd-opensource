package nl.topicus.cobra.web.components.file;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public abstract class TypedFileUploadField<T> extends FormComponentPanel<T>
{
	private static final long serialVersionUID = 1L;

	private FileUploadField upload;

	public TypedFileUploadField(String id)
	{
		super(id);
		init();
	}

	public TypedFileUploadField(String id, IModel<T> model)
	{
		super(id, model);
		init();
	}

	private void init()
	{
		upload = new FileUploadField("upload", new Model<FileUpload>())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isRequired()
			{
				return TypedFileUploadField.this.isRequired();
			}
		};
		add(upload);
	}

	@Override
	public FormComponent<T> setLabel(IModel<String> labelModel)
	{
		upload.setLabel(labelModel);
		return super.setLabel(labelModel);
	}

	@Override
	protected void convertInput()
	{
		FileUpload fileUpload = upload.getConvertedInput();
		try
		{
			setConvertedInput(fileUpload == null ? null : createUpload(fileUpload));
		}
		catch (Exception e)
		{
			error("Bestand kon niet gelezen worden: " + e.getLocalizedMessage());
			setConvertedInput(null);
		}
	}

	protected abstract T createUpload(FileUpload file) throws Exception;
}
