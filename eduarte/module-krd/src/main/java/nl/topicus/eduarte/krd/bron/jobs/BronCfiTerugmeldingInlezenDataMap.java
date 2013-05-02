package nl.topicus.eduarte.krd.bron.jobs;

import nl.topicus.cobra.quartz.DetachableJobDataMap;
import nl.topicus.cobra.web.components.form.AutoForm;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;

/**
 * Datamap voor het bijhouden van de benodigde instellingen van een inleesactie van een
 * CFI-terugmelding
 * 
 * @author vandekamp
 */
public class BronCfiTerugmeldingInlezenDataMap extends DetachableJobDataMap
{
	private static final long serialVersionUID = 1L;

	public BronCfiTerugmeldingInlezenDataMap()
	{
	}

	@AutoForm(required = true, order = 6, editorClass = FileUploadField.class)
	public FileUpload getBestand()
	{
		return (FileUpload) get("bestand");
	}

	public void setBestand(FileUpload bestand)
	{
		put("bestand", bestand);
	}
}
