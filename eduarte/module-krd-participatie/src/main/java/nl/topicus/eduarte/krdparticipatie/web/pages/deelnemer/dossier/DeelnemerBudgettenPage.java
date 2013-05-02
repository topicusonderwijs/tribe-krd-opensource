package nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomColumn;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.dao.participatie.helpers.BudgetDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.participatie.Budget;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krdparticipatie.principals.deelnemer.DeelnemerBudgettenRead;
import nl.topicus.eduarte.krdparticipatie.web.components.menu.enums.ParticipatieDeelnemerMenuItem;
import nl.topicus.eduarte.krdparticipatie.web.components.panels.filter.DeelnemerBudgettenZoekFilterPanel;
import nl.topicus.eduarte.participatie.zoekfilters.BudgetZoekFilter;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

/**
 * Pagina met alle budgetten van 1 deelnemer
 * 
 * @author vandekamp
 */
@PageInfo(title = "Budgetten", menu = {"Deelnemer > [deelnemer] > Aanwezigheid > Budgetten"})
@InPrincipal(DeelnemerBudgettenRead.class)
public class DeelnemerBudgettenPage extends AbstractDeelnemerPage
{
	private static final long serialVersionUID = 1L;

	private static final List<CustomColumn<Budget>> getColumns()
	{
		List<CustomColumn<Budget>> cols = new ArrayList<CustomColumn<Budget>>(10);
		cols.add(new CustomPropertyColumn<Budget>("onderwijsproduct", "Onderwijs Product",
			"onderwijsproduct", "onderwijsproduct"));
		cols.add(new CustomPropertyColumn<Budget>("verbintenis", "Verbintenis", "verbintenis",
			"verbintenis"));
		cols.add(new CustomPropertyColumn<Budget>("aantalUur", "Aantal uur", "aantalUur",
			"aantalUur"));
		return cols;
	}

	private final DeelnemerBudgettenZoekFilterPanel filterPanel;

	public DeelnemerBudgettenPage(DeelnemerProvider provider)
	{
		this(provider.getDeelnemer());
	}

	public DeelnemerBudgettenPage(Deelnemer deelnemer)
	{
		this(deelnemer, deelnemer.getEersteInschrijvingOpPeildatum());
	}

	public DeelnemerBudgettenPage(Verbintenis verbintenis)
	{
		this(verbintenis.getDeelnemer(), verbintenis);
	}

	public DeelnemerBudgettenPage(Deelnemer deelnemer, Verbintenis verbintenis)
	{
		super(ParticipatieDeelnemerMenuItem.Budgetten, deelnemer, verbintenis);

		BudgetZoekFilter filter = new BudgetZoekFilter();
		filter.setInschrijvingModel(getContextVerbintenisModel());

		IDataProvider<Budget> provider =
			GeneralDataProvider.of(filter, BudgetDataAccessHelper.class);

		CustomDataPanelContentDescription<Budget> contDesc =
			new CustomDataPanelContentDescription<Budget>("Budgetten");
		contDesc.setColumns(getColumns());
		EduArteDataPanel<Budget> datapanel =
			new EduArteDataPanel<Budget>("datapanel", provider, contDesc);
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Budget>(
			EditBudgettenPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<Budget> item)
			{
				setResponsePage(new EditBudgettenPage(getContextDeelnemer(),
					getContextVerbintenis(), item.getModelObject()));
			}

		});

		add(datapanel);
		filterPanel = new DeelnemerBudgettenZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new ToevoegenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new EditBudgettenPage(getContextDeelnemer(), getContextVerbintenis());
			}

			@Override
			public Class<EditBudgettenPage> getPageIdentity()
			{
				return EditBudgettenPage.class;
			}

		}));
	}
}
