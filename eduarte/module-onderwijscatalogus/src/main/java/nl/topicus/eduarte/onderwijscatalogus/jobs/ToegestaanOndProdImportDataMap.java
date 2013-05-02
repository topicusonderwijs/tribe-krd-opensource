package nl.topicus.eduarte.onderwijscatalogus.jobs;

import nl.topicus.cobra.quartz.DetachableJobDataMap;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.onderwijscatalogus.web.components.file.ToegestaanOndProdImportBestandField;

public class ToegestaanOndProdImportDataMap extends DetachableJobDataMap
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public ToegestaanOndProdImportDataMap()
	{
	}

	@AutoForm(required = true, editorClass = ToegestaanOndProdImportBestandField.class)
	public ToegestaanOndProdImportFile getBestand()
	{
		return (ToegestaanOndProdImportFile) get("bestand");
	}

	public void setBestand(ToegestaanOndProdImportFile bestand)
	{
		put("bestand", bestand);
	}
}
