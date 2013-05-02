package nl.topicus.eduarte.entities.taxonomie.vo;


/**
 * Profiel in de bovenbouw havo/vwo
 * 
 * @author loite
 */
public enum Profiel
{
	NT("Natuur en Techniek"), NG("Natuur en Gezondheid"), EM("Economie en Maatschappij"), CM(
			"Cultuur en Maatschappij"), NTNG(NT, NG), NTEM(NT, EM), NTCM(NT, CM), NGEM(NG, EM), NGCM(
			NG, CM), EMCM(EM, CM);

	private final String naam;

	private final Profiel profiel1;

	private final Profiel profiel2;

	private Profiel(String naam)
	{
		this.naam = naam;
		profiel1 = null;
		profiel2 = null;
	}

	private Profiel(Profiel profiel1, Profiel profiel2)
	{
		this.naam = profiel1.naam + " / " + profiel2.naam;
		this.profiel1 = profiel1;
		this.profiel2 = profiel2;
	}

	/**
	 * @return Returns the naam.
	 */
	public String getNaam()
	{
		return naam;
	}

	/**
	 * @return Returns the profiel1.
	 */
	public Profiel getProfiel1()
	{
		return profiel1;
	}

	/**
	 * @return Returns the profiel2.
	 */
	public Profiel getProfiel2()
	{
		return profiel2;
	}

}
