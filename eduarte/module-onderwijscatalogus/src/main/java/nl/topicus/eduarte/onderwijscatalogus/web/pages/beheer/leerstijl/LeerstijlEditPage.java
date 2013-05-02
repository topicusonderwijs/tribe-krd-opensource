package nl.topicus.eduarte.onderwijscatalogus.web.pages.beheer.leerstijl;

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
import nl.topicus.eduarte.entities.onderwijsproduct.Leerstijl;
import nl.topicus.eduarte.onderwijscatalogus.principals.beheer.Leerstijlen;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "Leerstijl", menu = "Beheer > Onderwijs > [Leerstijl] > Nieuwe leerstijl")
@InPrincipal(Leerstijlen.class)
public class LeerstijlEditPage extends AbstractBeheerPage<Leerstijl> implements IEditPage
{
	private Form<Void> form;

	public LeerstijlEditPage(Leerstijl leerstijl, SecurePage returnPage)
	{
		super(ModelFactory.getModel(leerstijl, new DefaultModelManager(Leerstijl.class)),
			BeheerMenuItem.Leerstijlen);
		setReturnPage(returnPage);
		createForm();
	}

	private void createForm()
	{
		form = new Form<Void>("form");
		add(form);

		AutoFieldSet<Leerstijl> fieldSet =
			new AutoFieldSet<Leerstijl>("leerstijl", getContextModel(), "Leerstijl");
		fieldSet.setPropertyNames("code", "naam", "actief");
		fieldSet.setRenderMode(RenderMode.EDIT);
		fieldSet.setSortAccordingToPropertyNames(true);
		form.add(fieldSet);
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
				Leerstijl leerstijl = getContextModelObject();
				leerstijl.saveOrUpdate();
				leerstijl.commit();
				EduArteRequestCycle.get().setResponsePage(LeerstijlZoekenPage.class);
			}
		});
		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return LeerstijlEditPage.this.getReturnPage();
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return LeerstijlEditPage.this.getReturnPageClass();
			}
		}));
		panel.addButton(new VerwijderButton(panel, "Verwijder")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				Leerstijl leerstijl = getContextModelObject();
				leerstijl.delete();
				leerstijl.commit();
				EduArteRequestCycle.get().setResponsePage(LeerstijlZoekenPage.class);
			}

			@SuppressWarnings("unchecked")
			@Override
			public boolean isVisible()
			{
				Leerstijl leerstijl = getContextModelObject();
				if (leerstijl.isSaved())
				{
					CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper<Leerstijl, ? > helper =
						DataAccessRegistry
							.getHelper(CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class);
					return !helper.isLeerstijlInGebruik(leerstijl);
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
