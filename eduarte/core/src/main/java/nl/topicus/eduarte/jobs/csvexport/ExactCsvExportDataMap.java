package nl.topicus.eduarte.jobs.csvexport;

import nl.topicus.cobra.quartz.DetachableJobDataMap;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.providers.OrganisatieEenheidLocatieProvider;

public class ExactCsvExportDataMap extends DetachableJobDataMap implements
		OrganisatieEenheidLocatieProvider
{
	private static final long serialVersionUID = 1L;

	@AutoForm(required = false, order = 1, htmlClasses = "unit_max")
	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return (OrganisatieEenheid) get("organisatieEenheid");
	}

	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		put("organisatieEenheid", organisatieEenheid);
	}

	@AutoForm(required = false, order = 2, htmlClasses = "unit_max")
	public Locatie getLocatie()
	{
		return (Locatie) get("locatie");
	}

	public void setLocatie(Locatie locatie)
	{
		put("locatie", locatie);
	}

	public void setOpleiding(Opleiding opleiding)
	{
		put("opleiding", opleiding);
	}

	@AutoForm(required = false, order = 3, htmlClasses = "unit_max")
	public Opleiding getOpleiding()
	{
		return (Opleiding) get("opleiding");
	}

}
