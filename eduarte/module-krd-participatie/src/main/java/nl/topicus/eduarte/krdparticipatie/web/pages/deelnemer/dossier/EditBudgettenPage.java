package nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.eduarte.dao.participatie.helpers.BudgetDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.participatie.Budget;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krdparticipatie.principals.deelnemer.DeelnemerBudgettenWrite;
import nl.topicus.eduarte.krdparticipatie.web.components.menu.enums.ParticipatieDeelnemerMenuItem;
import nl.topicus.eduarte.participatie.zoekfilters.BudgetZoekFilter;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.web.components.quicksearch.onderwijsproduct.OnderwijsproductSearchEditor;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.RangeValidator;

/**
 * Pagina met 1 budget voor 1 deelnemern
 * 
 * @author vandekamp
 */
@PageInfo(title = "Budget bewerken", menu = {"Deelnemer > [deelnemer] > Participatie > Budgetten > [Budget]"})
@InPrincipal(DeelnemerBudgettenWrite.class)
public class EditBudgettenPage extends AbstractDeelnemerPage implements IModuleEditPage<Budget>
{
	private static final long serialVersionUID = 1L;

	private Form<Budget> form;

	private PropertyModel<Onderwijsproduct> myModel;

	private static Budget getDefaultBudget(Verbintenis verbintenis)
	{
		Budget budget = new Budget();
		budget.setVerbintenis(verbintenis);
		budget.setAantalUur(0);
		return budget;
	}

	public EditBudgettenPage(DeelnemerProvider provider)
	{
		this(provider.getDeelnemer());
	}

	public EditBudgettenPage(Deelnemer deelnemer)
	{
		this(deelnemer, deelnemer.getEersteInschrijvingOpPeildatum());
	}

	public EditBudgettenPage(Verbintenis verbintenis)
	{
		this(verbintenis.getDeelnemer(), verbintenis);
	}

	public EditBudgettenPage(Deelnemer deelnemer, Verbintenis verbintenis)
	{
		this(deelnemer, verbintenis, getDefaultBudget(verbintenis));
	}

	public EditBudgettenPage(Deelnemer deelnemer, Verbintenis verbintenis, Budget budget)
	{
		super(ParticipatieDeelnemerMenuItem.Budgetten, deelnemer, verbintenis);

		form =
			new Form<Budget>("form", ModelFactory.getCompoundModel(budget, new DefaultModelManager(
				Budget.class)))
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onSubmit()
				{
					Budget budg = form.getModelObject();
					BudgetZoekFilter filter = new BudgetZoekFilter();
					filter.setVerbintenis(budg.getVerbintenis());
					filter.setOnderwijsproduct(budg.getOnderwijsproduct());
					BudgetDataAccessHelper helper =
						DataAccessRegistry.getHelper(BudgetDataAccessHelper.class);
					List<Budget> budgetList = helper.list(filter);
					if (budgetList == null || budgetList.isEmpty()
						|| (budgetList.size() == 1 && budgetList.get(0).equals(budg)))
					{
						budg.saveOrUpdate();
						budg.commit();
						setResponsePage(new DeelnemerBudgettenPage(getContextVerbintenis()));
					}
					else
					{
						error("Er bestaat voor deze inschrijving en de geselecteerde activiteit al een budget.");
						return;
					}
				}
			};
		form.setOutputMarkupId(true);
		myModel = new PropertyModel<Onderwijsproduct>(form.getModel(), "onderwijsproduct");
		form.add(new OnderwijsproductSearchEditor("onderwijsproduct", myModel).setRequired(true));
		form.add(new TextField<Integer>("aantalUur", Integer.class).setRequired(true).add(
			new RangeValidator<Integer>(1, 99999)));
		add(form);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form));
		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{

			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new DeelnemerBudgettenPage(getContextVerbintenis());
			}

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return DeelnemerBudgettenPage.class;
			}
		}));

		panel.addButton(new VerwijderButton(panel)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				Budget budget = form.getModelObject();
				budget.delete();
				budget.commit();
				setResponsePage(new DeelnemerBudgettenPage(getContextVerbintenis()));

			}

			@Override
			public boolean isVisible()
			{
				Budget budget = form.getModelObject();
				return budget.isSaved();
			}
		});
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(myModel);
	}

}
