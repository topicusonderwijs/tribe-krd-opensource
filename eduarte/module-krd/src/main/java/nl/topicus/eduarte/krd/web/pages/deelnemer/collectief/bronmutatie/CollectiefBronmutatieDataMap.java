package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.bronmutatie;

import java.util.List;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.quartz.DetachableJobDataMap;
import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.eduarte.entities.Entiteit;

public class CollectiefBronmutatieDataMap<T extends Entiteit> extends DetachableJobDataMap
{
	private static final long serialVersionUID = 1L;

	public CollectiefBronmutatieDataMap(
			ISelectionComponent< ? extends IdObject, ? extends IdObject> selection,
			CollectiefBronmutatieModel model)
	{
		setSelectie(selection.getSelectedElements());
		put("soortMutatie", model.getSoortMutatie());
	}

	public void setSelectie(List< ? extends IdObject> selectie)
	{
		put("selectie", selectie);
	}

}