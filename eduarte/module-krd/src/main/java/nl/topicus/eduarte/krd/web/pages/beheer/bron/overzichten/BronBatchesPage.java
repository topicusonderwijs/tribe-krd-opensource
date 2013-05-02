package nl.topicus.eduarte.krd.web.pages.beheer.bron.overzichten;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.CollectionDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.CustomColumn.Positioning;
import nl.topicus.cobra.web.components.datapanel.columns.ButtonColumn;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.entities.Entiteit;
import nl.topicus.eduarte.krd.bron.BronBatchBuilder;
import nl.topicus.eduarte.krd.dao.helpers.BronDataAccessHelper;
import nl.topicus.eduarte.krd.principals.beheer.bron.BronOverzichtInzien;
import nl.topicus.eduarte.krd.principals.beheer.bron.BronOverzichtWrite;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.columns.BronBatchDownloadenColumn;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.columns.BronBatchVerwijderenButtonColumn;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.table.BronBatchTable;
import nl.topicus.eduarte.krd.web.components.panels.filter.BronBatchZoekFilterPanel;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.AbstractBronPage;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.BronAlgemeenPage;
import nl.topicus.eduarte.krd.zoekfilters.BronBatchZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.BronMeldingZoekFilter;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.onderwijs.duo.bron.IBronBatch;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.swarm.checks.DataSecurityCheck;

@PageInfo(title = "BRON batches", menu = "Deelnemer")
@InPrincipal(BronOverzichtInzien.class)
@SearchImplementsActions( {Instelling.class, OrganisatieEenheid.class})
public class BronBatchesPage extends AbstractBronPage
{
	private EduArteDataPanel<IBronBatch> datapanel;

	private BronBatchZoekFilter filter;

	private final class BatchListModel extends LoadableDetachableModel<List<IBronBatch>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<IBronBatch> load()
		{
			return DataAccessRegistry.getHelper(BronDataAccessHelper.class).getBronBatches(filter);
		}
	}

	public BronBatchesPage(final BronBatchZoekFilter filter)
	{
		this.filter = filter;
		CollectionDataProvider<IBronBatch> provider =
			new CollectionDataProvider<IBronBatch>(new BatchListModel());
		BronBatchTable table = new BronBatchTable();
		table.addColumn(new ButtonColumn<IBronBatch>("details", "Details", "actionItem",
			"actionItem_grey")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(WebMarkupContainer item, IModel<IBronBatch> rowModel)
			{
				IBronBatch bronBatch = rowModel.getObject();
				BronMeldingZoekFilter meldingZoekfilter =
					new BronMeldingZoekFilter(filter.getBronAanleverpunt(), filter.getSchooljaar());
				meldingZoekfilter.setBatch(bronBatch);
				setResponsePage(new BronMeldingenPage(meldingZoekfilter));
			}
		}.setPositioning(Positioning.FIXED_RIGHT));

		if (new DataSecurityCheck(BronOverzichtWrite.BRON_WRITE).isActionAuthorized(Enable.class))
		{
			table.addColumn(new ButtonColumn<IBronBatch>("OpnieuwGenereren", "Opnieuw genereren",
				"cog_go", "cog_go")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(WebMarkupContainer item, IModel<IBronBatch> rowModel)
				{
					IBronBatch batch = rowModel.getObject();

					batch.berekenControleTotalen();

					batch.setBestand(BronBatchBuilder.writeBronBatch(batch));
					batch.setBestandsnaam(BronBatchBuilder.getFilename(batch));

					// Als iemand bedenkt dat een batch geen entiteit hoort te zijn, mag
					// 'ie
					// zijn hoed opeten.
					((Entiteit) batch).saveOrUpdate();
					((Entiteit) batch).commit();

					getSession().info(
						"Bestand " + batch.getBestandsnaam()
							+ " is opnieuw gegenereerd, u kunt het nu downloaden");
				}
			}.setPositioning(Positioning.FIXED_RIGHT));
			table.addColumn(new BronBatchVerwijderenButtonColumn()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(WebMarkupContainer item, IModel<IBronBatch> rowModel,
						AjaxRequestTarget target)
				{
					IBronBatch batch = rowModel.getObject();
					batch.verwijderBatch();
					getSession().info(
						"De meldingen van " + batch.getBestandsnaam()
							+ " staan weer in de wachtrij.");
					target.addComponent(datapanel);
				}
			}.setPositioning(Positioning.FIXED_RIGHT));
		}
		table.addColumn(new BronBatchDownloadenColumn("Download", "Download")
			.setPositioning(Positioning.FIXED_RIGHT));
		datapanel = new EduArteDataPanel<IBronBatch>("datapanel", provider, table);
		add(datapanel);
		add(new BronBatchZoekFilterPanel("filter", filter, datapanel));
		createComponents();

	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new TerugButton(panel, BronAlgemeenPage.class));
		super.fillBottomRow(panel);
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(BronBatchZoekFilter.class);
		ctorArgValues.add(filter);
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(filter);
		super.onDetach();
	}
}
