package nl.topicus.eduarte.web.components.panels.bijlage;

import java.util.Arrays;

import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.form.VersionedForm;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.bijlage.BijlageEntiteit;
import nl.topicus.eduarte.entities.bijlage.DocumentCategorie;
import nl.topicus.eduarte.entities.bijlage.TypeBijlage;
import nl.topicus.eduarte.web.components.choice.DocumentCategorieCombobox;
import nl.topicus.eduarte.web.components.choice.DocumentTypeCombobox;
import nl.topicus.eduarte.web.validators.BestandValidator;
import nl.topicus.eduarte.web.validators.LinkValidator;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * Panel voor het bewerken van bijlagen.
 * 
 * @author loite
 */
public class BijlageEditPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	private final WebMarkupContainer bestandContainer = new WebMarkupContainer("bestandContainer");

	private final WebMarkupContainer linkContainer = new WebMarkupContainer("linkContainer");

	private final FileUploadField bestand;

	private final Form<BijlageEntiteit> form;

	public BijlageEditPanel(String id, BijlageEntiteit bijlageTeBewerken, final Page returnToPage)
	{
		super(id);
		form =
			new VersionedForm<BijlageEntiteit>("form", ModelFactory
				.getCompoundChangeRecordingModel(bijlageTeBewerken, new DefaultModelManager(
					bijlageTeBewerken.doUnproxy().getClass(), Bijlage.class)))
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onSubmit()
				{
					BijlageEntiteit bijlageEntiteit = form.getModelObject();
					Bijlage bijlage = bijlageEntiteit.getBijlage();

					switch (bijlage.getTypeBijlage())
					{
						case Bestand:
							bijlage.setLink(null);

							if (bestand.getFileUpload() != null)
							{
								FileUpload upload =
									((FileUploadField) bestandContainer.get("bestand"))
										.getFileUpload();
								bijlage.setBestand(upload.getBytes());
								bijlage.setBestandsnaam(upload.getClientFileName());
								upload.closeStreams();
							}

							break;
						case Link:
							bijlage.setBestand(null);
							bijlage.setBestandsnaam(null);

							// Ook bestandsnaam proberen te herleiden zodat bij een link
							// naar
							// bijvoorbeeld een centrale documentenserver ook de
							// bestandsnaam
							// getoond wordt.
							String link = bijlage.getLink();
							String bestandsnaam;
							if (link.lastIndexOf('/') == -1)
							{
								bestandsnaam = link;
							}
							else
								bestandsnaam = link.substring(link.lastIndexOf('/') + 1);
							if (!bestandsnaam.isEmpty())
							{
								bijlage.setBestandsnaam(bestandsnaam);
							}

							break;
						case Overig:
							bijlage.setLink(null);
							bijlage.setBestand(null);
							bijlage.setBestandsnaam(null);

							break;
					}

					bijlage.saveOrUpdate();
					bijlageEntiteit.saveOrUpdate();
					bijlage.commit();
					setResponsePage(returnToPage);
				}
			};
		add(form);

		IModel<DocumentCategorie> categorieModel;
		if (bijlageTeBewerken.getBijlage().getDocumentType() == null)
			categorieModel = ModelFactory.getModel((DocumentCategorie) null);
		else
			categorieModel =
				new PropertyModel<DocumentCategorie>(form.getModel(),
					"bijlage.documentType.categorie");
		DocumentCategorieCombobox categorieCombo =
			new DocumentCategorieCombobox("categorie", categorieModel);

		categorieCombo.setNullValid(false).setRequired(true);
		form.add(categorieCombo);
		DocumentTypeCombobox typeCombo =
			new DocumentTypeCombobox("bijlage.documentType", categorieCombo.getModel());
		typeCombo.setNullValid(false).setRequired(true);
		typeCombo.setAddSelectedItemToChoicesWhenNotInList(false);
		form.add(typeCombo);
		categorieCombo.connectListForAjaxRefresh(typeCombo);
		form.add(new TextField<String>("bijlage.omschrijving"));
		form.add(new TextField<String>("bijlage.documentnummer"));
		form.add(new DatumField("bijlage.ontvangstdatum"));
		form.add(new DatumField("bijlage.geldigTot"));
		form.add(new TextField<String>("bijlage.locatie"));

		bestand = new FileUploadField("bestand", new Model<FileUpload>());
		bestandContainer.add(bestand);
		bestandContainer.setVisible(bijlageTeBewerken.getBijlage().getTypeBijlage().equals(
			TypeBijlage.Bestand));
		form.add(bestandContainer);

		TextField<String> link = new TextField<String>("bijlage.link");
		linkContainer.add(link);
		linkContainer.setVisible(bijlageTeBewerken.getBijlage().getTypeBijlage().equals(
			TypeBijlage.Link));
		form.add(linkContainer);

		RadioChoice<TypeBijlage> rc =
			new RadioChoice<TypeBijlage>("bijlage.typeBijlage", Arrays.asList(TypeBijlage.values()))
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected boolean wantOnSelectionChangedNotifications()
				{
					return true;
				}

				@Override
				protected void onSelectionChanged(Object newSelection)
				{
					if (newSelection.equals(TypeBijlage.Link))
					{
						bestandContainer.setVisible(false);
						linkContainer.setVisible(true);
					}
					else if (newSelection.equals(TypeBijlage.Bestand))
					{
						bestandContainer.setVisible(true);
						linkContainer.setVisible(false);
					}
					else
					{
						bestandContainer.setVisible(false);
						linkContainer.setVisible(false);
					}
				}
			};
		form.add(rc);
		form.add(new BestandValidator(bestand, rc)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void validate(Form< ? > pform)
			{
				BijlageEntiteit bijlage = form.getModelObject();
				if (bijlage.getBijlage().getBestand() == null)
					super.validate(form);
			}
		});

		form.add(new LinkValidator(link, rc));
	}

	public Form<BijlageEntiteit> getForm()
	{
		return form;
	}
}
