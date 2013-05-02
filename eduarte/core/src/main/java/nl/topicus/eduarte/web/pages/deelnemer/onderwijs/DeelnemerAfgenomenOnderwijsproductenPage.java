package nl.topicus.eduarte.web.pages.deelnemer.onderwijs;

import java.util.Iterator;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelAjaxClickableRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.core.principals.deelnemer.onderwijsproducten.DeelnemerOnderwijsproductenInzien;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductAfnameDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.web.components.factory.KRDModuleComponentFactory;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OnderwijsproductAfnameTable;
import nl.topicus.eduarte.web.components.panels.filter.OnderwijsproductAfnameZoekFilterPanel;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductAfnameZoekFilter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

/**
 * @author vandekamp
 */
@PageInfo(title = "Afgenomen onderwijsproducten", menu = {"Deelnemer > [deelnemer] > Onderwijs -> Afgenomen onderwijsproducten"})
@InPrincipal(DeelnemerOnderwijsproductenInzien.class)
public class DeelnemerAfgenomenOnderwijsproductenPage extends AbstractDeelnemerPage
{
	private static final long serialVersionUID = 1L;

	private IModel<OnderwijsproductAfname> selectedItem;

	private EduArteDataPanel<OnderwijsproductAfname> datapanel;

	public DeelnemerAfgenomenOnderwijsproductenPage(DeelnemerProvider provider)
	{
		this(provider.getDeelnemer(), provider.getDeelnemer()
			.getEersteInschrijvingOpPeildatum(true));
	}

	public DeelnemerAfgenomenOnderwijsproductenPage(Deelnemer deelnemer)
	{
		this(deelnemer, deelnemer.getEersteInschrijvingOpPeildatum(true));
	}

	public DeelnemerAfgenomenOnderwijsproductenPage(Verbintenis inschrijving)
	{
		this(inschrijving.getDeelnemer(), inschrijving);
	}

	public DeelnemerAfgenomenOnderwijsproductenPage(Deelnemer deelnemer, Verbintenis inschrijving)
	{
		super(DeelnemerMenuItem.AfgOnderwijsproducten, deelnemer, inschrijving);
		OnderwijsproductAfnameZoekFilter filter = new OnderwijsproductAfnameZoekFilter(deelnemer);
		filter.addOrderByProperty("onderwijsproduct.titel");
		IDataProvider<OnderwijsproductAfname> provider =
			GeneralFilteredSortableDataProvider.of(filter,
				OnderwijsproductAfnameDataAccessHelper.class);
		Iterator< ? extends OnderwijsproductAfname> it = provider.iterator(0, 1);
		if (it.hasNext())
			selectedItem =
				ModelFactory.getModel((OnderwijsproductAfname) it.next(), new DefaultModelManager(
					OnderwijsproductAfname.class));
		datapanel =
			new EduArteDataPanel<OnderwijsproductAfname>("datapanel", provider,
				new OnderwijsproductAfnameTable(false, getContextVerbintenisModel()));
		datapanel.setRowFactory(new CustomDataPanelAjaxClickableRowFactory<OnderwijsproductAfname>(
			selectedItem)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target, Item<OnderwijsproductAfname> item)
			{
				target.addComponent(datapanel);
			}

		});
		add(new OnderwijsproductAfnameZoekFilterPanel("filter", filter, datapanel));
		add(datapanel);
		createComponents();
	}

	/**
	 * @see nl.topicus.eduarte.web.pages.SecurePage#fillBottomRow(nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel)
	 */
	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{

		ModuleEditPageButton<Verbintenis> toevoegen =
			new ModuleEditPageButton<Verbintenis>(panel, "Toevoegen", CobraKeyAction.TOEVOEGEN,
				Verbintenis.class, getSelectedMenuItem(),
				DeelnemerAfgenomenOnderwijsproductenPage.this, getContextVerbintenisModel())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return super.isVisible()
						&& getContextVerbintenis() != null
						&& !getContextVerbintenis().isBeeindigd()
						&& getContextVerbintenis().getStatus().tussen(VerbintenisStatus.Intake,
							VerbintenisStatus.Definitief);
				}
			};
		panel.addButton(toevoegen);
		addAfnameBewerkenButton(panel);
	}

	private void addAfnameBewerkenButton(BottomRowPanel panel)
	{
		List<KRDModuleComponentFactory> factories =
			EduArteApp.get().getPanelFactories(KRDModuleComponentFactory.class);
		for (KRDModuleComponentFactory factory : factories)
		{
			factory.newEditOnderwijsproductAfnameKnop(panel, getContextVerbintenisModel(),
				selectedItem, new AbstractReadOnlyModel<Boolean>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public Boolean getObject()
					{
						return selectedItem != null && !getContextVerbintenis().isBeeindigd();
					}
				});
		}
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(selectedItem);
		super.onDetach();
	}

}
