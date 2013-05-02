package nl.topicus.eduarte.resultaten.web.pages.onderwijs;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.BewerkenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.dao.helpers.ToetsDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur.Type;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.Scoreschaal;
import nl.topicus.eduarte.resultaten.principals.onderwijs.ToetsNormeringenAanpassen;
import nl.topicus.eduarte.resultaten.web.components.datapanel.table.ToetsNormeringTable;
import nl.topicus.eduarte.resultaten.web.components.filter.ToetsNormeringZoekFilterPanel;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurZoekFilter;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.navigation.paging.IPageable;

@PageInfo(title = "Normeringen aanpassen", menu = "Onderwijs > Onderwijsproducten > Normeringen")
@InPrincipal(ToetsNormeringenAanpassen.class)
public class ToetsNormeringZoekenPage extends SecurePage
{
	public static final int MAX_TOETSEN = 250;

	private ToetsZoekFilter toetsFilter;

	private EduArteDataPanel<Toets> datapanel;

	public ToetsNormeringZoekenPage()
	{
		this(createFilter());
	}

	private static ToetsZoekFilter createFilter()
	{
		ToetsZoekFilter toetsFilter = new ToetsZoekFilter(new ResultaatstructuurZoekFilter());
		toetsFilter.getResultaatstructuurFilter().setCohort(Cohort.getHuidigCohort());
		toetsFilter.getResultaatstructuurFilter().setTaxonomiecode("3.2.*");
		toetsFilter.getResultaatstructuurFilter().setType(Type.SUMMATIEF);
		toetsFilter.setScoreschaal(Scoreschaal.Lineair);
		return toetsFilter;
	}

	public ToetsNormeringZoekenPage(ToetsZoekFilter toetsFilter)
	{
		super(CoreMainMenuItem.Onderwijs);
		this.toetsFilter = toetsFilter;

		toetsFilter.getResultaatstructuurFilter().setAuthorizationContext(
			new OrganisatieEenheidLocatieAuthorizationContext(this));

		datapanel =
			new EduArteDataPanel<Toets>("panel", GeneralFilteredSortableDataProvider.of(
				toetsFilter, ToetsDataAccessHelper.class), new ToetsNormeringTable(false));
		add(datapanel);

		add(new ToetsNormeringZoekFilterPanel("filter", toetsFilter, datapanel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onZoek(IPageable pageable, AjaxRequestTarget target)
			{
				checkMax();
				refreshBottomRow(target);
				refreshFeedback(target);
				super.onZoek(pageable, target);
			}
		});

		checkMax();
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new BewerkenButton<Void>(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return ToetsNormeringEditPage.class;
			}

			@Override
			public Page getPage()
			{
				return new ToetsNormeringEditPage(toetsFilter);
			}
		})
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				int size = datapanel.getDataProvider().size();
				return super.isVisible() && size <= MAX_TOETSEN && size > 0;
			}
		});
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new OnderwijsCollectiefMenu(id, OnderwijsCollectiefMenuItem.Normeringen);
	}

	private void checkMax()
	{
		if (datapanel.getDataProvider().size() > MAX_TOETSEN)
			error("Er kunnen maximaal " + MAX_TOETSEN
				+ " toetsen tegelijk aangepast worden; verfijn de zoekopdracht.");
	}
}
