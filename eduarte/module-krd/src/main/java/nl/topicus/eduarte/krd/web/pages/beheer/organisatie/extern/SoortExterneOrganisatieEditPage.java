package nl.topicus.eduarte.krd.web.pages.beheer.organisatie.extern;

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
import nl.topicus.cobra.web.validators.UniqueConstraintValidator;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.SoortExterneOrganisatie;
import nl.topicus.eduarte.krd.principals.beheer.SoortExterneOrganisaties;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "Soort externe organisatie", menu = "Beheer > Beheer tabellen > [Soort externe organisatie] > Nieuwe Soort externe organisatie")
@InPrincipal(SoortExterneOrganisaties.class)
public class SoortExterneOrganisatieEditPage extends AbstractBeheerPage<SoortExterneOrganisatie>
		implements IEditPage
{
	private Form<Void> form;

	public SoortExterneOrganisatieEditPage(SoortExterneOrganisatie soortExterneOrganisatie,
			SecurePage returnPage)
	{
		super(ModelFactory.getModel(soortExterneOrganisatie, new DefaultModelManager(
			SoortExterneOrganisatie.class)), BeheerMenuItem.SoortExterneOrganisaties);
		setReturnPage(returnPage);
		createForm();
	}

	private void createForm()
	{
		form = new Form<Void>("form");
		add(form);

		AutoFieldSet<SoortExterneOrganisatie> fieldSet =
			new AutoFieldSet<SoortExterneOrganisatie>("soortExterneOrganisatie", getContextModel(),
				"Soort externe organisatie");
		fieldSet.setPropertyNames("code", "naam", "tonenBijVooropleiding", "actief");

		fieldSet.setRenderMode(RenderMode.EDIT);
		fieldSet.setSortAccordingToPropertyNames(true);

		fieldSet.addModifier("code", new UniqueConstraintValidator<String>(fieldSet,
			"soort externe organisatie", "code", "organisatie"));

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
				SoortExterneOrganisatie soortExterneOrganisatie =
					(SoortExterneOrganisatie) SoortExterneOrganisatieEditPage.this
						.getDefaultModelObject();
				soortExterneOrganisatie.saveOrUpdate();
				soortExterneOrganisatie.commit();
				EduArteRequestCycle.get().setResponsePage(SoortExterneOrganisatieZoekenPage.class);
			}
		});
		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return SoortExterneOrganisatieEditPage.this.getReturnPage();
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return SoortExterneOrganisatieEditPage.this.getReturnPageClass();
			}

		}));
		panel.addButton(new VerwijderButton(panel, "Verwijder")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				SoortExterneOrganisatie soortExterneOrganisatie =
					(SoortExterneOrganisatie) SoortExterneOrganisatieEditPage.this
						.getDefaultModelObject();
				soortExterneOrganisatie.delete();
				soortExterneOrganisatie.commit();
				EduArteRequestCycle.get().setResponsePage(SoortExterneOrganisatieZoekenPage.class);
			}

			@SuppressWarnings("unchecked")
			@Override
			public boolean isVisible()
			{
				SoortExterneOrganisatie soortExterneOrganisatie = getContextModelObject();
				if (soortExterneOrganisatie.isSaved())
				{
					CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper<SoortExterneOrganisatie, ? > helper =
						DataAccessRegistry
							.getHelper(CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class);
					return !helper.isSoortExterneOrganisatieInGebruik(soortExterneOrganisatie);
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
