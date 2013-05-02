package nl.topicus.eduarte.krd.entities.bron;

import nl.topicus.cobra.util.StringUtil;

/**
 * Status van een BRON melding.
 */
public enum BronMeldingStatus
{
	GOEDGEKEURD,
	AFGEKEURD,
	WACHTRIJ,
	IN_BEHANDELING,
	VERWIJDERD;

	@Override
	public String toString()
	{
		return StringUtil.firstCharUppercase(name()).replace('_', ' ');
	}

	public char toChar()
	{
		return name().charAt(0);
	}

	public static BronMeldingStatus valueOf(char ch)
	{
		switch (Character.toUpperCase(ch))
		{
			case 'G':
				return GOEDGEKEURD;
			case 'A':
				return AFGEKEURD;
			case 'W':
				return WACHTRIJ;
			case 'I':
				return IN_BEHANDELING;
			case 'V':
				return VERWIJDERD;
		}
		throw new IllegalArgumentException("'" + ch
			+ "' is geen geldige waarde voor een BronStatusMelding");
	}
}
