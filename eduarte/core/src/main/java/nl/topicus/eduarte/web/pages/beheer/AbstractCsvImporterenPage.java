package nl.topicus.eduarte.web.pages.beheer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.ResourceUtil;
import nl.topicus.cobra.web.components.menu.main.MainMenuItem;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.eduarte.app.util.importeren.AbstractCsvImporterenFile;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCsvImporterenPage extends SecurePage
{
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(AbstractCsvImporterenPage.class);

	private final Form<Void> form;

	private final SecurePage returnToPage;

	private AbstractCsvImporterenFile importFile;

	public AbstractCsvImporterenPage(MainMenuItem mainMenuItem, String label,
			SecurePage returnToPage, AbstractCsvImporterenFile importFile)
	{
		super(mainMenuItem);
		this.returnToPage = returnToPage;
		this.importFile = importFile;
		add(new Label("caption", label));
		form = new Form<Void>("form");
		form.add(new FileUploadField("bestand", new Model<FileUpload>()).setRequired(true));
		add(form);
		ListView<String> kolommen = getKolommen();
		form.add(kolommen);
		createComponents();
	}

	private ListView<String> getKolommen()
	{
		ListView<String> kolommen =
			new ListView<String>("kolommen", Arrays.asList(getKolomOmschrijvingen()))
			{

				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem<String> item)
				{
					item.add(new Label("omschrijving", item.getDefaultModelObjectAsString()));
				}
			};
		return kolommen;
	}

	protected abstract String[] getKolomOmschrijvingen();

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form, "Importeren")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit()
			{
				processSubmit();
			}
		});
		if (returnToPage != null)
			panel.addButton(new AnnulerenButton(panel, returnToPage));
	}

	private void processSubmit()
	{
		FileUpload upload = ((FileUploadField) form.get("bestand")).getFileUpload();
		if (upload == null)
		{
			error("Geen importbestand geselecteerd");
			return;
		}
		BufferedReader reader = null;
		InputStream file = null;
		try
		{
			file = upload.getInputStream();
			reader = new BufferedReader(new InputStreamReader(file));
			importFile.readLines(reader);
		}
		catch (IOException e)
		{
			log.error("Importbestand kon niet gelezen worden.", e);
			error("Importbestand kon niet gelezen worden.\n" + e.getLocalizedMessage());
			return;
		}
		catch (IllegalArgumentException e)
		{
			log.error("Bestand werd niet herkend als een geldig importbestand.", e);
			error("Bestand werd niet herkend als een geldig importbestand.");
			return;
		}
		finally
		{
			ResourceUtil.closeQuietly(reader);
			ResourceUtil.closeQuietly(file);
		}

		try
		{
			importFile.importeer();
			DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
		}
		catch (HibernateException e)
		{
			log.error("Fout opgetreden tijdens het importeren van de gegevens.", e);
			error("Er is een fout opgetreden tijdens het importeren van de gegevens: "
				+ e.getCause().getLocalizedMessage());
		}
		catch (Exception e)
		{
			log.error("Fout opgetreden tijdens het importeren van de gegevens.", e);
			error("Er is een fout opgetreden tijdens het importeren van de gegevens: "
				+ e.getLocalizedMessage());
		}
		setResponsePage(new CsvImporterenResultatenPage(getSelectedItem(), getReturnPage(),
			importFile));
	}

	protected SecurePage getReturnPage()
	{
		return returnToPage;
	}

	@Override
	public boolean supportsBookmarks()
	{
		return false;
	}

	protected Form<Void> getForm()
	{
		return form;
	}

}
