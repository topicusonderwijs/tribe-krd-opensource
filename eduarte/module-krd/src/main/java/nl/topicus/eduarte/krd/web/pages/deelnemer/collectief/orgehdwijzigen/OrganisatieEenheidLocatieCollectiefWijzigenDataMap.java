package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.orgehdwijzigen;

import java.util.List;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.quartz.DetachableJobDataMap;
import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.eduarte.entities.Entiteit;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;

public class OrganisatieEenheidLocatieCollectiefWijzigenDataMap<T extends Entiteit> extends
		DetachableJobDataMap
{
	private static final long serialVersionUID = 1L;

	public OrganisatieEenheidLocatieCollectiefWijzigenDataMap(
			ISelectionComponent< ? extends IdObject, ? extends IdObject> selection,
			OrganisatieEenheidLocatieCollectiefWijzigenModel model)
	{
		setSelectie(selection.getSelectedElements());
		setOrganisatieEenheid(model.getOrganisatieEenheid());
		setLocatie(model.getLocatie());
	}

	public void setSelectie(List< ? extends IdObject> selectie)
	{
		put("selectie", selectie);
	}

	private void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		put("organisatieEenheid", organisatieEenheid);
	}

	private void setLocatie(Locatie locatie)
	{
		put("locatie", locatie);
	}
}