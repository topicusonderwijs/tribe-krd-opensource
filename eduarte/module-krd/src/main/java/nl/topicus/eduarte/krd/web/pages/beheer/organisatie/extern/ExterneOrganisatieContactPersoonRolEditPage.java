package nl.topicus.eduarte.krd.web.pages.beheer.organisatie.extern;

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
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoonRol;
import nl.topicus.eduarte.krd.principals.beheer.ContactpersoonRollen;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.bottomrow.ProbeerTeVerwijderenButton;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

@PageInfo(title = "Contactpersoon rollen", menu = "Beheer > Beheer tabellen > [ExterneOrganisatieContactPersoonRol]")
@InPrincipal(ContactpersoonRollen.class)
public class ExterneOrganisatieContactPersoonRolEditPage extends
		AbstractBeheerPage<ExterneOrganisatieContactPersoonRol> implements
		IModuleEditPage<ExterneOrganisatieContactPersoonRol>
{
	private Form<Void> form;

	/**
	 * Constructor voor de ToevoegenButton.
	 * 
	 * @param returnPage
	 */
	public ExterneOrganisatieContactPersoonRolEditPage(SecurePage returnPage)
	{
		this(ModelFactory.getCompoundChangeRecordingModel(
			new ExterneOrganisatieContactPersoonRol(), new DefaultModelManager(
				ExterneOrganisatieContactPersoonRol.class)), returnPage);
	}

	/**
	 * Constructor voor de BewerkenButton.
	 * 
	 * @param rolModel
	 * @param returnPage
	 */
	public ExterneOrganisatieContactPersoonRolEditPage(
			IModel<ExterneOrganisatieContactPersoonRol> rolModel, SecurePage returnPage)
	{
		super(rolModel, BeheerMenuItem.ExterneOrganisatieContactPersoonRol);
		setReturnPage(returnPage);
		createForm();
	}

	private void createForm()
	{
		form = new Form<Void>("form");
		add(form);

		AutoFieldSet<ExterneOrganisatieContactPersoonRol> fieldSet =
			new AutoFieldSet<ExterneOrganisatieContactPersoonRol>("rol", getContextModel(), "Rol");
		fieldSet.setPropertyNames("naam", "praktijkopleiderBPV", "contactpersoonBPV", "actief");
		fieldSet.setRenderMode(RenderMode.EDIT);
		form.add(fieldSet);

		fieldSet.addModifier("naam", new UniqueConstraintValidator<String>(fieldSet, "rol", "naam",
			"organisatie"));

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
				ExterneOrganisatieContactPersoonRol rol =
					(ExterneOrganisatieContactPersoonRol) ExterneOrganisatieContactPersoonRolEditPage.this
						.getDefaultModelObject();
				rol.saveOrUpdate();
				rol.commit();

				EduArteRequestCycle.get().setResponsePage(
					ExterneOrganisatieContactPersoonRolZoekenPage.class);
			}
		});

		panel.addButton(new AnnulerenButton(panel, ExterneOrganisatieContactPersoonRolEditPage.this
			.getReturnPageClass()));

		panel.addButton(new ProbeerTeVerwijderenButton(panel, getContextModel(),
			"deze contactpersoon rol", ExterneOrganisatieContactPersoonRolZoekenPage.class));
	}
}
