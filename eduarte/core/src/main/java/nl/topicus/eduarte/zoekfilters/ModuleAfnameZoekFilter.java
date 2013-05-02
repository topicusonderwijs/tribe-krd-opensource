package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.organisatie.ModuleAfname;
import nl.topicus.eduarte.entities.organisatie.Organisatie;

import org.apache.wicket.model.IModel;

public class ModuleAfnameZoekFilter extends AbstractZoekFilter<ModuleAfname>
{
	private static final long serialVersionUID = 1L;

	private String moduleName;

	private IModel<Organisatie> filterOrganisatie;

	public ModuleAfnameZoekFilter()
	{
	}

	public String getModuleName()
	{
		return moduleName;
	}

	public void setModuleName(String moduleName)
	{
		this.moduleName = moduleName;
	}

	public Organisatie getFilterOrganisatie()
	{
		return getModelObject(filterOrganisatie);
	}

	public void setFilterOrganisatie(Organisatie filterOrganisatie)
	{
		this.filterOrganisatie = makeModelFor(filterOrganisatie);
	}
}
