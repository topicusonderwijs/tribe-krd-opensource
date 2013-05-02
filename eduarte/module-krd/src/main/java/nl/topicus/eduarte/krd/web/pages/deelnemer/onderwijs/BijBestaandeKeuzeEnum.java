package nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs;

public enum BijBestaandeKeuzeEnum
{
	GeenWijziging
	{
		@Override
		public String toString()
		{
			return "Geen wijziging";
		}
	},
	KeuzeOverschrijven
	{
		@Override
		public String toString()
		{
			return "Keuze overschrijven";
		}
	},
	KeuzeVerwijderen
	{
		@Override
		public String toString()
		{
			return "Keuze verwijderen";
		}
	};
}
