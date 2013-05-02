package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.aanmaken;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.quartz.DetachableJobDataMap;
import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;

public class CollectiefAanmakenDataMap extends DetachableJobDataMap
{
	private static final long serialVersionUID = 1L;

	public CollectiefAanmakenDataMap(
			ISelectionComponent< ? extends IdObject, ? extends IdObject> selection,
			CollectiefAanmakenModel model)
	{
		put("selectie", selection.getSelectedElements());
		put("collectiefAanmakenModel", model);
	}
}