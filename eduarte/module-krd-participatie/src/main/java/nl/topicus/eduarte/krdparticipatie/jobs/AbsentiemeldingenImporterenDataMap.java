package nl.topicus.eduarte.krdparticipatie.jobs;

import nl.topicus.cobra.quartz.DetachableJobDataMap;
import nl.topicus.cobra.web.components.form.AutoForm;

/**
 * Datamap voor het bijhouden van de benodigde instellingen van een importtask voor
 * absentiemeldingen
 * 
 * @author loite
 */
public class AbsentiemeldingenImporterenDataMap extends DetachableJobDataMap
{
	private static final long serialVersionUID = 1L;

	public AbsentiemeldingenImporterenDataMap()
	{
	}

	@AutoForm(required = true, order = 6, editorClass = AbsentiemeldingenImporterenBestandField.class)
	public AbsentiemeldingenImportFile getBestand()
	{
		return (AbsentiemeldingenImportFile) get("bestand");
	}

	public void setBestand(AbsentiemeldingenImportFile bestand)
	{
		put("bestand", bestand);
	}
}
