package nl.topicus.eduarte.web.pages.onderwijs.opleiding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.core.principals.onderwijs.CurriculumWijzigen;
import nl.topicus.eduarte.entities.curriculum.Curriculum;
import nl.topicus.eduarte.entities.curriculum.CurriculumOnderwijsproduct;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.web.components.menu.OpleidingMenuItem;
import nl.topicus.eduarte.web.components.modalwindow.opleiding.CurriculumEditPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.CurriculumTable;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "Curriculum Overzicht", menu = {"Onderwijs > [opleiding] > Curriculum"})
@InPrincipal(CurriculumWijzigen.class)
public class CurriculumEditPage extends AbstractOpleidingPage implements IEditPage
{
	private static final long serialVersionUID = 1L;

	private SecurePage returnPage;

	private Form<Void> form;

	private IChangeRecordingModel<Opleiding> opleidingModel;

	private IModel<ArrayList<Curriculum>> toegevoegdeCurriculums =
		ModelFactory.getListModel(new ArrayList<Curriculum>());

	public CurriculumEditPage(Opleiding opleiding, SecurePage returnPage)
	{
		super(OpleidingMenuItem.Curriculum, opleiding);
		this.returnPage = returnPage;

		opleidingModel =
			ModelFactory.getCompoundChangeRecordingModel(opleiding, new DefaultModelManager(
				CurriculumOnderwijsproduct.class, Curriculum.class, Opleiding.class));

		form = new Form<Void>("form");

		CurriculumEditPanel editPanel =
			new CurriculumEditPanel("editpanel", new PropertyModel<List<Curriculum>>(
				opleidingModel, "curriculums"), opleidingModel.getManager(), new CurriculumTable())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Curriculum createNewT()
				{
					return new Curriculum(opleidingModel.getObject());
				}

				@Override
				protected void onSaveCurrent(AjaxRequestTarget target, Curriculum object)
				{
					sorteerCurriculums();
				}

				@Override
				protected void onSaveNew(AjaxRequestTarget target, Curriculum object)
				{
					controleerOpDubbelCohort(target, object);
					sorteerCurriculums();
				}
			};

		form.add(editPanel);
		add(form);

		createComponents();
	}

	private void sorteerCurriculums()
	{
		Collections.sort(opleidingModel.getObject().getCurriculums(), new Comparator<Curriculum>()
		{
			@Override
			public int compare(Curriculum cur1, Curriculum cur2)
			{
				return cur1.getCohort().getBegindatum().compareTo(cur2.getCohort().getBegindatum());
			}
		});
	}

	private void controleerOpDubbelCohort(AjaxRequestTarget target, Curriculum nieuwCurriculum)
	{
		boolean verwijderen = false;

		for (Curriculum curriclum : toegevoegdeCurriculums.getObject())
		{
			boolean gelijkCohort = curriclum.getCohort().equals(nieuwCurriculum.getCohort());
			boolean gelijkeOrganisatieEenheid =
				curriclum.getOrganisatieEenheid().equals(nieuwCurriculum.getOrganisatieEenheid());
			boolean gelijkeLocatie =
				(curriclum.getLocatie() == null && nieuwCurriculum.getLocatie() == null)
					|| (curriclum.getLocatie() != null && nieuwCurriculum.getLocatie() != null && curriclum
						.getLocatie().equals(nieuwCurriculum.getLocatie()));

			if (gelijkCohort && gelijkeOrganisatieEenheid && gelijkeLocatie)
			{
				verwijderen = true;
			}
		}

		if (verwijderen)
		{
			opleidingModel.getObject().getCurriculums().remove(nieuwCurriculum);
			warn("Er bestaat al een curriculum voor geselecteerd cohort, organisatie-eenheid en locatie.");
			refreshFeedback(target);
		}
		else
			toegevoegdeCurriculums.getObject().add(nieuwCurriculum);
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
				opleidingModel.saveObject();
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
				setResponsePage(returnPage);
			}
		});
		panel.addButton(new AnnulerenButton(panel, returnPage));
	}

	@Override
	public void detachModels()
	{
		super.detachModels();
		ComponentUtil.detachQuietly(opleidingModel);
		ComponentUtil.detachQuietly(toegevoegdeCurriculums);
	}
}
