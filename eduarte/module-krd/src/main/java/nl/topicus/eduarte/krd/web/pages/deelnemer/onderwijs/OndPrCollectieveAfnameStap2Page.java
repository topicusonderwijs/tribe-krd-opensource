package nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductAfnameContextDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ProductregelDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.krd.principals.deelnemer.onderwijsproduct.DeelnemersOnderwijsproductenCollectieveAfname;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.table.CollectieveAfnameContextInvulTable;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.DeelnemerZoekenPage;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductAfnameContextZoekFilter;
import nl.topicus.eduarte.zoekfilters.ProductregelZoekFilter;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author vandekamp
 */
@PageInfo(title = "Collectief productregels invullen (stap 2 van 2)", menu = {"Deelnemer > Onderwijsproducten > Collectieve afname > [deelnemers] > Verder"})
@InPrincipal(DeelnemersOnderwijsproductenCollectieveAfname.class)
@SearchImplementsActions( {Instelling.class, OrganisatieEenheid.class})
public class OndPrCollectieveAfnameStap2Page extends SecurePage
{
	private static final long serialVersionUID = 1L;

	private IModel<List<Verbintenis>> selectieModel;

	private List<DeelnemerCollectieveAfnameContext> contexten;

	private Form<Void> form;

	private SecurePage returnPage;

	public OndPrCollectieveAfnameStap2Page(List<Verbintenis> selectie, SecurePage returnPage)
	{
		super(CoreMainMenuItem.Deelnemer);
		this.returnPage = returnPage;
		this.selectieModel = ModelFactory.getListModel(selectie);
		IModel<List<Productregel>> productRegels = new ProductregelsModel();
		contexten = new ArrayList<DeelnemerCollectieveAfnameContext>();
		for (Productregel regel : productRegels.getObject())
		{
			contexten.add(new DeelnemerCollectieveAfnameContext(regel, selectie.get(0)
				.getOpleiding()));
		}
		CollectieveAfnameContextInvulTable table = new CollectieveAfnameContextInvulTable();
		ListDataProvider<DeelnemerCollectieveAfnameContext> provider =
			new ListDataProvider<DeelnemerCollectieveAfnameContext>(contexten);
		CustomDataPanel<DeelnemerCollectieveAfnameContext> datapanel =
			new EduArteDataPanel<DeelnemerCollectieveAfnameContext>("datapanel", provider, table);
		datapanel.setReuseItems(true);
		datapanel.setItemsPerPage(Integer.MAX_VALUE);
		form = new Form<Void>("form");
		add(form);
		form.add(datapanel);
		createComponents();
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(selectieModel);
		for (DeelnemerCollectieveAfnameContext context : contexten)
		{
			ComponentUtil.detachQuietly(context);
		}
		super.onDetach();
	}

	private final class ProductregelsModel extends LoadableDetachableModel<List<Productregel>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<Productregel> load()
		{
			ProductregelDataAccessHelper helper =
				DataAccessRegistry.getHelper(ProductregelDataAccessHelper.class);
			Verbintenis verbintenis = selectieModel.getObject().iterator().next();
			ProductregelZoekFilter filter =
				new ProductregelZoekFilter(verbintenis.getOpleiding(), verbintenis.getCohort());
			filter.addOrderByProperty("soortProductregel");
			return helper.list(filter);
		}
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id,
			DeelnemerCollectiefMenuItem.OnderwijsproductenToekennen);
	}

	@Override
	public boolean supportsBookmarks()
	{
		return false;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new AbstractLinkButton(panel, "Defaultwaarden invullen",
			CobraKeyAction.TOEVOEGEN, ButtonAlignment.LEFT)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				for (DeelnemerCollectieveAfnameContext context : contexten)
				{
					SortedSet<Onderwijsproduct> producten =
						context.getProductregel().getOnderwijsproducten(context.getOpleiding(),
							false, false);
					if (producten.size() == 1)
					{
						Onderwijsproduct product = producten.iterator().next();
						context.setOnderwijsproduct(product);
						context
							.setBeginDatum(context.getProductregel().getCohort().getBegindatum());
						context.setEindDatum(null);
					}
				}
			}
		});
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				OnderwijsproductAfnameContextDataAccessHelper helper =
					DataAccessRegistry
						.getHelper(OnderwijsproductAfnameContextDataAccessHelper.class);
				OnderwijsproductAfnameContextZoekFilter filter =
					new OnderwijsproductAfnameContextZoekFilter();
				boolean error = false;
				for (DeelnemerCollectieveAfnameContext context : contexten)
				{
					for (Verbintenis verbintenis : selectieModel.getObject())
					{
						filter.setVerbintenis(verbintenis);
						filter.setProductregel(context.getProductregel());
						List<OnderwijsproductAfnameContext> list = helper.list(filter);
						switch (context.getBijBestaandeKeuzeEnum())
						{
							case GeenWijziging:
								if (context.getOnderwijsproduct() != null && list.isEmpty())
								{
									if (!voegKeuzeToe(context, verbintenis))
									{
										error = true;
										error("Keuze kon niet aan "
											+ verbintenis.getDeelnemer().getPersoon()
												.getVolledigeNaam()
											+ " toegekend worden omdat deze deelnemer het onderwijsproduct "
											+ context.getOnderwijsproduct()
												.getContextInfoOmschrijving() + " al gekozen heeft");
									}
								}
								break;
							case KeuzeOverschrijven:
								if (context.getOnderwijsproduct() != null)
								{
									if (list.isEmpty())
									{
										if (!voegKeuzeToe(context, verbintenis))
										{
											error = true;
											error("Keuze kon niet aan "
												+ verbintenis.getDeelnemer().getPersoon()
													.getVolledigeNaam()
												+ " toegekend worden omdat deze deelnemer het onderwijsproduct "
												+ context.getOnderwijsproduct()
													.getContextInfoOmschrijving()
												+ " al gekozen heeft");
										}
									}
									else
									{
										if (!overschrijfKeuze(context, list.get(0)))
										{
											error = true;
											error("Keuze kon niet aan "
												+ verbintenis.getDeelnemer().getPersoon()
													.getVolledigeNaam()
												+ " toegekend worden omdat deze deelnemer het onderwijsproduct "
												+ context.getOnderwijsproduct()
													.getContextInfoOmschrijving()
												+ " al gekozen heeft");
										}
									}
								}
								break;
							case KeuzeVerwijderen:
								if (context.getOnderwijsproduct() != null)
								{
									if (list.isEmpty())
									{
										if (!voegKeuzeToe(context, verbintenis))
										{
											error = true;
											error("Keuze kon niet aan "
												+ verbintenis.getDeelnemer().getPersoon()
													.getVolledigeNaam()
												+ " toegekend worden omdat deze deelnemer het onderwijsproduct "
												+ context.getOnderwijsproduct()
													.getContextInfoOmschrijving()
												+ " al gekozen heeft");
										}
									}
									else
									{
										if (!overschrijfKeuze(context, list.get(0)))
										{
											error = true;
											error("Keuze kon niet aan "
												+ verbintenis.getDeelnemer().getPersoon()
													.getVolledigeNaam()
												+ " toegekend worden omdat deze deelnemer het onderwijsproduct "
												+ context.getOnderwijsproduct()
													.getContextInfoOmschrijving()
												+ " al gekozen heeft");
										}
									}
								}
								else if (!list.isEmpty())
									verwijderKeuze(list.get(0));
								break;
						}
					}
				}
				if (error)
				{
					info("Voor sommige deelnemers konden de gekozen onderwijsproducten niet opgeslagen worden. De overige keuzes zijn wel opgeslagen");
				}
				else
				{
					info("De gekozen onderwijsproducten zijn opgeslagen");
				}
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
				setVisible(false);
				// Ga terug naar deelnemerzoeken.
				// setResponsePage(DeelnemerZoekenPage.class);
			}

		});
		panel.addButton(new TerugButton(panel, returnPage));
		panel.addButton(new AnnulerenButton(panel, DeelnemerZoekenPage.class));
	}

	protected void verwijderKeuze(OnderwijsproductAfnameContext onderwijsproductAfnameContext)
	{
		onderwijsproductAfnameContext.delete();
		onderwijsproductAfnameContext.getOnderwijsproductAfname().delete();
	}

	protected boolean overschrijfKeuze(DeelnemerCollectieveAfnameContext context,
			OnderwijsproductAfnameContext onderwijsproductAfnameContext)
	{
		// Controleer dat de deelnemer dit product niet al gekozen heeft.
		if (onderwijsproductAfnameContext.getVerbintenis()
			.heeftOnderwijsproductGekozenBinnenVerbintenis(context.getOnderwijsproduct()))
		{
			return false;
		}
		OnderwijsproductAfname afname =
			onderwijsproductAfnameContext.getVerbintenis().getDeelnemer()
				.getOnderwijsproductAfname(context.getOnderwijsproduct(),
					context.getProductregel().getCohort());
		if (afname == null)
		{
			afname = new OnderwijsproductAfname();
			afname.setDeelnemer(onderwijsproductAfnameContext.getVerbintenis().getDeelnemer());
			afname.setBegindatum(context.getBeginDatum());
			afname.setEinddatum(context.getEindDatum());
			afname.setCohort(context.getProductregel().getCohort());
			afname.setOnderwijsproduct(context.getOnderwijsproduct());
			onderwijsproductAfnameContext.getVerbintenis().getDeelnemer()
				.getOnderwijsproductAfnames().add(afname);
		}
		onderwijsproductAfnameContext.setOnderwijsproductAfname(afname);
		afname.saveOrUpdate();
		onderwijsproductAfnameContext.saveOrUpdate();

		return true;
	}

	protected boolean voegKeuzeToe(DeelnemerCollectieveAfnameContext context,
			Verbintenis verbintenis)
	{
		// Controleer dat de deelnemer dit product niet al gekozen heeft.
		if (verbintenis
			.heeftOnderwijsproductGekozenBinnenVerbintenis(context.getOnderwijsproduct()))
		{
			return false;
		}
		OnderwijsproductAfnameContext afnContext = new OnderwijsproductAfnameContext();
		afnContext.setProductregel(context.getProductregel());
		afnContext.setVerbintenis(verbintenis);
		OnderwijsproductAfname afname =
			verbintenis.getDeelnemer().getOnderwijsproductAfname(context.getOnderwijsproduct(),
				context.getProductregel().getCohort());
		if (afname == null)
		{
			afname = new OnderwijsproductAfname();
			afname.setDeelnemer(verbintenis.getDeelnemer());
			afname.setBegindatum(context.getBeginDatum());
			afname.setEinddatum(context.getEindDatum());
			afname.setCohort(context.getProductregel().getCohort());
			verbintenis.getDeelnemer().getOnderwijsproductAfnames().add(afname);
		}
		afname.setOnderwijsproduct(context.getOnderwijsproduct());
		afname.saveOrUpdate();
		afnContext.setOnderwijsproductAfname(afname);
		afnContext.save();
		afname.getAfnameContexten().add(afnContext);
		verbintenis.getAfnameContexten().add(afnContext);

		return true;
	}
}
