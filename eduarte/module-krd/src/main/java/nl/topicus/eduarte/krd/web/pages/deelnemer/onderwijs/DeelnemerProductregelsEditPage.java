package nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessException;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.CollectionDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductAfnameContextDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ProductregelDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.entities.productregel.Productregel.TypeProductregel;
import nl.topicus.eduarte.krd.dao.helpers.BronDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.eduarte.krd.principals.deelnemer.onderwijsproduct.DeelnemerOnderwijsproductenWrite;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.table.EditOnderwijsproductAfnameContextTable;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.web.pages.deelnemer.onderwijs.DeelnemerProductregelsPage;
import nl.topicus.eduarte.zoekfilters.ProductregelZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.hibernate.exception.ConstraintViolationException;

@PageInfo(title = "Productregels", menu = {"Deelnemer > [deelnemer] > Onderwijs > Productregels"})
@InPrincipal(DeelnemerOnderwijsproductenWrite.class)
public class DeelnemerProductregelsEditPage extends AbstractDeelnemerPage implements
		IModuleEditPage<Verbintenis>
{
	private static final long serialVersionUID = 1L;

	private final Form<Void> form;

	private final EduArteDataPanel<OnderwijsproductAfnameContext> datapanel;

	public DeelnemerProductregelsEditPage(IModel<Verbintenis> inschrijvingModel,
			SecurePage returnPage)
	{
		this(inschrijvingModel.getObject(), returnPage);
	}

	/**
	 * 
	 * @param inschrijving
	 * @param returnPage
	 *            Wordt niet gebruikt, maar is wel noodzakelijk vanwege de manier waarop
	 *            module edit pagina's geinstantieerd worden.
	 */
	public DeelnemerProductregelsEditPage(Verbintenis inschrijving, SecurePage returnPage)
	{
		super(DeelnemerMenuItem.Productregels, inschrijving.getDeelnemer(), inschrijving);
		IModel<Boolean> toonOokHogerNiveauModel = new Model<Boolean>(Boolean.FALSE);
		EditOnderwijsproductAfnameContextTable table =
			new EditOnderwijsproductAfnameContextTable(getContextVerbintenisModel(),
				toonOokHogerNiveauModel);
		CollectionDataProvider<OnderwijsproductAfnameContext> provider =
			new CollectionDataProvider<OnderwijsproductAfnameContext>(getKeuzeObjecten(), true)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public IModel<OnderwijsproductAfnameContext> model(
						OnderwijsproductAfnameContext object)
				{
					return new CompoundPropertyModel<OnderwijsproductAfnameContext>(super
						.model(object));
				}

			};
		datapanel =
			new EduArteDataPanel<OnderwijsproductAfnameContext>("datapanel", provider, table);
		datapanel.setItemsPerPage(Integer.MAX_VALUE);
		datapanel.setReuseItems(true);
		form = new Form<Void>("form");
		form.setOutputMarkupId(true);
		add(form);
		form.add(datapanel);

		JaNeeCombobox toonOokHogerNiveau =
			new JaNeeCombobox("toonOokHogerNiveau", toonOokHogerNiveauModel)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean wantOnSelectionChangedNotifications()
				{
					return true;
				}

			};
		add(toonOokHogerNiveau);

		createComponents();
	}

	private IModel<List<OnderwijsproductAfnameContext>> getKeuzeObjecten()
	{
		return new LoadableDetachableModel<List<OnderwijsproductAfnameContext>>()
		{
			private static final long serialVersionUID = 1L;

			private Map<Long, Long> gemaakteKeuzes;

			@Override
			protected List<OnderwijsproductAfnameContext> load()
			{
				ProductregelDataAccessHelper helper =
					DataAccessRegistry.getHelper(ProductregelDataAccessHelper.class);
				ProductregelZoekFilter filter =
					new ProductregelZoekFilter(getContextVerbintenis().getOpleiding(),
						getContextVerbintenis().getCohort());
				filter.setTypeProductregel(TypeProductregel.Productregel);
				filter.addOrderByProperty("volgnummer");
				filter.addOrderByProperty("soortProductregel");
				List<Productregel> productregels = helper.list(filter);
				Map<Productregel, OnderwijsproductAfnameContext> contexten =
					DataAccessRegistry.getHelper(
						OnderwijsproductAfnameContextDataAccessHelper.class).list(
						getContextVerbintenis());
				List<OnderwijsproductAfnameContext> res =
					new ArrayList<OnderwijsproductAfnameContext>(productregels.size());
				for (Productregel productregel : productregels)
				{

					OnderwijsproductAfnameContext context = contexten.get(productregel);
					if (context == null)
					{
						context =
							createEmptyContext(getContextVerbintenis(), productregel, null,
								productregel.getCohort());
					}
					res.add(context);
					if (gemaakteKeuzes != null)
					{
						Onderwijsproduct product = getKeuze(productregel);
						context.getOnderwijsproductAfname().setOnderwijsproduct(product);
					}
				}
				return res;
			}

			private Onderwijsproduct getKeuze(Productregel productregel)
			{
				Onderwijsproduct product = null;
				Long productId = gemaakteKeuzes.get(productregel.getId());
				if (productId != null)
					product =
						DataAccessRegistry.getHelper(OnderwijsproductDataAccessHelper.class).get(
							Onderwijsproduct.class, productId);
				return product;
			}

			@Override
			protected void onDetach()
			{
				gemaakteKeuzes = new HashMap<Long, Long>();
				for (OnderwijsproductAfnameContext context : getObject())
				{
					if (context.getOnderwijsproductAfname().getOnderwijsproduct() != null)
						gemaakteKeuzes.put(context.getProductregel().getId(), context
							.getOnderwijsproductAfname().getOnderwijsproduct().getId());
				}
			}

		};
	}

	private OnderwijsproductAfnameContext createEmptyContext(Verbintenis verb, Productregel regel,
			Onderwijsproduct keuze, Cohort cohort)
	{
		OnderwijsproductAfnameContext context = new OnderwijsproductAfnameContext();
		context.setVerbintenis(verb);
		context.setProductregel(regel);

		OnderwijsproductAfname afname = new OnderwijsproductAfname();
		afname.setBegindatum(verb.getBegindatum());
		afname.setDeelnemer(verb.getDeelnemer());
		afname.setCohort(cohort);
		afname.setOnderwijsproduct(keuze);
		context.setOnderwijsproductAfname(afname);
		afname.getAfnameContexten().add(context);

		return context;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit()
			{
				// Controleer op dubbele waarden.
				Set<Onderwijsproduct> dubbelen = new HashSet<Onderwijsproduct>();
				List<OnderwijsproductAfnameContext> contexten =
					(List<OnderwijsproductAfnameContext>) ((CollectionDataProvider) datapanel
						.getDataProvider()).getCollectionModel().getObject();

				for (OnderwijsproductAfnameContext context1 : contexten)
				{
					for (OnderwijsproductAfnameContext context2 : contexten)
					{
						if (context1 != context2
							&& context1.getOnderwijsproductAfname().getOnderwijsproduct() != null
							&& context2.getOnderwijsproductAfname().getOnderwijsproduct() != null)
						{
							if (context1.getOnderwijsproductAfname().getOnderwijsproduct().equals(
								context2.getOnderwijsproductAfname().getOnderwijsproduct()))
							{
								dubbelen.add(context1.getOnderwijsproductAfname()
									.getOnderwijsproduct());
							}
						}
					}
				}
				if (dubbelen.isEmpty())
				{
					// Sla wijzigingen op.
					for (OnderwijsproductAfnameContext context : contexten)
					{
						if (context.isSaved()
							&& context.getOnderwijsproductAfname().getOnderwijsproduct() == null)
						{
							// Bestaande keuze verwijderd.
							List<BronBveAanleverRecord> records =
								DataAccessRegistry.getHelper(BronDataAccessHelper.class)
									.getAanleverRecords(context);
							for (BronBveAanleverRecord record : records)
							{
								record.setAfnameContext(null);
								record.update();
							}
							context.getVerbintenis().getAfnameContexten().remove(context);
							// Verwijder de afname uit de sessie om te voorkomen dat de
							// afname ook geupdated wordt.
							context.getOnderwijsproductAfname().evict();
							context.delete();
						}
						else if (context.getOnderwijsproductAfname().getOnderwijsproduct() != null)
						{
							// Ga op zoek naar onderwijsproductafnames die al bestaan,
							// maar niet binnen deze verbintenis.
							OnderwijsproductAfname bestaandeAfname = null;
							for (OnderwijsproductAfname afname : getContextDeelnemer()
								.getOnderwijsproductAfnames())
							{
								if (afname.getOnderwijsproduct() != null
									&& afname.getOnderwijsproduct().equals(
										context.getOnderwijsproductAfname().getOnderwijsproduct())
									&& afname.getCohort().equals(
										context.getOnderwijsproductAfname().getCohort()))
								{
									boolean inGebruik = false;
									for (OnderwijsproductAfnameContext c : afname
										.getAfnameContexten())
									{
										if (c.getVerbintenis().equals(context.getVerbintenis()))
										{
											inGebruik = true;
											break;
										}
									}
									if (!inGebruik)
									{
										bestaandeAfname = afname;
										break;
									}
								}
							}
							if (bestaandeAfname != null)
							{
								context.getOnderwijsproductAfname().evict();
								context.setOnderwijsproductAfname(bestaandeAfname);
							}
							if (!context.getOnderwijsproductAfname().isSaved())
								context.getOnderwijsproductAfname().getDeelnemer()
									.getOnderwijsproductAfnames().add(
										context.getOnderwijsproductAfname());
							if (!context.isSaved())
								context.getVerbintenis().getAfnameContexten().add(context);
							context.getOnderwijsproductAfname().saveOrUpdate();
							context.saveOrUpdate();
						}
					}
					try
					{
						DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
					}
					catch (DataAccessException e)
					{
						// Waarschijnlijk hetzelfde onderwijsproduct 2x gekozen.
						if (e.getCause() != null
							&& e.getCause().getClass().equals(ConstraintViolationException.class))
						{
							error("De keuzes konden niet opgeslagen worden. Foutmelding: "
								+ e.getLocalizedMessage());
							return;
						}
						else
						{
							throw e;
						}
					}
					setResponsePage(new DeelnemerProductregelsPage(getContextVerbintenis()));
				}
				else
				{
					for (Onderwijsproduct duplicaat : dubbelen)
					{
						DeelnemerProductregelsEditPage.this.error("Onderwijsproduct "
							+ duplicaat.getContextInfoOmschrijving()
							+ " is meerdere keren gekozen.");
					}
					return;
				}
			}
		});
		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new DeelnemerProductregelsPage(getContextVerbintenis());
			}

			@Override
			public Class<DeelnemerProductregelsPage> getPageIdentity()
			{
				return DeelnemerProductregelsPage.class;
			}

		}));
	}
}
