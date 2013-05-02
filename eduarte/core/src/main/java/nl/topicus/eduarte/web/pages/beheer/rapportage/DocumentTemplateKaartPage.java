/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.beheer.rapportage;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.templates.FieldInfo;
import nl.topicus.cobra.templates.TemplateManager;
import nl.topicus.cobra.templates.exceptions.TemplateException;
import nl.topicus.cobra.templates.resolvers.FieldResolver;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.modifier.FieldModifier.Action;
import nl.topicus.cobra.web.components.panels.bottomrow.BewerkenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.app.beanpropertyresolvers.EduArtePrototypeBeanPropertyResolver;
import nl.topicus.eduarte.core.principals.rapportage.DocumentTemplateRead;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateRecht;
import nl.topicus.eduarte.entities.sidebar.IContextInfoObject;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.DocumentTemplateFieldAdapter;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.DocumentTemplateRechtTable;
import nl.topicus.eduarte.web.pages.AbstractDynamicContextPage;
import nl.topicus.eduarte.web.pages.PageContext;
import nl.topicus.eduarte.web.pages.shared.BeheerPageContext;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

/**
 * @author hoeve
 */
@PageInfo(title = "Samenvoegdocument", menu = {"Beheer > Rapportage > Samenvoegdocumenten > [Samenvoegdocument]"})
@InPrincipal(DocumentTemplateRead.class)
public class DocumentTemplateKaartPage<T extends DocumentTemplate> extends
		AbstractDynamicContextPage<T>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor voor bookmarks
	 * 
	 * @param template
	 */
	public DocumentTemplateKaartPage(T template)
	{
		this(ModelFactory.getModel(template));
	}

	/**
	 * Constructor voor het doorklikken op zoeken pages
	 * 
	 * @param templateModel
	 */
	public DocumentTemplateKaartPage(IModel<T> templateModel)
	{
		this(templateModel, new BeheerPageContext("Samenvoegdocumenten",
			BeheerMenuItem.Samenvoegdocumenten));
	}

	public DocumentTemplateKaartPage(IModel<T> templateModel, PageContext context)
	{
		super(templateModel, context);

		AutoFieldSet<T> fieldset = new AutoFieldSet<T>("inputFields", templateModel);
		fieldset.setPropertyNames(getPropertyNames());

		fieldset.addFieldModifier(new DocumentTemplateFieldAdapter("bestandsnaam",
			Action.FIELD_CLASS));
		fieldset
			.addFieldModifier(new DocumentTemplateFieldAdapter("bestandsnaam", Action.CREATION));
		fieldset.setSortAccordingToPropertyNames(true);
		add(fieldset);

		add(new ListView<FieldInfo>("fields", new LoadableDetachableModel<List<FieldInfo>>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected List<FieldInfo> load()
			{
				return DocumentTemplateKaartPage.this.testTemplate();
			}
		})
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<FieldInfo> item)
			{
				FieldInfo info = item.getModelObject();
				item.add(new Label("name", info.getName()));
				Label typeLabel =
					new Label("type", info.getFieldClass() != null ? info.getFieldClass()
						.getSimpleName() : info.getMessage());
				item.add(typeLabel);
				if (!info.isValid())
					typeLabel.add(new SimpleAttributeModifier("class", "spanError"));
			}

		});

		DocumentTemplate template = templateModel.getObject();
		add(new Label("beperkAutorisatie", template.isBeperkAutorisatie() ? "Ja" : "Nee"));
		EduArteDataPanel<DocumentTemplateRecht> rollen =
			new EduArteDataPanel<DocumentTemplateRecht>("rollen",
				new ListModelDataProvider<DocumentTemplateRecht>(
					new PropertyModel<List<DocumentTemplateRecht>>(templateModel, "rechten")),
				new DocumentTemplateRechtTable());
		rollen.setVisible(template.isBeperkAutorisatie());
		add(rollen);

		createComponents();
	}

	public List<String> getPropertyNames()
	{
		return Arrays.asList("omschrijving", "context", "categorie", "documentType",
			"kopieBijContext", "bestandsnaam", "uitvoerFormaat", "valid", "forceerType", "actief");
	}

	public List<FieldInfo> testTemplate()
	{
		DocumentTemplate template = (DocumentTemplate) getDefaultModelObject();
		List<FieldInfo> fieldInfo = null;
		try
		{
			nl.topicus.cobra.templates.documents.DocumentTemplate document;
			ByteArrayInputStream inStream = new ByteArrayInputStream(template.getZzzBestand());

			document =
				TemplateManager.getInstance().createDocumentTemplateByFileExt(
					template.getBestandsnaam(), inStream);

			if (document != null)
			{
				document.setKeepMergeFields(false);

				FieldResolver resolver = new EduArtePrototypeBeanPropertyResolver(template);
				fieldInfo = document.getFieldInfo(resolver);
			}
		}
		catch (NumberFormatException e)
		{
			fieldInfo = new ArrayList<FieldInfo>();
			fieldInfo.add(new FieldInfo("Geen", null, false, "Fout(en) in de samenvoegvelden: "
				+ e.getMessage()));
		}
		catch (TemplateException e)
		{
			fieldInfo = new ArrayList<FieldInfo>();
			fieldInfo.add(new FieldInfo("Geen", null, false, "Geen geldig bestandsformaat!"));
		}
		catch (NullPointerException e)
		{
			fieldInfo = new ArrayList<FieldInfo>();
			fieldInfo.add(new FieldInfo("Geen", null, false, "Het bestand heeft geen inhoud!"));
		}
		finally
		{
			if (fieldInfo == null || fieldInfo.size() == 0)
			{
				fieldInfo = new ArrayList<FieldInfo>();
				fieldInfo
					.add(new FieldInfo("Geen", null, true, "Geen velden gevonden in bestand."));
			}
			else
			{
				for (FieldInfo field : fieldInfo)
				{
					if (!field.isValid())
					{
						template.setValid(false);
						break;
					}
				}
			}
		}

		return fieldInfo;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new BewerkenButton<T>(panel, DocumentTemplateEditPage.class,
			getContextModel()));
		panel.addButton(new TerugButton(panel, DocumentTemplateZoekenPage.class));
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getContextModelObject()
	{
		return (T) getDefaultModelObject();
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(DocumentTemplate.class);
		ctorArgValues.add(getContextModelObject());
	}

	private static final List<Class< ? extends IContextInfoObject>> CONTEXT_PARAMETER_TYPES =
		new ArrayList<Class< ? extends IContextInfoObject>>(2);
	static
	{
		CONTEXT_PARAMETER_TYPES.add(DocumentTemplate.class);
	}

	@Override
	public List<Class< ? extends IContextInfoObject>> getContextParameterTypes()
	{
		return CONTEXT_PARAMETER_TYPES;
	}

	@Override
	public IContextInfoObject getContextValue(Class< ? extends IContextInfoObject> clazz)
	{
		return getContextModelObject();
	}
}
