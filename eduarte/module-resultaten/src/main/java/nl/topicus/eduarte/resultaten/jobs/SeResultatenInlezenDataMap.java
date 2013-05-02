package nl.topicus.eduarte.resultaten.jobs;

import java.util.Date;

import nl.topicus.cobra.quartz.DetachableJobDataMap;
import nl.topicus.cobra.web.components.file.SimpleCSVFile;
import nl.topicus.cobra.web.components.file.SimpleCSVFileField;
import nl.topicus.cobra.web.components.form.AutoForm;

public class SeResultatenInlezenDataMap extends DetachableJobDataMap
{
	private static final long serialVersionUID = 1L;

	public SeResultatenInlezenDataMap()
	{
	}

	@AutoForm(required = true, order = 0)
	public Date getDatum()
	{
		return (Date) get("datum");
	}

	public void setDatum(Date datum)
	{
		put("datum", datum);
	}

	@AutoForm(required = true, order = 6, editorClass = SimpleCSVFileField.class)
	public SimpleCSVFile getBestand()
	{
		return (SimpleCSVFile) get("bestand");
	}

	public void setBestand(SimpleCSVFile bestand)
	{
		put("bestand", bestand);
	}
}
