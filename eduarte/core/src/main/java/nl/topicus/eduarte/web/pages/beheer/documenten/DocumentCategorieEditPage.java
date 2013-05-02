package nl.topicus.eduarte.web.pages.beheer.documenten;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.validators.UniqueConstraintValidator;
import nl.topicus.eduarte.core.principals.beheer.systeem.DocumentPrincipal;
import nl.topicus.eduarte.entities.bijlage.DocumentCategorie;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateRecht;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.modalwindow.documenttemplate.DocumentTemplateRechtEditPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ProbeerTeVerwijderenButton;
import nl.topicus.eduarte.web.components.panels.datapanel.table.DocumentTemplateRechtTable;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "Documentcategorie", menu = "Beheer > Beheer tabellen > Documentcategorieën > [documentcategorie]")
@InPrincipal(DocumentPrincipal.class)
public class DocumentCategorieEditPage extends AbstractBeheerPage<DocumentCategorie> implements
		IModuleEditPage<DocumentCategorie>
{
	private Form<Void> form;

	private DocumentTemplateRechtEditPanel rechtenPanel;

	/**
	 * Constructor voor de ToevoegenButton.
	 * 
	 * @param returnPage
	 */
	public DocumentCategorieEditPage(SecurePage returnPage)
	{
		this(ModelFactory.getCompoundChangeRecordingModel(new DocumentCategorie(),
			new DefaultModelManager(DocumentCategorie.class, DocumentTemplateRecht.class)),
			returnPage);
	}

	/**
	 * Constructor voor de BewerkenButton.
	 * 
	 * @param categorieModel
	 * @param returnPage
	 */
	public DocumentCategorieEditPage(IChangeRecordingModel<DocumentCategorie> categorieModel,
			SecurePage returnPage)
	{
		super(categorieModel, BeheerMenuItem.DocumentCategorien);
		setReturnPage(returnPage);
		createForm();
	}

	private void createForm()
	{
		form = new Form<Void>("form");
		add(form);

		AutoFieldSet<DocumentCategorie> fieldSet =
			new AutoFieldSet<DocumentCategorie>("categorie", getContextModel(), "Documentcategorie");
		fieldSet.setPropertyNames("code", "naam", "actief");
		fieldSet.setSortAccordingToPropertyNames(true);
		fieldSet.setRenderMode(RenderMode.EDIT);
		form.add(fieldSet);

		fieldSet.addModifier("naam", new UniqueConstraintValidator<String>(fieldSet, "categorie",
			"code", "organisatie"));

		rechtenPanel =
			new DocumentTemplateRechtEditPanel("rollen",
				new PropertyModel<List<DocumentTemplateRecht>>(getContextModel(), "rechten"),
				new DocumentTemplateRechtTable(), getCategorieModel().getManager())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public DocumentTemplateRecht createNewT()
				{
					return new DocumentTemplateRecht(getDocumentCategorie());
				}

				@Override
				public boolean isVisible()
				{
					return getDocumentCategorie().isBeperkAutorisatie();
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

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				DocumentCategorie categorie =
					(DocumentCategorie) DocumentCategorieEditPage.this.getDefaultModelObject();
				for (DocumentTemplateRecht recht : categorie.getRechten())
				{
					recht.saveOrUpdate();
					recht.commit();
				}
				categorie.saveOrUpdate();
				categorie.commit();
				setResponsePage(DocumentCategorieEditPage.this.getReturnPage());
			}
		});

		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return DocumentCategorieEditPage.this.getReturnPage();
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return DocumentCategorieEditPage.this.getReturnPageClass();
			}

		}));

		panel.addButton(new ProbeerTeVerwijderenButton(panel, getContextModel(),
			"deze documentcategorie", DocumentCategorieZoekenPage.class));
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, new PropertyModel<String>(getDefaultModel(), "naam"));
	}

	private DocumentCategorie getDocumentCategorie()
	{
		return getContextModel().getObject();
	}

	private IChangeRecordingModel<DocumentCategorie> getCategorieModel()
	{
		return (IChangeRecordingModel<DocumentCategorie>) getContextModel();
	}
}
