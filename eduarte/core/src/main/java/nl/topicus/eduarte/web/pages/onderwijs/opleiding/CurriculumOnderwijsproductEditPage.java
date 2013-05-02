package nl.topicus.eduarte.web.pages.onderwijs.opleiding;

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
import nl.topicus.eduarte.web.components.menu.OpleidingMenuItem;
import nl.topicus.eduarte.web.components.modalwindow.opleiding.CurriculumOnderwijsproductEditPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.CurriculumOnderwijsproductTable;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "Curriculum Onderwijsproduct bewerken", menu = {"Onderwijs > [opleiding] > Curriculum > [curriculum] > Bewerken"})
@InPrincipal(CurriculumWijzigen.class)
public class CurriculumOnderwijsproductEditPage extends AbstractOpleidingPage implements IEditPage
{
	private static final long serialVersionUID = 1L;

	private IChangeRecordingModel<Curriculum> curriculumModel;

	private SecurePage returnPage;

	private Form<Curriculum> form;

	public CurriculumOnderwijsproductEditPage(Curriculum curriculum, SecurePage returnPage)
	{
		super(OpleidingMenuItem.Curriculum, curriculum.getOpleiding());
		this.returnPage = returnPage;

		curriculumModel =
			ModelFactory.getCompoundChangeRecordingModel(curriculum, new DefaultModelManager(
				CurriculumOnderwijsproduct.class, Curriculum.class));

		form = new Form<Curriculum>("form", curriculumModel);

		CurriculumOnderwijsproductEditPanel panel =
			new CurriculumOnderwijsproductEditPanel("editpanel",
				new PropertyModel<List<CurriculumOnderwijsproduct>>(curriculumModel,
					"curriculumOnderwijsproducten"), curriculumModel.getManager(),
				new CurriculumOnderwijsproductTable(curriculum))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public CurriculumOnderwijsproduct createNewT()
				{
					CurriculumOnderwijsproduct newT = new CurriculumOnderwijsproduct();
					newT.setCurriculum(getCurriculum());
					return newT;
				}

				@Override
				protected void onSaveCurrent(AjaxRequestTarget target,
						CurriculumOnderwijsproduct object)
				{
					sorteerCurriculumOnderwijsproducten();
				}

				@Override
				protected void onSaveNew(AjaxRequestTarget target, CurriculumOnderwijsproduct object)
				{
					sorteerCurriculumOnderwijsproducten();
				}

			};

		form.add(panel);

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

				curriculumModel.saveObject();
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
				setResponsePage(returnPage);
			}
		});
		panel.addButton(new AnnulerenButton(panel, returnPage));

	}

	private void sorteerCurriculumOnderwijsproducten()
	{
		Collections.sort(getCurriculum().getCurriculumOnderwijsproducten(),
			new Comparator<CurriculumOnderwijsproduct>()
			{
				@Override
				public int compare(CurriculumOnderwijsproduct curOndpr1,
						CurriculumOnderwijsproduct curOndpr2)
				{
					if (curOndpr1.getLeerjaar().compareTo(curOndpr2.getLeerjaar()) == 0)
					{
						return curOndpr1.getPeriode().compareTo(curOndpr2.getPeriode());
					}
					return curOndpr1.getLeerjaar().compareTo(curOndpr2.getLeerjaar());
				}
			});
	}

	@SuppressWarnings("unused")
	private void checkOnvolledigeAfnames(AjaxRequestTarget target,
			CurriculumOnderwijsproduct curOndpr)
	{
		if (!curOndpr.isVolledigAfgenomen())
		{
			warn("Onderwijsproduct " + curOndpr.getOnderwijsproduct().toString()
				+ " is onvolledig afgenomen: "
				+ curOndpr.getAfgenomenOnderwijstijdOnderwijsproduct() + " van "
				+ curOndpr.getOnderwijsproduct().getOnderwijstijd() + " totaal.");

			refreshFeedback(target);
		}
	}

	private Curriculum getCurriculum()
	{
		return curriculumModel.getObject();
	}

	@Override
	protected void detachModel()
	{
		super.detachModel();
		ComponentUtil.detachQuietly(curriculumModel);
	}
}