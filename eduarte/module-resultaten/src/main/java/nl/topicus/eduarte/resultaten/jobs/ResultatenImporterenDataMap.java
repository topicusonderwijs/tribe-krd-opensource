package nl.topicus.eduarte.resultaten.jobs;

import nl.topicus.cobra.quartz.DetachableJobDataMap;
import nl.topicus.cobra.web.components.file.SimpleCSVFile;
import nl.topicus.cobra.web.components.file.SimpleCSVFileField;
import nl.topicus.cobra.web.components.form.AutoForm;

public class ResultatenImporterenDataMap extends DetachableJobDataMap
{
	private static final long serialVersionUID = 1L;

	public ResultatenImporterenDataMap()
	{
	}

	@AutoForm(required = true, editorClass = SimpleCSVFileField.class)
	public SimpleCSVFile getBestand()
	{
		return (SimpleCSVFile) get("bestand");
	}

	public void setBestand(SimpleCSVFile bestand)
	{
		put("bestand", bestand);
	}
}
