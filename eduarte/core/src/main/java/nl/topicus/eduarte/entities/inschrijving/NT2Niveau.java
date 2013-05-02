package nl.topicus.eduarte.entities.inschrijving;

public enum NT2Niveau
{
	Nul
	{
		@Override
		public String toString()
		{
			return "0";
		}
	},
	A1,
	A2,
	B1,
	B2,
	C1,
	C2;

	/**
	 * @return de beschikbare niveaus voor het niveau taalvaardigheid van inburgeraars
	 */
	public static NT2Niveau[] inburgeringsNiveaus()
	{
		return new NT2Niveau[] {Nul, A1, A2};
	}

}
