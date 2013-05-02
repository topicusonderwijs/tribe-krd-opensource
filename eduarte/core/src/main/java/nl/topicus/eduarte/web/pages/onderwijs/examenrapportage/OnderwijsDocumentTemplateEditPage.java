/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.onderwijs.examenrapportage;

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
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.cobra.web.validators.UniqueConstraintFormIdValidator;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.app.beanpropertyresolvers.EduArtePrototypeBeanPropertyResolver;
import nl.topicus.eduarte.core.principals.onderwijs.OnderwijsDocumentTemplateVerwijderen;
import nl.topicus.eduarte.core.principals.onderwijs.OnderwijsDocumentTemplateWrite;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateRecht;
import nl.topicus.eduarte.entities.rapportage.OnderwijsDocumentTemplate;
import nl.topicus.eduarte.entities.rapportage.OnderwijsDocumentTemplate.ExamenDocumentTemplateType;
import nl.topicus.eduarte.web.components.choice.DocumentTypeCombobox;
import nl.topicus.eduarte.web.components.choice.TaxonomieCombobox;
import nl.topicus.eduarte.web.components.link.DocumentTemplateDownloadLink;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.modalwindow.documenttemplate.DocumentTemplateRechtEditPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.DocumentTemplateRechtTable;
import nl.topicus.eduarte.web.pages.SecurePage;

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
@PageInfo(title = "Onderwijs samenvoegdocument bewerken", menu = {"Onderwijs > Samenvoegdocumenten > Onderwijs samenvoegdocumenten > Nieuw"})
@InPrincipal(OnderwijsDocumentTemplateWrite.class)
public class OnderwijsDocumentTemplateEditPage extends SecurePage implements IEditPage
{
	private static final long serialVersionUID = 1L;

	private static final Logger log =
		LoggerFactory.getLogger(OnderwijsDocumentTemplateEditPage.class);

	private final Form<OnderwijsDocumentTemplate> form;

	private FileUploadField fileUploadField;

	private DocumentTemplateRechtEditPanel rechtenPanel;

	public OnderwijsDocumentTemplateEditPage()
	{
		this(new OnderwijsDocumentTemplate());
	}

	public OnderwijsDocumentTemplateEditPage(IModel<OnderwijsDocumentTemplate> templateModel)
	{
		this(templateModel.getObject());
	}

	public OnderwijsDocumentTemplateEditPage(OnderwijsDocumentTemplate template)
	{
		super(ModelFactory.getCompoundChangeRecordingModel(template, new DefaultModelManager(
			DocumentTemplateRecht.class, OnderwijsDocumentTemplate.class)),
			CoreMainMenuItem.Onderwijs);

		form = new Form<OnderwijsDocumentTemplate>("form", getTemplateModel());
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
				OnderwijsDocumentTemplate myTemplate =
					(OnderwijsDocumentTemplate) getDefaultModelObject();
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
				myTemplate.setCategorie(myTemplate.getExamenDocumentType() == null ? null
					: myTemplate.getExamenDocumentType().getTemplateCategorie());
				myTemplate.setContext(myTemplate.getExamenDocumentType() == null ? null
					: myTemplate.getExamenDocumentType().getTemplateContext());

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

		TaxonomieCombobox taxonomieCombo = new TaxonomieCombobox("taxonomie");
		taxonomieCombo.setRequired(true);
		form.add(taxonomieCombo);

		form.add(new EnumCombobox<ExamenDocumentTemplateType>("examenDocumentType", true,
			ExamenDocumentTemplateType.values()).setNullValid(false).setRequired(true));
		DocumentTypeCombobox documentTypeCombo = new DocumentTypeCombobox("documentType", null);
		documentTypeCombo.setNullValid(false).setRequired(true);
		form.add(documentTypeCombo);
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
		bestaandBestand.add(new DocumentTemplateDownloadLink<OnderwijsDocumentTemplate>(
			"downloadlink", getTemplateModel()));
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
				new PropertyModel<List<DocumentTemplateRecht>>(getTemplateModel(), "rechten"),
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
				OnderwijsDocumentTemplate odt = getDocumentTemplate();

				odt.setCategorie(odt.getExamenDocumentType().getTemplateCategorie());
				odt.setContext(odt.getExamenDocumentType().getTemplateContext());

				getTemplateModel().saveObject();
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();

				EduArteRequestCycle.get().setResponsePage(
					new OnderwijsDocumentTemplateKaartPage(getTemplateModel()));
			}
		});
		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				if (getDocumentTemplate().isSaved())
					return new OnderwijsDocumentTemplateKaartPage(getTemplateModel());
				else
					return new OnderwijsDocumentTemplateZoekenPage();
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				if (getDocumentTemplate().isSaved())
					return OnderwijsDocumentTemplateKaartPage.class;
				else
					return OnderwijsDocumentTemplateZoekenPage.class;
			}

		}));
		panel.addButton(new OnderwijsDocumentTemplateVerwijderenButton(panel));

	}

	@SuppressWarnings("unchecked")
	private IChangeRecordingModel<OnderwijsDocumentTemplate> getTemplateModel()
	{
		return (IChangeRecordingModel<OnderwijsDocumentTemplate>) getDefaultModel();
	}

	private OnderwijsDocumentTemplate getDocumentTemplate()
	{
		return (OnderwijsDocumentTemplate) getDefaultModelObject();
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, new PropertyModel<String>(getDefaultModel(), "omschrijving"));
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new OnderwijsCollectiefMenu(id, OnderwijsCollectiefMenuItem.Samenvoegdocumenten);
	}

	@InPrincipal(OnderwijsDocumentTemplateVerwijderen.class)
	private class OnderwijsDocumentTemplateVerwijderenButton extends VerwijderButton
	{
		public OnderwijsDocumentTemplateVerwijderenButton(BottomRowPanel bottomRow)
		{
			super(bottomRow);
			ComponentUtil.setSecurityCheck(this, new ClassSecurityCheck(
				OnderwijsDocumentTemplateVerwijderenButton.class));
		}

		private static final long serialVersionUID = 1L;

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
			setResponsePage(new OnderwijsDocumentTemplateZoekenPage());
		}
	}
}
