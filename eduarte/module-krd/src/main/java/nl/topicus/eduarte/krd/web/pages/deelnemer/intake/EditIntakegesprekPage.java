package nl.topicus.eduarte.krd.web.pages.deelnemer.intake;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.cobra.web.security.RequiredSecurityCheck;
import nl.topicus.eduarte.app.security.checks.OrganisatieEenheidLocatieSecurityCheck;
import nl.topicus.eduarte.entities.inschrijving.Intakegesprek;
import nl.topicus.eduarte.entities.vrijevelden.IntakegesprekVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldOptieKeuze;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerIntakesWrite;
import nl.topicus.eduarte.krd.web.components.panels.intake.IntakegesprekEditPanel;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.web.pages.deelnemer.intake.DeelnemerIntakePage;

import org.apache.wicket.markup.html.form.Form;

@InPrincipal(DeelnemerIntakesWrite.class)
@PageInfo(title = "Intakegesprek Bewerken", menu = {"Deelnemer > [deelnemer] > Verbintenis > [intakegesprek] > Bewerken"})
@RequiredSecurityCheck(OrganisatieEenheidLocatieSecurityCheck.class)
public class EditIntakegesprekPage extends AbstractDeelnemerPage implements
		IModuleEditPage<Intakegesprek>
{
	private Form<Intakegesprek> form;

	private DeelnemerIntakePage returnPage;

	public EditIntakegesprekPage(Intakegesprek gesprek, DeelnemerIntakePage returnPage)
	{
		super(DeelnemerMenuItem.Intake, gesprek.getDeelnemer(), gesprek.getVerbintenis());

		this.returnPage = returnPage;

		add(form =
			new Form<Intakegesprek>("intakegesprekForm", ModelFactory
				.getCompoundChangeRecordingModel(gesprek, new DefaultModelManager(
					VrijVeldOptieKeuze.class, IntakegesprekVrijVeld.class, Intakegesprek.class))));

		IntakegesprekEditPanel editPanel = new IntakegesprekEditPanel("editpanel", form.getModel())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean getIsIntakeScherm()
			{
				return false;
			}
		};
		form.add(editPanel);

		createComponents();
	}

	private Intakegesprek getGesprek()
	{
		return form.getModelObject();
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
				((IChangeRecordingModel< ? >) form.getModel()).saveObject();
				getContextVerbintenis().saveOrUpdate();
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
				getContextVerbintenis().refresh();
				setResponsePage(new DeelnemerIntakePage(getContextVerbintenis(), getGesprek()));
			}
		});
		panel.addButton(new AnnulerenButton(panel, returnPage));
		panel.addButton(new VerwijderButton(panel)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				((IChangeRecordingModel< ? >) form.getModel()).deleteObject();
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
				getContextVerbintenis().refresh();
				setResponsePage(new DeelnemerIntakePage(getContextVerbintenis()));
			}

			@Override
			public boolean isVisible()
			{
				return getGesprek().isSaved();
			}
		});
	}
}
