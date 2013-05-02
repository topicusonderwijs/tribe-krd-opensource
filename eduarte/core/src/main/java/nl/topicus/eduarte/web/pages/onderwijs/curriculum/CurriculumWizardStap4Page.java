package nl.topicus.eduarte.web.pages.onderwijs.curriculum;

import java.util.Collections;
import java.util.Comparator;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.VolgendeButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VorigeButton;
import nl.topicus.eduarte.core.principals.onderwijs.CurriculumWizard;
import nl.topicus.eduarte.entities.curriculum.CurriculumOnderwijsproduct;
import nl.topicus.eduarte.web.components.menu.HomeMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.CurriculumOnderwijsproductEditTable;

import org.apache.poi.hssf.record.formula.functions.T;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

@InPrincipal(CurriculumWizard.class)
@PageInfo(title = "Curriculum Wizard Stap 4", menu = {"Home > Amarantis > Curriculum Wizard > Volgende > Volgende > Volgende"})
public class CurriculumWizardStap4Page extends AbstractCurriculumWizardPage
{
	private Form<Void> form;

	private EduArteDataPanel<CurriculumOnderwijsproduct> datapanel;

	public CurriculumWizardStap4Page(CurriculumWizardModel curriculumWizardModel)
	{
		super(HomeMenuItem.AmarantisCurriculumWizard, curriculumWizardModel,
			CurriculumWizardVoortgang.Stap4);

		curriculumWizardModel.createCurriculum();

		form = new Form<Void>("form");

		form.add(new CurriculumWizardInfoPanel("infopanel", curriculumWizardModel, getVoortgang()));

		ListModelDataProvider<CurriculumOnderwijsproduct> provider =
			new ListModelDataProvider<CurriculumOnderwijsproduct>(getCurriculumWizardModel()
				.getCurriculumOnderwijsproductenModel())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public IModel<CurriculumOnderwijsproduct> model(CurriculumOnderwijsproduct object)
				{
					return ModelFactory.getModel(object, getCurriculumWizardModel()
						.getModelManager());
				}
			};

		datapanel =
			new EduArteDataPanel<CurriculumOnderwijsproduct>("datapanel", provider,
				new CurriculumOnderwijsproductEditTable(getCurriculumWizardModel().getCurriculum())
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected void onDelete(IModel<CurriculumOnderwijsproduct> rowModel,
							AjaxRequestTarget target)
					{
						getCurriculumWizardModel().getCurriculumOnderwijsproducten().remove(
							rowModel.getObject());
						target.addComponent(datapanel);
					}

					@Override
					protected void onCopy(IModel<CurriculumOnderwijsproduct> rowModel,
							AjaxRequestTarget target)
					{
						getCurriculumWizardModel().voegNieuwCurriculumOnderwijsproductToe(
							rowModel.getObject());

						target.addComponent(datapanel);
					}
				});
		datapanel.setReuseItems(true);
		form.add(datapanel);

		AjaxLink<T> objectToevoegen = new AjaxLink<T>("objectToevoegen")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				getCurriculumWizardModel().voegNieuwCurriculumOnderwijsproductToe();
				target.addComponent(datapanel);
			}
		};
		form.add(objectToevoegen);

		AjaxLink<T> refresh = new AjaxLink<T>("refresh")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				sorteerCurriculumOnderwijsproducten();
				target.addComponent(datapanel);
			}
		};
		form.add(refresh);

		add(form);
		createComponents();
	}

	private void sorteerCurriculumOnderwijsproducten()
	{
		Collections.sort(getCurriculumWizardModel().getCurriculumOnderwijsproducten(),
			new Comparator<CurriculumOnderwijsproduct>()
			{
				@Override
				public int compare(CurriculumOnderwijsproduct curOndpr1,
						CurriculumOnderwijsproduct curOndpr2)
				{
					if (curOndpr1.getLeerjaar() == null)
					{
						if (curOndpr2.getLeerjaar() == null)
							return 0;
						else
							return 1;
					}

					if (curOndpr2.getLeerjaar() == null)
					{
						if (curOndpr1.getLeerjaar() == null)
							return 0;
						else
							return -1;
					}

					if (curOndpr1.getLeerjaar().compareTo(curOndpr2.getLeerjaar()) == 0)
					{
						if (curOndpr1.getPeriode() == null)
						{
							if (curOndpr2.getPeriode() == null)
								return 0;
							else
								return 1;
						}

						if (curOndpr2.getPeriode() == null)
						{
							if (curOndpr1.getPeriode() == null)
								return 0;
							else
								return -1;
						}

						return curOndpr1.getPeriode().compareTo(curOndpr2.getPeriode());
					}
					return curOndpr1.getLeerjaar().compareTo(curOndpr2.getLeerjaar());
				}
			});
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new VorigeButton(panel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onVorige()
			{
				setResponsePage(new CurriculumWizardStap3Page(getCurriculumWizardModel()));
			}
		});

		VolgendeButton opslaanEnNieuwOnderwijsproductButton = new VolgendeButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onVolgende()
			{
				getCurriculumWizardModel().save();

				CurriculumWizardModel model = new CurriculumWizardModel();
				model.setOrganisatieEenheid(getCurriculumWizardModel().getOrganisatieEenheid());
				model.setLocatie(getCurriculumWizardModel().getLocatie());
				model.setCohort(getCurriculumWizardModel().getCohort());
				model.setOpleiding(getCurriculumWizardModel().getOpleiding());

				setResponsePage(new CurriculumWizardStap2Page(model));
			}

		};
		opslaanEnNieuwOnderwijsproductButton
			.setLabel("Voltooien en nieuw onderwijsproduct toevoegen");
		panel.addButton(opslaanEnNieuwOnderwijsproductButton);

		voegVoltooienKnopToe(panel, form);
		voegAnnulerenKnopToe(panel);
	}
}
