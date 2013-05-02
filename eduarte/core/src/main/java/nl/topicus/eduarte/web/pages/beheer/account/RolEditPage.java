package nl.topicus.eduarte.web.pages.beheer.account;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.VersionedForm;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.cobra.web.components.shortcut.ActionKey;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.cobra.web.validators.UniqueConstraintValidator;
import nl.topicus.eduarte.core.principals.beheer.account.RightsWrite;
import nl.topicus.eduarte.entities.rapportage.DeelnemerZoekOpdrachtRecht;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateRecht;
import nl.topicus.eduarte.entities.security.authorization.AuthorisatieNiveau;
import nl.topicus.eduarte.entities.security.authorization.Recht;
import nl.topicus.eduarte.entities.security.authorization.Rol;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.rechten.RechtenPanel;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;

@PageInfo(title = "Rol bewerken", menu = {"Beheer > Accountbeheer > Rollen en rechten > toevoegen",
	"Beheer > Accountbeheer > Rollen en rechten > [Rol] > bewerken"})
@InPrincipal(RightsWrite.class)
public class RolEditPage extends AbstractBeheerPage<Rol> implements IEditPage
{
	private VersionedForm<Rol> form;

	public RolEditPage(Rol rol)
	{
		super(BeheerMenuItem.RollenEnRechten);
		setDefaultModel(ModelFactory.getCompoundChangeRecordingModel(rol, new DefaultModelManager(
			DocumentTemplateRecht.class, DeelnemerZoekOpdrachtRecht.class, Recht.class, Rol.class)));

		form = new VersionedForm<Rol>("form", getContextModel());
		form.setMultiPart(true);

		AutoFieldSet<Rol> fields = new AutoFieldSet<Rol>("fields", getContextModel(), "Rol");
		fields.setRenderMode(RenderMode.EDIT);
		fields.addFieldModifier(new ConstructorArgModifier("categorie", true));
		fields.addModifier("naam", new UniqueConstraintValidator<String>(form, "rol", "naam",
			"organisatie"));
		form.add(fields);

		form.add(new RechtenPanel("rechten", getContextModel(), false));
		add(form);

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
				saveRol();
			}

		});

		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				saveRol();
				Rol rol = new Rol();
				rol.setRechtenSoort(getRol().getRechtenSoort());
				rol.setAuthorisatieNiveau(AuthorisatieNiveau.REST);
				RolEditPage page = new RolEditPage(rol);
				setResponsePage(page);
			}

			@Override
			public String getLabel()
			{
				return "Opslaan en nieuwe toevoegen";
			}

			@Override
			public ActionKey getAction()
			{
				ActionKey action = CobraKeyAction.VOLGENDE;
				return action;
			}
		});
		panel.addButton(new VerwijderButton(panel, "Verwijderen",
			"Weet u zeker dat u deze rol wilt verwijderen?")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				deleteRol();
			}

			@Override
			public boolean isVisible()
			{
				return getRol().isSaved() && getRol().getAccounts().isEmpty();
			}
		});
		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				if (getRol().isSaved())
					return new RolOverviewPage(getRol());
				else
					return new RollenEnRechtenPage();
			}

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				if (getRol().isSaved())
					return RolOverviewPage.class;
				else
					return RollenEnRechtenPage.class;
			}
		}));
	}

	private void deleteRol()
	{
		IChangeRecordingModel<Rol> rolModel = getRolModel();
		rolModel.deleteObject();
		DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
		setResponsePage(new RollenEnRechtenPage());
	}

	private void saveRol()
	{
		IChangeRecordingModel<Rol> rolModel = getRolModel();
		rolModel.saveObject();
		getRol().commit();
		setResponsePage(new RolOverviewPage(rolModel.getObject()));
	}

	private Rol getRol()
	{
		return getContextModelObject();
	}

	private IChangeRecordingModel<Rol> getRolModel()
	{
		return (IChangeRecordingModel<Rol>) getContextModel();
	}
}
