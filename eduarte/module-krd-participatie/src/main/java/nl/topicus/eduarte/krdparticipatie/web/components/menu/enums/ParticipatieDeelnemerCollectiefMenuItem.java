package nl.topicus.eduarte.krdparticipatie.web.components.menu.enums;

import nl.topicus.cobra.web.components.menu.MenuItemKey;

/**
 * @author vandekamp
 */
public enum ParticipatieDeelnemerCollectiefMenuItem implements MenuItemKey
{
	/**
	 * Absentie meldingen
	 */
	Absentie_melden
	{
		@Override
		public String getLabel()
		{
			return "Absentie melden";
		}
	},

	/**
	 * Openstaande absentiemeldingen en waarnemingen.
	 */
	Openstaande_AbsentieMeldingen
	{
		@Override
		public String getLabel()
		{
			return "Openstaande meldingen";
		}
	},

	/**
	 * Openstaande absentiemeldingen en waarnemingen.
	 */
	Openstaande_AbsentieWaarnemingen
	{
		@Override
		public String getLabel()
		{
			return "Openstaande waarnemingen";
		}
	},
	/**
	 * Openstaande maatregelen
	 */
	Openstaande_maatregelen
	{
		@Override
		public String getLabel()
		{
			return "Openstaande maatregelen";
		}
	},
	/**
	 * Rapportage voor leerplicht
	 */
	VerzuimRapportage
	{
		@Override
		public String getLabel()
		{
			return "Verzuimrapportage";
		}
	},
	/**
	 * Budget toekennen
	 */
	Budget_toekennen
	{
		@Override
		public String getLabel()
		{
			return "Budget toekennen";
		}
	},

	/**
	 * @see nl.topicus.cobra.web.components.menu.MenuItemKey#getLabel()
	 */

	OLC
	{
		@Override
		public String getLabel()
		{
			return "OLC Registraties";
		}
	},

	CollectiefBudget
	{
		@Override
		public String getLabel()
		{
			return "Collectief Budget Toekennen";
		}
	};

	@Override
	public String getLabel()
	{
		return name();
	}

}
