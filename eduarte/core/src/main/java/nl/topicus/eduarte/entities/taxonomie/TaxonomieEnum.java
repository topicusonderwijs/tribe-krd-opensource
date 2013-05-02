package nl.topicus.eduarte.entities.taxonomie;

import nl.topicus.cobra.util.StringUtil;

/**
 * De herkanbare taxonomien voor de applicatie.
 * 
 * @author loite
 */
public enum TaxonomieEnum
{
	/**
	 * Competentiegericht onderwijs voor het MBO.
	 */
	CGO("1"),
	/**
	 * MBO kwalificatiestructuur.
	 */
	MBO("2"),
	/**
	 * Voortgezet onderwijs en VAVO.
	 */
	VO("3")
	{
		@Override
		public boolean heeftLWOO()
		{
			return true;
		}
	},
	/**
	 * Educatie
	 */
	Educatie("4"),
	Inburgering("5"),
	HO("8"),
	/**
	 * Overige taxonomien die door de onderwijsinstellingen zijn aangemaakt;
	 */
	Anders(null);

	// de taxonomiecodes t/m 19 zijn gereserveerd voor toekomstig gebruik
	public final static int EERSTE_VRIJE_CODE = 20;

	/**
	 * De code van de taxonomie.
	 */
	private final String code;

	/**
	 * Constructor
	 * 
	 * @param code
	 */
	private TaxonomieEnum(String code)
	{
		this.code = code;
	}

	/**
	 * @return Returns the code.
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * @param taxonomie
	 * @return true als de gegeven taxonomie overeenkomt met deze taxonomie enum.
	 */
	public boolean isTaxonomieEnum(Taxonomie taxonomie)
	{
		return taxonomie.getCode() != null && getCode() != null
			&& taxonomie.getCode().equals(getCode());
	}

	/**
	 * @return true indien deze taxonomie lwoo-opleidingen heeft (alleen van toepassing
	 *         voor het vo).
	 */
	public boolean heeftLWOO()
	{
		return false;
	}

	public static boolean isGereserveerd(String code)
	{
		try
		{
			if (TaxonomieEnum.valueOf(code) != null)
				return true;
		}
		catch (IllegalArgumentException e)
		{
			// doe niets
		}
		if (StringUtil.isNumeric(code))
		{
			return Integer.valueOf(code) < EERSTE_VRIJE_CODE;
		}

		return false;
	}

}
