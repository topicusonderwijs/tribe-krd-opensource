/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.beheer.rapportage;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.templates.FieldInfo;
import nl.topicus.cobra.templates.TemplateManager;
import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.cobra.templates.exceptions.TemplateException;
import nl.topicus.cobra.templates.resolvers.FieldResolver;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.behaviors.AjaxFormComponentSaveBehaviour;
import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.cobra.web.validators.UniqueConstraintFormIdValidator;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.app.beanpropertyresolvers.EduArtePrototypeBeanPropertyResolver;
import nl.topicus.eduarte.core.principals.rapportage.DocumentTemplateVerwijderen;
import nl.topicus.eduarte.core.principals.rapportage.DocumentTemplateWrite;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateCategorie;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateContext;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateRecht;
import nl.topicus.eduarte.web.components.choice.DocumentTemplateCategorieListModel;
import nl.topicus.eduarte.web.components.choice.DocumentTypeCombobox;
import nl.topicus.eduarte.web.components.link.DocumentTemplateDownloadLink;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.modalwindow.documenttemplate.DocumentTemplateRechtEditPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.DocumentTemplateRechtTable;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.security.checks.ClassSecurityCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hoeve
 */
@PageInfo(title = "Samenvoegdocumenten", menu = {
	"Beheer > Rapportage > Samenvoegdocumenten > Nieuw",
	"Beheer > Rapportage > Samenvoegdocumenten > [Samenvoegdocument]"})
@InPrincipal(DocumentTemplateWrite.class)
public class DocumentTemplateEditPage extends AbstractBeheerPage<DocumentTemplate> implements
		IEditPage
{
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(DocumentTemplateEditPage.class);

	private final Form<DocumentTemplate> form;

	private FileUploadField fileUploadField;

	private DocumentTemplateRechtEditPanel rechtenPanel;

	public DocumentTemplateEditPage(IModel<DocumentTemplate> templateModel)
	{
		this(templateModel.getObject());
	}

	public DocumentTemplateEditPage(DocumentTemplate template)
	{
		super(ModelFactory.getCompoundChangeRecordingModel(template, new DefaultModelManager(
			DocumentTemplateRecht.class, DocumentTemplate.class)),
			BeheerMenuItem.Samenvoegdocumenten);

		form = new Form<DocumentTemplate>("form", getContextModel());
		form.add(new AbstractFormValidator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public FormComponent< ? >[] getDependentFormComponents()
			{
				FormComponent< ? >[] components = new FormComponent[1];
				components[0] = fileUploadField;
				return components;
			}

			@Override
			public void validate(Form< ? > myForm)
			{
				DocumentTemplate myTemplate = getContextModelObject();
				myTemplate.setValid(false);
				FileUpload upload = fileUploadField.getFileUpload();

				// onthoudt de oude gegevens zodat we deze kunnen restoren upon error.
				byte[] oldTemplateBytes = myTemplate.getZzzBestand();
				String oldTemplateFilename = myTemplate.getBestandsnaam();

				if (upload != null)
				{
					myTemplate.setZzzBestand(upload.getBytes());
					myTemplate.setBestandsnaam(upload.getClientFileName());
					upload.closeStreams();
				}

				List<FieldInfo> fieldInfo = null;

				try
				{
					nl.topicus.cobra.templates.documents.DocumentTemplate document;
					ByteArrayInputStream inStream =
						new ByteArrayInputStream(myTemplate.getZzzBestand());

					document =
						TemplateManager.getInstance().createDocumentTemplateByFileExt(
							myTemplate.getBestandsnaam(), inStream);

					if (document != null)
					{
						document.setKeepMergeFields(false);

						FieldResolver resolver =
							new EduArtePrototypeBeanPropertyResolver(myTemplate);
						fieldInfo = document.getFieldInfo(resolver);

						myTemplate.setType(document.getType());
					}
				}
				catch (TemplateException e)
				{
					fieldInfo = new ArrayList<FieldInfo>();
					fieldInfo
						.add(new FieldInfo("Geen", null, false, "Geen geldig bestandsformaat!"));
					log.error(e.getMessage(), e);
				}
				catch (NumberFormatException e)
				{
					fieldInfo = new ArrayList<FieldInfo>();
					fieldInfo.add(new FieldInfo("Geen", null, false,
						"Fout(en) in de samenvoegvelden: " + e.getMessage()));
				}
				catch (NullPointerException e)
				{
					fieldInfo = new ArrayList<FieldInfo>();
					fieldInfo.add(new FieldInfo("Geen", null, false,
						"Het bestand heeft geen inhoud!"));
					log.error(e.getMessage(), e);
				}
				finally
				{
					myTemplate.setValid(true);

					if (fieldInfo == null)
					{
						fieldInfo = new ArrayList<FieldInfo>();
						fieldInfo.add(new FieldInfo("Geen", null, true,
							"Geen velden gevonden in bestand."));
					}
					else
					{
						for (FieldInfo field : fieldInfo)
						{
							if (!field.isValid())
							{
								myTemplate.setValid(false);
								fileUploadField.error(field.getMessage());
								break;
							}
						}
					}

					if (!myTemplate.isValid())
					{
						myTemplate.setZzzBestand(oldTemplateBytes);
						myTemplate.setBestandsnaam(oldTemplateFilename);
					}
				}
			}
		});
		add(form);

		RequiredTextField<String> omschrijving = new RequiredTextField<String>("omschrijving");
		UniqueConstraintFormIdValidator val =
			new UniqueConstraintFormIdValidator(form, "samenvoegdocument", "omschrijving");
		val.setProperties("organisatie", "categorie");
		form.add(val);

		form.add(omschrijving);
		EnumCombobox<DocumentTemplateContext> contextCombobox =
			new EnumCombobox<DocumentTemplateContext>("context", true, DocumentTemplateContext
				.getValues());
		form.add(contextCombobox.setNullValid(false).setRequired(true));
		EnumCombobox<DocumentTemplateCategorie> categorieCombo =
			new EnumCombobox<DocumentTemplateCategorie>("categorie", null,
				new DocumentTemplateCategorieListModel(contextCombobox, DocumentTemplateCategorie
					.getValues()));
		categorieCombo.setAddAjax(true);
		categorieCombo.setNullValid(false);
		categorieCombo.setRequired(true);
		categorieCombo.setAddSelectedItemToChoicesWhenNotInList(false);
		form.add(categorieCombo);
		DocumentTypeCombobox documentTypeCombo = new DocumentTypeCombobox("documentType", null);
		documentTypeCombo.setNullValid(false).setRequired(true);
		form.add(documentTypeCombo);
		contextCombobox.connectListForAjaxRefresh(categorieCombo);
		categorieCombo.connectListForAjaxRefresh(documentTypeCombo);

		final CheckBox sectiePerElement = new CheckBox("sectiePerElement")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled()
			{
				return super.isEnabled() && !getDocumentTemplate().isKopieBijContext();
			}
		};
		sectiePerElement.add(new AjaxFormComponentSaveBehaviour());
		sectiePerElement.setOutputMarkupId(true);
		form.add(sectiePerElement);
		form.add(new AjaxCheckBox("kopieBijContext")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				getDocumentTemplate().setSectiePerElement(true);
				sectiePerElement.clearInput();
				target.addComponent(sectiePerElement);
			}
		});
		form.add(new CheckBox("actief"));

		WebMarkupContainer bestaandBestand = new WebMarkupContainer("bestaandBestand");
		bestaandBestand.add(new DocumentTemplateDownloadLink<DocumentTemplate>("downloadlink",
			getContextModel()));
		bestaandBestand.setVisible(template.isSaved());
		form.add(bestaandBestand);

		WebMarkupContainer bestaandBestand2 = new WebMarkupContainer("bestaandBestand2");
		bestaandBestand2.add(new Label("bestandsformaat", new Model<String>(template
			.getUitvoerFormaat())));
		bestaandBestand2.setVisible(template.isSaved());
		form.add(bestaandBestand2);

		StringBuilder types = new StringBuilder();
		for (String ext : TemplateManager.getInstance().getRegisteredFileExts())
		{
			if (types.length() != 0)
				types.append(", ");
			types.append(ext);
		}
		form.add(new Label("types", types.toString()));
		fileUploadField = new FileUploadField("bestand", new Model<FileUpload>());
		fileUploadField.setRequired(!template.isSaved());
		form.add(fileUploadField);

		form.add(new EnumCombobox<DocumentTemplateType>("forceerType",
			new DocumentTemplateType[] {DocumentTemplateType.PDF}).setNullValid(true));

		rechtenPanel =
			new DocumentTemplateRechtEditPanel("rollen",
				new PropertyModel<List<DocumentTemplateRecht>>(getContextModel(), "rechten"),
				new DocumentTemplateRechtTable(), getTemplateModel().getManager())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public DocumentTemplateRecht createNewT()
				{
					return new DocumentTemplateRecht(getDocumentTemplate());
				}

				@Override
				public boolean isVisible()
				{
					return getDocumentTemplate().isBeperkAutorisatie();
				}
			};
		form.add(rechtenPanel);
		form.add(new AjaxCheckBox("beperkAutorisatie")
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				target.addComponent(rechtenPanel);
			}
		});

		createComponents();
	}

	protected String getFieldStart(String fieldname)
	{
		if (fieldname != null && !fieldname.isEmpty())
		{
			if (fieldname.contains("."))
				return fieldname.substring(0, fieldname.indexOf("."));
		}
		return fieldname;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				getTemplateModel().saveObject();
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();

				EduArteRequestCycle.get().setResponsePage(
					new DocumentTemplateKaartPage<DocumentTemplate>(getContextModel()));
			}
		});
		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				if (getDocumentTemplate().isSaved())
					return new DocumentTemplateKaartPage<DocumentTemplate>(getContextModel());
				else
					return new DocumentTemplateZoekenPage();
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				if (getDocumentTemplate().isSaved())
					return DocumentTemplateKaartPage.class;
				else
					return DocumentTemplateZoekenPage.class;
			}

		}));
		panel.addButton(new DocumentTemplateVerwijderenButton(panel));
	}

	@SuppressWarnings("unchecked")
	private IChangeRecordingModel<DocumentTemplate> getTemplateModel()
	{
		return (IChangeRecordingModel<DocumentTemplate>) getDefaultModel();
	}

	private DocumentTemplate getDocumentTemplate()
	{
		return (DocumentTemplate) getDefaultModelObject();
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, new PropertyModel<String>(getDefaultModel(), "omschrijving"));
	}

	@InPrincipal(DocumentTemplateVerwijderen.class)
	private class DocumentTemplateVerwijderenButton extends VerwijderButton
	{
		private static final long serialVersionUID = 1L;

		public DocumentTemplateVerwijderenButton(BottomRowPanel bottomRow)
		{
			super(bottomRow);
			ComponentUtil.setSecurityCheck(this, new ClassSecurityCheck(
				DocumentTemplateVerwijderenButton.class));
		}

		@Override
		public boolean isVisible()
		{
			return getDocumentTemplate().isSaved();
		}

		@Override
		protected void onClick()
		{
			getTemplateModel().deleteObject();
			DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
			setResponsePage(new DocumentTemplateZoekenPage());
		}
	}
}
