package nl.topicus.eduarte.web.pages.medewerker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ResourceUtil;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.util.importeren.medewerkers.MedewerkersImporterenFile;
import nl.topicus.eduarte.core.principals.beheer.account.AccountsWrite;
import nl.topicus.eduarte.dao.helpers.MedewerkerDataAccessHelper;
import nl.topicus.eduarte.web.components.menu.MedewerkerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.MedewerkerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author idserda
 */
@PageInfo(title = "Medewerkers importeren", menu = {"Medewerkers > Medewerkers Importeren"})
@InPrincipal(AccountsWrite.class)
public class MedewerkersImporterenPage extends SecurePage
{
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(MedewerkersImporterenPage.class);

	private final Form<MedewerkersImportSettings> form;

	public MedewerkersImporterenPage()
	{
		super(CoreMainMenuItem.Medewerker);

		MedewerkersImportSettings settings = new MedewerkersImportSettings();

		form =
			new Form<MedewerkersImportSettings>("form",
				new CompoundPropertyModel<MedewerkersImportSettings>(settings));
		form.add(new FileUploadField("bestand", new Model<FileUpload>()).setRequired(true));
		add(form);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form, "Importeren")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit()
			{
				processSubmit(true);
			}
		});
		panel.addButton(new OpslaanButton(panel, form, "Valideren")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit()
			{
				processSubmit(false);
			}
		}.setAction(CobraKeyAction.GEEN));
		panel.addButton(new AnnulerenButton(panel, MedewerkerZoekenPage.class));
	}

	private void processSubmit(boolean opslaan)
	{
		FileUpload upload = ((FileUploadField) form.get("bestand")).getFileUpload();
		if (upload == null)
		{
			error("Geen importbestand geselecteerd");
			return;
		}
		BufferedReader reader = null;
		InputStream file = null;
		MedewerkersImporterenFile importFile = null;
		try
		{
			file = upload.getInputStream();
			reader = new BufferedReader(new InputStreamReader(file, "Cp1252"));
			importFile = new MedewerkersImporterenFile(reader);
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
			importFile.importeerMedewerkers(opslaan);
			DataAccessRegistry.getHelper(MedewerkerDataAccessHelper.class).batchExecute();
		}
		catch (Exception e)
		{
			log.error("Fout opgetreden tijdens het importeren van accounts.", e);
			error("Er is een fout opgetreden tijdens het importeren van de accounts: "
				+ e.getLocalizedMessage());
		}
		setResponsePage(new MedewerkersImporterenResultatenPage(importFile, !opslaan));
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new MedewerkerCollectiefMenu(id, MedewerkerCollectiefMenuItem.Zoeken);
	}

	// Nu nog geen configureerbare settings
	private class MedewerkersImportSettings implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public MedewerkersImportSettings()
		{
		}

	}

}
