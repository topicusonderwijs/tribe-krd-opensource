package nl.topicus.eduarte.krdparticipatie.web.pages.beheer.lesweek;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.dao.participatie.helpers.LesweekIndelingDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.LesdagIndeling;
import nl.topicus.eduarte.entities.participatie.LesuurIndeling;
import nl.topicus.eduarte.entities.participatie.LesweekIndeling;
import nl.topicus.eduarte.krdparticipatie.principals.beheer.LesweekindelingWrite;
import nl.topicus.eduarte.krdparticipatie.web.components.menu.enums.ParticipatieBeheerMenuItem;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.form.Form;

@InPrincipal(LesweekindelingWrite.class)
@PageInfo(title = "Lesweekindeling toevoegen/bewerken", menu = "Beheer > Participatie > Lesweekindeling > [toevoegen]")
public class LesweekIndelingBewerkenPage extends AbstractBeheerPage<LesweekIndeling> implements
		IEditPage
{

	private Form<Void> form;

	private Page returnPage;

	public LesweekIndelingBewerkenPage(Page returnPage)
	{
		this(ModelFactory.getCompoundChangeRecordingModel(new LesweekIndeling(),
			new DefaultModelManager(LesuurIndeling.class, LesdagIndeling.class,
				LesweekIndeling.class)), returnPage);
	}

	public LesweekIndelingBewerkenPage(IChangeRecordingModel<LesweekIndeling> lesweekIndelingModel,
			Page returnPage)
	{
		super(lesweekIndelingModel, ParticipatieBeheerMenuItem.Lesweekindeling);

		this.returnPage = returnPage;
		AutoFieldSet<LesweekIndeling> fieldset =
			new AutoFieldSet<LesweekIndeling>("fieldset", getContextModel());
		fieldset.setPropertyNames("naam", "omschrijving", "actief", "lesdagIndelingen");
		fieldset.setSortAccordingToPropertyNames(true);
		fieldset.setRenderMode(RenderMode.EDIT);
		fieldset.addFieldModifier(new ConstructorArgModifier("lesdagIndelingen",
			lesweekIndelingModel));
		form = new Form<Void>("form");
		form.add(fieldset);
		add(form);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit()
			{
				super.onSubmit();
				((IChangeRecordingModel) LesweekIndelingBewerkenPage.this.getDefaultModel())
					.saveObject();
				DataAccessRegistry.getHelper(LesweekIndelingDataAccessHelper.class).batchExecute();

				setResponsePage(returnPage);
			}
		});
		panel.addButton(new TerugButton(panel, returnPage));
		panel.addButton(new VerwijderButton(panel, "Verwijderen",
			"Weet u zeker dat u deze lesweekindeling wilt verwijderen?")
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				LesweekIndeling week = getContextModelObject();
				for (LesdagIndeling dag : week.getLesdagIndelingen())
				{
					for (LesuurIndeling uur : dag.getLesuurIndeling())
					{
						uur.delete();
					}
					dag.delete();
				}
				week.delete();
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
				setResponsePage(returnPage);
			}

			@Override
			public boolean isVisible()
			{
				return getContextModelObject().isSaved() && !getContextModelObject().isInGebruik();
			}
		});
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		returnPage.detach();
	}
}
