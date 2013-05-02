package nl.topicus.eduarte.krdparticipatie.web.components.menu.enums;

import nl.topicus.cobra.web.components.menu.MenuItemKey;

/**
 * @author vandekamp
 */
public enum ParticipatieGroepMenuItem implements MenuItemKey
{
	/**
	 * Waarnemingen_invoeren
	 */
	Waarnemingen_invoeren
	{
		@Override
		public String getLabel()
		{
			return "Waarnemingen invoeren";
		}
	},
	/**
	 * Waarnemingen weekoverzicht
	 */
	Waarnemingen_weekoverzicht
	{
		@Override
		public String getLabel()
		{
			return "Waarnemingenoverzicht";
		}
	},
	/**
	 * Waarnemingen weekoverzicht
	 */
	Totalen
	{
		@Override
		public String getLabel()
		{
			return "Totalen";
		}
	};

	/**
	 * @see nl.topicus.cobra.web.components.menu.MenuItemKey#getLabel()
	 */
	@Override
	public String getLabel()
	{
		return name();
	}

}
