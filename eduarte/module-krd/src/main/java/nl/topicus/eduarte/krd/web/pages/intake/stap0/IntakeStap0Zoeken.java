package nl.topicus.eduarte.krd.web.pages.intake.stap0;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.EmptyMenu;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.cobra.web.security.DisableSecurityCheckMarker;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.security.checks.NietOverledenSecurityCheck;
import nl.topicus.eduarte.dao.helpers.VerbintenisDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.krd.principals.deelnemer.DeelnemerIntakeWizardStap0;
import nl.topicus.eduarte.krd.web.pages.intake.IntakeWizardPage;
import nl.topicus.eduarte.krd.web.pages.intake.stap1.IntakeStap1Personalia;
import nl.topicus.eduarte.web.components.panels.DeelnemerDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.DeelnemerTable;
import nl.topicus.eduarte.web.components.panels.filter.IntakeStap0ZoekFilterPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.DeelnemerZoekenPage;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.Model;

/**
 * Intake stap 0 voor het zoeken van een deelnemer om een nieuwe intake te starten.
 */
@PageInfo(title = "Intake stap 0 van 4", menu = {"Deelnemer > intake"})
@InPrincipal(DeelnemerIntakeWizardStap0.class)
public class IntakeStap0Zoeken extends IntakeWizardPage implements IEditPage
{
	private static final long serialVersionUID = 1L;

	private final IntakeStap0ZoekFilterPanel filterPanel;

	private static final VerbintenisZoekFilter getDefaultFilter()
	{
		VerbintenisZoekFilter filter = new VerbintenisZoekFilter();
		filter.setCustomPeildatumModel(new Model<Date>(null));
		filter.addOrderByProperty("beeindigd");
		filter.addOrderByProperty("deelnemer.deelnemernummer");
		filter.addOrderByProperty("persoon.roepnaam");
		filter.addOrderByProperty("persoon.achternaam");
		return filter;
	}

	public IntakeStap0Zoeken()
	{
		this(getDefaultFilter());
	}

	public IntakeStap0Zoeken(VerbintenisZoekFilter filter)
	{
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		GeneralFilteredSortableDataProvider<Verbintenis, VerbintenisZoekFilter> provider =
			new GeneralFilteredSortableDataProvider<Verbintenis, VerbintenisZoekFilter>(filter,
				VerbintenisDataAccessHelper.class)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Iterator<Verbintenis> iterator(int first, int count)
				{
					if (isFilterEmpty())
					{
						List<Verbintenis> emptyList = Collections.emptyList();
						return emptyList.iterator();
					}
					return super.iterator(first, count);
				}
			};
		final DeelnemerDataPanel datapanel =
			new DeelnemerDataPanel("datapanel", provider, new DeelnemerTable())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return !isFilterEmpty();
				}
			};
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Verbintenis>(
			IntakeStap1Personalia.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Item<Verbintenis> item)
			{
				Verbintenis inschrijving = item.getModelObject();
				setResponsePage(new IntakeStap1Personalia(inschrijving.getDeelnemer(), filterPanel
					.getZoekfilter()));
			}
		});
		datapanel.setOutputMarkupPlaceholderTag(true);
		datapanel.setItemsPerPage(20);
		add(datapanel);
		filterPanel = new IntakeStap0ZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);
		createComponents();
	}

	private boolean isFilterEmpty()
	{
		VerbintenisZoekFilter filter = filterPanel.getZoekfilter();
		return StringUtil.isEmpty(filter.getAchternaam()) && filter.getBsn() == null
			&& filter.getGeboortedatum() == null && StringUtil.isEmpty(filter.getPostcode())
			&& filter.getDeelnemernummer() == null
			&& (filter.getStatusIntake() == null || !filter.getStatusIntake().booleanValue());

	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new EmptyMenu(id);
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(VerbintenisZoekFilter.class);
		ctorArgValues.add(filterPanel.getZoekfilter());
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		ToevoegenButton toevoegenButton = new ToevoegenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				VerbintenisZoekFilter filter = filterPanel.getZoekfilter();
				return new IntakeStap1Personalia(filter);
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return IntakeStap1Personalia.class;
			}

		});
		DisableSecurityCheckMarker.place(toevoegenButton, NietOverledenSecurityCheck.class);
		panel.addButton(toevoegenButton);

		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new DeelnemerZoekenPage();
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return DeelnemerZoekenPage.class;
			}
		}));
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachFields(this);
	}

	@Override
	protected int getStapNummer()
	{
		return 0;
	}

	@Override
	protected String getStapTitel()
	{
		return "Zoeken of " + EduArteApp.get().getDeelnemerTerm().toLowerCase() + " reeds bestaat";
	}
}
