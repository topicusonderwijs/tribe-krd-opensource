package nl.topicus.eduarte.krd.web.pages.beheer.bron;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.JavaUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelAjaxClickableRowFactory;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractAjaxConfirmationButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.krd.dao.helpers.BronAanleverpuntDataAccessHelper;
import nl.topicus.eduarte.krd.dao.helpers.BronSchooljaarStatusDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;
import nl.topicus.eduarte.krd.entities.bron.BronSchooljaarStatus;
import nl.topicus.eduarte.krd.entities.bron.BronStatus;
import nl.topicus.eduarte.krd.principals.beheer.bron.BronOverzichtInzien;
import nl.topicus.eduarte.krd.web.components.modalwindow.bron.WijzigStatusModalWindow;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.table.BronSchooljaarStatusTable;
import nl.topicus.eduarte.krd.zoekfilters.BronSchooljaarStatusZoekFilter;
import nl.topicus.eduarte.web.components.menu.BronMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.checks.ClassSecurityCheck;

@PageInfo(title = "BRON Algemeen", menu = "Deelnemer")
@InPrincipal(BronOverzichtInzien.class)
@SearchImplementsActions( {Instelling.class, OrganisatieEenheid.class})
public class BronAlgemeenPage extends AbstractBronPage
{
	private EduArteDataPanel<BronSchooljaarStatus> master;

	private Panel detail;

	private IModel<BronSchooljaarStatus> schooljaarStatusModel;

	private WijzigStatusModalWindow statusWindow;

	public BronAlgemeenPage()
	{
		List<BronAanleverpunt> aanleverpunten =
			DataAccessRegistry.getHelper(BronAanleverpuntDataAccessHelper.class)
				.getBronAanleverpunten();
		if (aanleverpunten.isEmpty())
		{
			error("Er zijn geen aanleverpunten gedefinieerd. Definieer eerst aanleverpunten via BRON -> Instellingen");
			add(new EmptyPanel("statussen"));
			add(new EmptyPanel("detail"));
			add(new EmptyPanel("statusWindow"));
		}
		else
		{
			createSchooljarenStatussen(aanleverpunten);
			statusWindow = new WijzigStatusModalWindow("statusWindow");
			statusWindow.setWindowClosedCallback(new WindowClosedCallback()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClose(AjaxRequestTarget target)
				{
					target.addComponent(master);
					target.addComponent(detail);
				}
			});
			add(statusWindow);
			BronSchooljaarStatusZoekFilter filter = new BronSchooljaarStatusZoekFilter();
			filter.addOrderByProperty("schooljaar");
			Schooljaar huidigeJaar = Schooljaar.huidigSchooljaar();
			Schooljaar[] schooljaren =
				new Schooljaar[] {huidigeJaar.getVorigSchooljaar(), huidigeJaar,
					huidigeJaar.getVolgendSchooljaar()};
			filter.setSchooljaren(Arrays.asList(schooljaren));
			GeneralFilteredSortableDataProvider<BronSchooljaarStatus, BronSchooljaarStatusZoekFilter> provider =
				GeneralFilteredSortableDataProvider.of(filter,
					BronSchooljaarStatusDataAccessHelper.class);

			master =
				new EduArteDataPanel<BronSchooljaarStatus>("statussen", provider,
					new BronSchooljaarStatusTable());
			master.setSelecteerKolommenButtonVisible(false);
			master.setGroeperenButtonVisible(false);

			if (provider.iterator(1, 1).hasNext())
			{
				schooljaarStatusModel = ModelFactory.getModel(provider.iterator(1, 1).next());
				detail = new BronAlgemeenDetailPanel("detail", schooljaarStatusModel);
			}
			else
				detail = new EmptyPanel("detail");

			master.setRowFactory(new CustomDataPanelAjaxClickableRowFactory<BronSchooljaarStatus>(
				schooljaarStatusModel)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onClick(AjaxRequestTarget target, Item<BronSchooljaarStatus> item)
				{
					BronAlgemeenPage.this.addOrReplace(new BronAlgemeenDetailPanel("detail",
						selectedObject));
					target.addComponent(BronAlgemeenPage.this);
					refreshBottomRow(target);
				}

				@Override
				public WebMarkupContainer createHeaderRow(String id,
						CustomDataPanel<BronSchooljaarStatus> panel, Item<BronSchooljaarStatus> item)
				{
					return createClickableRow(id, panel, item, item.getModel());
				}

				@Override
				protected boolean isSelected(CustomDataPanel<BronSchooljaarStatus> panel,
						Item<BronSchooljaarStatus> item, IModel<BronSchooljaarStatus> itemModel)
				{
					return JavaUtil.equalsOrBothNull(selectedObject.getObject(), itemModel
						.getObject());
				}
			});

			add(master);
			detail.setOutputMarkupPlaceholderTag(true);
			add(detail);

		}
		createComponents();
	}

	private void createSchooljarenStatussen(List<BronAanleverpunt> aanleverpunten)
	{
		Schooljaar huidigeJaar = Schooljaar.huidigSchooljaar();
		Schooljaar vorigeJaar = huidigeJaar.getVorigSchooljaar();
		Schooljaar volgendeJaar = huidigeJaar.getVolgendSchooljaar();
		List<Schooljaar> schooljaren =
			Arrays.asList(new Schooljaar[] {vorigeJaar, huidigeJaar, volgendeJaar});

		BronSchooljaarStatusZoekFilter filter = new BronSchooljaarStatusZoekFilter();
		filter.addOrderByProperty("schooljaar");
		filter.setSchooljaren(schooljaren);
		BronSchooljaarStatusDataAccessHelper helper =
			DataAccessRegistry.getHelper(BronSchooljaarStatusDataAccessHelper.class);

		for (BronAanleverpunt aanleverpunt : aanleverpunten)
		{
			filter.setAanleverpunt(aanleverpunt);
			if (helper.list(filter).size() < 3)
			{
				for (Schooljaar schooljaar : schooljaren)
				{
					filter.setSchooljaren(Arrays.asList(schooljaar));
					List<BronSchooljaarStatus> statussen = helper.list(filter);
					if (statussen.isEmpty())
					{
						if (schooljaar == vorigeJaar)
							maakSchooljaarStatus(schooljaar, BronStatus.AssurancerapportOpgesteld,
								aanleverpunt);
						else if (schooljaar == volgendeJaar)
							maakSchooljaarStatus(schooljaar, BronStatus.GegevensWordenIngevoerd,
								aanleverpunt);
						else if (schooljaar == huidigeJaar)
						{
							Date eersteDagVanMaart =
								TimeUtil.getInstance().getFirstDayOfMonth(
									huidigeJaar.getEindJaar(), Calendar.MARCH);
							if (TimeUtil.getInstance().currentDate().before(eersteDagVanMaart))
								maakSchooljaarStatus(schooljaar,
									BronStatus.GegevensWordenIngevoerd, aanleverpunt);
							else
								maakSchooljaarStatus(schooljaar, BronStatus.MutatiestopIngesteld,
									aanleverpunt);
						}
					}
				}
			}
		}
	}

	private void maakSchooljaarStatus(Schooljaar schooljaar, BronStatus bronStatus,
			BronAanleverpunt aanleverpunt)
	{
		BronSchooljaarStatus status = new BronSchooljaarStatus();
		status.setAanleverpunt(aanleverpunt);
		status.setSchooljaar(schooljaar);
		status.setStatusBO(bronStatus);
		status.setStatusED(bronStatus);
		status.setStatusVAVO(bronStatus);
		status.setStatusVO(bronStatus);
		status.save();
		status.commit();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{

		panel
			.addButton(new AbstractAjaxConfirmationButton(
				panel,
				"Wijzig status",
				"Weet u zeker dat u de status wilt gaan wijzigen? Het is mogelijk dat na het wijzigen van de status bepaalde mutaties niet meer zijn toegestaan.",
				CobraKeyAction.GEEN, ButtonAlignment.RIGHT, new ClassSecurityCheck(
					BronInstellingenPage.class))
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onClick(AjaxRequestTarget target)
				{
					statusWindow.show(target);
				}

				@Override
				public boolean isVisible()
				{
					return statusWindow != null;
				}
			});
	}

	@Override
	protected MenuItemKey getSelectedMenuBarItem()
	{
		return BronMenuItem.BRON;
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(schooljaarStatusModel);
		super.onDetach();
	}
}
