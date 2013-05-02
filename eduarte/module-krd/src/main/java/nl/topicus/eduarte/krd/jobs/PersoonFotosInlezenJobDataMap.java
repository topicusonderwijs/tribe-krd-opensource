package nl.topicus.eduarte.krd.jobs;

import java.io.File;
import java.io.IOException;

import nl.topicus.cobra.quartz.DetachableJobDataMap;
import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;

public class PersoonFotosInlezenJobDataMap extends DetachableJobDataMap
{
	private static final long serialVersionUID = 1L;

	public enum Voor
	{
		Deelnemers,
		Medewerkers;

		public String enkelvoud()
		{
			return name().toLowerCase().substring(0, name().length() - 1);
		}
	}

	public PersoonFotosInlezenJobDataMap()
	{
	}

	@AutoForm(editorClass = EnumCombobox.class, required = true, order = 1, label = "Fotos voor")
	public Voor getFotosVoor()
	{
		return (Voor) get("fotosVoor");
	}

	public void setFotosVoor(Voor voor)
	{
		put("fotosVoor", voor);
	}

	@AutoForm(editorClass = FileUploadField.class, label = "Bestand", order = 2, required = true, description = "Voor deelnemerfoto's: selecteer een foto genaamd &lt;i&gt;&amp;lt;deelnemernummer&amp;gt;&lt;/i&gt;.jpg. Voor medewerkerfoto's: selecteer een foto genaamd &lt;i&gt;&amp;lt;afkorting&amp;gt;&lt;/i&gt;.jpg. U kunt ook een .zip-bestand met meerdere van dergelijke bestanden selecteren.")
	public FileUpload getBestand()
	{
		return (FileUpload) get("bestand");
	}

	public void setBestand(FileUpload fileupload)
	{
		try
		{
			if (fileupload != null)
			{
				File file = fileupload.writeToTempFile();
				put("bestand", file);
				put("bestandsnaam", fileupload.getClientFileName());
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}
