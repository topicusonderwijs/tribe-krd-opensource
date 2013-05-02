package nl.topicus.eduarte.entities.participatie.enums;

public enum WaarnemingWeergaveEnum
{

	/**
	 * Geoorloofd/Ongeoorloofd geeft een 'G' of een 'O' weer
	 */
	Geoorloofd_Ongeoorloofd
	{
		@Override
		public String toString()
		{
			return "Geoorloofd/Ongeoorloofd";
		}
	},
	/**
	 * Geeft de letter van de absentiemeling weer als deze aanwezig is bij de waarnemingm,
	 * anders de A van afwezig
	 */
	AbsentieMeldingOfWaarneming
	{
		@Override
		public String toString()
		{
			return "AbsentieMelding of Waarneming";
		}
	},
	/**
	 * Geeft alleen een P-Present 0f A-Aanwezig weer
	 */
	AlleenWaarneming
	{
		@Override
		public String toString()
		{
			return "Alleen waarneming";
		}
	};
}
