package nl.topicus.eduarte.resultaten.web.pages.onderwijs;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.quartz.JobDescriptionFilter;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.resultaten.principals.onderwijs.OnderwijsproductHerberekeningen;
import nl.topicus.eduarte.resultaten.web.pages.shared.HerberekenJobBeheerPanel;
import nl.topicus.eduarte.resultaten.zoekfilters.ResultaatHerberekenenJobRunZoekFilter;
import nl.topicus.eduarte.web.components.menu.OnderwijsproductMenuItem;
import nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus.AbstractOnderwijsproductPage;

/**
 * @author papegaaij
 */
@PageInfo(title = "Onderwijsproducten Herberekeningen", menu = "Onderwijs > Onderwijsproducten > [Onderwijsproducten] > Resultaten > Herberekeningen")
@InPrincipal(OnderwijsproductHerberekeningen.class)
public class ResultatenHerberekenOverzichtPage extends AbstractOnderwijsproductPage
{
	public ResultatenHerberekenOverzichtPage(Onderwijsproduct onderwijsproduct)
	{
		super(OnderwijsproductMenuItem.Herberekeningen, ModelFactory.getModel(onderwijsproduct));

		add(new HerberekenJobBeheerPanel("jobPanel")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected ResultaatHerberekenenJobRunZoekFilter createJobRunFilter()
			{
				ResultaatHerberekenenJobRunZoekFilter ret = super.createJobRunFilter();
				ret.setOnderwijsproduct(getContextOnderwijsproduct());
				return ret;
			}

			@Override
			protected JobDescriptionFilter getJobDescriptionFilter()
			{
				return new MatchOnderwijsproductFilter(getContextOnderwijsproduct());
			}
		});
		createComponents();
	}
}
