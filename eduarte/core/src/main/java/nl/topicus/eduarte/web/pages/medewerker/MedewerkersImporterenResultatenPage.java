package nl.topicus.eduarte.web.pages.medewerker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.app.util.importeren.medewerkers.MedewerkersImporterenFile;
import nl.topicus.eduarte.core.principals.beheer.account.AccountsWrite;
import nl.topicus.eduarte.dao.helpers.AccountDataAccessHelper;
import nl.topicus.eduarte.web.components.menu.MedewerkerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.MedewerkerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author idserda
 */
@PageInfo(title = "Resultaten medewerkers importeren", menu = {"Medewerkers > Medewerkers Importeren > Importeren"})
@InPrincipal(AccountsWrite.class)
public class MedewerkersImporterenResultatenPage extends SecurePage
{
	private static final long serialVersionUID = 1L;

	private static final Logger log =
		LoggerFactory.getLogger(MedewerkersImporterenResultatenPage.class);

	private final Form<Void> form;

	private boolean validate;

	public MedewerkersImporterenResultatenPage(final MedewerkersImporterenFile importFile,
			boolean validate)
	{
		super(CoreMainMenuItem.Medewerker);
		this.validate = validate;
		if (validate)
		{
			add(new Label("results",
				"Resultaten indien het bestand daadwerkelijk geimporteerd gaat worden:"));
			add(new Label("titel", "Resultaten validatie"));
		}
		else
		{
			add(new Label("results", "Resultaten van het importeren:"));
			add(new Label("titel", "Resultaten importeren"));
		}
		this.form = new Form<Void>("form")
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit()
			{
				try
				{
					importFile.importeerMedewerkers(true);
					DataAccessRegistry.getHelper(AccountDataAccessHelper.class).batchExecute();
				}
				catch (Exception e)
				{
					log.error("Fout opgetreden tijdens het importeren van accounts.", e);
					error("Er is een fout opgetreden tijdens het importeren van de accounts: "
						+ e.getLocalizedMessage());
				}
				setResponsePage(new MedewerkersImporterenResultatenPage(importFile, false));

			}
		};
		add(form);

		Date d = TimeUtil.getInstance().currentDate();
		File file = null;
		try
		{
			file =
				File.createTempFile("ResultatenAccountsImporteren "
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
		if (validate)
		{
			panel.addButton(new OpslaanButton(panel, form, "Importeren"));
		}
		panel.addButton(new TerugButton(panel, MedewerkersImporterenPage.class));
	}

	@Override
	public boolean supportsBookmarks()
	{
		return false;
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new MedewerkerCollectiefMenu(id, MedewerkerCollectiefMenuItem.Zoeken);
	}

}
