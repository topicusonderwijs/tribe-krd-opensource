package nl.topicus.eduarte.krd.web.pages.beheer.bron.overzichten;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.CollectionDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.krd.bron.BronBatchBuilder;
import nl.topicus.eduarte.krd.bron.BronUtils;
import nl.topicus.eduarte.krd.dao.helpers.BronDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IBronExamenMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IBronMelding;
import nl.topicus.eduarte.krd.principals.beheer.bron.BronOverzichtInzien;
import nl.topicus.eduarte.krd.principals.beheer.bron.BronOverzichtWrite;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.table.BronExamenmeldingTable;
import nl.topicus.eduarte.krd.web.components.panels.filter.BronExamenMeldingZoekFilterPanel;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.AbstractBronPage;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.BronAlgemeenPage;
import nl.topicus.eduarte.krd.zoekfilters.BronBatchZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.BronExamenmeldingZoekFilter;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.onderwijs.duo.bron.IBronBatch;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.security.checks.ClassSecurityCheck;

@PageInfo(title = "BRON examen meldingen", menu = "Deelnemer")
@InPrincipal(BronOverzichtInzien.class)
@SearchImplementsActions( {Instelling.class, OrganisatieEenheid.class})
public class BronExamenMeldingenPage extends AbstractBronPage
{
	private BronExamenmeldingZoekFilter filter;

	@InPrincipal(BronOverzichtWrite.class)
	private final class NaarBatchOmzettenButton extends AbstractLinkButton
	{
		private static final long serialVersionUID = 1L;

		private NaarBatchOmzettenButton(BottomRowPanel bottomRow)
		{
			super(bottomRow, "Naar batch omzetten", CobraKeyAction.LINKKNOP1, ButtonAlignment.LEFT);
			ComponentUtil.setSecurityCheck(this, new ClassSecurityCheck(
				NaarBatchOmzettenButton.class));
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onClick()
		{
			BronBatchBuilder builder = new BronBatchBuilder(filter.getExamenverzameling());
			IBronBatch batch = builder.createExamenverzamelingBatch();
			BronUtils.updateStatussenNaBatchAanmaken((List<IBronMelding>) batch.getMeldingen());
			DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
			BronBatchZoekFilter batchFilter =
				new BronBatchZoekFilter(filter.getExamenverzameling().getAanleverpunt(), filter
					.getExamenverzameling().getSchooljaar());
			setResponsePage(new BronBatchesPage(batchFilter));
		}

		@Override
		public boolean isVisible()
		{
			return filter.getExamenverzameling() != null
				&& filter.getExamenverzameling().getBveBatch() == null
				&& filter.getExamenverzameling().getVoBatch() == null;
		}
	}

	private final class ExamenMeldingListModel extends
			LoadableDetachableModel<List<IBronExamenMelding>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<IBronExamenMelding> load()
		{
			List<IBronExamenMelding> list =
				DataAccessRegistry.getHelper(BronDataAccessHelper.class).getBronExamenMeldingen(
					filter);
			return list;
		}
	}

	public BronExamenMeldingenPage()
	{
		this(new BronExamenmeldingZoekFilter());
	}

	public BronExamenMeldingenPage(BronExamenmeldingZoekFilter filter)
	{
		this.filter = filter;
		CollectionDataProvider<IBronExamenMelding> provider =
			new CollectionDataProvider<IBronExamenMelding>(new ExamenMeldingListModel());
		BronExamenmeldingTable table = new BronExamenmeldingTable();
		EduArteDataPanel<IBronExamenMelding> datapanel =
			new EduArteDataPanel<IBronExamenMelding>("datapanel", provider, table);
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<IBronExamenMelding>(
			BronExamenmeldingDetailsPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Item<IBronExamenMelding> item)
			{
				IBronExamenMelding examenMelding = item.getModelObject();
				setResponsePage(new BronExamenmeldingDetailsPage(examenMelding,
					BronExamenMeldingenPage.this));
			}
		});
		add(datapanel);
		add(new BronExamenMeldingZoekFilterPanel("filter", filter, datapanel));
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new PageLinkButton(panel, "BRON Startscherm", BronAlgemeenPage.class));
		panel.addButton(new NaarBatchOmzettenButton(panel));
		super.fillBottomRow(panel);
	}
}
