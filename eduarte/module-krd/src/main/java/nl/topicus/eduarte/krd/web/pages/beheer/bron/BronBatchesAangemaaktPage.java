package nl.topicus.eduarte.krd.web.pages.beheer.bron;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.CollectionDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.krd.principals.beheer.bron.BronOverzichtWrite;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.columns.BronBatchDownloadenColumn;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.table.BronBatchTable;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.onderwijs.duo.bron.IBronBatch;

@PageInfo(title = "Aangemaakt BRON-batches", menu = "Deelnemer")
@InPrincipal(BronOverzichtWrite.class)
public class BronBatchesAangemaaktPage extends AbstractBronPage
{

	public BronBatchesAangemaaktPage(BronBatchModel bronBatchModel)
	{
		CollectionDataProvider<IBronBatch> provider =
			new CollectionDataProvider<IBronBatch>(bronBatchModel);
		BronBatchTable table = new BronBatchTable();
		table.addColumn(new BronBatchDownloadenColumn("Download", "Download"));
		EduArteDataPanel<IBronBatch> datapanel =
			new EduArteDataPanel<IBronBatch>("datapanel", provider, table);
		add(datapanel);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new AnnulerenButton(panel, BronAlgemeenPage.class));
		super.fillBottomRow(panel);
	}

	@Override
	public boolean supportsBookmarks()
	{
		return false;
	}
}
