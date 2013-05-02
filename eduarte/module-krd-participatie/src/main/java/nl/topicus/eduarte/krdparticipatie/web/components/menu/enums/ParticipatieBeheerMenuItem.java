package nl.topicus.eduarte.krdparticipatie.web.components.menu.enums;

import nl.topicus.cobra.web.components.menu.MenuItemKey;

/**
 * @author loite
 */
public enum ParticipatieBeheerMenuItem implements MenuItemKey
{
	Absentieredenen,
	Maatregelen,
	Periode_indeling
	{
		@Override
		public String getLabel()
		{
			return "Periode-indelingen";
		}
	},
	AbsentiemeldingenImporteren
	{
		@Override
		public String getLabel()
		{
			return "Absentiemeldingen importeren";
		}
	},
	KRDWaarnemingenImporteren
	{
		@Override
		public String getLabel()
		{
			return "Waarnemingen importeren (KRD)";
		}
	},
	Waarnemingen_importeren
	{
		@Override
		public String getLabel()
		{
			return "Waarnemingen importeren";
		}
	},
	Waarnemingen_converteren
	{

		@Override
		public String getLabel()
		{
			return "Waarnemingen converteren";
		}
	},
	Basisroosters,
	Roosterimport
	{
		@Override
		public String getLabel()
		{
			return "Rooster importeren";
		}
	},
	Groepdocentimport
	{
		@Override
		public String getLabel()
		{
			return "Groepdocent importeren";
		}
	},
	AgendaKoppelingen
	{
		@Override
		public String getLabel()
		{
			return "Agendakoppelingen";
		}
	},
	Activiteiten,
	WaarnemingenGenereren
	{
		@Override
		public String getLabel()
		{
			return "Waarnemingen genereren";
		}
	},
	AfsprakenZonderPresentieRegistratie
	{
		@Override
		public String getLabel()
		{
			return "Openstaande afspraken";
		}
	},
	DeelnemerportaalInstellingen
	{
		@Override
		public String getLabel()
		{
			return "Self service";
		}
	},
	IIVOTijd
	{
		@Override
		public String getLabel()
		{
			return "IIVO-tijd";
		}
	},
	GefaseerdeInvoer
	{
		@Override
		public String getLabel()
		{
			return "Gefaseerde invoer instellingen";
		}
	},
	olcLocatie
	{
		@Override
		public String getLabel()
		{
			return "OLC Locaties";
		}
	},

	Lesweekindeling,
	Lesweekkoppeling,
	Verzuimloket;

	@Override
	public String getLabel()
	{
		return name();
	}

}
