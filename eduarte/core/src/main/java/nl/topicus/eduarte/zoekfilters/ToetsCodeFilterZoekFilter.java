package nl.topicus.eduarte.zoekfilters;

import java.util.List;

import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelEntiteit;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.resultaatstructuur.ToetsCodeFilter;

import org.apache.wicket.model.IModel;

public class ToetsCodeFilterZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<ToetsCodeFilter>
{
	private static final long serialVersionUID = 1L;

	private IModel<Medewerker> medewerker;

	private Boolean persoonlijk;

	private IModel<List< ? extends IOrganisatieEenheidLocatieKoppelEntiteit< ? >>> organisatieEenheidLocatieList;

	public static ToetsCodeFilterZoekFilter createDefaultFilter()
	{
		ToetsCodeFilterZoekFilter ret = new ToetsCodeFilterZoekFilter();
		ret.addOrderByProperty("naam");
		return ret;
	}

	public ToetsCodeFilterZoekFilter()
	{
	}

	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerker = makeModelFor(medewerker);
	}

	public Medewerker getMedewerker()
	{
		return getModelObject(medewerker);
	}

	public void setPersoonlijk(Boolean persoonlijk)
	{
		this.persoonlijk = persoonlijk;
	}

	public Boolean getPersoonlijk()
	{
		return persoonlijk;
	}

	public List< ? extends IOrganisatieEenheidLocatieKoppelEntiteit< ? >> getOrganisatieEenheidLocatieList()
	{
		return getModelObject(organisatieEenheidLocatieList);
	}

	@SuppressWarnings("unchecked")
	public <U extends IOrganisatieEenheidLocatieKoppelEntiteit<U>> void setOrganisatieEenheidLocatieList(
			List<U> organisatieEenheidLocatieList)
	{
		this.organisatieEenheidLocatieList = (IModel) makeModelFor(organisatieEenheidLocatieList);
	}
}
