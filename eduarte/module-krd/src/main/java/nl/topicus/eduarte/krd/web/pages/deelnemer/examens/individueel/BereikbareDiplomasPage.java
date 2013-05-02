package nl.topicus.eduarte.krd.web.pages.deelnemer.examens.individueel;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.CollectionDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelAjaxClickableRowFactory;
import nl.topicus.eduarte.dao.helpers.OpleidingDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.principals.deelnemer.examen.DeelnemerExamensInzien;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.table.BereikbareDiplomasTable;
import nl.topicus.eduarte.krd.web.components.panels.filter.BereikbareDiplomasZoekFilterPanel;
import nl.topicus.eduarte.krd.zoekfilters.BereikbareDiplomasZoekFilter;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.util.criteriumbank.BereikbareDiplomasUtil;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "Bereikbare Diplomas", menu = {
	"Deelnemer > [deelnemer] > Examens > Bereikbare Diploma's",
	"Groep > [groep] > [deelnemer] > Examens > Bereikbare Diploma's"})
@InPrincipal(DeelnemerExamensInzien.class)
public class BereikbareDiplomasPage extends AbstractDeelnemerPage
{
	private static final long serialVersionUID = 1L;

	public BereikbareDiplomasPage(DeelnemerProvider provider)
	{
		this(provider.getDeelnemer());
	}

	public BereikbareDiplomasPage(Deelnemer deelnemer)
	{
		this(deelnemer, deelnemer.getEersteInschrijvingOpPeildatum());
	}

	public BereikbareDiplomasPage(Verbintenis verbintenis)
	{
		this(verbintenis.getDeelnemer(), verbintenis);
	}

	public BereikbareDiplomasPage(Deelnemer deelnemer, Verbintenis verbintenis)
	{
		super(DeelnemerMenuItem.BereikbareDiplomas, deelnemer, verbintenis);
		final BereikbareDiplomasZoekFilter filter = new BereikbareDiplomasZoekFilter();
		filter.setTaxonomiecode(verbintenis == null || verbintenis.getOpleiding() == null ? null
			: verbintenis.getOpleiding().getVerbintenisgebied().getParent().getTaxonomiecode());
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		CollectionDataProvider<Opleiding> provider =
			new CollectionDataProvider<Opleiding>(new LoadableDetachableModel<List<Opleiding>>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected List<Opleiding> load()
				{
					List<Opleiding> res =
						DataAccessRegistry.getHelper(OpleidingDataAccessHelper.class).list(filter);
					if (filter.getGeslaagd() != null)
					{
						BereikbareDiplomasUtil util =
							new BereikbareDiplomasUtil(getContextDeelnemer(), res,
								getContextVerbintenis().getCohort());
						util.berekenOpleidingenWaarvoorDeDeelnemerIsGeslaagd();
						int index = 0;
						while (index < res.size())
						{
							if (util.isGeslaagdVoorOpleiding(res.get(index)) == filter
								.getGeslaagd().booleanValue())
							{
								index++;
							}
							else
							{
								res.remove(index);
							}
						}
					}
					return res;
				}

			});
		final EduArteDataPanel<Opleiding> datapanel =
			new EduArteDataPanel<Opleiding>("datapanel", provider, new BereikbareDiplomasTable(
				getContextDeelnemerModel(), new PropertyModel<Cohort>(getContextVerbintenisModel(),
					"cohort")));
		datapanel.setRowFactory(new CustomDataPanelAjaxClickableRowFactory<Opleiding>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target, Item<Opleiding> item)
			{
				target.addComponent(datapanel);
			}
		});
		datapanel.setOutputMarkupId(true);
		datapanel.setItemsPerPage(20);
		add(datapanel);
		BereikbareDiplomasZoekFilterPanel filterPanel =
			new BereikbareDiplomasZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);
		createComponents();
	}
}
