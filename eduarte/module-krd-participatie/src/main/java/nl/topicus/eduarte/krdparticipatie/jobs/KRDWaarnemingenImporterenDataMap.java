package nl.topicus.eduarte.krdparticipatie.jobs;

import nl.topicus.cobra.quartz.DetachableJobDataMap;
import nl.topicus.cobra.web.components.form.AutoForm;

/**
 * Datamap voor het bijhouden van de benodigde instellingen van een importtask voor
 * waarnemingen
 * 
 * @author loite
 */
public class KRDWaarnemingenImporterenDataMap extends DetachableJobDataMap
{
	private static final long serialVersionUID = 1L;

	public KRDWaarnemingenImporterenDataMap()
	{
	}

	@AutoForm(required = true, order = 6, editorClass = KRDWaarnemingenImporterenBestandField.class)
	public KRDWaarnemingenImportFile getBestand()
	{
		return (KRDWaarnemingenImportFile) get("bestand");
	}

	public void setBestand(KRDWaarnemingenImportFile bestand)
	{
		put("bestand", bestand);
	}
}
