package nl.topicus.eduarte.onderwijscatalogus.web.pages.beheer.soortOnderwijsproduct;

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
import nl.topicus.eduarte.entities.onderwijsproduct.SoortOnderwijsproduct;
import nl.topicus.eduarte.onderwijscatalogus.principals.beheer.SoortOnderwijsproducten;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "Soort onderwijsproduct", menu = "Beheer > Onderwijs > [Soort onderwijsproduct] > Nieuw soort onderwijsproduct")
@InPrincipal(SoortOnderwijsproducten.class)
public class SoortOnderwijsproductEditPage extends AbstractBeheerPage<SoortOnderwijsproduct>
		implements IEditPage
{
	private Form<Void> form;

	public SoortOnderwijsproductEditPage(SoortOnderwijsproduct soortOnderwijsproduct,
			SecurePage returnPage)
	{
		super(ModelFactory.getModel(soortOnderwijsproduct, new DefaultModelManager(
			SoortOnderwijsproduct.class)), BeheerMenuItem.SoortOnderwijsproducten);
		setReturnPage(returnPage);
		createForm();
	}

	private void createForm()
	{
		form = new Form<Void>("form");
		add(form);

		AutoFieldSet<SoortOnderwijsproduct> fieldSet =
			new AutoFieldSet<SoortOnderwijsproduct>("leerstijl", getContextModel(),
				"Soort onderwijsproduct");
		fieldSet.setPropertyNames("code", "naam", "actief", "summatief", "stage");
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
				SoortOnderwijsproduct soortOnderwijsproduct = getContextModelObject();
				soortOnderwijsproduct.saveOrUpdate();
				soortOnderwijsproduct.commit();
				EduArteRequestCycle.get().setResponsePage(SoortOnderwijsproductZoekenPage.class);
			}
		});
		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return SoortOnderwijsproductEditPage.this.getReturnPage();
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return SoortOnderwijsproductEditPage.this.getReturnPageClass();
			}
		}));
		panel.addButton(new VerwijderButton(panel, "Verwijder")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				SoortOnderwijsproduct soortOnderwijsproduct = getContextModelObject();
				soortOnderwijsproduct.delete();
				soortOnderwijsproduct.commit();
				EduArteRequestCycle.get().setResponsePage(SoortOnderwijsproductZoekenPage.class);
			}

			@SuppressWarnings("unchecked")
			@Override
			public boolean isVisible()
			{
				SoortOnderwijsproduct soortOnderwijsproduct = getContextModelObject();
				if (soortOnderwijsproduct.isSaved())
				{
					CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper<Leerstijl, ? > helper =
						DataAccessRegistry
							.getHelper(CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class);
					return !helper.isSoortOnderwijsproductInGebruik(soortOnderwijsproduct);
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
