package nl.topicus.eduarte.onderwijscatalogus.web.pages.beheer.typetoets;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.TypeToets;
import nl.topicus.eduarte.onderwijscatalogus.principals.beheer.TypeToetsen;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "Type toets", menu = "Beheer > Onderwijs > [Type toets] > Nieuw type toets")
@InPrincipal(TypeToetsen.class)
public class TypeToetsEditPage extends AbstractBeheerPage<TypeToets> implements IEditPage
{
	private Form<Void> form;

	public TypeToetsEditPage(TypeToets typeToets, SecurePage returnPage)
	{
		super(ModelFactory.getModel(typeToets, new DefaultModelManager(TypeToets.class)),
			BeheerMenuItem.TypeToetsen);
		setReturnPage(returnPage);
		createForm();
	}

	private void createForm()
	{
		form = new Form<Void>("form");
		add(form);

		AutoFieldSet<TypeToets> fieldset =
			new AutoFieldSet<TypeToets>("typeToets", getContextModel(), "Type toets");
		fieldset.setPropertyNames("code", "naam", "actief");
		fieldset.setRenderMode(RenderMode.EDIT);
		fieldset.setSortAccordingToPropertyNames(true);
		form.add(fieldset);
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
				TypeToets typeToets = (TypeToets) TypeToetsEditPage.this.getDefaultModelObject();
				typeToets.saveOrUpdate();
				typeToets.commit();
				EduArteRequestCycle.get().setResponsePage(TypeToetsZoekenPage.class);
			}
		});
		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return TypeToetsEditPage.this.getReturnPage();
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return TypeToetsEditPage.this.getReturnPageClass();
			}

		}));
		panel.addButton(new VerwijderButton(panel, "Verwijder")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				TypeToets typeToets = (TypeToets) TypeToetsEditPage.this.getDefaultModelObject();
				typeToets.delete();
				typeToets.commit();
				EduArteRequestCycle.get().setResponsePage(TypeToetsZoekenPage.class);
			}

			@SuppressWarnings("unchecked")
			@Override
			public boolean isVisible()
			{
				TypeToets typeToets = (TypeToets) TypeToetsEditPage.this.getDefaultModelObject();
				if (typeToets.isSaved())
				{
					CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper<TypeToets, ? > helper =
						DataAccessRegistry
							.getHelper(CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class);
					return !helper.isTypeToetsInGebruik(typeToets);
				}
				return false;
			}
		});
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, new PropertyModel<String>(getDefaultModel(), "naam"));
	}
}
