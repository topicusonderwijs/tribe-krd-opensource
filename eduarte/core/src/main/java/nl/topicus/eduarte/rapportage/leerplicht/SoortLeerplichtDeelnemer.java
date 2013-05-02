package nl.topicus.eduarte.rapportage.leerplicht;

public enum SoortLeerplichtDeelnemer
{

	LEERPLICHTIG
	{
		@Override
		public String toString()
		{
			return "Leerplichtigen";
		}
	},

	KWALIFICATIE_PLICHTIG
	{
		@Override
		public String toString()
		{
			return "Kwalificatie plichtigen";
		}
	},

	VSV
	{
		@Override
		public String toString()
		{
			return "VSV 18+";
		}
	},

	WTOS_WSF
	{
		@Override
		public String toString()
		{
			return "WTOS / WSF2000";
		}
	};

}
