package nl.topicus.eduarte.onderwijscatalogus.web.pages.beheer.soortpraktijklokaal;

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
import nl.topicus.eduarte.entities.onderwijsproduct.SoortPraktijklokaal;
import nl.topicus.eduarte.onderwijscatalogus.principals.beheer.SoortPraktijklokalen;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "Soort praktijklokaal", menu = "Beheer > Onderwijs > [Soort praktijklokaal] > Nieuwe soort praktijklokaal")
@InPrincipal(SoortPraktijklokalen.class)
public class SoortPraktijklokaalEditPage extends AbstractBeheerPage<SoortPraktijklokaal> implements
		IEditPage
{
	private Form<Void> form;

	public SoortPraktijklokaalEditPage(SoortPraktijklokaal soortPraktijklokaal,
			SecurePage returnPage)
	{
		super(ModelFactory.getModel(soortPraktijklokaal, new DefaultModelManager(
			SoortPraktijklokaal.class)), BeheerMenuItem.SoortPraktijklokalen);
		setReturnPage(returnPage);
		createForm();
	}

	private void createForm()
	{
		form = new Form<Void>("form");
		add(form);

		AutoFieldSet<SoortPraktijklokaal> fieldSet =
			new AutoFieldSet<SoortPraktijklokaal>("soortPraktijklokaal", getContextModel(),
				"Soort praktijklokaal");
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
				SoortPraktijklokaal soortPraktijklokaal = getContextModelObject();
				soortPraktijklokaal.saveOrUpdate();
				soortPraktijklokaal.commit();
				EduArteRequestCycle.get().setResponsePage(SoortPraktijklokaalZoekenPage.class);
			}
		});
		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return SoortPraktijklokaalEditPage.this.getReturnPage();
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return SoortPraktijklokaalEditPage.this.getReturnPageClass();
			}

		}));
		panel.addButton(new VerwijderButton(panel, "Verwijder")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				SoortPraktijklokaal soortPraktijklokaal = getContextModelObject();
				soortPraktijklokaal.delete();
				soortPraktijklokaal.commit();
				EduArteRequestCycle.get().setResponsePage(SoortPraktijklokaalZoekenPage.class);
			}

			@SuppressWarnings("unchecked")
			@Override
			public boolean isVisible()
			{
				SoortPraktijklokaal soortPraktijklokaal = getContextModelObject();
				if (soortPraktijklokaal.isSaved())
				{
					CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper<SoortPraktijklokaal, ? > helper =
						DataAccessRegistry
							.getHelper(CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class);
					return !helper.isSoortPraktijklokaalInGebruik(soortPraktijklokaal);
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
