package nl.topicus.eduarte.web.pages.beheer.organisatie;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.TitleModel;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.core.principals.beheer.organisatie.BPVDeelnemers;
import nl.topicus.eduarte.dao.helpers.BPVInschrijvingDataAccessHelper;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.web.components.menu.RelatieBeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.BPVInschrijvingTable;
import nl.topicus.eduarte.web.components.panels.filter.BPVInschrijvingZoekFilterPanel;
import nl.topicus.eduarte.web.pages.beheer.relatie.AbstractRelatieBeheerPage;
import nl.topicus.eduarte.zoekfilters.BPVInschrijvingZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

@PageInfo(title = "BPV-deelnemers", menu = "Relatie > Externe organisaties > BPV-deelnemers")
@InPrincipal(BPVDeelnemers.class)
public class BPVDeelnemersPage extends AbstractRelatieBeheerPage<ExterneOrganisatie>
{
	private static final long serialVersionUID = 1L;

	/**
	 * @return Default zoekfilter voor de ingelogde gebruiker.
	 */
	private static final BPVInschrijvingZoekFilter getDefaultFilter(
			ExterneOrganisatie externeOrganisatie)
	{
		BPVInschrijvingZoekFilter filter = new BPVInschrijvingZoekFilter();
		filter.setBpvBedrijfAlleRelaties(externeOrganisatie);
		filter.addOrderByProperty("deelnemer.deelnemernummer");
		filter.addOrderByProperty("persoon.roepnaam");
		filter.addOrderByProperty("persoon.achternaam");
		return filter;
	}

	public BPVDeelnemersPage(ExterneOrganisatie externeOrganisatie)
	{
		this(getDefaultFilter(externeOrganisatie), externeOrganisatie);
	}

	public BPVDeelnemersPage(BPVInschrijvingZoekFilter filter, ExterneOrganisatie externeOrganisatie)
	{
		super(ModelFactory.getModel(externeOrganisatie), RelatieBeheerMenuItem.ExterneOrganisaties);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		IDataProvider<BPVInschrijving> provider =
			GeneralFilteredSortableDataProvider.of(filter, BPVInschrijvingDataAccessHelper.class);
		EduArteDataPanel<BPVInschrijving> datapanel =
			new EduArteDataPanel<BPVInschrijving>("datapanel", provider, new BPVInschrijvingTable(
				getContextModel(), true))
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected IModel<String> createTitleModel(String title)
				{
					return new TitleModel(title, this)
					{
						private static final long serialVersionUID = 1L;

						@Override
						public String getObject()
						{
							ExterneOrganisatie extOrg =
								BPVDeelnemersPage.this.getContextModelObject();

							return super.getObject() + " deelnemers bij "
								+ extOrg.getVerkorteNaam();
						}
					};
				}
			};
		datapanel.setItemsPerPage(20);
		add(datapanel);
		add(new BPVInschrijvingZoekFilterPanel("filter", filter, datapanel));
		createComponents();
	}

	@Override
	public boolean supportsBookmarks()
	{
		return false;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new TerugButton(panel, new IPageLink()
		{

			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new ExterneOrganisatieOverzichtPage(
					(ExterneOrganisatie) getDefaultModelObject());
			}

			@Override
			public Class<ExterneOrganisatieOverzichtPage> getPageIdentity()
			{
				return ExterneOrganisatieOverzichtPage.class;
			}

		}));
	}
}
