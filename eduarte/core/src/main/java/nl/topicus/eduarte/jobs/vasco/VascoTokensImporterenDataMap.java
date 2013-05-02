package nl.topicus.eduarte.jobs.vasco;

import nl.topicus.cobra.quartz.DetachableJobDataMap;
import nl.topicus.cobra.web.components.form.AutoForm;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;

public class VascoTokensImporterenDataMap extends DetachableJobDataMap
{
	private static final long serialVersionUID = 1L;

	@AutoForm(required = true, order = 6)
	public String getDpxkey()
	{
		return (String) get("dpxkey");
	}

	public void setDpxkey(String dpxkey)
	{
		put("dpxkey", dpxkey);
	}

	@AutoForm(required = true, order = 7, editorClass = FileUploadField.class)
	public FileUpload getBestand()
	{
		return (FileUpload) get("bestand");
	}

	public void setBestand(FileUpload bestand)
	{
		put("bestand", bestand);
	}
}
