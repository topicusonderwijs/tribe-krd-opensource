package nl.topicus.eduarte.web.pages.beheer.documenten;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.validators.UniqueConstraintFormValidator;
import nl.topicus.eduarte.core.principals.beheer.systeem.DocumentPrincipal;
import nl.topicus.eduarte.entities.bijlage.DocumentType;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.bottomrow.ProbeerTeVerwijderenButton;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "Documenttype", menu = "Beheer > Beheer tabellen > Documenttypes > [documenttype]")
@InPrincipal(DocumentPrincipal.class)
public class DocumentTypeEditPage extends AbstractBeheerPage<DocumentType> implements
		IModuleEditPage<DocumentType>
{
	private Form<Void> form;

	/**
	 * Constructor voor de ToevoegenButton.
	 * 
	 * @param returnPage
	 */
	public DocumentTypeEditPage(SecurePage returnPage)
	{
		this(ModelFactory.getCompoundChangeRecordingModel(new DocumentType(),
			new DefaultModelManager(DocumentType.class)), returnPage);
	}

	/**
	 * Constructor voor de BewerkenButton.
	 * 
	 * @param documentTypeModel
	 * @param returnPage
	 */
	public DocumentTypeEditPage(IModel<DocumentType> documentTypeModel, SecurePage returnPage)
	{
		super(documentTypeModel, BeheerMenuItem.DocumentTypes);
		setReturnPage(returnPage);
		createForm();
	}

	private void createForm()
	{
		form = new Form<Void>("form");
		add(form);

		AutoFieldSet<DocumentType> fieldSet =
			new AutoFieldSet<DocumentType>("documenttype", getContextModel(), "Documenttype");
		fieldSet.setPropertyNames("categorie", "code", "naam", "actief");
		fieldSet.setSortAccordingToPropertyNames(true);
		fieldSet.setRenderMode(RenderMode.EDIT);
		form.add(fieldSet);
		form.add(new UniqueConstraintFormValidator(fieldSet, "documenttype", "code"));
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
				DocumentType documenttype =
					(DocumentType) DocumentTypeEditPage.this.getDefaultModelObject();
				documenttype.saveOrUpdate();
				documenttype.commit();
				setResponsePage(DocumentTypeEditPage.this.getReturnPage());
			}
		});

		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return DocumentTypeEditPage.this.getReturnPage();
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return DocumentTypeEditPage.this.getReturnPageClass();
			}

		}));

		panel.addButton(new ProbeerTeVerwijderenButton(panel, getContextModel(),
			"dit documenttype", DocumentTypeZoekenPage.class));
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, new PropertyModel<String>(getDefaultModel(), "naam"));
	}
}
