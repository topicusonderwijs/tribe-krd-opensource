package nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.krd.principals.deelnemer.onderwijsproduct.DeelnemersOnderwijsproductenKeuzeAangeven;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.table.ProductregelKeuzeAangevenTable;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.DeelnemerZoekenPage;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.IModel;

/**
 * @author vandekamp
 */
@PageInfo(title = "Individuele keuzes binnen productregels invullen (stap 3 van 3)", menu = {"Deelnemer > Onderwijsproducten > Keuzes aangeven > [deelnemers] > Verder > Volgende stap"})
@InPrincipal(DeelnemersOnderwijsproductenKeuzeAangeven.class)
@SearchImplementsActions( {Instelling.class, OrganisatieEenheid.class})
public class OndPrKeuzeAangevenStap3Page extends SecurePage
{
	private static final long serialVersionUID = 1L;

	private IModel<List<Verbintenis>> selectedVerbintenissen;

	private IModel<List<Productregel>> selectedProductregels;

	private KeuzeAangevenStap3DatumPanel datumPanel;

	List<OnderwijsproductAfnameContextListModel> contexten;

	private Form<Void> form;

	private SecurePage returnPage;

	public OndPrKeuzeAangevenStap3Page(List<Verbintenis> selectedVerbintenissen,
			List<Productregel> selectedProductregels, SecurePage returnPage)
	{
		super(CoreMainMenuItem.Deelnemer);
		this.returnPage = returnPage;
		this.selectedVerbintenissen = ModelFactory.getListModel(selectedVerbintenissen);
		this.selectedProductregels = ModelFactory.getListModel(selectedProductregels);
		ProductregelKeuzeAangevenTable table =
			new ProductregelKeuzeAangevenTable(selectedProductregels);
		contexten = new ArrayList<OnderwijsproductAfnameContextListModel>();

		for (Verbintenis verbintenis : selectedVerbintenissen)
		{
			contexten.add(new OnderwijsproductAfnameContextListModel(this.selectedProductregels,
				verbintenis));
		}
		ListDataProvider<OnderwijsproductAfnameContextListModel> provider =
			new ListDataProvider<OnderwijsproductAfnameContextListModel>(contexten);
		CustomDataPanel<OnderwijsproductAfnameContextListModel> datapanel =
			new EduArteDataPanel<OnderwijsproductAfnameContextListModel>("datapanel", provider,
				table);
		datapanel.setItemsPerPage(Integer.MAX_VALUE);

		form = new Form<Void>("form");
		add(form);
		datumPanel = new KeuzeAangevenStap3DatumPanel("datumPanel");
		form.add(datumPanel);
		form.add(datapanel);
		createComponents();
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(selectedVerbintenissen);
		ComponentUtil.detachQuietly(selectedProductregels);
		for (OnderwijsproductAfnameContextListModel listModel : contexten)
		{
			ComponentUtil.detachQuietly(listModel);
		}
		super.onDetach();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id, DeelnemerCollectiefMenuItem.KeuzesAangeven);
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				boolean error = false;
				for (OnderwijsproductAfnameContextListModel listModel : contexten)
				{
					// Controleer op dubbele keuze.
					Set<Onderwijsproduct> dubbelen = new HashSet<Onderwijsproduct>();
					List<OnderwijsproductAfnameContext> nieuweContexten =
						listModel.getVerbintenis().getAfnameContexten();
					nieuweContexten.addAll(listModel.getObject());
					List<OnderwijsproductAfname> afnames =
						listModel.getVerbintenis().getDeelnemer().getOnderwijsproductAfnames();
					for (OnderwijsproductAfnameContext context1 : nieuweContexten)
					{
						for (OnderwijsproductAfnameContext context2 : nieuweContexten)
						{
							if (context1 != context2
								&& context1.getOnderwijsproductAfname().getOnderwijsproduct() != null
								&& context2.getOnderwijsproductAfname().getOnderwijsproduct() != null)
							{
								if (context1.getOnderwijsproductAfname().getOnderwijsproduct()
									.equals(
										context2.getOnderwijsproductAfname().getOnderwijsproduct()))
								{
									dubbelen.add(context1.getOnderwijsproductAfname()
										.getOnderwijsproduct());
								}
							}
						}
						for (OnderwijsproductAfname afname : afnames)
						{
							if (afname != context1.getOnderwijsproductAfname()
								&& afname.getOnderwijsproduct().equals(
									context1.getOnderwijsproductAfname().getOnderwijsproduct()))
							{
								dubbelen.add(afname.getOnderwijsproduct());
							}
						}
					}
					if (dubbelen.isEmpty())
					{
						listModel.batchSaveModel(datumPanel.getDatum());
					}
					else
					{
						error = true;
						for (Onderwijsproduct duplicaat : dubbelen)
						{
							OndPrKeuzeAangevenStap3Page.this.error(listModel.getVerbintenis()
								.getDeelnemer().getPersoon().getVolledigeNaam()
								+ " heeft onderwijsproduct "
								+ duplicaat.getContextInfoOmschrijving()
								+ " meerdere keren gekozen.");
						}
					}

				}
				if (error)
				{
					DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchRollback();
				}
				else
				{
					DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
					info("De gekozen onderwijsproducten zijn opgeslagen");
				}
			}
		});
		panel.addButton(new TerugButton(panel, returnPage));
		panel.addButton(new AnnulerenButton(panel, DeelnemerZoekenPage.class));
	}

	@Override
	public boolean supportsBookmarks()
	{
		return false;
	}

}
