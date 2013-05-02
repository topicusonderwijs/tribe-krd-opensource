package nl.topicus.eduarte.krd.web.pages.beheer;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.validators.UniqueConstraintValidator;
import nl.topicus.eduarte.entities.kenmerk.KenmerkCategorie;
import nl.topicus.eduarte.krd.principals.beheer.systeemtabellen.KenmerkPrincipal;
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

@PageInfo(title = "Kenmerkcategorie", menu = "Beheer > Beheer tabellen > Kenmerkcategoriën > [kenmerkcategorie]")
@InPrincipal(KenmerkPrincipal.class)
public class KenmerkCategorieEditPage extends AbstractBeheerPage<KenmerkCategorie> implements
		IModuleEditPage<KenmerkCategorie>
{
	private Form<Void> form;

	/**
	 * Constructor voor de ToevoegenButton.
	 * 
	 * @param returnPage
	 */
	public KenmerkCategorieEditPage(SecurePage returnPage)
	{
		this(ModelFactory.getCompoundChangeRecordingModel(new KenmerkCategorie(),
			new DefaultModelManager(KenmerkCategorie.class)), returnPage);
	}

	/**
	 * Constructor voor de BewerkenButton.
	 * 
	 * @param categorieModel
	 * @param returnPage
	 */
	public KenmerkCategorieEditPage(IModel<KenmerkCategorie> categorieModel, SecurePage returnPage)
	{
		super(categorieModel, BeheerMenuItem.Kenmerkcategorien);
		setReturnPage(returnPage);
		createForm();
	}

	private void createForm()
	{
		form = new Form<Void>("form");
		add(form);

		AutoFieldSet<KenmerkCategorie> fieldSet =
			new AutoFieldSet<KenmerkCategorie>("categorie", getContextModel(), "Kenmerkcategorie");
		fieldSet.setPropertyNames("code", "naam", "actief");
		fieldSet.setSortAccordingToPropertyNames(true);
		fieldSet.setRenderMode(RenderMode.EDIT);
		form.add(fieldSet);

		fieldSet.addModifier("naam", new UniqueConstraintValidator<String>(fieldSet, "categorie",
			"code", "organisatie"));

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
				KenmerkCategorie categorie =
					(KenmerkCategorie) KenmerkCategorieEditPage.this.getDefaultModelObject();
				categorie.saveOrUpdate();
				categorie.commit();
				setResponsePage(KenmerkCategorieEditPage.this.getReturnPage());
			}
		});

		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return KenmerkCategorieEditPage.this.getReturnPage();
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return KenmerkCategorieEditPage.this.getReturnPageClass();
			}

		}));

		panel.addButton(new ProbeerTeVerwijderenButton(panel, getContextModel(),
			"deze kenmerkcategorie", KenmerkCategorieZoekenPage.class));
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, new PropertyModel<String>(getDefaultModel(), "naam"));
	}
}
