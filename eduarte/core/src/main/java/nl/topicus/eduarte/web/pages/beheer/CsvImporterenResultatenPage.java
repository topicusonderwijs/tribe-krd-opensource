package nl.topicus.eduarte.web.pages.beheer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.main.MainMenuItem;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.app.util.importeren.AbstractCsvImporterenFile;
import nl.topicus.eduarte.core.principals.Always;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PageInfo(title = "Resultaten CSV-import", menu = {"Beheer > Financieel > Grootboekrekeningen > CSV-Import > Importeren"})
@InPrincipal(Always.class)
public class CsvImporterenResultatenPage extends SecurePage
{
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(CsvImporterenResultatenPage.class);

	private final SecurePage returnPage;

	public CsvImporterenResultatenPage(MainMenuItem mainMenuItem, final SecurePage returnPage,
			final AbstractCsvImporterenFile importFile)
	{
		super(mainMenuItem);
		this.returnPage = returnPage;
		Date d = TimeUtil.getInstance().currentDate();
		File file = null;
		try
		{
			file =
				File.createTempFile("CSV-import_Resultaten-"
					+ TimeUtil.getInstance().formatDateAsIsoString(d), ".txt");
		}
		catch (IOException e1)
		{
			log.error("Fout bij het maken van tijdelijk bestand: " + e1.getLocalizedMessage(), e1);
		}
		try
		{
			FileWriter outFile = new FileWriter(file);
			BufferedWriter out = new BufferedWriter(outFile);
			StringBuffer buffer = new StringBuffer();
			for (String regel : importFile.getAlleResultaten())
			{
				buffer.append(regel);
				buffer.append("\r\n");
			}
			out.write(buffer.toString());// only the last row there
			out.flush();
			out.close();
		}
		catch (Exception e)
		{
			log.error("Error: " + e.getMessage());
		}
		add(new DownloadLink("file", file));

		add(new ListView<String>("resultaten", importFile.getAlleResultaten())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<String> item)
			{
				item.add(new Label("label", item.getModel()));
			}
		});
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new TerugButton(panel, returnPage));
	}

	@Override
	public boolean supportsBookmarks()
	{
		return false;
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return returnPage.createMenu(id);
	}

}
