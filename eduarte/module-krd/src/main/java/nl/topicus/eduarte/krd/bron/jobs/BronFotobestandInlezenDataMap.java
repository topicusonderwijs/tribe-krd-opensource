package nl.topicus.eduarte.krd.bron.jobs;

import java.io.File;

import nl.topicus.cobra.quartz.DetachableJobDataMap;
import nl.topicus.cobra.web.components.file.TempFileUploadField;
import nl.topicus.cobra.web.components.form.AutoForm;

/**
 * Datamap voor het bijhouden van de benodigde instellingen van een inleesactie van een
 * bron fotobestand.
 * 
 * @author loite
 */
public class BronFotobestandInlezenDataMap extends DetachableJobDataMap
{
	private static final long serialVersionUID = 1L;

	public BronFotobestandInlezenDataMap()
	{
	}

	@AutoForm(required = true, order = 6, editorClass = TempFileUploadField.class)
	public File getBestand()
	{
		return (File) get("bestand");
	}

	public void setBestand(File bestand)
	{
		put("bestand", bestand);
	}
}
