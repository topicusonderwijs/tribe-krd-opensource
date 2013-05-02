package nl.topicus.eduarte.onderwijscatalogus.jobs;

import nl.topicus.cobra.quartz.DetachableJobDataMap;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.onderwijscatalogus.web.components.file.OnderwijsproductImportBestandField;

/**
 * Datamap voor het bijhouden van de benodigde instellingen van een importtask voor een
 * onderwijsproducten
 * 
 * @author vandekamp
 */
public class OnderwijsproductImportDataMap extends DetachableJobDataMap
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public OnderwijsproductImportDataMap()
	{
	}

	@AutoForm(required = true, order = 6, editorClass = OnderwijsproductImportBestandField.class)
	public OnderwijsproductImportFile getBestand()
	{
		return (OnderwijsproductImportFile) get("bestand");
	}

	public void setBestand(OnderwijsproductImportFile bestand)
	{
		put("bestand", bestand);
	}
}
