package nl.topicus.eduarte.resultaten.jobs;

import nl.topicus.cobra.quartz.DetachableJobDataMap;
import nl.topicus.cobra.web.components.form.AutoFormEmbedded;

public class ResultaatstructurenImporterenJobDataMap extends DetachableJobDataMap
{
	private static final long serialVersionUID = 1L;

	public ResultaatstructurenImporterenJobDataMap()
	{
		setSettings(new ResultaatstructuurImporteerSettings());
	}

	@AutoFormEmbedded
	public ResultaatstructuurImporteerSettings getSettings()
	{
		return (ResultaatstructuurImporteerSettings) get("settings");
	}

	public void setSettings(ResultaatstructuurImporteerSettings settings)
	{
		put("settings", settings);
	}
}
