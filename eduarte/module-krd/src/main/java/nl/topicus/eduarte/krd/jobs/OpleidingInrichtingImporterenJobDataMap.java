package nl.topicus.eduarte.krd.jobs;

import nl.topicus.cobra.quartz.DetachableJobDataMap;
import nl.topicus.cobra.web.components.form.AutoFormEmbedded;

public class OpleidingInrichtingImporterenJobDataMap extends DetachableJobDataMap
{
	private static final long serialVersionUID = 1L;

	public OpleidingInrichtingImporterenJobDataMap()
	{
		setSettings(new OpleidingInrichtingImporteerSettings());
	}

	@AutoFormEmbedded
	public OpleidingInrichtingImporteerSettings getSettings()
	{
		return (OpleidingInrichtingImporteerSettings) get("settings");
	}

	public void setSettings(OpleidingInrichtingImporteerSettings settings)
	{
		put("settings", settings);
	}
}
