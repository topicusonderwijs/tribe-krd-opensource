package nl.topicus.eduarte.krdparticipatie.web.components.menu.enums;

import nl.topicus.cobra.web.components.menu.MenuItemKey;

/**
 * @author loite
 */
public enum ParticipatieDeelnemerMenuItem implements MenuItemKey
{

	Absentiemeldingen,

	Waarnemingen,

	Maatregelen,

	Budgetten,

	Weekoverzicht,

	Maandoverzicht,

	Jaaroverzicht,

	Agenda,

	OnderwijsproductTotalen
	{
		@Override
		public String toString()
		{
			return "Totalen per onderwijsproduct";
		}
	},

	Verzuimloket;

	// OLC
	// {
	// @Override
	// public String getLabel()
	// {
	// return "OLC Registraties";
	// }
	// },
	//
	// CollectiefBudget
	// {
	// @Override
	// public String getLabel()
	// {
	// return "Collectief Budget Toekennen";
	// }
	// };
	@Override
	public String getLabel()
	{
		return toString();
	}

}
