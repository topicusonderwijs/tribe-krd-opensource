package nl.topicus.eduarte.web.components.panels.bijlage;

import nl.topicus.cobra.web.components.link.ConfirmationAjaxLink;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.bijlage.TypeBijlage;
import nl.topicus.eduarte.web.components.modalwindow.BijlageLink;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class BijlageEditField extends FormComponentPanel<Bijlage>
{
	private static final long serialVersionUID = 1L;

	private FileUploadField bestandField;

	public BijlageEditField(String id, IModel<Bijlage> model)
	{
		super(id, model);

		BijlageLink<Bijlage> link = new BijlageLink<Bijlage>("link", getModel())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && BijlageEditField.this.getModelObject() != null;
			}
		};
		add(link);

		add(new ConfirmationAjaxLink<Void>("delete",
			"Weet u zeker dat u deze bijlage wilt verwijderen?")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				BijlageEditField.this.setModelObject(null);
				target.addComponent(BijlageEditField.this);
			}

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && BijlageEditField.this.getModelObject() != null;
			}
		});

		bestandField = new FileUploadField("bestand", new Model<FileUpload>())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && BijlageEditField.this.getModelObject() == null;
			}
		};
		add(bestandField);
	}

	@Override
	protected void convertInput()
	{
		FileUpload upload = bestandField.getFileUpload();
		if (upload != null)
		{
			Bijlage newBijlage = new Bijlage();
			newBijlage.setTypeBijlage(TypeBijlage.Bestand);
			newBijlage.setBestandsnaam(upload.getClientFileName());
			newBijlage.setBestand(upload.getBytes());
			setConvertedInput(newBijlage);
			setModelObject(getConvertedInput());
		}
	}

	@Override
	public void updateModel()
	{
		// do nothing
	}
}
