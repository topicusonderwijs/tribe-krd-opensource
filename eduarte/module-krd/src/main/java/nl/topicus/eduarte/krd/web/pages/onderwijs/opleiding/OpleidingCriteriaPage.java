package nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractAjaxConfirmationButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.security.TargetBasedSecurityCheck;
import nl.topicus.eduarte.app.EduArteSession;
import nl.topicus.eduarte.core.principals.onderwijs.OpleidingInzien;
import nl.topicus.eduarte.dao.helpers.CriteriumDataAccessHelper;
import nl.topicus.eduarte.entities.criteriumbank.Criterium;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.krd.web.components.panels.filter.CriteriumZoekFilterPanel;
import nl.topicus.eduarte.providers.OpleidingProvider;
import nl.topicus.eduarte.util.criteriumbank.CriteriumbankControleTest;
import nl.topicus.eduarte.web.components.menu.OpleidingMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.CriteriumTable;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.onderwijs.opleiding.AbstractOpleidingPage;
import nl.topicus.eduarte.zoekfilters.CriteriumZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * Pagina met de criteria van een opleiding. Een criterium kan zowel landelijk (voor het
 * verbintenisgebied) als lokaal direct aan de opleiding gekoppeld zijn. Zowel de
 * landelijke als de lokale criteria worden op deze pagina getoond.
 * 
 * @author loite
 */
@PageInfo(title = "Criteria", menu = {"Onderwijs > [opleiding] > Criteria"})
@InPrincipal(OpleidingInzien.class)
public class OpleidingCriteriaPage extends AbstractOpleidingPage
{
	private static final long serialVersionUID = 1L;

	private final CriteriumZoekFilterPanel filterPanel;

	private final CustomDataPanel<Criterium> datapanel;

	private static final CriteriumZoekFilter getDefaultFilter(Opleiding opleiding,
			IModel<Cohort> cohortModel)
	{
		CriteriumZoekFilter filter = new CriteriumZoekFilter(opleiding, cohortModel);
		return filter;
	}

	public OpleidingCriteriaPage(OpleidingProvider provider)
	{
		this(provider.getOpleiding());
	}

	public OpleidingCriteriaPage(Opleiding opleiding)
	{
		this(opleiding, getDefaultFilter(opleiding, EduArteSession.get().getSelectedCohortModel()));
	}

	public OpleidingCriteriaPage(Opleiding opleiding, CriteriumZoekFilter filter)
	{
		super(OpleidingMenuItem.Criteria, opleiding);
		GeneralDataProvider<Criterium, CriteriumZoekFilter> provider =
			GeneralDataProvider.of(filter,  CriteriumDataAccessHelper.class);
		datapanel = new EduArteDataPanel<Criterium>("datapanel", provider, new CriteriumTable());
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Criterium>(
			CriteriumPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Item<Criterium> item)
			{
				Criterium criterium = item.getModelObject();
				setResponsePage(new CriteriumPage(getContextOpleiding(), criterium,
					OpleidingCriteriaPage.this));
			}
		});
		datapanel.setItemsPerPage(Integer.MAX_VALUE);
		add(datapanel);
		filterPanel = new CriteriumZoekFilterPanel("filter", filter, datapanel);
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
				Criterium criterium = new Criterium(EntiteitContext.INSTELLING);
				criterium.setOpleiding(getContextOpleiding());
				criterium.setVerbintenisgebied(getContextOpleiding().getVerbintenisgebied());
				criterium.setCohort(filterPanel.getZoekfilter().getCohort());
				criterium.setVolgnummer(getContextOpleiding().getMaxCriteriumVolgnummer(
					criterium.getCohort()) + 1);
				return new EditCriteriumPage(criterium, OpleidingCriteriaPage.this);
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return EditCriteriumPage.class;
			}
		}));
		panel.addButton(new AbstractLinkButton(panel, "Criteria testen", CobraKeyAction.LINKKNOP1,
			ButtonAlignment.LEFT)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				CriteriumbankControleTest test =
					new CriteriumbankControleTest(getContextOpleiding(), filterPanel
						.getZoekfilter().getCohort());
				boolean succes = test.testCriteria();
				if (!succes)
				{
					error("Er zijn fouten gevonden bij het doorlopen van de criteria");
					for (Criterium criterium : test.getCriteriumbank().getOngeldigeCriteria())
					{
						String melding =
							test.getCriteriumbank().getCriteriumMeldingen().get(criterium);
						error(criterium.getVolgnummer() + " - " + criterium.getNaam() + ": "
							+ melding);
					}
				}
				else
				{
					info("Criteria succesvol doorlopen zonder fouten");
				}
			}
		});
		panel.addButton(new PageLinkButton(panel, "Kopiëren", ButtonAlignment.LEFT, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new CriteriaKopierenStap1Page(getContextOpleiding(),
					OpleidingCriteriaPage.this);
			}

			@Override
			public Class<CriteriaKopierenStap1Page> getPageIdentity()
			{
				return CriteriaKopierenStap1Page.class;
			}
		}));
		panel
			.addButton(new AbstractAjaxConfirmationButton(
				panel,
				"Criteria genereren",
				"Weet u zeker dat u criteria wilt genereren voor deze opleiding op basis van de al ingevoerde productregels? Hiermee wordt een #{productregel_behaald}-criterium toegevoegd voor elke verplichte productregel bij de opleiding.",
				CobraKeyAction.GEEN, ButtonAlignment.LEFT, new TargetBasedSecurityCheck(panel,
					EditCriteriumPage.class))
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onClick(AjaxRequestTarget target)
				{
					Opleiding opleiding = getContextOpleiding();
					Cohort cohort = filterPanel.getZoekfilter().getCohort();
					int volgnummer = opleiding.getMaxCriteriumVolgnummer(cohort) + 1;
					for (Productregel regel : opleiding.getLandelijkeEnLokaleProductregels(cohort))
					{
						if (regel.isVerplicht())
						{
							String naam = regel.getNaam() + " behaald";
							Criterium criterium = opleiding.getCriterium(naam, cohort);
							if (criterium == null)
							{
								criterium = new Criterium();
								criterium.setCohort(cohort);
								criterium.setFormule("#{" + regel.getAfkorting() + "_behaald}");
								criterium.setMelding(regel.getNaam() + " is niet behaald");
								criterium.setNaam(naam);
								criterium.setOpleiding(opleiding);
								criterium.setVerbintenisgebied(opleiding.getVerbintenisgebied());
								criterium.setVolgnummer(volgnummer);
								criterium.save();
								volgnummer++;
							}
						}
					}
					opleiding.commit();
					target.addComponent(datapanel);
				}
			});
	}
}
