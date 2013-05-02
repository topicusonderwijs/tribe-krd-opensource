package nl.topicus.eduarte.web.pages.beheer.organisatie;

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
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.core.principals.beheer.organisatie.OrganisatiemodelPrincipal;
import nl.topicus.eduarte.entities.organisatie.SoortOrganisatieEenheid;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.bottomrow.ProbeerTeVerwijderenButton;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

@PageInfo(title = "Soort organisatie-eenheid", menu = "Beheer > Beheer tabellen > [SoortOrganisatieEenheid]")
@InPrincipal(OrganisatiemodelPrincipal.class)
public class SoortOrganisatieEenheidEditPage extends AbstractBeheerPage<SoortOrganisatieEenheid>
		implements IModuleEditPage<SoortOrganisatieEenheid>
{
	private Form<Void> form;

	public SoortOrganisatieEenheidEditPage(SecurePage returnPage)
	{
		this(ModelFactory.getModel(new SoortOrganisatieEenheid(), new DefaultModelManager(
			SoortOrganisatieEenheid.class)), returnPage);
	}

	public SoortOrganisatieEenheidEditPage(IModel<SoortOrganisatieEenheid> soortOrganisatieEenheid,
			SecurePage returnPage)
	{
		super(soortOrganisatieEenheid, BeheerMenuItem.SoortOrganisatie_Eenheden);
		setReturnPage(returnPage);
		createForm();
	}

	private void createForm()
	{
		form = new Form<Void>("form");
		add(form);

		AutoFieldSet<SoortOrganisatieEenheid> soortOrganisatieEenheidFieldSet =
			new AutoFieldSet<SoortOrganisatieEenheid>("soortOrganisatieEenheid", getContextModel(),
				"Soort organisatie-eenheid");
		soortOrganisatieEenheidFieldSet.setPropertyNames("code", "naam", "actief");
		soortOrganisatieEenheidFieldSet.setRenderMode(RenderMode.EDIT);
		soortOrganisatieEenheidFieldSet.setSortAccordingToPropertyNames(true);
		form.add(soortOrganisatieEenheidFieldSet);

		form.add(new UniqueConstraintFormValidator(soortOrganisatieEenheidFieldSet,
			"soort organisatie-eenheid", "code"));

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
				SoortOrganisatieEenheid organisatieEenheid = getContextModelObject();
				organisatieEenheid.saveOrUpdate();
				organisatieEenheid.commit();

				EduArteRequestCycle.get().setResponsePage(
					SoortOrganisatieEenheidEditPage.this.getReturnPage());
			}
		});

		panel.addButton(new AnnulerenButton(panel, SoortOrganisatieEenheidEditPage.this
			.getReturnPage()));

		panel.addButton(new ProbeerTeVerwijderenButton(panel, getContextModel(),
			"deze soort organisatie-eenheid", SoortOrganisatieEenheidZoekenPage.class));
	}
}
